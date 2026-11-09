# Implementation Plan - Fix Login Page

The goal is to fix the layout issues in the login page (`activity_main.xml`) and add navigation logic in `MainActivity.kt`.

## User Review Required

> [!IMPORTANT]
> The current layout uses absolute positioning which will break on different screen sizes. I will migrate it to use proper `ConstraintLayout` constraints.

## Proposed Changes

### UI Fixes

#### [MODIFY] [activity_main.xml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/res/layout/activity_main.xml)
- Fix constraints for all views.
- Replace `android:text="Name"` with `android:hint="Name"`.
- Change `android:inputType="numberPassword"` to `android:inputType="textPassword"`.
- Update IDs to be more descriptive (`et_username`, `et_password`, `btn_login`).
- Use `0dp` (match constraint) with margins instead of hardcoded `412dp`.

#### [MODIFY] [strings.xml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/res/values/strings.xml)
- Add string resources for "Username", "Password", and "Login".

### Logic Fixes

#### [MODIFY] [MainActivity.kt](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/java/com/example/intentdemo/MainActivity.kt)
- Add a click listener to the Login button to navigate to `SecondActivity`.
- (Optional) Add simple validation for username/password.

## Verification Plan

### Automated Tests
- N/A (Manual verification is more suitable for this UI fix).

### Manual Verification
- Deploy the app to an emulator.
- Verify that the layout looks correct and centered.
- Click the Login button and verify navigation to the next screen.
