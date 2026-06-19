# Kitchen POS (Android)

A native **Kotlin / Jetpack Compose** kitchen order screen for Android phones and
tablets. It is a 1:1 port of the web **Oshxona POS** (`/pos`) from the
[`hotel-menu`](../hotel-menu) app — same UI, same Uzbek labels, same dark board —
talking to the **same backend API**.

Kitchen staff watch incoming in-room dining orders move across three columns and
advance each order through its lifecycle.

## What it does

- **Login** with the POS password (role `pos`) — sets the `hm_session` cookie.
- **Live board** with three columns: **Yangi** (new), **Tayyorlanmoqda**
  (preparing), **Tayyor** (ready), each showing a count.
- **Order tickets** show room number, short id, minutes waited (turns red after
  15 min), item lines, an optional guest note, time and total.
- **Advance / cancel** an order — optimistic update, then `PATCH /api/orders/:id`.
- **Live updates** over Server-Sent Events (`/api/orders/stream`); the header
  shows a Jonli / Qayta ulanmoqda… (live / reconnecting) indicator. The stream
  auto-reconnects with a short backoff and refetches on reconnect.
- **Hotel switcher** when the backend serves more than one hotel.

## UI source of truth

The look is taken directly from the web POS components, so the two stay visually
in sync:

| Android | Web (`hotel-menu`) |
| --- | --- |
| `ui/pos/PosBoard.kt` | `src/components/pos/PosBoard.tsx` |
| `ui/pos/OrderTicket.kt` | `src/components/pos/OrderTicket.tsx` |
| `ui/login/LoginScreen.kt` | `src/components/LoginForm.tsx` (`/pos/login`) |
| `ui/theme/Color.kt` | `tailwind.config.ts` (brand + slate tokens) |

## Backend

This app does **not** include a backend — it calls the running `hotel-menu`
Next.js server (default port `3001`). Endpoints used:

- `POST /api/auth/login` — `{ role: "pos", password }`
- `GET  /api/orders?active=1&limit=200`
- `GET  /api/hotels`
- `PATCH /api/orders/:id` — `{ status }`
- `GET  /api/orders/stream` — SSE live feed
- `POST /api/auth/logout`

### Server URL

Set the backend URL on the **login screen** (`Server manzili`). Defaults to
`http://10.0.2.2:3001` (the host machine's `localhost` as seen from an Android
emulator). On a real tablet, use the backend's LAN address, e.g.
`http://192.168.1.50:3001`.

The default POS password is `kitchen123` (see `hotel-menu/.env` → `POS_PASSWORD`).

> Cleartext HTTP to the local network is allowed via
> `res/xml/network_security_config.xml` because hotel backends are typically
> served over plain HTTP on-prem.

## Build & run

```bash
# from this directory
./gradlew assembleDebug          # build the APK
./gradlew installDebug           # install on a connected device/emulator
```

Requirements: Android SDK 34, JDK 17. `local.properties` must point at your SDK
(`sdk.dir=...`).

## Tech

- Kotlin, Jetpack Compose (Material 3), Compose BOM `2024.06.00`
- OkHttp + `okhttp-sse` for REST and the live stream
- kotlinx.serialization for JSON
- Cookie-based auth persisted in `SharedPreferences`
- `minSdk 24`, `targetSdk 34`
