# Documentation

This repository contains **two independent product lines** that happen to share a
monorepo:

### 1. Hotel TV platform
A custom in-room **TV launcher** for hotels (Android TV) with a Node backend and
an admin dashboard, integrated with **Exely PMS** for automated
check-in/check-out personalization.

### 2. Hotel Menu
A separate, self-contained **in-room dining ordering** web app (guest QR menu +
kitchen POS + admin). Not connected to the TV platform.

---

## Per-project documentation

| Project | Doc | What it is | Stack |
| --- | --- | --- | --- |
| `my-hotel/` | — | Android TV launcher (LauncherCompose) — **the current TV client** | Kotlin, Jetpack Compose, Hilt, Retrofit, Room |
| `backend/` | [backend.md](./backend.md) | Central API + WebSocket server for the TV platform | Fastify, Prisma, PostgreSQL, Redis |
| `admin/` | [admin.md](./admin.md) | Staff dashboard to monitor/control the TVs | Next.js, React Query |
| `hotel-menu/` | [hotel-menu.md](./hotel-menu.md) | In-room dining ordering platform (guest / POS / admin) | Next.js, Prisma, SQLite |

---

## How the Hotel TV pieces fit together

```
                ┌───────────────┐
  Exely PMS ───▶│   backend/    │◀──── admin/  (manage rooms, services, content)
  (webhooks)    │ Fastify + DB  │      (Next.js dashboard, JWT)
                │ Redis pub/sub │
                └──────┬────────┘
                       │ device token + WebSocket (/api/v1/ws)
                       ▼
                   my-hotel/
            (Kotlin + Jetpack Compose,
             Android TV — current)
```

- The TV client registers for a **device token**, fetches `/room/config`, and
  holds a **WebSocket** for live updates (refresh, clear guest, announcement,
  background, reboot).
- The **admin** panel and **Exely** webhooks both mutate backend state, which
  fans out to the relevant TVs via Redis pub/sub.

## Hotel Menu (independent)

`hotel-menu/` is a standalone Next.js app with its **own** database (SQLite) and
API (Next.js route handlers) — it does not use `backend/`. See
[hotel-menu.md](./hotel-menu.md) for its three surfaces (guest menu, kitchen POS,
admin), multi-hotel model, en/ru/uz auto-translation, and UZS pricing.

---

## Quick start pointers

- **TV platform:** start `backend/` (see [backend.md](./backend.md)), then run
  `my-hotel/` with Gradle; manage it from `admin/`.
- **Hotel Menu:** `cd hotel-menu && npm install && npm run setup && npm run dev`
  (see [hotel-menu.md](./hotel-menu.md)).

Each doc has its own detailed setup, configuration, and API sections.
