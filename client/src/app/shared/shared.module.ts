import {CdkTableModule} from '@angular/cdk/table';
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  MatButtonModule, MatCardModule, MatCheckboxModule, MatDatepickerModule, MatDialogModule, MatExpansionModule,
  MatIconModule, MatInputModule, MatListModule, MatMenuModule, MatNativeDateModule, MatPaginatorModule,
  MatProgressBarModule, MatProgressSpinnerModule, MatSelectModule, MatSidenavModule, MatSnackBarModule, MatTableModule,
  MatTabsModule, MatToolbarModule, MatTooltipModule
} from '@angular/material';
import {RouterModule} from '@angular/router';
import {TranslateModule} from '@ngx-translate/core';
import {EvaluateFieldInputComponent} from './components/evaluate-field-input/evaluate-field-input.component';
import {EvaluateFieldUpdateDialogComponent} from './components/evaluate-field-update-dialog/evaluate-field-update-dialog.component';
import {EvaluateTemplateInputComponent} from './components/evaluate-template-input/evaluate-template-input.component';
import {EvaluateTemplatePickDialogComponent} from './components/evaluate-template-pick-dialog/evaluate-template-pick-dialog.component';
import {EvaluateTemplateUpdateDialogComponent} from './components/evaluate-template-update-dialog/evaluate-template-update-dialog.component';
import {FeedbackTemplateFieldsValueComponent} from './components/feedback-template-fields-value/feedback-template-fields-value.component';
import {FeedbackTemplateInputComponent} from './components/feedback-template-input/feedback-template-input.component';
import {FeedbackTemplatePickDialogComponent} from './components/feedback-template-pick-dialog/feedback-template-pick-dialog.component';
import {FeedbackTemplateUpdateDialogComponent} from './components/feedback-template-update-dialog/feedback-template-update-dialog.component';
import {FieldInputComponent} from './components/field-input/field-input.component';
import {FieldUpdateDialogComponent} from './components/field-update-dialog/field-update-dialog.component';
import {PickOperatorDialogComponent} from './components/pick-operator-dialog/pick-operator-dialog.component';
import {MyPaginatorIntl} from './my-paginator-intl';
import {EvaluateTemplateService} from './services/evaluate-template.service';
import {FeedbackTemplateService} from './services/feedback-template.service';
import {OperatorAvatarPipe} from './services/operator-avatar.pipe';
import {OperatorNicknamePipe} from './services/operator-nickname.pipe';
import {ShareTimelineUrlPipe} from './services/share-timeline-url.pipe';

export const ENTRYCOMPONENTS = [
  FieldUpdateDialogComponent,
  FeedbackTemplatePickDialogComponent,
  FeedbackTemplateUpdateDialogComponent,
  FeedbackTemplateFieldsValueComponent,
  EvaluateTemplatePickDialogComponent,
  EvaluateTemplateUpdateDialogComponent,
  EvaluateFieldUpdateDialogComponent,
  PickOperatorDialogComponent,
];
export const COMPONENTS = [
  OperatorAvatarPipe,
  OperatorNicknamePipe,
  ShareTimelineUrlPipe,
  FieldInputComponent,
  FeedbackTemplateInputComponent,
  EvaluateTemplateInputComponent,
  EvaluateFieldInputComponent,
  ...ENTRYCOMPONENTS
];
export const IMPORTS = [
  RouterModule,
  CommonModule,
  FormsModule,
  ReactiveFormsModule,
  TranslateModule,
  FlexLayoutModule,
  CdkTableModule,
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatDatepickerModule,
  MatDialogModule,
  MatExpansionModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatSelectModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTooltipModule,
];

@NgModule({
  declarations: COMPONENTS,
  entryComponents: ENTRYCOMPONENTS,
  imports: IMPORTS,
  exports: [IMPORTS, ...COMPONENTS],
  providers: [
    MyPaginatorIntl,
    FeedbackTemplateService,
    EvaluateTemplateService,
    // {provide: DateAdapter, useValue: MyDateAdapter},
  ]
})
export class SharedModule {
}
