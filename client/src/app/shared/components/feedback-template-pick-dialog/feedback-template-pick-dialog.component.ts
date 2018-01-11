import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Observable} from 'rxjs/Observable';
import {DefaultCompare} from '../../../core/services/util.service';
import {FeedbackTemplate} from '../../models/feedback-template';
import {FeedbackTemplateService} from '../../services/feedback-template.service';
import {FeedbackTemplateUpdateDialogComponent} from '../feedback-template-update-dialog/feedback-template-update-dialog.component';

@Component({
  selector: 'jwjh-feedback-template-pick-dialog',
  templateUrl: './feedback-template-pick-dialog.component.html',
  styleUrls: ['./feedback-template-pick-dialog.component.less']
})
export class FeedbackTemplatePickDialogComponent {
  feedbackTemplates$: Observable<FeedbackTemplate[]>;

  constructor(private dialog: MatDialog,
              private feedbackTemplateService: FeedbackTemplateService,
              private dialogRef: MatDialogRef<FeedbackTemplatePickDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.feedbackTemplates$ = feedbackTemplateService.list()
      .map(res => res.sort((o1, o2) => DefaultCompare(o1, o2)));
  }

  select(feedbackTemplate: FeedbackTemplate) {
    this.dialogRef.close(feedbackTemplate);
  }

  update(feedbackTemplate: FeedbackTemplate, ev: Event) {
    ev.stopPropagation();
    this.dialog.open(FeedbackTemplateUpdateDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {feedbackTemplate}
    }).afterClosed()
      .filter(it => it)
      .subscribe(it => this.dialogRef.close(it));
  }

}
