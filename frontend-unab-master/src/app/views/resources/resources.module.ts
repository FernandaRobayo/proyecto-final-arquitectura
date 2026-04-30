import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { GeneralModule } from '../general/general.module';
import { ResourceFormComponent } from './resource-form/resource-form.component';
import { ResourceIndexComponent } from './resource-index/resource-index.component';

@NgModule({
  declarations: [
    ResourceIndexComponent,
    ResourceFormComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule,
    GeneralModule
  ],
  exports: [
    ResourceIndexComponent,
    ResourceFormComponent
  ]
})
export class ResourcesModule { }
