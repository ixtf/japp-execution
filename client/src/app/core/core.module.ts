import {NgModule, Optional, SkipSelf} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {SharedModule} from '../shared/shared.module';
import {ConfirmDialogComponent} from './components/confirm-dialog/confirm-dialog.component';
import {LoginFormComponent} from './components/login-form/login-form.component';
import {LoginWeixinComponent} from './components/login-weixin/login-weixin.component';
import {PersonalUpdateFormComponent} from './components/personal-update-form/personal-update-form.component';
import {WxQrcodeDialogComponent} from './components/wx-qrcode-dialog/wx-qrcode-dialog.component';
import {AppComponent} from './containers/app/app.component';
import {HomePageComponent} from './containers/home-page/home-page.component';
import {LoginPageComponent} from './containers/login-page/login-page.component';
import {NotFoundPageComponent} from './containers/not-found-page/not-found-page.component';
import {PersonalCenterPageComponent} from './containers/personal-center-page/personal-center-page.component';
import {CoreRoutingModule} from './core-routing.module';
import {AdminGuard} from './services/admin-guard.service';
import {ApiService} from './services/api.service';
import {AuthGuard} from './services/auth-guard.service';
import {AuthService} from './services/auth.service';
import {UtilService} from './services/util.service';
import {WebSocketService} from './services/web-socket.service';
import {WeixinGuard} from './services/weixin-guard.service';
import {WeixinService} from './services/weixin.service';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [
  ConfirmDialogComponent,
  WxQrcodeDialogComponent,
];
export const COMPONENTS = [
  AppComponent,
  LoginPageComponent,
  LoginWeixinComponent,
  LoginFormComponent,
  PersonalCenterPageComponent,
  PersonalUpdateFormComponent,
  HomePageComponent,
  NotFoundPageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    SharedModule,
    CoreRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    WeixinService,
    UtilService,
    WebSocketService,
    AuthService,
    AuthGuard,
    AdminGuard,
    WeixinGuard,
    ApiService,
  ]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error(
        'CoreModule is already loaded. Import it in the AppModule only');
    }
  }
}
