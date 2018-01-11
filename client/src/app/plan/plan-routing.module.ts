import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PlanManagePageComponent} from './containers/plan-manage-page/plan-manage-page.component';
import {PlanUpdatePageComponent} from './containers/plan-update-page/plan-update-page.component';

const routes: Routes = [
  {
    path: '',
    component: PlanManagePageComponent,
  },
  {
    path: 'edit',
    component: PlanUpdatePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PlanRoutingModule {
}
