import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {ChartComponent} from './components/chart/chart.component';

export const COMPONENTS = [
  ChartComponent,
];

@NgModule({
  imports: [
    SharedModule,
  ],
  declarations: COMPONENTS,
  exports: COMPONENTS,
})
export class ChartModule {
}
