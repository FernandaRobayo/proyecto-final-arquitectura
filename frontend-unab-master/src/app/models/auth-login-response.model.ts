export interface AuthLoginResponse {
  username: string;
  fullName: string;
  roles: string[];
  tokenType: string;
  accessToken: string;
}
