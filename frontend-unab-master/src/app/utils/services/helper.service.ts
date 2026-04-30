import { Location } from '@angular/common';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class HelperService {

  constructor(private router: Router, public location: Location, private toast: ToastrService) {}

  redirectApp(url: string): void {
    this.router.navigate([url]);
  }

  onClickBack(): void {
    this.location.back();
  }

  createDatatable(): void {
    // Datatables was removed from the main flow to keep the UI lightweight.
  }

  destroyDatatable(): void {
    // Datatables was removed from the main flow to keep the UI lightweight.
  }

  confirmDelete(callback: () => void): void {
    if (window.confirm('Esta accion no se puede deshacer. Deseas continuar?')) {
      callback();
    }
  }

  showMessage(type: string, message: string, title: string = 'Mensaje del sistema'): void {
    switch (type) {
      case MessageType.ERROR:
        this.toast.error(message, title);
        break;
      case MessageType.SUCCESS:
        this.toast.success(message, title);
        break;
      case MessageType.WARNING:
        this.toast.warning(message, title);
        break;
      default:
        break;
    }
  }

  getHttpErrorMessage(error: any, fallback: string): string {
    if (error?.status === 0) {
      return 'No fue posible conectar con el backend. Verifica que el servicio este activo en el puerto 9090.';
    }

    if (error?.status === 401) {
      return 'Usuario o contrasena invalidos.';
    }

    if (error?.status === 403) {
      return 'No tienes permisos para realizar esta accion.';
    }

    if (error?.status === 404) {
      return 'El recurso solicitado no existe o no esta disponible.';
    }

    if (error?.error?.message) {
      return error.error.message;
    }

    if (error?.message) {
      return error.message;
    }

    return fallback;
  }
}

export const MessageType = {
  SUCCESS: 'S',
  WARNING: 'W',
  ERROR: 'E'
};
