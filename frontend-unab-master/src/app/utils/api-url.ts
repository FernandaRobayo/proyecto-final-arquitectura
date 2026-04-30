export function resolveApiBaseUrl(): string {
  const protocol = typeof window !== 'undefined' && window.location?.protocol
    ? window.location.protocol
    : 'http:';
  const hostname = typeof window !== 'undefined' && window.location?.hostname
    ? window.location.hostname
    : 'localhost';

  return `${protocol}//${hostname}:9090`;
}
