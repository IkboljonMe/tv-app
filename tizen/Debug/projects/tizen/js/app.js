/* Hotel TV — Main application (Tizen WRT)
 * Screens: provisioning → attract (vacant) | welcome (occupied)
 * WebSocket events: REFRESH_CONFIG | SHOW_ANNOUNCEMENT | CLEAR_GUEST |
 *                   UPDATE_BACKGROUND | REBOOT | PING
 */
(function () {
  'use strict';

  /* ────────────────────────────────────────────
     i18n — minimal UI strings
  ──────────────────────────────────────────── */
  const T = {
    en: { welcome:'Welcome',   checkin:'Check-in',  checkout:'Check-out', nights:'night(s) left' },
    ru: { welcome:'Добро пожаловать', checkin:'Заезд', checkout:'Выезд', nights:'ноч. осталось' },
    uz: { welcome:'Xush kelibsiz', checkin:'Kirish', checkout:'Chiqish', nights:'kun qoldi' },
    kk: { welcome:'Қош келдіңіз', checkin:'Кіру', checkout:'Шығу', nights:'түн қалды' },
    tg: { welcome:'Хуш омадед', checkin:'Воридшавӣ', checkout:'Хуруҷ', nights:'шаб монд' },
    ar: { welcome:'مرحباً',    checkin:'تسجيل الوصول', checkout:'تسجيل المغادرة', nights:'ليالٍ متبقية' },
    de: { welcome:'Willkommen', checkin:'Anreise', checkout:'Abreise', nights:'Nächte verbleibend' },
    fr: { welcome:'Bienvenue', checkin:'Arrivée', checkout:'Départ', nights:'nuits restantes' },
    'zh-CN': { welcome:'欢迎',  checkin:'入住',   checkout:'退房',   nights:'晚剩余' },
    tr: { welcome:'Hoş geldiniz', checkin:'Giriş', checkout:'Çıkış', nights:'gece kaldı' },
  };

  function t(key, lang) {
    const l = (lang || 'en').split('-')[0];
    return (T[lang] || T[l] || T.en)[key] || T.en[key] || key;
  }

  /* ────────────────────────────────────────────
     Application state
  ──────────────────────────────────────────── */
  const state = {
    screen:     'provisioning',   // provisioning | attract | welcome
    config:     null,             // last fetched RoomConfig
    lang:       'en',
    wsOnline:   false,
    focusIdx:   0,                // focused service tile index
    lastFetch:  0,                // timestamp of last /room/config fetch
    annoTimer:  null,             // setTimeout for announcement dismissal
  };

  /* ────────────────────────────────────────────
     Screen management
  ──────────────────────────────────────────── */
  function showScreen(name) {
    ['provisioning', 'attract', 'welcome'].forEach(function (id) {
      const el = document.getElementById('screen-' + id);
      if (el) el.classList.toggle('active', id === name);
    });
    state.screen = name;
  }

  /* ────────────────────────────────────────────
     Render helpers
  ──────────────────────────────────────────── */
  function setBg(elId, url) {
    const el = document.getElementById(elId);
    if (el && url) el.style.backgroundImage = 'url("' + url + '")';
  }

  function setImg(elId, src) {
    const el = document.getElementById(elId);
    if (el) { el.src = src || ''; el.style.display = src ? '' : 'none'; }
  }

  function setText(elId, text) {
    const el = document.getElementById(elId);
    if (el) el.textContent = text || '';
  }

  function formatDateHuman(isoDate, lang) {
    if (!isoDate) return '';
    const d = new Date(isoDate + 'T00:00:00');
    return Clock.formatDate(d, lang);
  }

  /* ────────────────────────────────────────────
     Render: Attract screen
  ──────────────────────────────────────────── */
  function renderAttract(cfg) {
    const hotel = (cfg && cfg.hotel) || {};
    setBg('attract-bg', (cfg && cfg.room && cfg.room.backgroundUrl) || '');
    setImg('attract-logo', hotel.logoUrl || '');
    setText('attract-hotel-name', hotel.name || '');

    // Ticker from announcements
    const ticker = document.getElementById('attract-ticker-inner');
    if (ticker && cfg && cfg.announcements && cfg.announcements.length) {
      ticker.textContent = cfg.announcements.map(function (a) {
        return '  •  ' + (a.title || '') + (a.body ? ': ' + a.body : '');
      }).join('');
      document.getElementById('attract-ticker').style.display = '';
    } else if (ticker) {
      document.getElementById('attract-ticker').style.display = 'none';
    }

    Clock.start(
      document.getElementById('attract-clock'),
      document.getElementById('attract-date'),
      state.lang
    );
  }

  /* ────────────────────────────────────────────
     Render: Welcome screen
  ──────────────────────────────────────────── */
  function renderWelcome(cfg) {
    const room    = cfg.room    || {};
    const hotel   = cfg.hotel   || {};
    const guest   = cfg.guest   || {};
    const weather = cfg.weather || {};
    const lang    = guest.language || state.lang;
    state.lang = lang;

    setBg('welcome-bg', room.backgroundUrl || '');
    setImg('welcome-logo', hotel.logoUrl || '');

    // Weather
    if (weather.tempC !== undefined) {
      setText('weather-temp', Math.round(weather.tempC) + '°C');
      const iconSrc = weather.iconCode
        ? 'https://openweathermap.org/img/wn/' + weather.iconCode + '@2x.png'
        : '';
      setImg('weather-icon', iconSrc);
    }

    // Welcome text
    const firstName = guest.firstName || '';
    setText('welcome-text', t('welcome', lang) + (firstName ? ', ' + firstName : ''));

    // Stay info
    setText('stay-checkin',  t('checkin', lang)  + '  ' + (guest.checkIn  || ''));
    setText('stay-checkout', t('checkout', lang) + '  ' + (guest.checkOut || ''));
    const nights = guest.nightsRemaining;
    if (nights !== undefined && nights !== null) {
      setText('stay-nights', nights + ' ' + t('nights', lang));
      document.getElementById('stay-nights').style.display = '';
    } else {
      document.getElementById('stay-nights').style.display = 'none';
    }

    // Services
    renderServices(cfg.services || [], lang);

    Clock.start(
      document.getElementById('welcome-clock'),
      document.getElementById('welcome-date'),
      lang
    );
  }

  /* ────────────────────────────────────────────
     Render: Service tiles
  ──────────────────────────────────────────── */
  function renderServices(services, lang) {
    const grid = document.getElementById('services-grid');
    if (!grid) return;
    grid.innerHTML = '';

    const available = services.filter(function (s) { return s.available !== false; });
    available.forEach(function (svc, idx) {
      const tile = document.createElement('div');
      tile.className = 'service-tile';
      tile.tabIndex = 0;
      tile.dataset.index = idx;

      if (svc.iconUrl) {
        const img = document.createElement('img');
        img.src = svc.iconUrl;
        img.alt = '';
        tile.appendChild(img);
      }

      const label = document.createElement('span');
      // label may be JSONB object or plain string
      if (svc.label && typeof svc.label === 'object') {
        const l = lang.split('-')[0];
        label.textContent = svc.label[lang] || svc.label[l] || svc.label.en || Object.values(svc.label)[0] || '';
      } else {
        label.textContent = svc.label || '';
      }
      tile.appendChild(label);

      tile.addEventListener('focus',  function () { state.focusIdx = idx; highlightTile(idx); });
      tile.addEventListener('keydown', handleServiceKey);
      grid.appendChild(tile);
    });

    // Focus first tile
    state.focusIdx = 0;
    highlightTile(0);
  }

  function highlightTile(idx) {
    const tiles = document.querySelectorAll('.service-tile');
    tiles.forEach(function (t, i) { t.classList.toggle('focused', i === idx); });
  }

  /* ────────────────────────────────────────────
     D-pad navigation
  ──────────────────────────────────────────── */
  function handleServiceKey(e) {
    const tiles = document.querySelectorAll('.service-tile');
    if (!tiles.length) return;

    if (e.keyCode === 37 || e.keyCode === 21) {       // Left / VK_LEFT
      e.preventDefault();
      state.focusIdx = Math.max(0, state.focusIdx - 1);
      tiles[state.focusIdx].focus();
    } else if (e.keyCode === 39 || e.keyCode === 22) { // Right / VK_RIGHT
      e.preventDefault();
      state.focusIdx = Math.min(tiles.length - 1, state.focusIdx + 1);
      tiles[state.focusIdx].focus();
    } else if (e.keyCode === 13 || e.keyCode === 10009) { // Enter / VK_ENTER
      e.preventDefault();
      // Service activation: open deep_link if available (future feature)
    }
  }

  document.addEventListener('keydown', function (e) {
    // Back key — do nothing (prevent exit)
    if (e.keyCode === 10009 || e.keyCode === 8 || e.keyCode === 461) {
      e.preventDefault();
      return;
    }
    // On welcome screen, right/left arrow navigates services
    if (state.screen === 'welcome') {
      if (e.keyCode === 37 || e.keyCode === 39) {
        const tiles = document.querySelectorAll('.service-tile');
        if (tiles.length && document.activeElement && !document.activeElement.classList.contains('service-tile')) {
          tiles[state.focusIdx].focus();
        }
      }
    }
  });

  /* ────────────────────────────────────────────
     Connection dot
  ──────────────────────────────────────────── */
  function setDot(online) {
    state.wsOnline = online;
    ['attract-dot', 'welcome-dot'].forEach(function (id) {
      const el = document.getElementById(id);
      if (el) el.classList.toggle('offline', !online);
    });
  }

  /* ────────────────────────────────────────────
     Announcement overlay
  ──────────────────────────────────────────── */
  function showAnnouncement(message, duration) {
    setText('announcement-text', message);
    const overlay = document.getElementById('announcement-overlay');
    if (overlay) overlay.classList.remove('hidden');

    if (state.annoTimer) clearTimeout(state.annoTimer);
    state.annoTimer = setTimeout(function () {
      if (overlay) overlay.classList.add('hidden');
    }, (duration || 10) * 1000);
  }

  /* ────────────────────────────────────────────
     Fetch config + decide screen
  ──────────────────────────────────────────── */
  function fetchConfig() {
    state.lastFetch = Date.now();
    API.getRoomConfig()
      .then(function (cfg) {
        state.config = cfg;
        if (cfg.guest && cfg.guest.firstName) {
          renderWelcome(cfg);
          showScreen('welcome');
        } else {
          renderAttract(cfg);
          showScreen('attract');
        }
      })
      .catch(function (err) {
        console.error('[Hotel TV] fetchConfig error:', err);
        // Keep current screen; will retry on next WS event or interval
      });
  }

  function maybeFetchConfig() {
    const STALE_MS = 5 * 60 * 1000; // 5 minutes
    if (Date.now() - state.lastFetch > STALE_MS) fetchConfig();
  }

  /* ────────────────────────────────────────────
     WebSocket
  ──────────────────────────────────────────── */
  var _ws = null;
  var _wsDelay = 1000;
  var _pingTimer = null;
  var _pongWatchdog = null;

  function connectWs() {
    if (_ws) { try { _ws.close(); } catch (e) {} }
    const url = API.getWsUrl();

    _ws = new WebSocket(url);

    _ws.onopen = function () {
      _wsDelay = 1000;
      setDot(true);
      _startPing();
      // Fetch config immediately on (re)connect
      fetchConfig();
    };

    _ws.onmessage = function (e) {
      var msg;
      try { msg = JSON.parse(e.data); } catch (ex) { return; }
      handleWsMessage(msg);
    };

    _ws.onerror = function () { /* onclose will follow */ };

    _ws.onclose = function () {
      setDot(false);
      _stopPing();
      setTimeout(function () {
        _wsDelay = Math.min(_wsDelay * 2, 30000);
        connectWs();
      }, _wsDelay);
    };
  }

  function _startPing() {
    _stopPing();
    _pingTimer = setInterval(function () {
      if (_ws && _ws.readyState === WebSocket.OPEN) {
        _ws.send(JSON.stringify({ type: 'PING' }));
        // Watchdog: if no PONG in 60s → force reconnect
        _pongWatchdog = setTimeout(function () {
          if (_ws) _ws.close();
        }, 60000);
      }
    }, 30000);
  }

  function _stopPing() {
    if (_pingTimer)    { clearInterval(_pingTimer);  _pingTimer = null; }
    if (_pongWatchdog) { clearTimeout(_pongWatchdog); _pongWatchdog = null; }
  }

  function handleWsMessage(msg) {
    switch (msg.type) {
      case 'PONG':
        if (_pongWatchdog) { clearTimeout(_pongWatchdog); _pongWatchdog = null; }
        break;

      case 'PING':
        if (_ws && _ws.readyState === WebSocket.OPEN) {
          _ws.send(JSON.stringify({ type: 'PONG' }));
        }
        break;

      case 'REFRESH_CONFIG':
        fetchConfig();
        break;

      case 'CLEAR_GUEST':
        state.config = state.config ? Object.assign({}, state.config, { guest: null }) : null;
        if (state.config) { renderAttract(state.config); }
        showScreen('attract');
        break;

      case 'UPDATE_BACKGROUND':
        if (msg.url) {
          setBg('welcome-bg', msg.url);
          setBg('attract-bg', msg.url);
        }
        break;

      case 'SHOW_ANNOUNCEMENT':
        showAnnouncement(msg.message || '', msg.duration || 10);
        break;

      case 'REBOOT':
        // Reload the web app — Tizen WRT equivalent of app restart
        window.location.reload();
        break;
    }
  }

  /* ────────────────────────────────────────────
     Periodic ping to backend (device heartbeat)
  ──────────────────────────────────────────── */
  setInterval(function () {
    if (API.hasToken()) {
      API.ping().catch(function () {});
      maybeFetchConfig();
    }
  }, 60000);

  /* ────────────────────────────────────────────
     Provisioning
  ──────────────────────────────────────────── */
  function setupProvisioning() {
    const btn      = document.getElementById('register-btn');
    const roomInp  = document.getElementById('room-input');
    const hotelInp = document.getElementById('hotel-input');
    const errEl    = document.getElementById('provision-error');

    if (!btn) return;

    btn.addEventListener('click', function () {
      const roomNum = (roomInp.value || '').trim();
      const hotelId = (hotelInp.value || '').trim();
      errEl.textContent = '';

      if (!roomNum) { errEl.textContent = 'Please enter a room number.'; return; }
      if (!hotelId) { errEl.textContent = 'Please enter the hotel ID.'; return; }

      btn.textContent = 'Registering…';
      btn.disabled = true;

      API.setHotelId(hotelId);
      API.registerDevice(hotelId, roomNum)
        .then(function (res) {
          API.setToken(res.device_token || res.deviceToken);
          init(); // re-run init with token now set
        })
        .catch(function (err) {
          errEl.textContent = 'Registration failed: ' + err.message;
          btn.textContent = 'Register Device';
          btn.disabled = false;
        });
    });

    // Allow Enter key on inputs
    [roomInp, hotelInp].forEach(function (inp) {
      if (inp) inp.addEventListener('keydown', function (e) {
        if (e.keyCode === 13) btn.click();
      });
    });
  }

  /* ────────────────────────────────────────────
     Initialisation
  ──────────────────────────────────────────── */
  function init() {
    if (!API.hasToken()) {
      showScreen('provisioning');
      setupProvisioning();
      return;
    }

    // Token exists → show attract initially then fetch real config
    showScreen('attract');
    connectWs();
    fetchConfig();
  }

  /* ────────────────────────────────────────────
     Boot
  ──────────────────────────────────────────── */
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  // Tizen app lifecycle
  if (typeof tizen !== 'undefined') {
    document.addEventListener('visibilitychange', function () {
      if (!document.hidden) {
        maybeFetchConfig();
      }
    });
  }

}());
