import { environment } from '../../environments/environment';

export function resolveApiBaseUrl(): string {
  const configuredBaseUrl = (environment.apiBaseUrl || '').trim();

  if (configuredBaseUrl) {
    return configuredBaseUrl.replace(/\/+$/, '');
  }

  if (environment.production) {
    return '';
  }

  const protocol = typeof window !== 'undefined' && window.location?.protocol
    ? window.location.protocol
    : 'http:';
  const hostname = typeof window !== 'undefined' && window.location?.hostname
    ? window.location.hostname
    : 'localhost';

  return `${protocol}//${hostname}:9090`;
}
