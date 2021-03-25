import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Constants } from '../constants';
import { User } from '../user';

export const TOKEN = 'token';

@Injectable({
  providedIn: 'root'
})
export class OauthLoginService {
  constructor(private http: HttpClient) { }

  // tslint:disable-next-line: typedef
  basicJwtAuthLogin(user: User) {
    return this.http.post<any>(Constants.API_BASE_URL + '/auth/login', user).pipe(
      map(
        data => {
          localStorage.setItem(TOKEN, `Bearer ${data.accessToken}`);
          return data;
        }
      )
    );
  }

  // tslint:disable-next-line: typedef
  userSignup(user: User) {
    return this.http.post<any>(Constants.API_BASE_URL + '/auth/signup', user);
  }

  // tslint:disable-next-line: typedef
  getVerificationLink(email: string) {
    return this.http.post<any>(Constants.API_BASE_URL + '/auth/send-email', email);
  }

  // tslint:disable-next-line: typedef
  getOtp(body: any) {
    console.log('service' , body);
    return this.http.post<any>(Constants.API_BASE_URL + '/auth/generate-otp', body);
  }

  // tslint:disable-next-line: typedef
  submitOtp(body: any) {
    return this.http.post<any>(Constants.API_BASE_URL + '/auth/validate-otp', body);
  }

  // tslint:disable-next-line: typedef
  resetPassword(body: any) {
    return this.http.post<any>(Constants.API_BASE_URL + '/auth/reset-password', body);
  }

  getAuthToken(): any {
    if (localStorage.getItem(TOKEN)) {
      return localStorage.getItem(TOKEN);
    }
  }
  // tslint:disable-next-line: typedef
  setAuthToken(token: string) {
    localStorage.setItem(TOKEN, token);
  }
  // tslint:disable-next-line: typedef
  isUserLoggedIn() {
    const token = localStorage.getItem(TOKEN);
    if ( token === null || token.includes('undefined')) {
      return false;
    }
    else {
      return true;
    }
  }
  removeToken(): void {
    localStorage.removeItem(TOKEN);
  }
}
