import { Component, DoCheck, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements DoCheck {
  public localStorage: Storage = localStorage;

  constructor(@Inject(DOCUMENT) private document: Document, private router: Router) {}

  get isAuthenticated(): boolean {
    return this.localStorage.getItem('token') != null;
  }

  get isAuthScreen(): boolean {
    return this.router.url.startsWith('/login') || this.router.url.startsWith('/register');
  }

  get showAppShell(): boolean {
    return this.isAuthenticated && !this.isAuthScreen;
  }

  ngDoCheck(): void {
    const body = this.document.body;
    body.classList.remove('hold-transition', 'sidebar-mini', 'login-page');
    body.classList.add('hold-transition');
    body.classList.add(this.showAppShell ? 'sidebar-mini' : 'login-page');
  }

  logout(): void {
    this.localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
