# Roots & Squares Mathematics

An Android application designed to perform quick mathematical calculations for squares and square roots. This app is optimized for **Android 15 (API 35)** and follows modern development standards.

## 🚀 Key Features

*   **Square Calculator:** Instantly compute the square of any given number.
*   **Square Root Calculator:** High-precision calculations for square roots.
*   **Edge-to-Edge Experience:** Fully compatible with Android 15's mandatory edge-to-edge display, ensuring the UI flows beautifully under system bars.
*   **Performance:** Uses Kotlin Coroutines for efficient, non-blocking mathematical operations.

## 🛠 Technical Specifications

*   **Target SDK:** 35 (Android 15)
*   **Minimum SDK:** 24 (Android 7.0)
*   **Language:** Kotlin
*   **UI Architecture:** XML with `ConstraintLayout` & Google Material Design 3
*   **Dependencies:**
    *   `androidx.activity-ktx:1.9.3` (for Edge-to-Edge support)
    *   `kotlinx-coroutines` (for background tasks)
    *   `material:1.12.0`

## 📱 Installation & Setup

1.  Open the project in **Android Studio** (Ladybug or newer recommended).
2.  Ensure you have **JDK 17** configured.
3.  Sync Gradle files to download dependencies.
4.  Run the `app` module on an emulator or physical device.

## 🔧 Android 15 Compatibility Note
This app addresses the "Edge-to-Edge" recommendation from Google Play. 
- It uses `enableEdgeToEdge()` in `MainActivity`.
- It implements `WindowInsetsCompat` to ensure that UI elements like buttons and text inputs are not covered by the system status bar or navigation gesture area.

## 📦 Version History
*   **Current Version:** 3.3 (Version Code 9)
    *   Added full support for Android 15 Edge-to-Edge display.
    *   Updated `targetSdk` to 35.
    *   Optimized layout padding for system bars.

## 👤 Author
**Habijanić**
Project Path: `C:/Users/Admin/Documents/GitHub/RootsAndSquaresMathematics`

---
*Developed for educational and practical mathematical purposes.*