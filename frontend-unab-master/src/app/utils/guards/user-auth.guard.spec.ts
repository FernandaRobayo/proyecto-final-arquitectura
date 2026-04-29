import { TestBed } from '@angular/core/testing';
import { HelperService } from '../services/helper.service';
import { UserAuthGuard } from './user-auth.guard';

describe('UserAuthGuard', () => {
  let guard: UserAuthGuard;
  let helperServiceSpy: { redirectApp: any };

  beforeEach(() => {
    helperServiceSpy = {
      redirectApp: jasmine.createSpy('redirectApp')
    };

    TestBed.configureTestingModule({
      providers: [
        UserAuthGuard,
        { provide: HelperService, useValue: helperServiceSpy }
      ]
    });

    guard = TestBed.inject(UserAuthGuard);
    localStorage.clear();
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should allow access when there is a token', () => {
    localStorage.setItem('token', 'abc');

    expect(guard.canActivate({} as any, {} as any)).toBe(true);
    expect(helperServiceSpy.redirectApp).not.toHaveBeenCalled();
  });

  it('should redirect to login when there is no token', () => {
    expect(guard.canActivate({} as any, {} as any)).toBe(false);
    expect(helperServiceSpy.redirectApp).toHaveBeenCalledWith('login');
  });
});
