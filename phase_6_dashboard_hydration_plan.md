# 🚀 Phase 6 & Step 19 Architecture Plan — Post-Auth Dashboard Hydration & Session Maintenance

> **Goal:** Bridge the completed Authentication System (Phase 1–5) with the Android App Dashboard (`HomeScreen`, `HomeViewModel`, `ProfileScreen`), hydrating all screens with real user data from encrypted storage, and implementing automatic 401 session expiry handling.

---

## 📌 Executive Summary & Step 19 Recap

### What Was Accomplished in Step 19 (Final Auth Verification):
- **Server Side (Phase 1–3):** Node.js/TypeScript Express server complete with Firebase Admin SDK, Play Integrity token decoding, session UUID creation, rate limiting, and dual audit logging.
- **Android Side (Phase 4–5):** EncryptedSharedPreferences (AES-256-GCM), Retrofit API client, `AuthRepository.kt` (5-step auth flow), `LoginViewModel.kt` (mock-free state machine), and `LoginScreen.kt` (real-time loading text, error banner, auto-login check).
- **CI/CD Status:** GitHub Actions Build **SUCCESSFUL** ([Run #30159704920](https://github.com/achal518/Instavault_app/actions/runs/30159704920)).

---

## 🗺️ Phase 6 Roadmap (Post-Auth Dashboard Integration)

Now that authentication is 100% complete and verified, **Phase 6** connects the saved `session_token` and `user_profile` to the rest of the Android application.

```
       ┌──────────────────────────────────────────────────────────┐
       │             EncryptedSharedPreferences                   │
       │  - session_token ("48cde6ae-0347-46b5-...")              │
       │  - user_profile (first_name, spark_balance, rank_tier)   │
       └────────────────────────────┬─────────────────────────────┘
                                    │
                                    ▼
       ┌──────────────────────────────────────────────────────────┐
       │                      AuthRepository                      │
       └──────────────┬────────────────────────────┬──────────────┘
                      │                            │
                      ▼                            ▼
       ┌──────────────────────────┐    ┌──────────────────────────┐
       │      HomeViewModel       │    │      ProfileViewModel    │
       │  (Hydrates HomeScreen)   │    │ (Hydrates ProfileScreen) │
       └──────────────┬───────────┘    └───────────┬──────────────┘
                      │                            │
                      ▼                            ▼
               HomeScreen UI                ProfileScreen UI
             - Real User Name             - Real Vault ID (#VLT-...)
             - Real Spark Balance         - Real Instagram Handle
             - Real Rank Tier             - Functional Logout Button
```

---

## 🛠️ Step-by-Step Implementation Plan for Phase 6

### **Step 20: `HomeViewModel.kt` Rewrite & Dashboard Hydration**
- **File:** `app/src/main/kotlin/com/instavault/app/ui/home/HomeViewModel.kt`
- **Objective:** Convert `HomeViewModel` into a state-managed ViewModel using `AndroidViewModel`.
- **Logic:**
  1. Inject `AuthRepository` (which reads from `SessionManager`).
  2. Expose `val userProfile: StateFlow<UserProfile?>`.
  3. On initialization, load `getCachedProfile()` to immediately populate `HomeScreen` with real user data (`sparkBalance`, `rankTier`, `firstName`, `lifetimeSparks`).
  4. Trigger background telemetry sync (`authRepository.sendTelemetry()`) when the home screen is displayed.

---

### **Step 21: Automatic 401 Session Expiry Interceptor**
- **File:** `app/src/main/kotlin/com/instavault/app/data/remote/RetrofitClient.kt`
- **Objective:** Add an OkHttp Interceptor for session maintenance.
- **Logic:**
  1. Automatically attach `Authorization: Bearer <session_token>` and `X-Vault-ID: <vault_id>` to all outgoing API requests.
  2. Intercept incoming responses: If the server returns `HTTP 401 Unauthorized` (indicating the session token was invalidated or revoked on the server), automatically invoke `sessionManager.clearSession()`.
  3. Emit a global `SessionExpired` event so `AppNavigation` redirects the user back to `LoginScreen` safely.

---

### **Step 22: `ProfileScreen.kt` Hydration & Real Logout Action**
- **File:** `app/src/main/kotlin/com/instavault/app/ui/profile/ProfileScreen.kt`
- **Objective:** Display real user statistics and wire the Logout button.
- **Logic:**
  1. Display the user's real Vault ID (`#VLT-7437014244`), Instagram handle, total orders, and lifetime sparks.
  2. Wire the "Logout / Disconnect Vault" button to `authRepository.logout()`.
  3. Clearing the session wipes the AES-256 encrypted storage and resets navigation back to `LoginScreen`.

---

## 🔒 User Review & Design Decisions

> [!IMPORTANT]
> **Zero Mock Guarantee:** All dashboard screens will display empirical data fetched from Firestore during login. Default fallbacks (e.g. `sparkBalance = 0`, `rankTier = "Rookie Vaulter"`) will only be used if a field is null in database.

> [!NOTE]
> **No UI Redesign Needed:** All existing cyber-vault Compose animations, gradient cards, and bottom navigation bar styling will remain 100% intact. We are only wiring data and state engines.

---

## 🧪 Verification & Testing Plan

1. **Gradle Build Verification:** Run `./gradlew assembleDebug` / `git push` to verify 0 Kotlin compilation errors.
2. **Dashboard Data Check:** Verify `HomeScreen` displays the exact `spark_balance` and `first_name` returned from Firestore.
3. **Logout & Session Wipe Check:** Click "Logout" on Profile screen → verify EncryptedSharedPreferences is cleared and user is safely returned to Login.
