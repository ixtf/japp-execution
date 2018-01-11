import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {FeedbackTemplate} from '../../models/feedback-template';
import {FeedbackTemplateService} from '../../services/feedback-template.service';

@Component({
  selector: 'jwjh-feedback-template-update-dialog',
  templateUrl: './feedback-template-update-dialog.component.html',
  styleUrls: ['./feedback-template-update-dialog.component.less']
})
export class FeedbackTemplateUpdateDialogComponent {
  title: string;
  feedbackTemplate: FeedbackTemplate;

  constructor(private feedbackTemplateService: FeedbackTemplateService,
              private dialogRef: MatDialogRef<FeedbackTemplateUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.feedbackTemplate = Object.assign(new FeedbackTemplate(), data.feedbackTemplate);
    this.title = 'NAV.FEEDBACK-TEMPLATE-' + (this.feedbackTemplate.id ? 'UPDATE' : 'CREATE');
  }

  submit() {
    this.feedbackTemplateService.save(this.feedbackTemplate)
      .subscribe(res => this.dialogRef.close(res));
  }

}
