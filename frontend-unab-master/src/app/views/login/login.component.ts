import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthLoginRequest } from '../../models/auth-login-request.model';
import { HelperService, MessageType } from '../../utils/services/helper.service';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public frmLogin: FormGroup;
  public authError = '';
  public isSubmitting = false;
  public backendUrl = '';

  constructor(public helperService: HelperService, public service: LoginService) {
    this.frmLogin = new FormGroup({
      username: new FormControl(null, Validators.required),
      password: new FormControl(null, Validators.required)
    });
  }

  ngOnInit(): void {
    this.backendUrl = this.service.getApiUrl();
  }

  iniciarSesion(): void {
    this.authError = '';

    if (this.frmLogin.invalid) {
      this.authError = 'Completa usuario y contrasena antes de continuar.';
      this.helperService.showMessage(MessageType.WARNING, 'Existen campos vacios');
      return;
    }

    this.isSubmitting = true;

    const payload: AuthLoginRequest = {
      username: (this.frmLogin.controls.username.value || '').trim(),
      password: this.frmLogin.controls.password.value
    };

    this.service.login(payload).subscribe(
      (response) => {
        this.isSubmitting = false;
        this.helperService.showMessage(MessageType.SUCCESS, `Bienvenido, ${response.fullName}`);
        this.helperService.redirectApp('dashboard');
      },
      (error) => {
        this.isSubmitting = false;
        this.authError = this.helperService.getHttpErrorMessage(error, 'No fue posible iniciar sesion');
        this.helperService.showMessage(
          MessageType.ERROR,
          this.authError
        );
      }
    );
  }
}
