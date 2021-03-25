import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthModule } from './auth/auth.module';
import { OauthLoginService } from './auth/_services/oauth-login.service';
import { HttpClientModule } from '@angular/common/http';
import { HttpIntercpterService } from './auth/_services/http-intercpter.service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    AuthModule,
    HttpClientModule
  ],
  providers: [
    OauthLoginService,
    HttpIntercpterService

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
