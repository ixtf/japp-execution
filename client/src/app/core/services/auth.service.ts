import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {Authenticate, AuthenticateResponse} from '../../shared/models/authenticate';

@Injectable()
export class AuthService {
  protected baseUrl = baseApiUrl + '/auth';

  constructor(private http: HttpClient,
              private router: Router) {
  }

  get(): Observable<AuthenticateResponse> {
    return this.http.get(this.baseUrl);
  }

  login(authenticate: Authenticate): Observable<AuthenticateResponse> {
    return this.http.post(this.baseUrl, authenticate);
  }

  logout(): Observable<any> {
    return this.http.get(`${baseApiUrl}/logout`);
  }

  navigateAfterLoginSuccess(): Promise<boolean> {
    let url = localStorage.getItem('_LoginAfterUrl_') || '/';
    if (url.indexOf('login') >= 0) {
      url = '/';
    }
    return this.router.navigateByUrl(url);
  }

  setAfterLoginSuccess(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): void {
    localStorage.setItem('_LoginAfterUrl_', state.url);
  }

}
