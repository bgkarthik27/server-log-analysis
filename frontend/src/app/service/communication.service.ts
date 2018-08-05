import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';


@Injectable()
export class CommunicationService {

    messageReceiver = new Subject <string>();

    sendMessage( mission: string ) {
        this.messageReceiver.next( mission );
    }

    getMessage(){
        return this.messageReceiver.asObservable();
    }
}
