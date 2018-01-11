import {TaskBatchPageEffects} from './task-batch-page.effects';
import {TaskEvaluateEffects} from './task-evaluate.effects';
import {TaskFeedbackCommentEffects} from './task-feedback-comment.effects';
import {TaskFeedbackEffects} from './task-feedback.effects';
import {TaskHistoryPageEffect} from './task-history-page.effect';
import {TaskNoticeEffects} from './task-notice.effects';
import {TaskProgressPageEffects} from './task-progress-page.effects';
import {TaskUpdatePageEffects} from './task-update-page.effects';
import {TaskEffects} from './task.effects';

export const featureEffects = [
  TaskEffects,
  TaskNoticeEffects,
  TaskFeedbackEffects,
  TaskFeedbackCommentEffects,
  TaskEvaluateEffects,
  TaskUpdatePageEffects,
  TaskProgressPageEffects,
  TaskHistoryPageEffect,
  TaskBatchPageEffects,
];
