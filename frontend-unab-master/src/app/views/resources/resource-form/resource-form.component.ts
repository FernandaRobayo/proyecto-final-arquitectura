import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HelperService, MessageType } from '../../../utils/services/helper.service';
import { ResourceConfig, ResourceFieldConfig, ResourceRegistryService, SelectOption } from '../../../services/resource-registry.service';

@Component({
  selector: 'app-resource-form',
  templateUrl: './resource-form.component.html',
  styleUrls: ['./resource-form.component.css']
})
export class ResourceFormComponent implements OnInit {
  public config: ResourceConfig;
  public form: FormGroup = new FormGroup({});
  public options: Record<string, SelectOption[]> = {};
  public id: string | null = null;
  public loading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private helperService: HelperService,
    private resourceRegistryService: ResourceRegistryService
  ) {}

  get pageTitle(): string {
    if (!this.config) {
      return '';
    }

    return this.id ? `Editar ${this.config.singularLabel}` : `Crear ${this.config.singularLabel}`;
  }

  ngOnInit(): void {
    this.config = this.resourceRegistryService.getConfig(this.route.snapshot.data.resource);
    this.id = this.route.snapshot.paramMap.get('id');
    this.buildForm();
    this.loadOptions();

    if (this.id) {
      this.loadEntity();
    }
  }

  save(): void {
    if (this.form.invalid) {
      Object.keys(this.form.controls).forEach((key) => this.form.controls[key].markAsTouched());
      this.helperService.showMessage(MessageType.WARNING, 'Completa los campos requeridos');
      return;
    }

    const payload = this.resourceRegistryService.preparePayload(this.config, this.form.value);
    const request = this.id
      ? this.config.service.update(this.id, payload)
      : this.config.service.create(payload);

    request.subscribe(
      () => {
        this.helperService.showMessage(
          MessageType.SUCCESS,
          `${this.config.singularLabel} guardado con exito`
        );
        this.router.navigate([this.config.path]);
      },
      (error) => {
        this.helperService.showMessage(
          MessageType.ERROR,
          this.helperService.getHttpErrorMessage(error, `No fue posible guardar ${this.config.singularLabel}`)
        );
      }
    );
  }

  cancel(): void {
    this.router.navigate([this.config.path]);
  }

  hasError(field: ResourceFieldConfig): boolean {
    const control = this.form.get(field.key);
    return !!control && control.invalid && control.touched;
  }

  private buildForm(): void {
    const controls = this.config.fields.reduce((accumulator, field) => {
      accumulator[field.key] = new FormControl('', field.required ? Validators.required : []);
      return accumulator;
    }, {} as Record<string, FormControl>);

    this.form = new FormGroup(controls);
  }

  private loadOptions(): void {
    this.resourceRegistryService.loadOptions(this.config).subscribe((options) => {
      this.options = options;
    });
  }

  private loadEntity(): void {
    this.loading = true;

    this.config.service.findById(this.id as string).subscribe(
      (entity) => {
        this.form.patchValue(this.resourceRegistryService.mapEntityToFormValues(this.config, entity));
        this.loading = false;
      },
      (error) => {
        this.loading = false;
        this.helperService.showMessage(
          MessageType.ERROR,
          this.helperService.getHttpErrorMessage(error, `No fue posible cargar ${this.config.singularLabel}`)
        );
        this.router.navigate([this.config.path]);
      }
    );
  }
}
