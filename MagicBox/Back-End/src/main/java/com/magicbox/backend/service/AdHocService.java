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
        new JoinEdge(Table.SIMILARIDADE_ARTISTA, Table.ARTISTA, JoinType.INNER, "artista", "id")
    );

    // Busca o menor caminho de joins entre todas as tabelas selecionadas
    private Set<JoinDTO> findJoinPath(Set<Table> tables) {
        System.out.println("=== DEBUG: findJoinPath chamado com tabelas: " + tables + " ===");
        if (tables.size() <= 1) {
            System.out.println("Apenas uma tabela, retornando joins vazios");
            return Set.of();
        }
        
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
        
        List<Table> tableList = new ArrayList<>(tables);
        Table root = tableList.get(0);
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
        
        System.out.println("Joins finais: " + joins);
        return joins;
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
        
        if (columnSet.isEmpty()) {
            System.out.println("Nenhuma coluna específica - selecionando todos os campos de todas as tabelas");
            System.out.println("Tabelas disponíveis para seleção: " + joins.keySet());
            return joins.keySet().stream()
                    .flatMap(table -> {
                        try {
                            System.out.println("Processando tabela: " + table);
                            return Select.getFromTable(table).stream()
                                    .map(field -> {
                                        try {
                                            String alias = table.attribute() + "_" + field.attribute();
                                            System.out.println("Selecionando campo: " + table + "." + field.attribute() + " -> " + alias);
                                            return selectDefault(joins.get(table), field, alias);
                                        } catch (Exception e) {
                                            System.err.println("Aviso: Ignorando campo " + table + "." + field.attribute() + " - " + e.getMessage());
                                            return null;
                                        }
                                    })
                                    .filter(Objects::nonNull); // Remove campos que falharam
                        } catch (Exception e) {
                            System.err.println("Aviso: Erro ao processar tabela " + table + " - " + e.getMessage());
                            return java.util.stream.Stream.<Selection<Object>>empty();
                        }
                    })
                    .collect(Collectors.toList());
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
            case ARTISTA -> "artista"; // Para joins que vêm de ARTISTA_TAG para ARTISTA
            case ALBUM -> "album";
            case MUSICA -> "musica";
            case PAIS -> "pais";
            case TAG -> "tag";
            case ARTISTA_TAG -> "artistaTags"; // Corrigido para o nome do atributo na entidade Artista
            case SIMILARIDADE_ARTISTA -> "similaridade_artista"; // Não deve ser usado para joins
            case RANKING_ARTISTAS -> "rankingsArtistas"; // Relacionamento adicionado em Artista
            case RANKING_MUSICAS -> "rankingsMusicas"; // Relacionamento que será adicionado em Musica
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
        
        // Primeiro, cria os joins diretos do DTO
        prepareJoinTables(rootTable, joins, joinDTOSet);
        System.out.println("Joins criados após DTO: " + joins.keySet());
        
        // Depois, para cada tabela que precisa ser acessada, garante que o caminho completo existe
        for (Table targetTable : allTables) {
            if (!joins.containsKey(targetTable) && !targetTable.equals(rootTable)) {
                System.out.println("Tentando conectar " + rootTable + " -> " + targetTable);
                try {
                    List<JoinEdge> path = findPathBetweenTables(rootTable, targetTable);
                    System.out.println("Caminho encontrado: " + path.stream().map(e -> e.from + "->" + e.to).collect(Collectors.joining(", ")));
                    
                    // Cria cada join do caminho se ainda não existir
                    for (JoinEdge edge : path) {
                        if (!joins.containsKey(edge.to)) {
                            From<?, ?> from = joins.get(edge.from);
                            if (from == null) {
                                System.err.println("ERRO: From é null para " + edge.from + ". Joins disponíveis: " + joins.keySet());
                                continue;
                            }
                            String joinAttribute = getJoinAttribute(edge.to);
                            System.out.println("Criando join: " + edge.from + " -> " + edge.to + " via " + joinAttribute);
                            joins.put(edge.to, from.join(joinAttribute, edge.type));
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
        
        // Verifica se todas as tabelas necessárias estão disponíveis
        Set<Table> missingTables = allTables.stream()
                .filter(table -> !joins.containsKey(table))
                .collect(Collectors.toSet());
        
        if (!missingTables.isEmpty()) {
            System.err.println("ERRO: Tabelas não encontradas no mapa de joins: " + missingTables);
        }
    }
}
