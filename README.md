# Hotel TV App

Custom hotel room TV launcher for **Android TV** and **Samsung Tizen**, built with Flutter.

Integrates with Exely PMS for automated check-in/check-out personalization.

## Projects

| Folder | Description |
|--------|-------------|
| `android/` | Flutter app — builds for Android TV (APK) and Tizen (TPK) |
| `backend/` | Node.js + Fastify + Prisma REST/WebSocket API |
| `tizen/` | Legacy HTML5 Tizen web app (superseded by Flutter) |

## Quick start

### Backend
```bash
cd backend
cp .env.example .env   # fill in secrets
npm install
npx prisma migrate dev --name init
npm run dev
```

### Android TV
```bash
cd android
flutter run
```

### Tizen
```bash
cd android
flutter-tizen run --dart-define=IS_TIZEN=true
```
