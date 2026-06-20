# my-hotel — Android TV Launcher

The in-room **Android TV launcher** ("LauncherCompose"). It boots to the app on
each room's TV, greets the guest, surfaces hotel apps/services, and lets the
guest **order in-room dining** without leaving the TV.

- **Stack:** Kotlin, Jetpack Compose for TV, Hilt, Retrofit/OkHttp, Proto
  DataStore, multi-module ("Now in Android"-style convention plugins).
- **Package:** `com.karuhun.launcher` (demo flavor: `com.karuhun.launcher.demo`).
- **Entry:** `app/.../MainActivity.kt` → onboarding gate → launcher home.

## What it does

1. **Onboarding (first launch).** Fetches the hotel list from `backend`, the
   guest picks their hotel, then enters the **room number** on a D-pad keypad.
   The choice (`hotelSlug`, `hotelName`, `roomNumber`, `onboardingComplete`) is
   saved to **Proto DataStore** and persists across reboots.
2. **Personalized welcome.** On the home screen it reads the saved room, calls
   `GET /menu/guest`, and greets the checked-in guest by name (e.g.
   "Good evening! John Doe", "ROOM 201") — or "Guest" if the room is empty.
3. **In-room dining.** The big **Menu** card opens a categories + products +
   cart screen and places orders via `POST /menu/orders` using the stored hotel
   + room. Orders land in the shared DB and appear in the kitchen POS.
4. **App dock + All apps.** Quick tiles plus an **All apps** screen that lists
   the device's installed launchable apps (PackageManager) and launches them.

## Two upstreams

| Calls | Base URL (`app.properties`) | Used for |
| --- | --- | --- |
| **`backend/`** (this repo, Node :3000) | `MENU_API_BASE_URL` e.g. `http://10.0.2.2:3000/api/v1/` | in-room dining: hotels, categories, products, orders, **guest** |
| **PHP backend** (separate service, :8000) | `APP_BASE_URL` e.g. `http://10.0.2.2:8000/api/v1/` | hotel profile, room detail, content/foods sync, weather, app list |

`10.0.2.2` is the host loopback as seen from the Android emulator. Requests carry
an `X-API-KEY` header (`AuthInterceptor`); the menu endpoints ignore it.

## Module layout (high level)

```
app/                      MainActivity, onboarding gate, app-wide background
core/network              Retrofit instances: default, @Named("hotel"|"menu"|"weather")
core/datastore(-proto)    Proto DataStore — hotel profile + booking (slug/room/onboarded)
core/domain, core/model   repository interfaces, use cases, domain models
core/data                 ApplicationLauncher (PackageManager launch + list)
feature/onboarding/...    HotelSelection → RoomInput screens (MVI)
feature/home/ui           launcher home: greeting, room panel, app dock
feature/restaurant/...    data: MenuApiService + MenuRepository + BookingRepository
                          ui:   MenuOrderScreen (categories/products/cart/checkout)
feature/main-menu/ui      "All apps" grid (device apps + backend apps)
navigation/               OnboardingNavGraph + MainAppNavGraph (type-safe routes)
```

## Data layer for in-room dining

`feature/restaurant/data` hosts the backend integration (uses the
`@Named("menu")` Retrofit):

| Use case | Endpoint |
| --- | --- |
| `GetMenuHotelsUseCase` | `GET /menu/hotels` |
| `GetMenuCategoriesUseCase` | `GET /menu/categories` |
| `GetMenuProductsUseCase` | `GET /menu/products?categoryId&availableOnly` |
| `GetMenuGuestUseCase` | `GET /menu/guest?hotelSlug&roomNumber` |
| `PlaceMenuOrderUseCase` | `POST /menu/orders` |
| `Get/SaveBookingUseCase` | local Proto DataStore (no network) |

Responses use the app's `BaseResponse<T>` envelope (`{ data, code, message }`),
so the backend's `/menu/*` endpoints return that shape.

## Build & run

```bash
cd my-hotel
cp app.properties.example app.properties   # set MENU_API_BASE_URL + APP_BASE_URL + TOKEN
./gradlew :app:assembleDemoDebug           # APK → app/build/outputs/apk/demo/debug/
./gradlew :app:installDemoDebug            # onto a running TV emulator/device
# launch (TV app → LEANBACK, not the default LAUNCHER category):
adb shell am start -n com.karuhun.launcher.demo.debug/com.karuhun.launcher.MainActivity
```

Make sure `backend/` is running first (the onboarding hotel list comes from it).
To demo the welcome, check a guest in:
`POST /api/v1/menu/guest {hotelSlug, roomNumber, fullName, days}`.

> Running the emulator on the dev machine: use the **`TV_api34`** AVD with
> `ANDROID_EMU_VK_ICD=swiftshader … -gpu swiftshader_indirect -no-window`
> (the AMD iGPU crashes the on-screen renderer). See project memory.
