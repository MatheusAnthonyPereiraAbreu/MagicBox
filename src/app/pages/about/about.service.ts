import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface GithubUser {
    login: string;
    name: string;
    avatar_url: string;
    html_url: string;
    bio: string;
    location: string;
}

@Injectable({ providedIn: 'root' })
export class AboutService {
    private apiUrl = 'https://api.github.com/users/';

    constructor(public http: HttpClient) { }


    getUser(username: string): Observable<GithubUser> {
        return this.http.get<GithubUser>(
            `${this.apiUrl}${username}`,
            {
                headers: { Authorization: `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` }
            }
        );
    }
}