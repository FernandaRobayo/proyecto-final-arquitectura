import { TestBed } from '@angular/core/testing';
import { LoginGuard } from './login.guard';

describe('LoginGuard', () => {
  let guard: LoginGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoginGuard]
    });

    guard = TestBed.inject(LoginGuard);
    localStorage.clear();
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should allow access when there is no token', () => {
    expect(guard.canActivate({} as any, {} as any)).toBe(true);
  });

  it('should allow access even when there is a token', () => {
    localStorage.setItem('token', 'abc');
    expect(guard.canActivate({} as any, {} as any)).toBe(true);
  });
});
