# Hotel TV App

Custom hotel room TV launcher for **Android TV**, built natively with Kotlin + Jetpack Compose.

Integrates with Exely PMS for automated check-in/check-out personalization.

## Projects

| Folder | Description |
|--------|-------------|
| `my-hotel/` | Android TV launcher app (Kotlin + Jetpack Compose) |
| `backend/` | Node.js + Fastify + Prisma REST/WebSocket API |
| `admin/` | Next.js admin panel |
| `hotel-menu/` | Next.js in-room food menu (guest / POS / admin) |

## Quick start

### Backend
```bash
cd backend
cp .env.example .env   # fill in secrets
npm install
npx prisma migrate dev --name init
npm run dev
```

### Android TV (my-hotel)
```bash
cd my-hotel
cp app.properties.example app.properties   # fill in secrets
./gradlew installDebug
```
