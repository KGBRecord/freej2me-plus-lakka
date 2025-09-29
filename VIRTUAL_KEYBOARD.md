# 🎮 FreeJ2ME Plus - Virtual Keyboard Feature

## Overview
The virtual keyboard feature has been added to FreeJ2ME Plus to provide a modern text input method for J2ME games that require text entry.

## Usage
- **Open virtual keyboard:** `SELECT + START` (press both buttons simultaneously)
  - *Nintendo Switch Lite:* `- (Minus) + + (Plus)` buttons
- **Move cursor:** Use D-pad (`↑↓←→`)
- **Select character:** `A` button (*Nintendo Switch Lite:* `B` button)
- **Close keyboard:** `SELECT + START` again

## Keyboard Layout
```
Q W E R T Y U I O P
A S D F G H J K L  
Z X C V B N M , .  
  SPACE    0-9
```

## Technical Features
- **Overlay design:** Keyboard appears on game screen without interrupting gameplay
- **Input isolation:** When keyboard is open, D-pad and A button are dedicated to keyboard control
- **Visual feedback:** Current cursor position is highlighted
- **Game compatibility:** Works with all J2ME games that support text input

## Technical Implementation
- **File:** `src/libretro/freej2me_libretro.c`
- **Constants:** VKB_ROWS, VKB_COLS, virtualKeyboardLayout[][]
- **Functions:** draw_virtual_keyboard(), handle_virtual_keyboard_input(), send_virtual_key()
- **Integration:** Integrated into video output pipeline and input handling loop

## Comparison with Old Method
- **Before:** Used T9 input like old phones (D-pad to select characters, slow)
- **Now:** Modern QWERTY virtual keyboard, fast and intuitive

---
*This feature enhances the J2ME gaming experience on Lakka with modern text input methods.*