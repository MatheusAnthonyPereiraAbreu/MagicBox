package com.purplebox.backend.service;

import com.purplebox.backend.model.dto.*;
import com.purplebox.backend.model.dto.select.*;
import com.purplebox.backend.model.dto.table.Table;
import com.purplebox.backend.model.dto.where.Operator;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdHocService {

    private final EntityManager entityManager;

    public AdHocService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Object gerarRelatorio(AdHocDTO request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Table rootTable = request.table();
        GroupByDTO groupBy = request.groupBy();

        Map<Table, From<?, ?>> joins = new HashMap<>();
        Root<? extends Selectable> root = query.from(rootTable.toSelectable());
        joins.put(rootTable, root);
        prepareJoinTables(rootTable, joins, request.join());

        if (Objects.isNull(groupBy)) {
            List<Selection<Object>> fields = prepareSelectStatements(request.column(), joins, cb);
            query.multiselect(fields.toArray(new Selection[0]));
        } else {
            prepareGroupByStatements(groupBy, joins, query, cb);
        }

        if (Objects.nonNull(request.where()) && !request.where().isEmpty()) {
            List<Predicate> predicates = prepareWhereStatements(request.where(), joins, cb);
            query.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<Tuple> jpaQuery = entityManager.createQuery(query);
        List<Tuple> resultList = jpaQuery.getResultList();

        return mapResultsToFriendlyFormat(resultList);
    }

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

    private Expression<?> getPath(From<?, ?> from, String attribute) {
        try {
            return from.get(attribute);
        } catch (IllegalArgumentException e) {
            return from.get("id").get(attribute);
        }
    }

    private List<Selection<Object>> prepareSelectStatements(Set<ColumnDTO> columnSet, Map<Table, From<?, ?>> joins,
            CriteriaBuilder cb) {
        if (columnSet.isEmpty()) {
            return joins.keySet().stream()
                    .flatMap(table -> Select.getFromTable(table).stream()
                            .map(field -> {
                                String alias = table.attribute() + "_" + field.attribute();
                                return selectDefault(joins.get(table), field, alias);
                            }))
                    .collect(Collectors.toList());
        }

        return columnSet.stream()
                .map(col -> {
                    Table table = col.table();
                    Select field = col.field();
                    String alias = Optional.ofNullable(col.alias())
                            .filter(a -> !a.isEmpty())
                            .orElse(table.attribute() + "_" + field.attribute());
                    return selectDefault(joins.get(table), field, alias);
                })
                .collect(Collectors.toList());
    }

    private void prepareJoinTables(Table reference, Map<Table, From<?, ?>> joins, Set<JoinDTO> joinDTOSet) {
        if (!joins.containsKey(reference))
            return;

        joinDTOSet.stream()
                .filter(join -> join.from().equals(reference))
                .forEach(join -> {
                    From<?, ?> from = joins.get(join.from());
                    joins.put(join.to(), from.join(join.to().attribute(), join.type()));
                    prepareJoinTables(join.to(), joins, joinDTOSet);
                });
    }

    private Selection<Object> selectDefault(From<?, ?> from, Select field, String alias) {
        Path<Object> select = from.get(field.attribute());
        return alias != null ? select.alias(alias) : select;
    }
}
