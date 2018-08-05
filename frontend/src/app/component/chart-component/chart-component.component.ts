import { Directive, Component, OnInit, Output, EventEmitter } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { D3Service, D3, Selection } from 'd3-ng2-service';
import { BackendService } from '../../service/backend.service';
import { CommunicationService } from '../../service/communication.service';
import { multi } from "../../app.component";

@Component( {
    selector: 'app-chart-component',
    templateUrl: './chart-component.component.html',
    styleUrls: ['./chart-component.component.css'],
    providers: [BackendService]
} )

export class ChartComponentComponent implements OnInit {
    d3: D3;
    backendService: BackendService;
    result: any;
    multi: any[];
    view: any[] = [600, 600];
    showXAxis = true;
    showYAxis = true;
    gradient = false;
    showLegend = false;
    showXAxisLabel = true;
    xAxisLabel : any;
    showYAxisLabel = true;
    yAxisLabel = 'No. of Requests';
    communcationService: CommunicationService;
    public showChart: boolean;
    

    colorScheme = {
        domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
    };


    constructor( private bs: BackendService, d3Service: D3Service, private cService: CommunicationService ) {
        this.d3 = d3Service.getD3();
        this.backendService = bs;
        this.communcationService = cService;
        this.backendService.getLogService().subscribe(( logs: any[] ) => {
            this.result = logs;
            if(this.result.length > 0){
                this.updateChart( "default" );
                this.showChart = true;
            }
        } );
        this.communcationService.getMessage().subscribe(( typeOfChar ) => {
            this.updateChart( typeOfChar );
        } );
    }

    updateChart( typeofChar ) {
        let groupedResult: any[];
        let vtemp = [600, 600];
        let xAxisLabelTemp : any;
        type chartObject = {
            "host": string;
            "datetime": {
                "day": string;
                "hour": string;
                "minute": string;
                "second": string;
            };
            "request": {
                "method": string;
                "url": string;
                "protocol": string;
                "protocol_version": string;
            };
            "response_code": string;
            "document_size": string;
        };

        if ( typeofChar === "request" ) {
            vtemp = [20000, 1500];
            groupedResult = this.d3.nest<chartObject>().key( function( d ) { return d.datetime.day + "-" + d.datetime.hour + ":" + d.datetime.minute; } ).entries( this.result );
            xAxisLabelTemp = '(Day-Hour:Minute)';
        }

        if ( typeofChar === "method" || typeofChar === "default" ) {
            groupedResult = this.d3.nest<chartObject>().key( function( d ) { return d.request.method; } ).entries( this.result );
            xAxisLabelTemp = 'Http Method Type';
        }

        if ( typeofChar === "codes" ) {
            groupedResult = this.d3.nest<chartObject>().key( function( d ) { return d.response_code; } ).entries( this.result );
            xAxisLabelTemp = 'Http Response Code';
        }

        if ( typeofChar === "size" ) {
            let filteredResult = this.result.filter( function( d ) { return d.document_size < 1000 && d.response_code === "200"; } );
            groupedResult = this.d3.nest<chartObject>().key( function( d ) { return d.request.method; } ).entries( filteredResult );
            xAxisLabelTemp = 'Http Method Type';
        }

        let chartArraryTemp: { name: string, value: number }[] = new Array();
        for ( let aResult of groupedResult ) {
            chartArraryTemp.push( { name: aResult.key, value: aResult.values.length } );
        }
        
        this.xAxisLabel = [...xAxisLabelTemp];
        this.view = [...vtemp];
        this.multi = [...chartArraryTemp];
    }

    ngOnInit() {
        Object.assign( this, { multi } )
    }
    

}
