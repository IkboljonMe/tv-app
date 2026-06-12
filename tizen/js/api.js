/* Hotel TV — Backend API client for Tizen WRT */
const API = (() => {
  const DEFAULT_BACKEND = 'https://your-backend.com';

  /* ── Storage helpers (tizen.preference) ── */
  function prefGet(key, fallback) {
    try { return tizen.preference.getValue(key); } catch (e) { return fallback; }
  }
  function prefSet(key, val) {
    try { tizen.preference.setValue(key, val); } catch (e) { /* non-fatal */ }
  }

  function getBaseUrl()    { return prefGet('backend_url', DEFAULT_BACKEND); }
  function getToken()      { return prefGet('device_token', null); }
  function setToken(tok)   { prefSet('device_token', tok); }
  function getHotelId()    { return prefGet('hotel_id', null); }
  function setHotelId(id)  { prefSet('hotel_id', id); }

  /* ── HTTP helper ── */
  async function request(path, options) {
    options = options || {};
    const token   = getToken();
    const url     = getBaseUrl() + '/api/v1' + path;
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['X-Device-Token'] = token;
    if (options.headers) Object.assign(headers, options.headers);

    const res = await fetch(url, Object.assign({}, options, { headers }));
    if (!res.ok) {
      const body = await res.text().catch(() => '');
      throw new Error('HTTP ' + res.status + (body ? ': ' + body : ''));
    }
    return res.json();
  }

  /* ── Public API ── */
  return {
    /* Storage */
    getToken,
    setToken,
    getHotelId,
    setHotelId,
    getBaseUrl,
    setBackendUrl: function(url) { prefSet('backend_url', url); },

    hasToken: function() { return !!getToken(); },

    /* TV endpoints */
    getRoomConfig: function() {
      return request('/room/config');
    },

    ping: function() {
      return request('/room/ping');
    },

    getServices: function() {
      return request('/hotel/services');
    },

    getContent: function(type) {
      return request('/hotel/content/' + type);
    },

    /* Device registration (provisioning) */
    registerDevice: function(hotelId, roomNumber) {
      return request('/devices/register', {
        method: 'POST',
        body: JSON.stringify({ hotel_id: hotelId, room_number: roomNumber }),
      });
    },

    /* WebSocket URL (query-param auth — no CORS preflight) */
    getWsUrl: function() {
      const base  = getBaseUrl().replace(/^http/, 'ws');
      const token = getToken();
      return base + '/api/v1/ws?token=' + encodeURIComponent(token || '');
    },
  };
})();
