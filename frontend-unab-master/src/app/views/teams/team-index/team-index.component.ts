import { Component, OnInit } from '@angular/core';
import { Team } from '../../../utils/entitys/team.entity';
import { HelperService } from '../../../utils/services/helper.service';
import { TeamsService } from '../teams.service';

@Component({
  selector: 'app-team-index',
  templateUrl: './team-index.component.html',
  styleUrls: ['./team-index.component.css']
})
export class TeamIndexComponent implements OnInit {

  public data: Team[] = [];

  constructor(private helperService: HelperService, private service: TeamsService) { }

  ngOnInit(): void {
    this.getData();
  }

  getData(): void {
    if (this.data.length > 0) {
      this.helperService.destroyDatatable();
    }

    this.service.getTeams().subscribe((teams) => {
      this.data = teams;
      this.helperService.createDatatable();
    });
  }

  deleteTeam(id: number): void {
    this.helperService.confirmDelete(() => {
      this.service.deleteTeam(id).subscribe(() => this.getData());
    });
  }
}
