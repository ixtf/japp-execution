import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TaskGroupManagePageComponent} from './containers/task-group-manage-page/task-group-manage-page.component';
import {TaskGroupUpdatePageComponent} from './containers/task-group-update-page/task-group-update-page.component';

const routes: Routes = [
  {
    path: '',
    component: TaskGroupManagePageComponent,
  },
  {
    path: 'edit',
    component: TaskGroupUpdatePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TaskGroupRoutingModule {
}
