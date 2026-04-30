const browserHost = typeof window !== 'undefined' && window.location?.hostname
  ? window.location.hostname
  : 'localhost';
const apiBaseUrl = `http://${browserHost}:9090`;

export const environment = {
  production: true,
  apiUrl: apiBaseUrl,
  apiBaseUrl
};
