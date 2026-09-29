# Implementation Plan - Neat UI Arrangement for ThirdActivity

This plan aims to improve the visual layout of `ThirdActivity` by grouping system information and standardizing button styles for a "neater" look.

## Proposed Changes

### [MODIFY] [activity_thirdactivity.xml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/res/layout/activity_thirdactivity.xml)

#### UI Enhancements:
1.  **Group Battery Status**: Wrap the battery info `TextView`s in a `MaterialCardView`. This creates a visual container that separates system info from actions.
2.  **Standardize Buttons**:
    *   Change button width to `match_constraint` (0dp) with a fixed horizontal margin (e.g., 32dp) to ensure they look consistent on all screen sizes.
    *   Set a consistent height (e.g., 56dp).
    *   Keep the vertical chain for even distribution.
3.  **Refine Spacing**: Adjust vertical margins to prevent the UI from feeling cramped.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify no layout compilation errors.

### Manual Verification
- Deploy to an emulator/device.
- Verify that the layout is centered, the battery info is in a card, and the buttons are consistently sized.
- Ensure the Logout button is still distinct (red color).
