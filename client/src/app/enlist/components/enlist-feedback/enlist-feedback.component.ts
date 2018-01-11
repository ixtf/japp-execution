import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Store} from '@ngrx/store';
import {TranslateService} from '@ngx-translate/core';
import 'rxjs/add/operator/take';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {UtilService} from '../../../core/services/util.service';
import {coreAuthOperator} from '../../../core/store/index';
import {Enlist} from '../../../shared/models/enlist';
import {EnlistFeedback} from '../../../shared/models/enlist-feedback';
import {Field} from '../../../shared/models/field';
import {Operator} from '../../../shared/models/operator';
import {enlistFeedbackActions} from '../../store';
import {EnlistFeedbackUpdateDialogComponent} from '../enlist-feedback-update-dialog/enlist-feedback-update-dialog.component';

@Component({
  selector: 'jwjh-enlist-feedback',
  templateUrl: './enlist-feedback.component.html',
  styleUrls: ['./enlist-feedback.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistFeedbackComponent {
  @Input()
  enlist: Enlist;
  @Input()
  enlistFeedback: EnlistFeedback;
  private authOperator$: Observable<Operator>;

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private translate: TranslateService,
              private utilService: UtilService) {
    this.authOperator$ = this.store.select(coreAuthOperator);
  }

  get payHref(): string {
    return `http://www.wjh001.com/execution/weixin/pay/paymentMerchants/${this.enlist.paymentMerchant.id}?enlistFeedbackId=${this.enlistFeedback.id}`;
  }

  get isShowPayHref$(): Observable<boolean> {
    return this.store.select(coreAuthOperator).take(1)
      .map(it => it.id === this.enlistFeedback.creator.id && !this.isPaySuccessed);
  }

  get isPaySuccessed(): boolean {
    return this.enlistFeedback && this.enlistFeedback.payment && this.enlistFeedback.payment.successed;
  }

  get isShowEdit$(): Observable<boolean> {
    return this.store.select(coreAuthOperator).take(1)
      .map(it => it.id === this.enlistFeedback.creator.id);
  }

  get isShowDelete$(): Observable<boolean> {
    return this.authOperator$.take(1).map(it => !this.isPaySuccessed && it.id === this.enlistFeedback.creator.id);
  }

  edit() {
    EnlistFeedbackUpdateDialogComponent.open(this.dialog, {enlist: this.enlist, enlistFeedback: this.enlistFeedback});
  }

  delete() {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new enlistFeedbackActions.Delete(this.enlist.id, this.enlistFeedback.id)));
  }

  getFieldValue(field: Field): Observable<string | any> {
    this.enlistFeedback.fieldsValue = this.enlistFeedback.fieldsValue || [];
    const fieldValue = this.enlistFeedback.fieldsValue.find(it => it.id === field.id);
    switch (field.type) {
      case 'SELECT_MULTIPLE': {
        fieldValue.valuesString = fieldValue.valuesString || [];
        return of(fieldValue && fieldValue.valuesString);
      }
      case 'BOOLEAN': {
        const b = fieldValue && fieldValue.valueString;
        if (b === 'true') {
          return this.translate.get('COMMON.YES');
        }
        return this.translate.get('COMMON.NO');
      }
      default:
        return of(fieldValue && fieldValue.valueString);
    }
  }

}
