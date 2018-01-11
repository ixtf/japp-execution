import {HttpClient} from '@angular/common/http';
import {MetaReducer} from '@ngrx/store';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

export const environment = {
  production: true,
};

export function TranslateLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, 'assets/i18n/');
}

export const metaReducers: MetaReducer<any>[] = [];

export const baseUrl = 'www.wjh001.com/execution';
export const baseApiUrl = `http://${baseUrl}/api`;
export const baseWsUrl = `ws://${baseUrl}`;
export const baseShareTimelineUrl = `http://${baseUrl}/shareTimeline`;
export const shareTimelineUrls = {
  task: `${baseShareTimelineUrl}/task?token=`,
  taskClock: `${baseShareTimelineUrl}/taskClock?token=`,
  taskGroup: `${baseShareTimelineUrl}/taskGroup?token=`,
};
