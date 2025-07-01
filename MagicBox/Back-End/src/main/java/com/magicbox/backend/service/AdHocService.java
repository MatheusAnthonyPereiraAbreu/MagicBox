package com.magicbox.backend.service;

import com.magicbox.backend.model.dto.*;
import com.magicbox.backend.model.dto.select.*;
import com.magicbox.backend.model.dto.table.Table;
import com.magicbox.backend.model.dto.where.Operator;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
// Serviço responsável por gerar relatórios personalizados (ad-hoc) de acordo com parâmetros dinâmicos enviados pelo usuário.
public class AdHocService {

    private final EntityManager entityManager;

    public AdHocService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // --- INÍCIO: Grafo de relacionamentos entre tabelas ---
    // Classe auxiliar para representar uma aresta de join
    private static class JoinEdge {
        Table from;
        Table to;
        JoinType type;
        String fromField;
        String toField;
        JoinEdge(Table from, Table to, JoinType type, String fromField, String toField) {
            this.from = from;
            this.to = to;
            this.type = type;
            this.fromField = fromField;
            this.toField = toField;
        }
    }

    // Grafo de relacionamentos (preencha conforme seu modelo)
    private static final List<JoinEdge> RELATIONSHIP_GRAPH = List.of(
        new JoinEdge(Table.ALBUM, Table.ARTISTA, JoinType.INNER, "artista", "id"),
        new JoinEdge(Table.MUSICA, Table.ALBUM, JoinType.INNER, "album", "id"),
        new JoinEdge(Table.MUSICA, Table.ARTISTA, JoinType.INNER, "artista", "id"),
        new JoinEdge(Table.RANKING_MUSICAS, Table.MUSICA, JoinType.INNER, "musica", "id"),
        new JoinEdge(Table.RANKING_MUSICAS, Table.PAIS, JoinType.INNER, "pais", "id"),
        new JoinEdge(Table.RANKING_ARTISTAS, Table.ARTISTA, JoinType.INNER, "artista", "id"),
        new JoinEdge(Table.RANKING_ARTISTAS, Table.PAIS, JoinType.INNER, "pais", "id"),
        new JoinEdge(Table.ARTISTA_TAG, Table.ARTISTA, JoinType.INNER, "artista", "id"),
        new JoinEdge(Table.ARTISTA_TAG, Table.TAG, JoinType.INNER, "tag", "id"),
        new JoinEdge(Table.SIMILARIDADE_ARTISTA, Table.ARTISTA, JoinType.INNER, "artistaBase", "id"),
        new JoinEdge(Table.SIMILARIDADE_ARTISTA, Table.ARTISTA, JoinType.INNER, "artistaSimilar", "id"),
        // Adicionando joins faltantes para melhor conectividade
        new JoinEdge(Table.ARTISTA, Table.ARTISTA_TAG, JoinType.LEFT, "artistaTags", "artista"),
        new JoinEdge(Table.ARTISTA, Table.RANKING_ARTISTAS, JoinType.LEFT, "rankingsArtistas", "artista"),
        new JoinEdge(Table.MUSICA, Table.RANKING_MUSICAS, JoinType.LEFT, "rankingsMusicas", "musica"),
        // NOVO: Joins para conectar via RANKING_ARTISTAS e RANKING_MUSICAS
        new JoinEdge(Table.RANKING_ARTISTAS, Table.ARTISTA, JoinType.LEFT, "artista", "id"),
        new JoinEdge(Table.RANKING_MUSICAS, Table.MUSICA, JoinType.LEFT, "musica", "id"),
        new JoinEdge(Table.RANKING_MUSICAS, Table.ARTISTA, JoinType.LEFT, "artista", "id")
    );

    // Busca o menor caminho de joins entre todas as tabelas selecionadas
    private Set<JoinDTO> findJoinPath(Set<Table> tables) {
        System.out.println("=== DEBUG: findJoinPath chamado com tabelas: " + tables + " ===");
        if (tables.size() <= 1) {
            System.out.println("Apenas uma tabela, retornando joins vazios");
            return Set.of();
        }
        
        // SOLUÇÃO RÁPIDA: Usa ARTISTA como hub central sempre que possível
        Set<JoinDTO> joins = new HashSet<>();
        
        // Se ARTISTA está nas tabelas selecionadas, usa como hub
        if (tables.contains(Table.ARTISTA)) {
            System.out.println("Usando ARTISTA como hub central");
            joins.addAll(createJoinsFromArtista(tables));
        } else {
            // NOVO: Verifica se ARTISTA é necessária para conectar as tabelas
            if (isArtistaNeededForConnection(tables)) {
                System.out.println("ARTISTA é necessária para conectar as tabelas, adicionando como hub");
                Set<Table> tablesWithArtista = new HashSet<>(tables);
                tablesWithArtista.add(Table.ARTISTA);
                joins.addAll(createJoinsFromArtista(tablesWithArtista));
            } else {
                // Se ARTISTA não está, usa o algoritmo BFS original
                System.out.println("ARTISTA não está nas tabelas, usando algoritmo BFS");
                joins.addAll(findJoinPathBFS(tables));
            }
        }
        
        System.out.println("Joins finais: " + joins);
        return joins;
    }

    // NOVO: Verifica se ARTISTA é necessária para conectar as tabelas
    private boolean isArtistaNeededForConnection(Set<Table> tables) {
        // Tabelas que precisam de ARTISTA para serem conectadas
        Set<Table> tablesNeedingArtista = Set.of(
            Table.ARTISTA_TAG, 
            Table.SIMILARIDADE_ARTISTA, 
            Table.RANKING_ARTISTAS
        );
        
        // Se alguma dessas tabelas está presente, ARTISTA é necessária
        boolean needsArtista = tables.stream().anyMatch(tablesNeedingArtista::contains);
        
        System.out.println("Verificando se ARTISTA é necessária: " + needsArtista);
        System.out.println("Tabelas que precisam de ARTISTA: " + tablesNeedingArtista);
        System.out.println("Tabelas selecionadas: " + tables);
        
        return needsArtista;
    }

    // NOVO: Cria joins rapidamente usando ARTISTA como hub central
    private Set<JoinDTO> createJoinsFromArtista(Set<Table> tables) {
        Set<JoinDTO> joins = new HashSet<>();
        
        // Mapeia tabelas que podem ser conectadas diretamente ao ARTISTA
        Map<Table, JoinType> directConnections = Map.of(
            Table.ARTISTA_TAG, JoinType.LEFT,
            Table.RANKING_ARTISTAS, JoinType.LEFT
        );
        
        // Adiciona joins diretos para ARTISTA
        for (Table table : tables) {
            if (table != Table.ARTISTA && directConnections.containsKey(table)) {
                joins.add(new JoinDTO(Table.ARTISTA, table, directConnections.get(table)));
                System.out.println("Adicionando join direto: ARTISTA -> " + table);
            }
        }
        
        // Adiciona joins para tabelas que precisam de caminho indireto
        if (tables.contains(Table.SIMILARIDADE_ARTISTA)) {
            // SIMILARIDADE_ARTISTA se conecta via artistaBase
            // Não adicionamos join aqui, pois SIMILARIDADE_ARTISTA já tem join com ARTISTA no grafo
            System.out.println("SIMILARIDADE_ARTISTA será conectada via join existente no grafo");
        }
        
        if (tables.contains(Table.RANKING_MUSICAS)) {
            // RANKING_MUSICAS precisa de MUSICA -> ARTISTA
            joins.add(new JoinDTO(Table.ARTISTA, Table.MUSICA, JoinType.LEFT));
            joins.add(new JoinDTO(Table.MUSICA, Table.RANKING_MUSICAS, JoinType.LEFT));
            System.out.println("Adicionando caminho: ARTISTA -> MUSICA -> RANKING_MUSICAS");
        }
        
        if (tables.contains(Table.ALBUM)) {
            joins.add(new JoinDTO(Table.ARTISTA, Table.ALBUM, JoinType.LEFT));
            System.out.println("Adicionando join: ARTISTA -> ALBUM");
        }
        
        if (tables.contains(Table.TAG)) {
            // TAG precisa de ARTISTA_TAG
            if (!joins.stream().anyMatch(j -> j.to() == Table.ARTISTA_TAG)) {
                joins.add(new JoinDTO(Table.ARTISTA, Table.ARTISTA_TAG, JoinType.LEFT));
            }
            joins.add(new JoinDTO(Table.ARTISTA_TAG, Table.TAG, JoinType.LEFT));
            System.out.println("Adicionando caminho: ARTISTA -> ARTISTA_TAG -> TAG");
        }
        
        if (tables.contains(Table.PAIS)) {
            // PAIS pode ser acessado via RANKING_ARTISTAS ou RANKING_MUSICAS
            if (tables.contains(Table.RANKING_ARTISTAS)) {
                joins.add(new JoinDTO(Table.RANKING_ARTISTAS, Table.PAIS, JoinType.LEFT));
                System.out.println("Adicionando join: RANKING_ARTISTAS -> PAIS");
            } else if (tables.contains(Table.RANKING_MUSICAS)) {
                joins.add(new JoinDTO(Table.RANKING_MUSICAS, Table.PAIS, JoinType.LEFT));
                System.out.println("Adicionando join: RANKING_MUSICAS -> PAIS");
            }
        }
        
        return joins;
    }

    // Método BFS original (renomeado)
    private Set<JoinDTO> findJoinPathBFS(Set<Table> tables) {
        // Algoritmo BFS para encontrar árvore de conexões
        Map<Table, List<JoinEdge>> graph = new HashMap<>();
        for (JoinEdge edge : RELATIONSHIP_GRAPH) {
            graph.computeIfAbsent(edge.from, k -> new ArrayList<>()).add(edge);
            // Bidirecional
            graph.computeIfAbsent(edge.to, k -> new ArrayList<>()).add(
                new JoinEdge(edge.to, edge.from, edge.type, edge.toField, edge.fromField)
            );
        }
        
        System.out.println("Grafo construído: " + graph.keySet());
        
        // Escolhe a melhor tabela raiz baseada na conectividade
        Table root = selectBestRootTable(tables, graph);
        System.out.println("Tabela raiz escolhida: " + root);
        
        Set<Table> visited = new HashSet<>();
        Map<Table, JoinEdge> parent = new HashMap<>();
        Queue<Table> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root);
        
        while (!queue.isEmpty()) {
            Table current = queue.poll();
            System.out.println("Processando tabela: " + current);
            for (JoinEdge edge : graph.getOrDefault(current, List.of())) {
                if (!visited.contains(edge.to) && tables.contains(edge.to)) {
                    visited.add(edge.to);
                    parent.put(edge.to, edge);
                    queue.add(edge.to);
                    System.out.println("Conectou " + current + " -> " + edge.to);
                }
            }
        }
        
        System.out.println("Tabelas visitadas: " + visited);
        System.out.println("Tabelas necessárias: " + tables);
        
        if (!tables.stream().allMatch(visited::contains)) {
            Set<Table> missing = tables.stream()
                    .filter(t -> !visited.contains(t))
                    .collect(Collectors.toSet());
            System.err.println("Tabelas não conectadas: " + missing);
            throw new IllegalArgumentException("Não é possível conectar todas as tabelas selecionadas por joins válidos. Tabelas não conectadas: " + missing);
        }
        
        // Monta os JoinDTOs a partir dos pais
        Set<JoinDTO> joins = new HashSet<>();
        for (Table t : tables) {
            if (t.equals(root)) continue;
            JoinEdge edge = parent.get(t);
            if (edge != null) {
                joins.add(new JoinDTO(edge.from, edge.to, edge.type));
                System.out.println("Adicionando join: " + edge.from + " -> " + edge.to);
            }
        }
        
        return joins;
    }

    // NOVO: Seleciona a melhor tabela raiz baseada na conectividade
    private Table selectBestRootTable(Set<Table> tables, Map<Table, List<JoinEdge>> graph) {
        Table bestRoot = null;
        int maxConnections = -1;
        
        for (Table table : tables) {
            List<JoinEdge> connections = graph.getOrDefault(table, List.of());
            int validConnections = (int) connections.stream()
                    .filter(edge -> tables.contains(edge.to))
                    .count();
            
            System.out.println("Tabela " + table + " tem " + validConnections + " conexões válidas");
            
            if (validConnections > maxConnections) {
                maxConnections = validConnections;
                bestRoot = table;
            }
        }
        
        if (bestRoot == null) {
            // Fallback: escolhe a primeira tabela
            bestRoot = tables.iterator().next();
        }
        
        System.out.println("Melhor tabela raiz: " + bestRoot + " com " + maxConnections + " conexões");
        return bestRoot;
    }

    // NOVO: Encontra o caminho de joins entre duas tabelas específicas
    private List<JoinEdge> findPathBetweenTables(Table from, Table to) {
        System.out.println("=== DEBUG: findPathBetweenTables chamado: " + from + " -> " + to + " ===");
        if (from.equals(to)) {
            System.out.println("Mesma tabela, retornando caminho vazio");
            return List.of();
        }
        
        Map<Table, List<JoinEdge>> graph = new HashMap<>();
        for (JoinEdge edge : RELATIONSHIP_GRAPH) {
            graph.computeIfAbsent(edge.from, k -> new ArrayList<>()).add(edge);
            // Bidirecional
            graph.computeIfAbsent(edge.to, k -> new ArrayList<>()).add(
                new JoinEdge(edge.to, edge.from, edge.type, edge.toField, edge.fromField)
            );
        }
        
        System.out.println("Grafo disponível: " + graph.keySet());
        
        Map<Table, JoinEdge> parent = new HashMap<>();
        Set<Table> visited = new HashSet<>();
        Queue<Table> queue = new LinkedList<>();
        queue.add(from);
        visited.add(from);
        
        while (!queue.isEmpty()) {
            Table current = queue.poll();
            System.out.println("Processando: " + current);
            if (current.equals(to)) {
                System.out.println("Destino encontrado!");
                break;
            }
            
            for (JoinEdge edge : graph.getOrDefault(current, List.of())) {
                if (!visited.contains(edge.to)) {
                    visited.add(edge.to);
                    parent.put(edge.to, edge);
                    queue.add(edge.to);
                    System.out.println("Conectou " + current + " -> " + edge.to);
                }
            }
        }
        
        if (!visited.contains(to)) {
            System.err.println("Não foi possível encontrar caminho de " + from + " para " + to);
            System.err.println("Tabelas visitadas: " + visited);
            throw new IllegalArgumentException("Não é possível conectar " + from + " e " + to + " por joins válidos.");
        }
        
        // Reconstrói o caminho
        List<JoinEdge> path = new ArrayList<>();
        Table current = to;
        while (!current.equals(from)) {
            JoinEdge edge = parent.get(current);
            path.add(0, edge); // Adiciona no início para manter a ordem
            current = edge.from;
        }
        
        System.out.println("Caminho encontrado: " + path.stream().map(e -> e.from + "->" + e.to).collect(Collectors.joining(", ")));
        return path;
    }
    // --- FIM: Grafo de relacionamentos ---

    /**
     * Gera um relatório dinâmico baseado nos parâmetros enviados no AdHocDTO.
     */
    public Object gerarRelatorio(AdHocDTO request, int page, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Table rootTable = request.table();
        GroupByDTO groupBy = request.groupBy();

        // --- NOVO: Completa joins automaticamente se necessário ---
        Set<Table> allTables = new HashSet<>();
        allTables.add(rootTable);
        if (request.column() != null) request.column().forEach(c -> allTables.add(c.table()));
        if (request.where() != null) request.where().forEach(w -> allTables.add(w.table()));
        if (groupBy != null && groupBy.columnSet() != null) groupBy.columnSet().forEach(g -> allTables.add(g.table()));
        
        System.out.println("=== DEBUG: Tabelas necessárias: " + allTables + " ===");
        
        if (request.join() == null || request.join().isEmpty()) {
            // Se não veio join, monta todos necessários
            System.out.println("Nenhum join fornecido, criando automaticamente");
            Set<JoinDTO> autoJoins = findJoinPath(allTables);
            System.out.println("Joins automáticos criados: " + autoJoins);
            request = new AdHocDTO(rootTable, autoJoins, request.column(), request.where(), groupBy);
        } else {
            // Garante que todos os joins necessários estão presentes
            Set<Table> joinedTables = new HashSet<>();
            joinedTables.add(rootTable);
            request.join().forEach(j -> {
                joinedTables.add(j.from());
                joinedTables.add(j.to());
            });
            System.out.println("Tabelas conectadas pelos joins: " + joinedTables);
            if (!allTables.stream().allMatch(joinedTables::contains)) {
                System.out.println("Joins insuficientes, criando automaticamente");
                Set<JoinDTO> autoJoins = findJoinPath(allTables);
                System.out.println("Joins automáticos criados: " + autoJoins);
                request = new AdHocDTO(rootTable, autoJoins, request.column(), request.where(), groupBy);
            }
        }
        // --- FIM NOVO ---

        Map<Table, From<?, ?>> joins = new HashMap<>();
        Root<? extends Selectable> root = query.from(rootTable.toSelectable());
        joins.put(rootTable, root);
        
        System.out.println("=== DEBUG: Root criado para tabela: " + rootTable + " ===");
        
        // NOVO: Garante que todos os joins intermediários sejam criados
        ensureAllJoinsCreated(rootTable, joins, request.join(), allTables);
        
        if (Objects.isNull(groupBy)) {
            System.out.println("=== DEBUG: Preparando seleção sem group by ===");
            List<Selection<Object>> fields = prepareSelectStatements(request.column(), joins, cb);
            System.out.println("=== DEBUG: Campos selecionados: " + fields.size() + " ===");
            query.multiselect(fields.toArray(new Selection[0]));
        } else {
            System.out.println("=== DEBUG: Preparando seleção com group by ===");
            prepareGroupByStatements(groupBy, joins, query, cb);
        }

        if (Objects.nonNull(request.where()) && !request.where().isEmpty()) {
            List<Predicate> predicates = prepareWhereStatements(request.where(), joins, cb);
            query.where(predicates.toArray(new Predicate[0]));
        }

        // Query de contagem total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<? extends Selectable> countRoot = countQuery.from(rootTable.toSelectable());
        Map<Table, From<?, ?>> countJoins = new HashMap<>();
        countJoins.put(rootTable, countRoot);
        ensureAllJoinsCreated(rootTable, countJoins, request.join(), allTables);
        if (Objects.nonNull(request.where()) && !request.where().isEmpty()) {
            List<Predicate> countPredicates = prepareWhereStatements(request.where(), countJoins, cb);
            countQuery.where(countPredicates.toArray(new Predicate[0]));
        }
        countQuery.select(cb.count(countRoot));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        TypedQuery<Tuple> jpaQuery = entityManager.createQuery(query);
        jpaQuery.setFirstResult(page * size);
        jpaQuery.setMaxResults(size);
        List<Tuple> resultList = jpaQuery.getResultList();

        Map<String, Object> response = new HashMap<>();
        response.put("data", mapResultsToFriendlyFormat(resultList));
        response.put("total", total);
        return response;
    }

    /**
     * Prepara as instruções de group by e seleção para consultas agregadas.
     */
    private void prepareGroupByStatements(GroupByDTO groupBy, Map<Table, From<?, ?>> joins, CriteriaQuery<Tuple> query,
            CriteriaBuilder cb) {
        List<Selection<Object>> groupByList = prepareSelectStatements(groupBy.columnSet(), joins, cb);
        List<Selection<?>> selections = new ArrayList<>(groupByList);

        AggregationDTO aggregation = groupBy.aggregation();
        if (Objects.nonNull(aggregation)) {
            Selection<?> selection = prepareAggregation(aggregation, joins, cb);
            selections.add(selection);
        }

        query.groupBy(groupByList.toArray(new Expression[0]));
        query.multiselect(selections);
    }

    /**
     * Prepara a seleção de campos agregados (SUM, AVG, COUNT, etc).
     */
    private Selection<?> prepareAggregation(AggregationDTO aggregation, Map<Table, From<?, ?>> joins,
            CriteriaBuilder cb) {
        Table table = aggregation.table();
        Select field = aggregation.field();
        String alias = Optional.ofNullable(aggregation.alias())
                .orElse(table.attribute() + "_" + field.attribute() + "_" + aggregation.aggregation());

        From<?, ?> from = joins.get(table);
        Expression<? extends Number> path = (Expression<? extends Number>) getPath(from, field.attribute());

        return switch (aggregation.aggregation()) {
            case AVG -> cb.avg(path).alias(alias);
            case MAX -> cb.max(path).alias(alias);
            case MIN -> cb.min(path).alias(alias);
            case SUM -> cb.sum(path).alias(alias);
            case COUNT -> cb.count(path).alias(alias);
            case COUNT_DISTINCT -> cb.countDistinct(path).alias(alias);
        };
    }

    /**
     * Mapeia o resultado da consulta para um formato amigável (lista de mapas).
     */
    private static List<Map<String, Object>> mapResultsToFriendlyFormat(List<Tuple> resultList) {

        return resultList
                .stream()
                .map(tuple -> {
                    Map<String, Object> linha = new HashMap<>();
                    for (TupleElement<?> el : tuple.getElements()) {
                        linha.put(el.getAlias(), tuple.get(el));
                    }
                    return linha;
                }).toList();
    }

    /**
     * Prepara os filtros (where) da consulta.
     */
    private List<Predicate> prepareWhereStatements(Set<WhereDTO> whereSet, Map<Table, From<?, ?>> joins,
            CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        for (WhereDTO whereDTO : whereSet) {
            Table table = whereDTO.table();
            Select field = whereDTO.field();
            Object value = field.castValue(whereDTO.value());
            String attribute = field.attribute();
            Operator operator = whereDTO.operator();
            Expression<?> path = getPath(joins.get(table), attribute);

            Predicate predicate = null;
            if (value instanceof Double v) {
                predicate = getPredicateFilter(v, operator, path.as(Double.class), cb);
            } else if (value instanceof Integer i) {
                predicate = getPredicateFilter(i, operator, path.as(Integer.class), cb);
            } else if (value instanceof Long l) {
                predicate = getPredicateFilter(l, operator, path.as(Long.class), cb);
            } else if (value instanceof LocalDate d) {
                predicate = getPredicateFilter(d, operator, path.as(LocalDate.class), cb);
            } else {
                predicate = getPredicateFilter((String) value, operator, path.as(String.class), cb);
            }

            if (predicate != null) {
                predicates.add(predicate);
            }
        }

        return predicates;
    }

    /**
     * Cria o filtro (Predicate) para cada tipo de operador e valor.
     */
    private <T extends Comparable<? super T>> Predicate getPredicateFilter(T value, Operator operator,
            Expression<T> path, CriteriaBuilder cb) {
        return switch (operator) {
            case GREATER_THAN -> cb.greaterThan(path, value);
            case GREATER_THAN_EQUALS -> cb.greaterThanOrEqualTo(path, value);
            case LESS_THAN -> cb.lessThan(path, value);
            case LESS_THAN_EQUALS -> cb.lessThanOrEqualTo(path, value);
            case EQUALS -> cb.equal(path, value);
            case NOT_EQUALS -> cb.notEqual(path, value);
            case LIKE -> cb.like(path.as(String.class), "%" + value + "%");
        };
    }

    /**
     * Obtém o caminho (path) do atributo na entidade, tratando casos de chave composta.
     */
    private Expression<?> getPath(From<?, ?> from, String attribute) {
        if (from == null) {
            throw new IllegalStateException("From é null ao tentar acessar atributo: " + attribute);
        }
        String[] parts = attribute.split("\\.");
        Path<?> path = from;
        for (String part : parts) {
            path = path.get(part);
        }
        return path;
    }


    /**
     * Prepara a seleção dos campos a serem retornados na consulta.
     */
    private List<Selection<Object>> prepareSelectStatements(Set<ColumnDTO> columnSet, Map<Table, From<?, ?>> joins,
            CriteriaBuilder cb) {
        System.out.println("=== DEBUG: Preparando seleção de campos ===");
        System.out.println("Colunas específicas: " + columnSet);
        System.out.println("Tabelas disponíveis para seleção: " + joins.keySet());
        System.out.println("Número de tabelas disponíveis: " + joins.size());
        
        if (columnSet.isEmpty()) {
            System.out.println("Nenhuma coluna específica - selecionando todos os campos de todas as tabelas");
            System.out.println("Tabelas disponíveis para seleção: " + joins.keySet());
            
            List<Selection<Object>> allSelections = new ArrayList<>();
            
            for (Table table : joins.keySet()) {
                System.out.println("=== Processando tabela: " + table + " ===");
                try {
                    List<Select> fields = Select.getFromTable(table);
                    System.out.println("Campos disponíveis para " + table + ": " + fields.stream().map(Select::attribute).collect(Collectors.toList()));
                    
                    for (Select field : fields) {
                        try {
                            String alias = table.attribute() + "_" + field.attribute();
                            System.out.println("Tentando selecionar campo: " + table + "." + field.attribute() + " -> " + alias);
                            
                            From<?, ?> from = joins.get(table);
                            if (from == null) {
                                System.err.println("ERRO: From é null para tabela " + table);
                                continue;
                            }
                            
                            Selection<Object> selection = selectDefault(from, field, alias);
                            allSelections.add(selection);
                            System.out.println("✓ Campo selecionado com sucesso: " + table + "." + field.attribute());
                        } catch (Exception e) {
                            System.err.println("✗ Erro ao selecionar campo " + table + "." + field.attribute() + ": " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("✗ Erro ao processar tabela " + table + ": " + e.getMessage());
                }
            }
            
            System.out.println("Total de campos selecionados: " + allSelections.size());
            return allSelections;
        }

        System.out.println("Processando colunas específicas");
        System.out.println("Colunas a processar: " + columnSet.size());
        return columnSet.stream()
                .filter(col -> {
                    // Filtra colunas de tabelas que não estão disponíveis
                    Table table = col.table();
                    if (!joins.containsKey(table)) {
                        System.err.println("Aviso: Ignorando coluna " + table + "." + col.field().attribute() + " - tabela não disponível");
                        return false;
                    }
                    System.out.println("Processando coluna: " + table + "." + col.field().attribute());
                    return true;
                })
                .map(col -> {
                    Table table = col.table();
                    Select field = col.field();
                    String alias = Optional.ofNullable(col.alias())
                            .filter(a -> !a.isEmpty())
                            .orElse(table.attribute() + "_" + field.attribute());
                    
                    // Validação: verifica se a tabela está no mapa de joins
                    From<?, ?> from = joins.get(table);
                    if (from == null) {
                        System.err.println("ERRO: Tabela " + table + " não encontrada no mapa de joins. Tabelas disponíveis: " + joins.keySet());
                        throw new IllegalStateException("Tabela " + table + " não está disponível para seleção. Verifique se o join foi criado corretamente.");
                    }
                    
                    try {
                        System.out.println("Selecionando coluna: " + table + "." + field.attribute() + " -> " + alias + " (From: " + from.getJavaType().getSimpleName() + ")");
                        return selectDefault(from, field, alias);
                    } catch (Exception e) {
                        System.err.println("Aviso: Ignorando coluna " + table + "." + field.attribute() + " - " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Remove colunas que falharam
                .collect(Collectors.toList());
    }

    /**
     * Realiza os joins necessários entre as tabelas, conforme solicitado.
     * Agora garante que todos os joins intermediários do caminho sejam criados.
     */
    private void prepareJoinTables(Table reference, Map<Table, From<?, ?>> joins, Set<JoinDTO> joinDTOSet) {
        if (!joins.containsKey(reference))
            return;

        // Para cada join solicitado a partir da referência
        joinDTOSet.stream()
                .filter(join -> join.from().equals(reference))
                .forEach(join -> {
                    if (!joins.containsKey(join.to())) {
                        From<?, ?> from = joins.get(join.from());
                        if (from == null) {
                            System.err.println("ERRO: From é null para " + join.from() + ". Joins disponíveis: " + joins.keySet());
                            return;
                        }
                        String joinAttribute = getJoinAttribute(join.to());
                        System.out.println("DEBUG: Criando join " + join.from() + " -> " + join.to() + " via " + joinAttribute);
                        joins.put(join.to(), from.join(joinAttribute, join.type()));
                        // Recursivamente cria os joins intermediários
                        prepareJoinTables(join.to(), joins, joinDTOSet);
                    }
                });
    }

    /**
     * Seleciona o campo padrão da entidade, aplicando alias se necessário.
     */
    private Selection<Object> selectDefault(From<?, ?> from, Select field, String alias) {
    Path<Object> select = (Path<Object>) getPath(from, field.attribute()); 
    return alias != null ? select.alias(alias) : select;
}

    // NOVO: Mapeia o nome correto do atributo para joins
    private String getJoinAttribute(Table table) {
        String attribute = switch (table) {
            case ARTISTA -> "artista"; // Para joins que vêm de outras tabelas para ARTISTA
            case ALBUM -> "album";
            case MUSICA -> "musica";
            case PAIS -> "pais";
            case TAG -> "tag";
            case ARTISTA_TAG -> "artistaTags"; // Relacionamento na entidade Artista
            case SIMILARIDADE_ARTISTA -> "artista"; // Para joins que vêm de SIMILARIDADE_ARTISTA para ARTISTA
            case RANKING_ARTISTAS -> "rankingsArtistas"; // Relacionamento na entidade Artista
            case RANKING_MUSICAS -> "rankingsMusicas"; // Relacionamento na entidade Musica
            case USUARIO -> "usuario";
        };
        System.out.println("DEBUG: Mapeando tabela " + table + " para atributo " + attribute);
        return attribute;
    }

    // NOVO: Garante que todos os joins necessários sejam criados
    private void ensureAllJoinsCreated(Table rootTable, Map<Table, From<?, ?>> joins, Set<JoinDTO> joinDTOSet, Set<Table> allTables) {
        System.out.println("=== DEBUG: Iniciando criação de joins ===");
        System.out.println("Tabela raiz: " + rootTable);
        System.out.println("Todas as tabelas necessárias: " + allTables);
        System.out.println("Joins do DTO: " + joinDTOSet);
        System.out.println("Joins iniciais: " + joins.keySet());
        
        // Primeiro, cria os joins diretos do DTO
        prepareJoinTables(rootTable, joins, joinDTOSet);
        System.out.println("Joins criados após DTO: " + joins.keySet());
        System.out.println("Número de joins após DTO: " + joins.size());
        
        // Depois, para cada tabela que precisa ser acessada, garante que o caminho completo existe
        for (Table targetTable : allTables) {
            System.out.println("=== Verificando tabela: " + targetTable + " ===");
            if (!joins.containsKey(targetTable) && !targetTable.equals(rootTable)) {
                System.out.println("Tentando conectar " + rootTable + " -> " + targetTable);
                try {
                    List<JoinEdge> path = findPathBetweenTables(rootTable, targetTable);
                    System.out.println("Caminho encontrado: " + path.stream().map(e -> e.from + "->" + e.to).collect(Collectors.joining(", ")));
                    
                    // Cria cada join do caminho se ainda não existir
                    for (JoinEdge edge : path) {
                        System.out.println("Processando edge: " + edge.from + " -> " + edge.to);
                        if (!joins.containsKey(edge.to)) {
                            From<?, ?> from = joins.get(edge.from);
                            if (from == null) {
                                System.err.println("ERRO: From é null para " + edge.from + ". Joins disponíveis: " + joins.keySet());
                                continue;
                            }
                            String joinAttribute = getJoinAttribute(edge.to);
                            System.out.println("Criando join: " + edge.from + " -> " + edge.to + " via " + joinAttribute);
                            try {
                                joins.put(edge.to, from.join(joinAttribute, edge.type));
                                System.out.println("✓ Join criado com sucesso: " + edge.from + " -> " + edge.to);
                            } catch (Exception e) {
                                System.err.println("✗ Erro ao criar join " + edge.from + " -> " + edge.to + ": " + e.getMessage());
                            }
                        } else {
                            System.out.println("Join já existe: " + edge.from + " -> " + edge.to);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    // Se não conseguir conectar, ignora (pode ser uma tabela isolada)
                    System.err.println("Aviso: Não foi possível conectar " + rootTable + " e " + targetTable + ": " + e.getMessage());
                }
            } else {
                System.out.println("Tabela " + targetTable + " já está disponível ou é a raiz");
            }
        }
        
        System.out.println("=== DEBUG: Joins finais criados: " + joins.keySet() + " ===");
        System.out.println("=== DEBUG: Tabelas que deveriam estar disponíveis: " + allTables + " ===");
        System.out.println("Número final de joins: " + joins.size());
        
        // Verifica se todas as tabelas necessárias estão disponíveis
        Set<Table> missingTables = allTables.stream()
                .filter(table -> !joins.containsKey(table))
                .collect(Collectors.toSet());
        
        if (!missingTables.isEmpty()) {
            System.err.println("ERRO: Tabelas não encontradas no mapa de joins: " + missingTables);
        } else {
            System.out.println("✓ Todas as tabelas necessárias estão disponíveis!");
        }
    }
}
