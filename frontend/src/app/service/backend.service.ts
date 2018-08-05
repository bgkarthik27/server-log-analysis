import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable()
export class BackendService {
    httpClient: HttpClient;

    constructor( private http: HttpClient ) {
        this.httpClient = http;
    }

    getLogService() {
        return this.httpClient.get( "https://server-log-analysis.herokuapp.com/logs" );
    }


}
