import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {createSelector} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import 'rxjs/add/operator/mergeMap';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {Subject} from 'rxjs/Subject';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskBatchService} from '../../services/task-batch.service';
import {TaskService} from '../../services/task.service';

interface State {
  operatorEntities: { [id: string]: Operator };
  nickNameEntities: { [id: string]: string[] };
}

const initialState: State = {
  operatorEntities: {},
  nickNameEntities: {},
};

const getOperatorEntities = (state: State) => state.operatorEntities;
const getNickNameEntities = (state: State) => state.nickNameEntities;
const getOperators = createSelector(getOperatorEntities, entities => Object.keys(entities).map(it => entities[it]).sort((o1, o2) => o1.id.localeCompare(o2.id)));

@Component({
  selector: 'jwjh-nicknames-update-dialog',
  templateUrl: './nicknames-update-dialog.component.html',
  styleUrls: ['./nicknames-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NicknamesUpdateDialogComponent implements OnDestroy {
  readonly title = 'BUTTON.UPDATE-NICKNAME';
  readonly tasks: Task[];
  private subscriptions = [];
  private readonly onGetTaskOperatorContextData = new Subject<{ task: Task, operator: Operator }>();
  private readonly state$ = new BehaviorSubject(initialState);
  readonly operators$ = this.state$.map(getOperators);
  private readonly updates: { [id: string]: string } = {};

  constructor(private taskService: TaskService,
              private taskBatchService: TaskBatchService,
              private dialogRef: MatDialogRef<NicknamesUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.tasks = data.tasks;
    this.onGetTaskOperatorContextData
      .distinct(action => action.task.id + action.operator.id)
      .mergeMap(
        action => this.taskService.getOperatorContextData(action.task.id, action.operator.id),
        (action, res) => ({...action, nickName: res.nickName}),
      )
      .subscribe(res => {
        const operator = res.operator;
        const nickName = res.nickName || operator.name;
        let {operatorEntities, nickNameEntities} = this.state$.value;
        operatorEntities = Object.assign({[operator.id]: operator}, operatorEntities);
        const nickNames = nickNameEntities[operator.id] || [];
        if (nickNames.indexOf(nickName) < 0) {
          nickNames.push(nickName);
        }
        nickNameEntities = Object.assign(nickNameEntities, {[operator.id]: nickNames});
        this.state$.next({operatorEntities, nickNameEntities});
      });
    this.tasks.forEach(task => {
      let operators = [task.charger];
      operators = operators.concat(task.participants || []);
      operators = operators.concat(task.followers || []);
      operators.forEach(operator => this.onGetTaskOperatorContextData.next({task, operator}));
    });
  }

  static open(dialog: MatDialog, data: { tasks: Task[] }) {
    dialog.open(NicknamesUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  submit() {
    if (Object.keys(this.updates).length > 0) {
      this.taskBatchService.updateNickNames(this.tasks, this.updates)
        .subscribe(() => this.dialogRef.close());
    } else {
      this.dialogRef.close();
    }
  }

  getNickNamesClass(operator: Operator): string {
    return this.updates[operator.id] ? 'nick-name-update' : '';
  }

  getOperatorNickNames(operator: Operator): Observable<string[] | string> {
    const nickName = this.updates[operator.id];
    if (nickName) {
      return of(nickName);
    }
    return this.state$.map(getNickNameEntities)
      .map(nickNameEntities => nickNameEntities[operator.id]);
  }

  updateNickName(operator: Operator) {
    const nickName = prompt('', this.updates[operator.id]);
    if (nickName) {
      this.updates[operator.id] = nickName;
    } else {
      delete this.updates[operator.id];
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
