# Documentation

Monorepo for a **hotel in-room experience**: a custom Android TV launcher, an
in-room dining ordering platform (web + kitchen apps), and the backend that ties
them together. As of the latest work, **everything shares one database** — the
PostgreSQL owned by `backend/`.

---

## What each folder is

| Folder | What it is | Stack | Talks to |
| --- | --- | --- | --- |
| [`backend/`](../backend) | Central API + the **single source-of-truth database**. Serves the TV platform API, the in-room dining REST API, and a Prisma data bridge for the web app. | Fastify, Prisma, **PostgreSQL**, Redis | — (owns the DB) |
| [`my-hotel/`](../my-hotel) | The **Android TV launcher** (LauncherCompose) installed in each room. Onboarding (pick hotel + room), personalized welcome, in-room dining ordering, app dock. | Kotlin, Jetpack Compose (TV), Hilt, Retrofit, DataStore | `backend/` (menu + guest) and a separate PHP backend (hotel profile/content) |
| [`hotel-menu/`](../hotel-menu) | The **in-room dining web platform**: guest QR menu, kitchen **POS**, and **admin**. Multi-hotel, en/ru/uz auto-translation, UZS pricing. | Next.js, Prisma client (remote) | `backend/` (persists via the data bridge) |
| [`kitchen-pos/`](../kitchen-pos) | A native **Android kitchen POS** app — a 1:1 port of hotel-menu's web `/pos`. Live order board for kitchen staff. | Kotlin, Jetpack Compose | `hotel-menu/` web API |
| [`admin/`](../admin) | Staff **dashboard** for the TV platform (rooms, services, content, device control). | Next.js, React Query | `backend/` |
| [`docs/`](.) | This documentation. | Markdown | — |

Per-project deep dives: [my-hotel.md](./my-hotel.md) · [backend.md](./backend.md)
· [hotel-menu.md](./hotel-menu.md) · [admin.md](./admin.md) ·
[`kitchen-pos/README.md`](../kitchen-pos/README.md)

---

## How everything connects

```
                                  ┌────────────────────────────────────────┐
                                  │              backend/  (:3000)          │
                                  │        Fastify · Prisma · PostgreSQL     │
   Exely PMS ──webhook──────────▶ │                                          │
   admin/ (:3002) ──REST────────▶ │  /api/v1/room,/devices,/admin,/ws   ◀────┼── my-hotel TV
                                  │  /api/v1/menu/hotels|categories|products │   (device/room platform)
   my-hotel TV ──REST──────────▶  │  /api/v1/menu/orders|guest               │
                                  │  /api/v1/menu/data/:model/:op  (bridge)  │
                                  └───────▲──────────────────────────▲───────┘
                                          │ data bridge              │ REST
                                          │ (Prisma-over-HTTP)       │
                              hotel-menu/ (:3001)            (one Postgres,
                              Next.js: guest menu,            shared by all)
                              /pos, /admin
                                          ▲
                                          │ web API (cookies, SSE)
                                  kitchen-pos/ (Android)
```

Key point: **`backend/` is the only thing that touches the database.** Every
other app reaches the DB through it over HTTP.

### Connection details per app

- **`hotel-menu/` → `backend/`** — its data layer (`src/lib/prisma.ts`) no
  longer uses a local SQLite file. It forwards every `prisma.<model>.<op>(args)`
  call to `backend`'s **data bridge** (`POST /api/v1/menu/data/:model/:op`),
  which runs the same Prisma op on PostgreSQL. Config: `MENU_DATA_API_URL`
  (default `http://localhost:3000/api/v1`). hotel-menu's own route handlers,
  auth, translation, and SSE are unchanged.

- **`my-hotel/` (TV) → `backend/`** — Retrofit client (`@Named("menu")`) pointed
  at `MENU_API_BASE_URL` (e.g. `http://10.0.2.2:3000/api/v1/`). Uses the
  **in-room dining REST API**:
  - `GET /menu/hotels` — onboarding hotel list
  - `GET /menu/categories`, `GET /menu/products` — the dining menu
  - `POST /menu/orders` — place an order (by `hotelSlug` + `roomNumber`)
  - `GET /menu/guest?hotelSlug=&roomNumber=` — the checked-in guest (welcome screen)

  The TV also talks to a **separate PHP backend** (`APP_BASE_URL`, e.g.
  `http://10.0.2.2:8000/api/v1/`, not in this repo) for hotel profile, room
  detail, content/foods sync and weather. So my-hotel has two upstreams.

- **`kitchen-pos/` (Android) → `hotel-menu/`** — Retrofit pointed at
  `http://10.0.2.2:3001`. Uses hotel-menu's web API (`/api/auth/login`,
  `/api/orders`, `/api/orders/stream` SSE). Those handlers persist through
  `backend/`, so the kitchen app, the web POS, and the TV all see the same orders.

- **`admin/` → `backend/`** — `fetch(${NEXT_PUBLIC_API_URL}/api/v1/...)`.

### One shared order, four surfaces
A guest orders on the **TV** (`my-hotel`) → `POST /menu/orders` → PostgreSQL.
The same order instantly shows up in the **web POS** (`hotel-menu/pos`), the
**Android kitchen POS** (`kitchen-pos`), and any **admin** view — because they
all read the one database through `backend/`.

---

## Quick start

```bash
# 1) Infra + backend (the DB everything shares)
cd backend
cp .env.example .env                 # DATABASE_URL → Postgres, REDIS_URL, JWT_SECRET
docker compose up -d postgres redis  # or point at your own
npm install && npx prisma db push
npx ts-node prisma/seed-menu.ts      # seed hotels/categories/products
npm run dev                          # :3000

# 2) In-room dining web (guest menu / POS / admin)
cd ../hotel-menu
cp .env.example .env                 # MENU_DATA_API_URL=http://localhost:3000/api/v1
npm install && npm run dev           # :3001

# 3) Android TV launcher
cd ../my-hotel
cp app.properties.example app.properties   # set MENU_API_BASE_URL + APP_BASE_URL
./gradlew :app:installDemoDebug            # 10.0.2.2 = host from the emulator

# 4) (optional) Android kitchen POS  → ../kitchen-pos  (BASE_URL = http://10.0.2.2:3001)
# 5) (optional) TV admin dashboard   → ../admin        (NEXT_PUBLIC_API_URL = :3000)
```

> Emulator note: launching the Android TV emulator on the dev machine needs the
> `TV_api34` AVD with software rendering — see project memory / `android.md`-style
> notes. `10.0.2.2` is how the emulator reaches services on the host.

Each per-project doc has the detailed setup, environment, and API reference.
