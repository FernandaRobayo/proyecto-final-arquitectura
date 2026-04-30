import { Component, OnInit } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Appointment } from '../../models/appointment.model';
import { AppointmentService } from '../../services/appointment.service';
import { PetService } from '../../services/pet.service';
import { TreatmentService } from '../../services/treatment.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  public petsRegistered = 0;
  public appointmentsToday = 0;
  public activePatients = 0;
  public treatmentsInProgress = 0;
  public nextAppointment = 'No appointments scheduled';
  public assignedVet = 'Not assigned';
  public appointmentStatus = 'No status';

  constructor(
    private petService: PetService,
    private appointmentService: AppointmentService,
    private treatmentService: TreatmentService
  ) { }

  ngOnInit(): void {
    this.loadDashboardMetrics();
  }

  private loadDashboardMetrics(): void {
    forkJoin({
      pets: this.petService.findAll().pipe(catchError(() => of([]))),
      appointments: this.appointmentService.findAll().pipe(catchError(() => of([]))),
      treatments: this.treatmentService.findAll().pipe(catchError(() => of([])))
    }).subscribe(({ pets, appointments, treatments }) => {
      const todayAppointments = appointments.filter((appointment) => this.isToday(appointment.appointmentDateTime));
      const nextAppointment = this.resolveNextAppointment(todayAppointments);

      this.petsRegistered = pets.length;
      this.appointmentsToday = todayAppointments.length;
      this.activePatients = 0;
      this.treatmentsInProgress = treatments.length;
      this.nextAppointment = nextAppointment
        ? `${nextAppointment.petName || 'Pet'} - ${this.formatTime(nextAppointment.appointmentDateTime)}`
        : 'No appointments scheduled';
      this.assignedVet = nextAppointment?.veterinarianFullName || 'Not assigned';
      this.appointmentStatus = nextAppointment?.status || 'No status';
    });
  }

  private isToday(value?: string): boolean {
    if (!value) {
      return false;
    }

    const appointmentDate = new Date(value);
    const today = new Date();

    if (Number.isNaN(appointmentDate.getTime())) {
      return false;
    }

    return appointmentDate.getFullYear() === today.getFullYear()
      && appointmentDate.getMonth() === today.getMonth()
      && appointmentDate.getDate() === today.getDate();
  }

  private resolveNextAppointment(appointments: Appointment[]): Appointment | undefined {
    if (!appointments.length) {
      return undefined;
    }

    const now = Date.now();
    const sortedAppointments = appointments
      .filter((appointment) => !Number.isNaN(new Date(appointment.appointmentDateTime).getTime()))
      .sort((left, right) => new Date(left.appointmentDateTime).getTime() - new Date(right.appointmentDateTime).getTime());

    return sortedAppointments.find((appointment) => new Date(appointment.appointmentDateTime).getTime() >= now)
      || sortedAppointments[0];
  }

  private formatTime(value?: string): string {
    if (!value) {
      return '--:--';
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return '--:--';
    }

    return date.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: '2-digit'
    });
  }
}
