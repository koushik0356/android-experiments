# Implementation Plan - Fix Black Screen on Startup

The app is launching into a completely black screen. `dumpsys` confirms the activity is resumed and the view hierarchy is present, but it's not rendering visible content. This often happens on modern Android (API 35+) when using `enableEdgeToEdge()` without a proper window background or when the layout doesn't have a solid background, causing it to blend with a black system window.

## Proposed Changes

### [Component: UI Layout]
#### [MODIFY] [activity_main.xml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/res/layout/activity_main.xml)
- Explicitly set `android:background="#FFFFFF"` (White) to ensure the layout is visible regardless of the theme.

### [Component: Theme]
#### [MODIFY] [themes.xml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/res/values/themes.xml)
- Set `android:windowBackground` to `@color/white` in the base theme to ensure the window itself isn't black.

### [Component: Activity Logic]
#### [MODIFY] [MainActivity.kt](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/java/com/example/intentdemo/MainActivity.kt)
- Restore `enableEdgeToEdge()`.
- Simplify the `onCreate` method to ensure there are no issues with the `WindowInsetsListener`.
- Add a programmatic background color set as a fallback.

## Verification Plan

### Automated Tests
- `adb shell dumpsys activity top` to verify the activity is still resumed.
- `take_screenshot` to confirm the UI is no longer black.

### Manual Verification
- Deploy to emulator and check the screen.
