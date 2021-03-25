import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OauthLoginService } from '../_services/oauth-login.service';

@Component({
  selector: 'app-popup-modal',
  templateUrl: './popup-modal.component.html',
  styleUrls: ['./popup-modal.component.css']
})
export class PopupModalComponent implements OnInit {
    title: string;
    btn: string;
    type: string;
    userEmail: string;

    verifyEmailForm: FormGroup;
  otpForm: FormGroup;
  isOtpVerified = false;
  resetPasswordForm: FormGroup;
  emailSent = false;
  successMsg: string;
  errorMsg: string;
  onClick = false;

  constructor(private router: Router,
              private fb: FormBuilder,
              private modalService: NgbModal,
              private authService: OauthLoginService
    ) { }

  ngOnInit(): void {
      console.log(this.title, this.btn, this.type);
      this.verifyEmailForm = this.fb.group({
        email : ['', [Validators.email, Validators.required]]
      }),
      this.otpForm = this.fb.group({
        otpNo : ['', [Validators.required]]
      }),
      this.resetPasswordForm = this.fb.group({
        password: ['', [Validators.required]],
        confirmPassword: ['', [Validators.required]]
      });
    }

    // tslint:disable-next-line: typedef
    get verifyEmailFormControl() {
      return this.verifyEmailForm.controls;
    }
    // tslint:disable-next-line: typedef
    get otpFormControl() {
      return this.otpForm.controls;
    }
    // tslint:disable-next-line: typedef
    get resetPasswordFormControl() {
      return this.resetPasswordForm.controls;
    }

    closeModal(): void {
        this.modalService.dismissAll();
    }
   
    onSubmit(): any{
      if (this.type === 'verify') {
        // tslint:disable-next-line: deprecation
        this.authService.getVerificationLink(this.verifyEmailForm.value).subscribe(
          response => {
            this.btn = 'Resend link';
          },
          error => {
            this.btn = 'Send link';
          }
        );
      } else if (this.type === 'reset') {
        // tslint:disable-next-line: deprecation
        console.log(this.verifyEmailForm.value);
        this.authService.getOtp(this.verifyEmailForm.value).subscribe(
          response => {
            this.btn = 'Resend OTP';
            this.userEmail = this.verifyEmailForm.value.email;
            console.log(this.userEmail);
            this.emailSent = true;
          },
          error => {
            this.btn = 'Send OTP';
          }
        );
      }
    }

    onOTPSubmit(): void{
      const body = {
        email: this.userEmail,
        otpNo: this.otpForm.value.otpNo
      };
      // tslint:disable-next-line: deprecation
      this.authService.submitOtp(body).subscribe(
        response => {
          this.isOtpVerified = true;
          this.onClick = true;
          console.log('otp no + email' + JSON.stringify (body));
        },
        error => {
        }
      );
    }
    resetPassword(): void{
      const body = {
        email: this.userEmail,
        password: this.resetPasswordForm.value.password
      };
      // tslint:disable-next-line: deprecation
      this.authService.resetPassword(body).subscribe(
        response => {
          console.log('reset' + body);
          console.log('reset response' + response);
          this.modalService.dismissAll();
        },
        error => {
        }
      );
    }
    // clearValues() {
    //   this.successMsg = null;
    //   this.errorMsg = null;
    // }
    // setPreRequestValues() {
    //   this.clearValues();
    // }
  }


