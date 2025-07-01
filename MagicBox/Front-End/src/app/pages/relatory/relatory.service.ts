import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export type Table = string;

export interface Select {
    name: string;
}

export enum JoinType {
    INNER = 'INNER',
    LEFT = 'LEFT',
    RIGHT = 'RIGHT'
}

export enum Operator {
    EQUALS = '=',
    NOT_EQUALS = '!=',
    GREATER_THAN = '>',
    LESS_THAN = '<',
    GREATER_EQUAL = '>=',
    LESS_EQUAL = '<=',
    LIKE = 'LIKE',
    IN = 'IN'
}

export enum Aggregation {
    COUNT = 'COUNT',
    SUM = 'SUM',
    AVG = 'AVG',
    MAX = 'MAX',
    MIN = 'MIN'
}

export interface JoinDTO {
    from: Table;
    to: Table;
    type: JoinType;
}

export interface ColumnDTO {
    table: Table;
    field: string;
    alias: string;
}

export interface WhereDTO {
    table: Table;
    field: string;
    operator: Operator;
    value: string;
}

export interface AggregationDTO {
    table: Table;
    field: string;
    aggregation: Aggregation;
    alias: string;
}

export interface GroupByDTO {
    columnSet: ColumnDTO[];
    aggregation: AggregationDTO;
}

export interface AdHocDTO {
    table: Table;
    join: JoinDTO[];
    column: ColumnDTO[];
    where: WhereDTO[];
    groupBy: GroupByDTO | null;
}

@Injectable({
    providedIn: 'root'
})
export class RelatoryService {
    private readonly apiUrl = 'http://localhost:8080/api/relatorios';
    private readonly httpOptions = {
        headers: new HttpHeaders({
            'Accept': 'application/json'
        })
    };

    constructor(private http: HttpClient) { }

    postAdHoc(dto: AdHocDTO): Observable<any> {
        return this.http.post(`${this.apiUrl}/ad-hoc`, dto, this.httpOptions);
    }
} 