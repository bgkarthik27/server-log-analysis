import { Component } from '@angular/core';
import { CommunicationService }     from './service/communication.service';

@Component( {
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    providers: [CommunicationService]
} )
export class AppComponent {
    title = 'app';
    communicationService: CommunicationService
    
    constructor(private cs: CommunicationService){
        this.communicationService = cs;
    }
    
    updateChart(chartFor: string){
        this.communicationService.sendMessage(chartFor);
    }
}

export var multi = [];