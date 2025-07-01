package com.magicbox.backend.model.dto.where;

public enum Operator {
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_EQUALS,
    LESS_THAN_EQUALS,
    LIKE;

    public static Operator getOperator(String symbol) {

        return switch (symbol) {
            case "=" -> Operator.EQUALS;
            case "!=" -> Operator.NOT_EQUALS;
            case ">" -> Operator.GREATER_THAN;
            case "<" -> Operator.LESS_THAN;
            case ">=" -> Operator.GREATER_THAN_EQUALS;
            case "<=" -> Operator.LESS_THAN_EQUALS;
            default -> valueOf(symbol);
        };
    }

}
