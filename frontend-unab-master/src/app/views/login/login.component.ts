import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HelperService, MessageType } from '../../utils/services/helper.service';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public frmLogin: FormGroup;

  constructor(public helperService: HelperService, public service: LoginService) {
    this.frmLogin = new FormGroup({
      usuario: new FormControl(null, Validators.required),
      password: new FormControl(null, Validators.required)
    });
  }

  ngOnInit(): void {}

  iniciarSesion(): void {
    if (this.frmLogin.invalid) {
      this.helperService.showMessage(MessageType.WARNING, 'Existen campos vacios');
      return;
    }

    this.service.login(this.frmLogin.controls.usuario.value, this.frmLogin.controls.password.value).subscribe(
      (response) => {
        localStorage.setItem('token', response.access_token);
        this.helperService.redirectApp('matchs');
      },
      () => {
        this.helperService.showMessage(MessageType.ERROR, 'Usuario o contrasena invalidos');
      }
    );
  }
}
