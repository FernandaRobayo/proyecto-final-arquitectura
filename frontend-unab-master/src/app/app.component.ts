import { Component, DoCheck, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
import { ResourceNavigationItem, ResourceRegistryService } from './services/resource-registry.service';
import { LoginService } from './views/login/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements DoCheck {
  public navItems: ResourceNavigationItem[] = [];

  constructor(
    @Inject(DOCUMENT) private document: Document,
    private router: Router,
    private loginService: LoginService,
    private resourceRegistryService: ResourceRegistryService
  ) {
    this.navItems = this.resourceRegistryService.getNavigationItems();
  }

  get isAuthenticated(): boolean {
    return this.loginService.isAuthenticated();
  }

  get isAuthScreen(): boolean {
    return this.router.url.startsWith('/login');
  }

  get showAppShell(): boolean {
    return this.isAuthenticated && !this.isAuthScreen;
  }

  get fullName(): string {
    return this.loginService.getFullName();
  }

  ngDoCheck(): void {
    const body = this.document.body;
    body.classList.remove('hold-transition', 'sidebar-mini', 'login-page');
    body.classList.add('hold-transition');
    body.classList.add(this.showAppShell ? 'sidebar-mini' : 'login-page');
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }
}
