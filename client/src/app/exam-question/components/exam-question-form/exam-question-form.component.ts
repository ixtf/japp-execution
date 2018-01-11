import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output,} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ExamQuestion} from '../../../shared/models/exam-question';

@Component({
  selector: 'jwjh-exam-question-form',
  templateUrl: './exam-question-form.component.html',
  styleUrls: ['./exam-question-form.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExamQuestionFormComponent {
  examQuestionForm: FormGroup;
  @Output()
  onSubmit = new EventEmitter<ExamQuestion>();

  constructor(private fb: FormBuilder) {
    this.examQuestionForm = fb.group({
      'id': null,
      'name': [null, Validators.required],
      'question': [null, Validators.required],
      'answer': [null, Validators.required],
      'lab': null,
    });
  }

  private _examQuestion: ExamQuestion;

  get examQuestion(): ExamQuestion {
    return this._examQuestion;
  }

  @Input()
  set examQuestion(value: ExamQuestion) {
    this._examQuestion = value;
    this.examQuestionForm.patchValue(value || new ExamQuestion());
  }

  get name() {
    return this.examQuestionForm.get('name');
  }

  get question() {
    return this.examQuestionForm.get('question');
  }

  get answer() {
    return this.examQuestionForm.get('answer');
  }

  submit() {
    this.onSubmit.emit(this.examQuestionForm.value);
  }
}
