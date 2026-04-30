import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HelperService, MessageType } from '../../../utils/services/helper.service';
import { ResourceColumnConfig, ResourceConfig, ResourceRegistryService } from '../../../services/resource-registry.service';

@Component({
  selector: 'app-resource-index',
  templateUrl: './resource-index.component.html',
  styleUrls: ['./resource-index.component.css']
})
export class ResourceIndexComponent implements OnInit {
  public config: ResourceConfig;
  public items: any[] = [];
  public loading = false;

  constructor(
    private route: ActivatedRoute,
    private helperService: HelperService,
    private resourceRegistryService: ResourceRegistryService
  ) {}

  ngOnInit(): void {
    this.config = this.resourceRegistryService.getConfig(this.route.snapshot.data.resource);
    this.loadItems();
  }

  loadItems(): void {
    this.loading = true;

    this.config.service.findAll().subscribe(
      (items) => {
        this.items = items;
        this.loading = false;
      },
      (error) => {
        this.loading = false;
        this.helperService.showMessage(
          MessageType.ERROR,
          this.helperService.getHttpErrorMessage(error, `No fue posible cargar ${this.config.label}`)
        );
      }
    );
  }

  deleteItem(item: any): void {
    this.helperService.confirmDelete(() => {
      this.config.service.delete(item.id).subscribe(
        () => {
          this.helperService.showMessage(MessageType.SUCCESS, `${this.config.singularLabel} eliminado con exito`);
          this.loadItems();
        },
        (error) => {
          this.helperService.showMessage(
            MessageType.ERROR,
            this.helperService.getHttpErrorMessage(error, `No fue posible eliminar ${this.config.singularLabel}`)
          );
        }
      );
    });
  }

  formatValue(item: any, column: ResourceColumnConfig): string {
    const value = item[column.key];

    if (value == null || value === '') {
      return '-';
    }

    if (column.type === 'date') {
      return new Date(value).toLocaleDateString('es-CO');
    }

    if (column.type === 'datetime') {
      return new Date(value).toLocaleString('es-CO');
    }

    return `${value}`;
  }
}
