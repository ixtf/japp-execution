import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ShareTimelineTaskClockPageComponent} from './containers/share-timeline-task-clock-page/share-timeline-task-clock-page.component';
import {ShareTimelineTaskGroupPageComponent} from './containers/share-timeline-task-group-page/share-timeline-task-group-page.component';
import {ShareTimelineTaskPageComponent} from './containers/share-timeline-task-page/share-timeline-task-page.component';

const routes: Routes = [
  {
    path: 'task',
    component: ShareTimelineTaskPageComponent,
  },
  {
    path: 'taskClock',
    component: ShareTimelineTaskClockPageComponent,
  },
  {
    path: 'taskGroup',
    component: ShareTimelineTaskGroupPageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ShareTimelineRoutingModule {
}
