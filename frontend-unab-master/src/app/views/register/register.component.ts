import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HelperService, MessageType } from '../../utils/services/helper.service';
import { RegisterService } from './register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  public frmRegister: FormGroup;

  constructor(public helperService: HelperService, public service: RegisterService) {
    this.frmRegister = new FormGroup({
      nombre: new FormControl(null, Validators.required),
      correo: new FormControl(null, Validators.required),
      usuario: new FormControl(null, Validators.required),
      password: new FormControl(null, Validators.required)
    });
  }

  ngOnInit(): void {
  }

  registrar(): void {
    if (this.frmRegister.invalid) {
      this.helperService.showMessage(MessageType.WARNING, 'Existen campos vacios');
      return;
    }

    this.service.registerUser(
      this.frmRegister.controls.nombre.value,
      this.frmRegister.controls.correo.value,
      this.frmRegister.controls.usuario.value,
      this.frmRegister.controls.password.value
    ).subscribe(
      () => {
        this.helperService.redirectApp('login');
      },
      () => {
        this.helperService.showMessage(MessageType.ERROR, 'Error al registrar el usuario');
      }
    );
  }
}
