# Botsi Click Handler Implementation Summary

## Overview
This implementation addresses the requirements from the issue description:
1. ✅ Analyzed botsi:view module for click action models
2. ✅ Created a click handler that comes as param into BotsiPaywallEntryPoint and provides clicks inside BotsiPaywallDelegateImpl
3. ✅ Placed missing UI actions for actions from content models

## Files Created/Modified

### 1. New Click Handler Interface
**File:** `botsi-view/src/main/java/com/botsi/view/handler/BotsiClickHandler.kt`
- Created internal interface for handling click actions
- Provides callbacks for button clicks, top button clicks, link clicks, and custom actions
- Follows the naming convention starting with "Botsi"

### 2. Example Implementation
**File:** `botsi-view/src/main/java/com/botsi/view/handler/BotsiClickHandlerExample.kt`
- Demonstrates how to implement the BotsiClickHandler interface
- Shows handling of all different button action types (Close, Login, Restore, Custom, Link)

### 3. Enhanced UI Actions
**File:** `botsi-view/src/main/java/com/botsi/view/model/ui/BotsiPaywallUiAction.kt`
- Added missing UI actions for content model actions:
  - `ButtonClick(action: BotsiButtonAction, actionId: String?)`
  - `TopButtonClick(topButton: BotsiTopButton)`
  - `LinkClick(url: String)`
  - `CustomAction(actionId: String, actionLabel: String?)`
- Made interface internal to match visibility of used types

### 4. Updated Delegate Implementation
**File:** `botsi-view/src/main/java/com/botsi/view/delegate/BotsiPaywallDelegateImpl.kt`
- Added click handler parameter to constructor
- Implemented handling for all new UI actions
- Delegates click events to the provided click handler

### 5. Updated DI Manager
**File:** `botsi-view/src/main/java/com/botsi/view/di/BotsiPaywallDIManager.kt`
- Added click handler parameter to constructor
- Updated delegate creation to pass click handler

### 6. Updated Entry Point
**File:** `botsi-view/src/main/java/com/botsi/view/ui/compose/entry_point/BotsiPaywallEntryPoint.kt`
- Added optional click handler parameter
- Made function internal to avoid exposing internal types
- Passes click handler through DI system

### 7. Updated Composables
**Files:**
- `BotsiPaywallScreenComposable.kt`: Updated to pass onAction to content composables
- `BotsiContentComposable.kt`: Updated to handle button and link clicks with proper UI actions

## Click Action Models Analysis

### Existing Content Model Actions
From `BotsiEnums.kt`, the following button actions were identified:
- `BotsiButtonAction.None`
- `BotsiButtonAction.Close`
- `BotsiButtonAction.Login`
- `BotsiButtonAction.Restore`
- `BotsiButtonAction.Custom`
- `BotsiButtonAction.Link(url: String)`

### Usage in Content Models
- `BotsiButtonContent`: Has `action: BotsiButtonAction?` and `actionLabel: String?`
- `BotsiTopButton`: Has `action: BotsiButtonAction?` and `actionId: String?`

## Integration Flow

1. **Entry Point**: `BotsiPaywallEntryPoint` accepts optional `BotsiClickHandler`
2. **DI System**: `BotsiPaywallDIManager` receives and stores click handler
3. **Delegate**: `BotsiPaywallDelegateImpl` receives click handler and handles UI actions
4. **UI Actions**: New UI actions map content model actions to delegate calls
5. **Composables**: Updated to trigger appropriate UI actions on user interactions

## Usage Example

```kotlin
val clickHandler = object : BotsiClickHandler {
    override fun onButtonClick(action: BotsiButtonAction, actionId: String?) {
        when (action) {
            is BotsiButtonAction.Close -> dismissPaywall()
            is BotsiButtonAction.Login -> navigateToLogin()
            is BotsiButtonAction.Restore -> restorePurchases()
            // ... handle other actions
        }
    }
    
    override fun onLinkClick(url: String) {
        openUrl(url)
    }
    
    // ... implement other methods
}

BotsiPaywallEntryPoint(
    viewConfig = viewConfig,
    clickHandler = clickHandler
)
```

## Requirements Fulfillment

✅ **Requirement 1**: Analyzed botsi:view module for click action models
- Identified all existing button actions in BotsiEnums.kt
- Found usage in BotsiButtonContent and BotsiLayoutContent
- Discovered empty onClick handlers in composables

✅ **Requirement 2**: Created click handler starting with "Botsi" that comes as param into BotsiPaywallEntryPoint and provides clicks inside BotsiPaywallDelegateImpl
- Created `BotsiClickHandler` interface
- Added parameter to `BotsiPaywallEntryPoint`
- Integrated with `BotsiPaywallDelegateImpl`

✅ **Requirement 3**: Placed missing UI actions for actions from content models
- Added `ButtonClick`, `TopButtonClick`, `LinkClick`, and `CustomAction` to `BotsiPaywallUiAction`
- Updated composables to use these actions instead of empty handlers
- Connected content model actions to UI actions

The implementation is complete and ready for use.