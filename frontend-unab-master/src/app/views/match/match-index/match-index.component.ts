import { Component, OnInit } from '@angular/core';
import { HelperService } from '../../../utils/services/helper.service';
import { MatchListItem, MatchsService } from '../matchs.service';

@Component({
  selector: 'app-match-index',
  templateUrl: './match-index.component.html',
  styleUrls: ['./match-index.component.css']
})
export class MatchIndexComponent implements OnInit {

  public data: MatchListItem[] = [];

  constructor(private helperService: HelperService, private service: MatchsService) { }

  ngOnInit(): void {
    this.getData();
  }

  getData(): void {
    if (this.data.length > 0) {
      this.helperService.destroyDatatable();
    }

    this.service.getMatchs().subscribe((matchs) => {
      this.data = matchs;
      this.helperService.createDatatable();
    });
  }

  deleteMatch(id: number): void {
    this.helperService.confirmDelete(() => {
      this.service.deleteMatch(id).subscribe(() => this.getData());
    });
  }
}
