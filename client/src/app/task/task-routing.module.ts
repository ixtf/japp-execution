import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TaskBatchPageComponent} from './containers/task-batch-page/task-batch-page.component';
import {TaskHistoryPageComponent} from './containers/task-history-page/task-history-page.component';
import {TaskProgressPageComponent} from './containers/task-progress-page/task-progress-page.component';
import {TaskUpdatePageComponent} from './containers/task-update-page/task-update-page.component';

const routes: Routes = [
  {
    path: 'progress',
    component: TaskProgressPageComponent,
  },
  {
    path: 'history',
    component: TaskHistoryPageComponent,
  },
  {
    path: 'edit',
    component: TaskUpdatePageComponent,
  },
  {
    path: 'batch',
    component: TaskBatchPageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TaskRoutingModule {
}
