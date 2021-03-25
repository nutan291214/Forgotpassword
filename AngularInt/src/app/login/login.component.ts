import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Constants } from '../auth/constants';
import { PopupModalComponent } from '../auth/popup-modal/popup-modal.component';
import { OauthLoginService } from '../auth/_services/oauth-login.service';


@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: []
  })
export class LoginComponent implements OnInit{
    errorMessage = '';
    invalidLogin = false;
    loginForm: FormGroup;
    rememberMe = false;
    display = 'none';
    title: string;
    btn: string;
    type: string;

    constructor(private router: Router,
                private fb: FormBuilder,
                private oauthService: OauthLoginService,
                private modalService: NgbModal) {
                }

    ngOnInit(): void {
      this.loginForm = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required]
      });
      if (localStorage.getItem('email') && localStorage.getItem('password')){
        this.loginForm.patchValue({
          email: localStorage.getItem('email'),
          password: localStorage.getItem('password')
        });
      }
    }

    // tslint:disable-next-line: typedef
    get loginFormControl() {
      return this.loginForm.controls;
    }


    onSubmit(): void{
      const user = this.loginForm.value;
      // tslint:disable-next-line: deprecation
      this.oauthService.basicJwtAuthLogin(user).subscribe(
        response => {
          console.log(response);
          this.invalidLogin = false;
          this.router.navigate(['home']);
        },
        error => {
          this.invalidLogin = true;
          if (error.error.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'Unknown error occured, try after some time..';
          }
        }
      );
    }

    // toggleValue(event) {
    //   if (event.target.checked) {
    //     this.rememberMe = true;
    //   }
    // }

    // saveCredentials() {
    //   if (this.rememberMe) {
    //     localStorage.setItem('email', this.loginForm.value.email);
    //     localStorage.setItem('password', this.loginForm.value.password);
    //   }
    // }
    // openVerifyEmailModal() {
    //   this.setVerifyModalData('Verify email', 'Send link', 'verify');
    //   this.display = 'block';
    // }
    openForgotPassModal(): void {
      const ref = this.modalService.open(PopupModalComponent, { centered: true });
      ref.componentInstance.title = 'Reset Password';
      ref.componentInstance.btn = 'Send OTP';
      ref.componentInstance.type = 'reset';
    }

    openVerifyEmailModal(): void {
      const ref = this.modalService.open(PopupModalComponent, { centered: true });
      ref.componentInstance.title = 'Verify email';
      ref.componentInstance.btn = 'Send Link';
      ref.componentInstance.type = 'verify';
    }
    // eventDisplay(event) {
    //   this.display = 'none';
    // }
    // setVerifyModalData(title: string = '', btn: string = '', type: string = '') {
    //   this.verifyModalData = {
    //     title,
    //     btn,
    //     type
    //   };
    // }
}
