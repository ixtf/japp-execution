import {Action} from '@ngrx/store';
import {ExamQuestionLab} from '../../../shared/models/exam-question-lab';

export const SAVE = '[ExamQuestionLab] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public lab: ExamQuestionLab) {
  }
}

export const CREATE_SUCCESS = '[ExamQuestionLab] CREATE_SUCCESS';

export class CreateSuccess implements Action {
  readonly type = CREATE_SUCCESS;

  constructor(public lab: ExamQuestionLab) {
  }
}

export const UPDATE_SUCCESS = '[ExamQuestionLab] UPDATE_SUCCESS';

export class UpdateSuccess implements Action {
  readonly type = UPDATE_SUCCESS;

  constructor(public lab: ExamQuestionLab) {
  }
}

export const DELETE = '[ExamQuestionLab] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public id: string) {
  }
}

export const DELETE_SUCCESS = '[ExamQuestionLab] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public id: string) {
  }
}

export const DELETE_PARTICIPANT = '[ExamQuestionLab] DELETE_PARTICIPANT';

export class DeleteParticipant implements Action {
  readonly type = DELETE_PARTICIPANT;

  constructor(public labId: string, public participantId: string) {
  }
}

export const DELETE_PARTICIPANT_SUCCESS = '[ExamQuestionLab] DELETE_PARTICIPANT_SUCCESS';

export class DeleteParticipantSuccess implements Action {
  readonly type = DELETE_PARTICIPANT_SUCCESS;

  constructor(public labId: string, public participantId) {
  }
}


export type Actions
  = Delete
  | DeleteSuccess
  | CreateSuccess
  | UpdateSuccess
  | DeleteParticipant
  | DeleteParticipantSuccess;
