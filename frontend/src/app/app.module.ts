import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatStepperModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
} from '@angular/material';
import { RouterModule  } from '@angular/router';
import { ChartComponentComponent } from './component/chart-component/chart-component.component';
import { NgxChartsModule } from "@swimlane/ngx-charts/release";
import { D3Service } from "d3-ng2-service";


@NgModule( {
    declarations: [
        AppComponent,
        ChartComponentComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        MatButtonModule,
        MatMenuModule,
        MatToolbarModule,
        MatIconModule,
        MatCardModule,
        BrowserAnimationsModule,
        NgxChartsModule,
        MatSidenavModule,
        MatButtonToggleModule,
        MatProgressSpinnerModule
    ],
    providers: [D3Service],
    bootstrap: [AppComponent]
} )
export class AppModule { }
