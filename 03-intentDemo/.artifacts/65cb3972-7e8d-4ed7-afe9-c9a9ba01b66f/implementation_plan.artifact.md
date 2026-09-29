# Implementation Plan - Fix Unresolved Reference 'nextBtn'

The project is failing to build because the `nextBtn` ID is referenced in `MainActivity.kt` but not defined in the layout file `activity_main.xml`. Additionally, `tv_result` is also referenced but missing from the layout.

## User Review Required

> [!NOTE]
> I will be adding both `nextBtn` and `tv_result` to the layout since both are referenced in the Kotlin code but missing from the XML.

## Proposed Changes

### Resources

#### [MODIFY] [strings.xml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/res/values/strings.xml)
- Add string resource for the "Next" button.

#### [MODIFY] [activity_main.xml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/res/layout/activity_main.xml)
- Add `TextView` with id `tv_result`.
- Add `Button` with id `nextBtn`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to ensure the unresolved reference errors are resolved.

### Manual Verification
- Deploy the app to a device/emulator and verify that the "Next" button and result view are visible and functional.
