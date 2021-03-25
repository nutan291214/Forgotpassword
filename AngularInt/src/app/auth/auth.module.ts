// Angular
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { AuthComponent } from './auth.component';
import { LoginComponent } from '../login/login.component';
import { PopupModalComponent } from './popup-modal/popup-modal.component';
import { SignupComponent } from '../signup/signup.component';
import { OauthLoginService } from './_services/oauth-login.service';


const routes: Routes = [];





@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule.forChild(routes),
    ],
    providers: [
        OauthLoginService
    ],
    exports: [AuthComponent,
        LoginComponent,
        SignupComponent,
        PopupModalComponent],
    declarations: [
        AuthComponent,
        LoginComponent,
        SignupComponent,
        PopupModalComponent
    ],
})

export class AuthModule {
}
