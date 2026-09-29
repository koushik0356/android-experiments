# Walkthrough - Fix Unresolved Reference 'Worker'

I have fixed the unresolved reference error by correctly defining the WorkManager dependency in the version catalog and updating the build script.

## Changes Made

### Build Configuration

#### [libs.versions.toml](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/gradle/libs.versions.toml)
Added the WorkManager version and library definition:
```toml
[versions]
...
work = "2.11.2"

[libraries]
...
androidx-work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "work" }
```

#### [app/build.gradle.kts](file:///C:/Users/koush/AndroidStudioProjects/intentDemo/app/build.gradle.kts)
Updated the incorrect reference from `libs.androidx.Worker.ktx` to `libs.androidx.work.runtime.ktx`.

## Verification Results

### Automated Tests
- **Gradle Sync**: Successfully completed. The "Unresolved reference" error is no longer present.
