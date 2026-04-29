import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Team } from '../../../utils/entitys/team.entity';
import { User } from '../../../utils/entitys/user.entity';
import { HelperService, MessageType } from '../../../utils/services/helper.service';
import { TeamsService } from '../../teams/teams.service';
import { MatchsService } from '../matchs.service';

@Component({
  selector: 'app-match-form',
  templateUrl: './match-form.component.html',
  styleUrls: ['./match-form.component.css']
})
export class MatchFormComponent implements OnInit {

  public listUsers: User[] = [];
  public listTeams: Team[] = [];
  public frm: FormGroup;
  public id: string | null = null;

  constructor(
    public serviceTeam: TeamsService,
    public service: MatchsService,
    private helperService: HelperService,
    private route: ActivatedRoute
  ) {
    this.frm = new FormGroup({
      usuario: new FormControl(null, Validators.required),
      local: new FormControl(null, Validators.required),
      visitante: new FormControl(null, Validators.required),
      fecha: new FormControl(null, Validators.required),
      goleslocal: new FormControl(null, Validators.required),
      golesvisitante: new FormControl(null, Validators.required)
    });
    this.id = this.route.snapshot.paramMap.get('id');
  }

  ngOnInit(): void {
    this.cargarListas();
    if (this.id != null) {
      this.service.getMatchById(this.id).subscribe((match) => {
        this.frm.patchValue({
          usuario: match.usuario,
          local: match.local,
          visitante: match.visitante,
          fecha: match.fecha,
          goleslocal: match.golesLocal,
          golesvisitante: match.golesVisitante
        });
      });
    }
  }

  cargarListas(): void {
    this.service.getUsers().subscribe((users) => {
      this.listUsers = users;
    });
    this.serviceTeam.getTeams().subscribe((teams) => {
      this.listTeams = teams;
    });
  }

  guardar(): void {
    if (this.frm.invalid) {
      this.helperService.showMessage(MessageType.ERROR, 'Existen campos vacios');
      return;
    }

    const data = {
      usuario: {
        id: this.frm.controls.usuario.value
      },
      local: {
        id: this.frm.controls.local.value
      },
      visitante: {
        id: this.frm.controls.visitante.value
      },
      fecha: this.frm.controls.fecha.value,
      golesLocal: this.frm.controls.goleslocal.value,
      golesVisitante: this.frm.controls.golesvisitante.value
    };

    this.service.saveMatch(this.id, data).subscribe(
      () => {
        this.helperService.showMessage(MessageType.SUCCESS, 'Partido guardado con exito');
        this.helperService.redirectApp('matchs');
      },
      () => {
        this.helperService.showMessage(MessageType.ERROR, 'Error al guardar o modificar');
      }
    );
  }
}
