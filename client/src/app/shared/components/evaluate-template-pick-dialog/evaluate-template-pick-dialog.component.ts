import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Observable} from 'rxjs/Observable';
import {EvaluateTemplate} from '../../models/evaluate-template';
import {FeedbackTemplate} from '../../models/feedback-template';
import {EvaluateTemplateService} from '../../services/evaluate-template.service';
import {EvaluateTemplateUpdateDialogComponent} from '../evaluate-template-update-dialog/evaluate-template-update-dialog.component';

@Component({
  selector: 'jwjh-evaluate-template-pick-dialog',
  templateUrl: './evaluate-template-pick-dialog.component.html',
  styleUrls: ['./evaluate-template-pick-dialog.component.less']
})
export class EvaluateTemplatePickDialogComponent {
  evaluateTemplates$: Observable<FeedbackTemplate[]>;

  constructor(private dialog: MatDialog,
              private evaluateTemplateService: EvaluateTemplateService,
              private dialogRef: MatDialogRef<EvaluateTemplatePickDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.evaluateTemplates$ = evaluateTemplateService.list();
  }

  select(evaluateTemplate: EvaluateTemplate, ev: Event) {
    this.dialogRef.close(evaluateTemplate);
  }

  update(evaluateTemplate: EvaluateTemplate, ev: Event) {
    this.dialog.open(EvaluateTemplateUpdateDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {evaluateTemplate}
    }).afterClosed()
      .filter(it => it)
      .subscribe(it => this.dialogRef.close(it));
  }
}
