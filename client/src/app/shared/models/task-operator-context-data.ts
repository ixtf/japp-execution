import {Operator} from './operator';

export interface TaskOperatorContextData {
  operator: Operator;
  nickName: string;
  weixinShareTimeline: boolean;
  weixinShareTimelineDateTime: Date | number;
  lastReadDateTime: Date | number;
  neverRead: boolean;
  readCount: number;
  taskFeedbackCount: number;
  taskFeedbackUnreadCount: number;
  taskFeedbackCommentCount: number;
  taskFeedbackCommentUnreadCount: number;
}
