import {Action} from '@ngrx/store';
import {ExamQuestion} from '../../../shared/models/exam-question';
import {ExamQuestionLab} from '../../../shared/models/exam-question-lab';

export const INIT = '[ExamQuestionLabPage] SET_LAB_ID';

export class Init implements Action {
  readonly type = INIT;

  constructor(public labId?: string) {
  }
}

export const INIT_SUCCESS = '[ExamQuestionLabPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public labId: string, public labs: ExamQuestionLab[]) {
  }
}

export const LIST_EXAM_QUESTION = '[ExamQuestionLabPage] LIST_EXAM_QUESTION';

export class ListExamQuestion implements Action {
  readonly type = LIST_EXAM_QUESTION;

  constructor(public labId: string) {
  }
}

export const LIST_EXAM_QUESTION_SUCCESS = '[ExamQuestionLabPage] LIST_EXAM_QUESTION_SUCCESS';

export class ListExamQuestionSuccess implements Action {
  readonly type = LIST_EXAM_QUESTION_SUCCESS;

  constructor(public labId: string, public examQuestions: ExamQuestion[]) {
  }
}

export const INVITE = '[ExamQuestionLabPage] INVITE';

export class Invite implements Action {
  readonly type = INVITE;

  constructor(public labId: string) {
  }
}


export type Actions
  = Init
  | InitSuccess
  | Invite
  | ListExamQuestion
  | ListExamQuestionSuccess;
