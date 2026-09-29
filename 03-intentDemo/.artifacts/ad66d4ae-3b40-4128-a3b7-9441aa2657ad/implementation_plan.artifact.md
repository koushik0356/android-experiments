# Fix UI Layout for Login Screen

The current UI is broken because several views (Username, Login button, Clear button) lack vertical constraints, causing them to stack at the top of the screen (y=0) at runtime. This plan will establish proper `ConstraintLayout` relationships to create a clean, functional login interface.

## User Review Required

> [!IMPORTANT]
> I will rename the "clear" button ID from `@+id/button3` to `@+id/btn_clear` to match the project's naming convention (`btn_login`, `et_username`, etc.). This might require updating its usage in code if it's referenced (I'll check `MainActivity.kt`).

## Proposed Changes

### UI Layout

#### [MODIFY] [activity_main.xml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/res/layout/activity_main.xml)

- Establish a vertical chain for the views.
- **Username (`et_username`)**:
    - Constraint top to parent top.
    - Use `android:hint="@string/hint_username"` instead of hardcoded `android:text`.
- **Password (`et_password`)**:
    - Constraint top to bottom of `et_username`.
    - Use `android:hint="@string/hint_password"`.
- **Buttons (`btn_login` and `btn_clear`)**:
    - Constraint top to bottom of `et_password`.
    - Create a horizontal chain or simply position them side-by-side below the password field.
- Remove all `tools:layout_editor_absoluteY` and `tools:layout_editor_absoluteX` properties.
- Use `0dp` (match_constraint) for `EditText` widths with side margins to ensure they look good on all screen sizes.

### Code cleanup

#### [MODIFY] [MainActivity.kt](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/src/main/java/com/example/intentdemo/MainActivity.kt)

- I'll check if `button3` is used and update it if I rename it to `btn_clear`.

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` to ensure the project still builds.

### Manual Verification
- Render the Compose Preview (if applicable, but this is XML) or ask the user to check the layout in the Android Studio Layout Editor / Emulator.
