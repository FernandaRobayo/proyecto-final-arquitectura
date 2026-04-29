import { Location } from '@angular/common';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';

declare const $: any;

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
    setTimeout(() => {
      $("#table").DataTable({
        paging: true,
        lengthChange: false,
        searching: true,
        ordering: true,
        info: true,
        autoWidth: false,
        responsive: true,
        language: {
          url: '//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Spanish.json'
        }
      });
    }, 500);
  }

  destroyDatatable(): void {
    if ($.fn.DataTable.isDataTable('#table')) {
      $('#table').DataTable().destroy();
    }
  }

  confirmDelete(callback: () => void): void {
    Swal.fire({
      title: 'Seguro que desea realizar esta accion?',
      text: 'Esta accion no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#F8E12E',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        callback();
      }
    });
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
}

export const MessageType = {
  SUCCESS: 'S',
  WARNING: 'W',
  ERROR: 'E'
};
