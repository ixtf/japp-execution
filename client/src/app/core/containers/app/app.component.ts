import {ChangeDetectionStrategy, Component, OnDestroy, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material';
import {Title} from '@angular/platform-browser';
import {Store} from '@ngrx/store';
import {TranslateService} from '@ngx-translate/core';
import 'rxjs/add/observable/merge';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {Subscription} from 'rxjs/Subscription';
import {baseWsUrl} from '../../../../environments/environment';
import {Operator} from '../../../shared/models/operator';
import {Nav} from '../../models/nav';
import {UtilService} from '../../services/util.service';
import {WebSocketService} from '../../services/web-socket.service';
import {
  coreActions, coreAuthOperator, coreIsAdmin, coreIsMobile, coreShowSidenav, coreShowToolbar,
  coreSidenavOpened
} from '../../store';

@Component({
  selector: 'jwjh-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent implements OnDestroy {
  @ViewChild('sidenav') sidenav: MatSidenav;
  sidenavOpened$: Observable<boolean>;
  showToolbar$: Observable<boolean>;
  showSidenav$: Observable<boolean>;
  navs$: Observable<Nav[]>;
  authOperator$: Observable<Operator>;
  isMobile$: Observable<boolean>;
  isAdmin$: Observable<boolean>;
  isLoggedIn$: Observable<boolean>;
  sidenavMode$: Observable<string>;
  private _subscription: Subscription;

  constructor(private store: Store<any>,
              private translate: TranslateService,
              private webSocketService: WebSocketService,
              private title: Title,
              private utilService: UtilService) {
    this.translate.setDefaultLang('zh_CN');
    this.translate.use('zh_CN');
    this.translate.get('TITLE').subscribe(title.setTitle);
    this.webSocketService.connect(`${baseWsUrl}/global`);
    this.authOperator$ = this.store.select(coreAuthOperator);
    this.isLoggedIn$ = this.authOperator$.map(it => !!it);
    this.sidenavOpened$ = this.store.select(coreSidenavOpened);
    this.showSidenav$ = this.store.select(coreShowSidenav);
    this.showToolbar$ = this.store.select(coreShowToolbar);
    this.isMobile$ = this.store.select(coreIsMobile);
    this.isAdmin$ = this.store.select(coreIsAdmin);
    this.sidenavMode$ = this.isMobile$.switchMap(isMobile => of(isMobile ? 'over' : 'side'));
  }

  closeSidenav() {
    /**
     * All state updates are handled through dispatched actions in 'container'
     * components. This provides a clear, reproducible history of state
     * updates and user interaction through the life of our
     * application.
     */
    this.store.dispatch(new coreActions.CloseSidenavAction());
  }

  openSidenav() {
    this.store.dispatch(new coreActions.OpenSidenavAction());
  }

  ngOnDestroy(): void {
    if (this._subscription) {
      this._subscription.unsubscribe();
    }
  }

  sidenavToggle() {
    if (this.utilService.isMobile()) {
      this.sidenav.toggle();
    }
  }

}
