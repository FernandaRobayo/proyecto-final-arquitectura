import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HelperService, MessageType } from '../../../utils/services/helper.service';
import { TeamsService } from '../teams.service';

@Component({
  selector: 'app-team-form',
  templateUrl: './team-form.component.html',
  styleUrls: ['./team-form.component.css']
})
export class TeamFormComponent implements OnInit {

  public frm: FormGroup;
  public id: string | null = null;

  constructor(public route: ActivatedRoute, private service: TeamsService, private helperService: HelperService) {
    this.frm = new FormGroup({
      team: new FormControl(null, Validators.required)
    });
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id != null) {
      this.service.getTeamById(this.id).subscribe((team) => {
        this.frm.controls.team.setValue(team.nombre);
      });
    }
  }

  guardar(): void {
    if (this.frm.invalid) {
      this.helperService.showMessage(MessageType.ERROR, 'Existen campos vacios');
      return;
    }

    const data = {
      nombre: this.frm.controls.team.value
    };

    this.service.saveTeam(this.id, data).subscribe(
      () => {
        this.helperService.showMessage(MessageType.SUCCESS, 'Equipo guardado con exito');
        this.helperService.redirectApp('teams');
      },
      () => {
        this.helperService.showMessage(MessageType.ERROR, 'Error al guardar o modificar');
      }
    );
  }
}
