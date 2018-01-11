import {createSelector} from '@ngrx/store';
import {ExamQuestion} from '../../../shared/models/exam-question';
import {ExamQuestionLab} from '../../../shared/models/exam-question-lab';
import {examQuestionActions, examQuestionLabActions, examQuestionLabPageActions} from '../actions';

export interface State {
  labEntities: { [id: string]: ExamQuestionLab };
  examQuestions: ExamQuestion[];
  labId: string | null;
}

const initialState: State = {
  labEntities: {},
  examQuestions: [],
  labId: '0',
};

export function reducer(state = initialState, action: examQuestionLabPageActions.Actions | examQuestionActions.Actions | examQuestionLabActions.Actions): State {
  switch (action.type) {
    case examQuestionLabPageActions.INIT_SUCCESS: {
      const {labId, labs} = action;
      const labEntities = {...state.labEntities};
      (labs || []).forEach(lab => {
        lab = Object.assign(new ExamQuestionLab(), state.labEntities[lab.id], lab);
        labEntities[lab.id] = lab;
      });
      return {...state, labId, labEntities};
    }

    case examQuestionLabPageActions.LIST_EXAM_QUESTION_SUCCESS: {
      return {...state, ...action};
    }

    case examQuestionActions.DELETE_SUCCESS: {
      const examQuestions = state.examQuestions.filter(it => it.id !== action.id);
      return {...state, examQuestions};
    }

    case examQuestionLabActions.CREATE_SUCCESS:
    case examQuestionLabActions.UPDATE_SUCCESS: {
      const {lab} = action;
      const labEntities = {...state.labEntities};
      labEntities[lab.id] = lab;
      return {...state, labEntities};
    }

    default:
      return state;
  }
}

export const getLabEntities = (state: State) => state.labEntities;
export const getLabId = (state: State) => state.labId;
export const getExamQuestions = (state: State) => state.examQuestions.sort((o1, o2) => o1.name.localeCompare(o2.name));
export const getLabs = createSelector(getLabEntities, entities => Object.keys(entities).map(id => entities[id]));
export const getLab = createSelector(getLabEntities, getLabId, (entities, id) => entities[id]);
