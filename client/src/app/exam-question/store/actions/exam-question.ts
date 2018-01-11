import {Action} from '@ngrx/store';
import {ExamQuestion} from '../../../shared/models/exam-question';

export const DELETE = '[ExamQuestion] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public id: string) {
  }
}

export const DELETE_SUCCESS = '[ExamQuestion] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public id: string) {
  }
}

export const LIST = '[ExamQuestion] LIST';

export class ListExamQuestion implements Action {
  readonly type = LIST;

  constructor(public labId: string) {
  }
}

export const LIST_SUCCESS = '[ExamQuestion] LIST_SUCCESS';

export class ListExamQuestionSuccess implements Action {
  readonly type = LIST_SUCCESS;

  constructor(public labId: string, public examQuestions: ExamQuestion[]) {
  }
}


export type Actions
  = Delete
  | DeleteSuccess
  | ListExamQuestion
  | ListExamQuestionSuccess;
