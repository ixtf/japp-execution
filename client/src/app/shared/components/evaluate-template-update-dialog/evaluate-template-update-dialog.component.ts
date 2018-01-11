import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {EvaluateTemplate} from '../../models/evaluate-template';
import {EvaluateTemplateService} from '../../services/evaluate-template.service';

@Component({
  selector: 'jwjh-evaluate-template-update-dialog',
  templateUrl: './evaluate-template-update-dialog.component.html',
  styleUrls: ['./evaluate-template-update-dialog.component.less']
})
export class EvaluateTemplateUpdateDialogComponent {
  title: string;
  evaluateTemplate: EvaluateTemplate;

  constructor(private evaluateTemplateService: EvaluateTemplateService,
              private dialogRef: MatDialogRef<EvaluateTemplateUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.evaluateTemplate = Object.assign(new EvaluateTemplate(), data.evaluateTemplate);
    this.title = 'NAV.EVALUATE-TEMPLATE-' + (this.evaluateTemplate.id ? 'UPDATE' : 'CREATE');
  }

  submit() {
    this.evaluateTemplateService.save(this.evaluateTemplate)
      .subscribe(res => this.dialogRef.close(res));
  }

}
