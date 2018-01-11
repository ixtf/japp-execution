import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/scan';
import {Observable} from 'rxjs/Observable';
import {shareTimelineUrls} from '../../../../environments/environment';
import {isWeixinBrowser, UtilService} from '../../../core/services/util.service';
import {WeixinService} from '../../../core/services/weixin.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {MyTaskGroup} from '../../../shared/models/task-group';
import {TaskOperatorContextData} from '../../../shared/models/task-operator-context-data';
import {
  shareTimelineTaskPageActions, shareTimelineTaskPageShareOperator, shareTimelineTaskPageTask,
  shareTimelineTaskPageTaskOperatorContextDatas
} from '../../store';

@Component({
  selector: 'jwjh-share-timeline-task-page',
  templateUrl: './share-timeline-task-page.component.html',
  styleUrls: ['./share-timeline-task-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ShareTimelineTaskPageComponent implements OnDestroy {
  @HostBinding('class.jwjh-page') b1 = true;
  @HostBinding('class.jwjh-share-timeline-task-page') b2 = true;
  shareOperator$: Observable<Operator>;
  task$: Observable<Task>;
  taskOperatorContextDatas$: Observable<TaskOperatorContextData[]>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private route: ActivatedRoute,
              private weixinService: WeixinService,
              private utilService: UtilService) {
    this.shareOperator$ = store.select(shareTimelineTaskPageShareOperator);
    this.task$ = store.select(shareTimelineTaskPageTask).do(it => this.wxConfig(it));
    this.taskOperatorContextDatas$ = store.select(shareTimelineTaskPageTaskOperatorContextDatas);

    this.subscriptions.push(
      route.queryParams.scan((acc, cur) => {
        store.dispatch(new shareTimelineTaskPageActions.Init(acc, cur));
        return cur;
      }, null).subscribe(),

      this.task$.filter(it => {
        return isWeixinBrowser() && it && it.taskGroup.id !== MyTaskGroup.id;
      }).subscribe(it => this.wxConfig(it)),
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

  private wxConfig(task: Task) {
    if (isWeixinBrowser() && task && task.taskGroup.id !== MyTaskGroup.id) {
      const imgUrlToken = task.taskGroup.logo && task.taskGroup.logo.downloadToken;
      let shareData = {
        title: `我正在参加“${task.title}”`,
      };
      this.subscriptions.push(
        this.utilService.wxJsConfig().subscribe(() => {
          const link = `${shareTimelineUrls.task}${this.route.snapshot.queryParams.token}`;
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
