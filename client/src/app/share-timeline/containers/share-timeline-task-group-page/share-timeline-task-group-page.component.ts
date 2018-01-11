import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/scan';
import {Observable} from 'rxjs/Observable';
import {combineLatest} from 'rxjs/observable/combineLatest';
import {shareTimelineUrls} from '../../../../environments/environment';
import {isWeixinBrowser, UtilService} from '../../../core/services/util.service';
import {WeixinService} from '../../../core/services/weixin.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {MyTaskGroup, TaskGroup} from '../../../shared/models/task-group';
import {
  shareTimelineTaskGroupChartOptions, shareTimelineTaskGroupFinishedTaskEntities,
  shareTimelineTaskGroupPageActions, shareTimelineTaskGroupPageShareOperator, shareTimelineTaskGroupPageTaskGroup,
  shareTimelineTaskGroupPageTasks, shareTimelineTaskGroupUnStartedTaskEntities,
} from '../../store';

@Component({
  selector: 'jwjh-share-timeline-task-group-page',
  templateUrl: './share-timeline-task-group-page.component.html',
  styleUrls: ['./share-timeline-task-group-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ShareTimelineTaskGroupPageComponent implements OnInit, OnDestroy {
  @HostBinding('class.jwjh-page') b1 = true;
  @HostBinding('class.jwjh-share-timeline-task-group-page') b2 = true;
  shareOperator$: Observable<Operator>;
  taskGroup$: Observable<TaskGroup>;
  tasks$: Observable<Task[]>;
  chartOptions$: Observable<any>;
  finishedTaskEntities$: Observable<{ [id: string]: Task }>;
  unStartedTaskEntities$: Observable<{ [id: string]: Task }>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private route: ActivatedRoute,
              private weixinService: WeixinService,
              private utilService: UtilService) {
    this.shareOperator$ = this.store.select(shareTimelineTaskGroupPageShareOperator);
    this.taskGroup$ = this.store.select(shareTimelineTaskGroupPageTaskGroup);
    this.tasks$ = this.store.select(shareTimelineTaskGroupPageTasks);
    this.chartOptions$ = this.store.select(shareTimelineTaskGroupChartOptions);
    this.finishedTaskEntities$ = this.store.select(shareTimelineTaskGroupFinishedTaskEntities);
    this.unStartedTaskEntities$ = this.store.select(shareTimelineTaskGroupUnStartedTaskEntities);

    this.subscriptions.push(
      route.queryParams.scan((acc, cur) => {
        store.dispatch(new shareTimelineTaskGroupPageActions.Init(acc, cur));
        return cur;
      }, null).subscribe(),
    );
  }

  getTaskItemColor$(task: Task): Observable<string> {
    return combineLatest(
      this.finishedTaskEntities$,
      this.unStartedTaskEntities$,
      (finishedTaskEntities, unStartedTaskEntities) => {
        if (finishedTaskEntities[task.id]) {
          return 'limegreen';
        }
        if (unStartedTaskEntities[task.id]) {
          return 'darkgrey';
        }
        return 'lightskyblue';
      }
    );
  }

  ngOnInit() {
    setTimeout(() => {
      $('#mcover').css('display', 'none');
    }, 3000);

    this.subscriptions.push(
      this.taskGroup$.filter(it => !!it)
        .subscribe(it => this.wxConfig(it))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

  private wxConfig(taskGroup: TaskGroup) {
    if (isWeixinBrowser() && taskGroup && taskGroup.id !== MyTaskGroup.id) {
      const imgUrlToken = taskGroup.logo && taskGroup.logo.downloadToken;
      let shareData = {
        title: `我正在参加“${taskGroup.name}”`,
      };
      this.subscriptions.push(
        this.utilService.wxJsConfig().subscribe(() => {
          const link = `${shareTimelineUrls.taskGroup}${this.route.snapshot.queryParams.token}`;
          shareData = Object.assign(shareData, {link});
          if (imgUrlToken) {
            const imgUrl = `http://www.wjh001.com/execution/api/opens/downloads/${imgUrlToken}/image`;
            shareData = Object.assign(shareData, {imgUrl});
          }
          this.utilService.onWxShareData(shareData);
        })
      );
    }
  }
}
