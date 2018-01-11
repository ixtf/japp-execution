import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomePageComponent} from './containers/home-page/home-page.component';
import {LoginPageComponent} from './containers/login-page/login-page.component';
import {PersonalCenterPageComponent} from './containers/personal-center-page/personal-center-page.component';
import {AdminGuard} from './services/admin-guard.service';
import {AuthGuard} from './services/auth-guard.service';
import {WeixinGuard} from './services/weixin-guard.service';

const routes: Routes = [
  {
    path: '',
    component: HomePageComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginPageComponent,
  },
  {
    path: 'personal',
    component: PersonalCenterPageComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'tasks',
    loadChildren: '../task/task.module#TaskModule',
    canActivate: [AuthGuard],
  },
  {
    path: 'enlists',
    loadChildren: '../enlist/enlist.module#EnlistModule',
    canActivate: [AuthGuard],
  },
  {
    path: 'taskGroups',
    loadChildren: '../task-group/task-group.module#TaskGroupModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'reports',
    loadChildren: '../report/report.module#ReportModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'plans',
    loadChildren: '../plan/plan.module#PlanModule',
    canActivate: [AuthGuard],
  },
  {
    path: 'examQuestions',
    loadChildren: '../exam-question/exam-question.module#ExamQuestionModule',
    canActivate: [AuthGuard],
  },
  {
    path: 'shareTimeline',
    loadChildren: '../share-timeline/share-timeline.module#ShareTimelineModule',
  },
  {
    path: 'admins',
    loadChildren: '../admin/admin.module#AdminModule',
    canActivate: [AuthGuard, AdminGuard],
  },
  {
    path: 'weixin/pay',
    loadChildren: '../weixin-pay/weixin-pay.module#WeixinPayModule',
    canActivate: [AuthGuard, WeixinGuard],
  },
  {
    path: '**',
    redirectTo: '/tasks/progress',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class CoreRoutingModule {
}
