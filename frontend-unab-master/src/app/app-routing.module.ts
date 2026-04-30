import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginGuard } from './utils/guards/login.guard';
import { UserAuthGuard } from './utils/guards/user-auth.guard';
import { DashboardComponent } from './views/dashboard/dashboard.component';
import { LoginComponent } from './views/login/login.component';
import { NotFoundComponent } from './views/not-found/not-found.component';
import { ResourceFormComponent } from './views/resources/resource-form/resource-form.component';
import { ResourceIndexComponent } from './views/resources/resource-index/resource-index.component';

const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login',component: LoginComponent, canActivate: [LoginGuard]},
  {path: 'not-found', component: NotFoundComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [UserAuthGuard]},
  {path: 'customers', component: ResourceIndexComponent, canActivate: [UserAuthGuard], data: { resource: 'customers' }},
  {path: 'customers/create', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'customers' }},
  {path: 'customers/edit/:id', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'customers' }},
  {path: 'species', component: ResourceIndexComponent, canActivate: [UserAuthGuard], data: { resource: 'species' }},
  {path: 'species/create', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'species' }},
  {path: 'species/edit/:id', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'species' }},
  {path: 'breeds', component: ResourceIndexComponent, canActivate: [UserAuthGuard], data: { resource: 'breeds' }},
  {path: 'breeds/create', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'breeds' }},
  {path: 'breeds/edit/:id', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'breeds' }},
  {path: 'pets', component: ResourceIndexComponent, canActivate: [UserAuthGuard], data: { resource: 'pets' }},
  {path: 'pets/create', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'pets' }},
  {path: 'pets/edit/:id', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'pets' }},
  {path: 'veterinarians', component: ResourceIndexComponent, canActivate: [UserAuthGuard], data: { resource: 'veterinarians' }},
  {path: 'veterinarians/create', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'veterinarians' }},
  {path: 'veterinarians/edit/:id', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'veterinarians' }},
  {path: 'appointments', component: ResourceIndexComponent, canActivate: [UserAuthGuard], data: { resource: 'appointments' }},
  {path: 'appointments/create', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'appointments' }},
  {path: 'appointments/edit/:id', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'appointments' }},
  {path: 'medical-records', component: ResourceIndexComponent, canActivate: [UserAuthGuard], data: { resource: 'medical-records' }},
  {path: 'medical-records/create', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'medical-records' }},
  {path: 'medical-records/edit/:id', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'medical-records' }},
  {path: 'treatments', component: ResourceIndexComponent, canActivate: [UserAuthGuard], data: { resource: 'treatments' }},
  {path: 'treatments/create', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'treatments' }},
  {path: 'treatments/edit/:id', component: ResourceFormComponent, canActivate: [UserAuthGuard], data: { resource: 'treatments' }},
  {path: '**', redirectTo: 'not-found', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
