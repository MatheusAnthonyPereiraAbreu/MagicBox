import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class HomeService {
    private readonly apiUrl = 'http://localhost:8080/api/relatorios';

    constructor(private http: HttpClient) { }

    getTopMusicas(): Observable<any> {
        return this.http.get(`${this.apiUrl}/top-musicas`);
    }
} 