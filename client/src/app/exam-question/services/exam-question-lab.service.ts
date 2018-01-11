import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {CurdService} from '../../core/services/curd.service';
import {ExamQuestion} from '../../shared/models/exam-question';
import {ExamQuestionLab} from '../../shared/models/exam-question-lab';
import {ExamQuestionLabInvite} from '../../shared/models/exam-question-lab-invite';

@Injectable()
export class ExamQuestionLabService extends CurdService<ExamQuestionLab, string> {
  constructor(http: HttpClient, private store: Store<any>) {
    super(http, 'examQuestionLabs');
  }

  listExamQuestion(labId: string): Observable<ExamQuestion[]> {
    return this.http.get(`${this.baseUrl}/${labId}/examQuestions`);
  }

  inviteTicket(labId: string): Observable<ExamQuestionLabInvite> {
    return this.http.get(`${this.baseUrl}/${labId}/inviteTicket`);
  }

  deleteParticipant(labId: string, participantId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${labId}/participants/${participantId}`);
  }
}
