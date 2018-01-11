import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {Channel} from '../../../shared/models/channel';
import {planUpdatePagePlan} from '../../store';

@Component({
  selector: 'jwjh-plan-update-page',
  templateUrl: './plan-update-page.component.html',
  styleUrls: ['./plan-update-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlanUpdatePageComponent implements OnDestroy {
  readonly channels$: Observable<Channel[]>;
  form: FormGroup;
  private subscriptions: Subscription[] = [];

  constructor(private store: Store<any>,
              private router: Router,
              private fb: FormBuilder,
              private utilService: UtilService,
              private apiService: ApiService) {
    this.form = fb.group({
      'id': null,
      'channel': [null, Validators.required],
      'name': [null, Validators.required],
      'note': [null, Validators.required],
      'shared': true,
      'items': [null, [Validators.required, Validators.min(1)]],
    });
    this.channels$ = this.apiService.listChannel();
    this.subscriptions.push(
      this.store.select(planUpdatePagePlan)
        .subscribe(plan => this.form.patchValue(plan)),
    );
  }

  get name() {
    return this.form.get('name');
  }

  get note() {
    return this.form.get('note');
  }

  get shared() {
    return this.form.get('shared');
  }

  get items() {
    return this.form.get('items');
  }

  submit(): void {
    this.apiService.savePlan(this.form.value)
      .subscribe(() => {
        this.utilService.showSuccess();
        this.router.navigate(['/plans']);
      }, error => {
        this.utilService.showError(error);
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
