import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/scan';
import {Observable} from 'rxjs/Observable';
import {shareTimelineUrls} from '../../../../environments/environment';
import {isWeixinBrowser, UtilService} from '../../../core/services/util.service';
import {WeixinService} from '../../../core/services/weixin.service';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {
  shareTimelineTaskClockPageChartOptions, shareTimelineTaskClockPageDays, shareTimelineTaskClockPageNickName,
  shareTimelineTaskClockPagePercent, shareTimelineTaskClockPageTask, shareTimelineTaskClockPageTaskGroup,
  shareTimelineTaskClockPageTasks,
} from '../../store';

@Component({
  selector: 'jwjh-share-timeline-task-clock-page',
  templateUrl: './share-timeline-task-clock-page.component.html',
  styleUrls: ['./share-timeline-task-clock-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ShareTimelineTaskClockPageComponent implements OnInit, OnDestroy {
  @HostBinding('class.jwjh-page') b1 = true;
  @HostBinding('class.jwjh-share-timeline-task-clock-page') b2 = true;
  nickName$: Observable<string>;
  days$: Observable<number>;
  percent$: Observable<number>;
  totalCount$: Observable<number>;
  taskGroup$: Observable<TaskGroup>;
  task$: Observable<Task>;
  tasks$: Observable<Task[]>;
  chartOptions$: Observable<any>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private route: ActivatedRoute,
              private weixinService: WeixinService,
              private utilService: UtilService) {
    this.nickName$ = this.store.select(shareTimelineTaskClockPageNickName);
    this.days$ = this.store.select(shareTimelineTaskClockPageDays);
    this.percent$ = this.store.select(shareTimelineTaskClockPagePercent);
    this.taskGroup$ = this.store.select(shareTimelineTaskClockPageTaskGroup);
    this.task$ = this.store.select(shareTimelineTaskClockPageTask);
    this.tasks$ = this.store.select(shareTimelineTaskClockPageTasks);
    this.chartOptions$ = this.store.select(shareTimelineTaskClockPageChartOptions);
    this.totalCount$ = this.tasks$.map(it => it && it.length);
  }

  get signImgUrl$(): Observable<string> {
    return this.taskGroup$
      .filter(it => it && it.sign && it.sign.downloadToken && isWeixinBrowser())
      .map(it => `http://www.wjh001.com/execution/api/opens/downloads/${it.sign.downloadToken}/image`);
  }

  ngOnInit() {
    setTimeout(() => {
      $('#mcover').css('display', 'none');
    }, 3000);

    this.subscriptions.push(
      this.utilService.wxJsConfig()
        .switchMap(() => this.task$)
        .filter(it => it && it.id && it.taskGroup && isWeixinBrowser())
        .subscribe(task => {
          const taskGroup = task.taskGroup;
          const imgUrlToken = taskGroup.logo && taskGroup.logo.downloadToken;
          let shareData = {
            title: `我正在参加“${taskGroup.name}”`,
          };
          const link = `${shareTimelineUrls.taskClock}${this.route.snapshot.queryParams.token}`;
          shareData = Object.assign(shareData, {link});
          if (imgUrlToken) {
            const imgUrl = `http://www.wjh001.com/execution/api/opens/downloads/${imgUrlToken}/image`;
            shareData = Object.assign(shareData, {imgUrl});
          }
          this.utilService.onWxShareData(shareData);
        })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }
}
