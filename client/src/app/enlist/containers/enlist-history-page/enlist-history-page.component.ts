import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material';
import {Store} from '@ngrx/store';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {Enlist} from '../../../shared/models/enlist';
import {
  enlistActions, enlistHistoryPageActions, historyPageCount, historyPagePageSiz,
  historyPageTasks
} from '../../store';

@Component({
  selector: 'jwjh-enlist-history-page',
  templateUrl: './enlist-history-page.component.html',
  styleUrls: ['./enlist-history-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistHistoryPageComponent extends DataSource<Enlist> implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  enlistDataSource = this;
  displayedColumns = ['title', 'paymentMerchant', 'btns'];
  enlists$: Observable<Enlist[]>;
  count$: Observable<number>;
  pageSize$: Observable<number>;

  constructor(private store: Store<any>,
              private translate: TranslateService,
              private utilService: UtilService) {
    super();
    this.enlists$ = store.select(historyPageTasks);
    this.count$ = store.select(historyPageCount);
    this.pageSize$ = store.select(historyPagePageSiz);
    this.store.dispatch(new enlistHistoryPageActions.List(0, 10));
  }

  ngOnInit(): void {
    // todo  provider MdPaginatorIntl
    this.translate.get('PAGINATOR.ITEMSPERPAGELABEL')
      .subscribe(s => this.paginator._intl.itemsPerPageLabel = s);
  }

  restart(task: Enlist) {
    this.store.dispatch(new enlistActions.Restart(task.id));
  }

  delete(task: Enlist) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new enlistActions.Delete(task.id)));
  }

  connect(collectionViewer: CollectionViewer): Observable<Enlist[]> {
    return this.enlists$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

  onPage(ev: PageEvent) {
    const pageSize = ev.pageSize;
    const first = ev.pageIndex * pageSize;
    this.store.dispatch(new enlistHistoryPageActions.List(first, pageSize));
  }

}
