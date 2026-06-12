/* Live clock and date display */
const Clock = (() => {
  let _intervalId = null;

  const MONTH_NAMES = {
    en: ['January','February','March','April','May','June','July','August','September','October','November','December'],
    ru: ['января','февраля','марта','апреля','мая','июня','июля','августа','сентября','октября','ноября','декабря'],
    uz: ['yanvar','fevral','mart','aprel','may','iyun','iyul','avgust','sentabr','oktabr','noyabr','dekabr'],
    kk: ['қаңтар','ақпан','наурыз','сәуір','мамыр','маусым','шілде','тамыз','қыркүйек','қазан','қараша','желтоқсан'],
    de: ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember'],
    fr: ['janvier','février','mars','avril','mai','juin','juillet','août','septembre','octobre','novembre','décembre'],
    tr: ['Ocak','Şubat','Mart','Nisan','Mayıs','Haziran','Temmuz','Ağustos','Eylül','Ekim','Kasım','Aralık'],
  };

  const DAY_NAMES = {
    en: ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
    ru: ['Воскресенье','Понедельник','Вторник','Среда','Четверг','Пятница','Суббота'],
    uz: ['Yakshanba','Dushanba','Seshanba','Chorshanba','Payshanba','Juma','Shanba'],
    kk: ['Жексенбі','Дүйсенбі','Сейсенбі','Сәрсенбі','Бейсенбі','Жұма','Сенбі'],
    de: ['Sonntag','Montag','Dienstag','Mittwoch','Donnerstag','Freitag','Samstag'],
    fr: ['dimanche','lundi','mardi','mercredi','jeudi','vendredi','samedi'],
    tr: ['Pazar','Pazartesi','Salı','Çarşamba','Perşembe','Cuma','Cumartesi'],
  };

  function pad(n) { return String(n).padStart(2, '0'); }

  function formatTime(date) {
    return `${pad(date.getHours())}:${pad(date.getMinutes())}`;
  }

  function formatDate(date, lang) {
    const lang2 = lang ? lang.split('-')[0] : 'en';
    const months = MONTH_NAMES[lang2] || MONTH_NAMES.en;
    const days   = DAY_NAMES[lang2]   || DAY_NAMES.en;
    const day    = days[date.getDay()];
    const d      = date.getDate();
    const m      = months[date.getMonth()];
    const y      = date.getFullYear();

    if (lang2 === 'ru' || lang2 === 'kk') return `${day}, ${d} ${m} ${y}`;
    return `${day}, ${m} ${d}, ${y}`;
  }

  function _tick(clockEl, dateEl, lang) {
    const now = new Date();
    if (clockEl) clockEl.textContent = formatTime(now);
    if (dateEl)  dateEl.textContent  = formatDate(now, lang);
  }

  function start(clockEl, dateEl, lang) {
    stop();
    _tick(clockEl, dateEl, lang);
    _intervalId = setInterval(() => _tick(clockEl, dateEl, lang), 1000);
  }

  function stop() {
    if (_intervalId !== null) {
      clearInterval(_intervalId);
      _intervalId = null;
    }
  }

  return { start, stop, formatTime, formatDate };
})();
