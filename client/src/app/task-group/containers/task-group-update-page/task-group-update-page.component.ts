import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {TaskGroup} from '../../../shared/models/task-group';
import {taskGroupUpdatePageActions, taskGroupUpdatePageTaskGroup, taskGroupUpdatePageTitle} from '../../store/index';

@Component({
  selector: 'jwjh-task-group-update-page',
  templateUrl: './task-group-update-page.component.html',
  styleUrls: ['./task-group-update-page.component.less']
})
export class TaskGroupUpdatePageComponent implements OnDestroy {
  title$: Observable<string>;
  taskGroup$: Observable<TaskGroup>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private router: Router,
              private route: ActivatedRoute,
              private utilService: UtilService) {
    this.taskGroup$ = this.store.select(taskGroupUpdatePageTaskGroup);
    this.title$ = this.store.select(taskGroupUpdatePageTitle);
    this.subscriptions.push(
      this.route.queryParams
        .map(it => it.id)
        .distinctUntilChanged()
        .subscribe(it => this.store.dispatch(new taskGroupUpdatePageActions.Init(it)))
    );
  }

  save(taskGroup: TaskGroup) {
    this.store.dispatch(new taskGroupUpdatePageActions.Save(taskGroup));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
