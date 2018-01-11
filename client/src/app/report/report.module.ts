import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {ChartModule} from '../chart/chart.module';
import {SharedModule} from '../shared/shared.module';
import {MyReportPageComponent} from './containers/my-report-page/my-report-page.component';
import {ReportRoutingModule} from './report-routing.module';
import {ReportService} from './services/report.service';
import {featureName, reducers} from './store';
import {MyReportPageEffects} from './store/effects/my-report-page.effects';

export const ENTRYCOMPONENTS = [];
export const COMPONENTS = [
  MyReportPageComponent,
  ...ENTRYCOMPONENTS,
];

@NgModule({
  imports: [
    SharedModule,
    ChartModule,
    ReportRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature([MyReportPageEffects]),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    ReportService,
  ]
})
export class ReportModule {
}
