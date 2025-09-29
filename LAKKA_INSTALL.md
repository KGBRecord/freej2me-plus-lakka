# FreeJ2ME-Plus for Lakka OS - Complete Installation Guide

## 📋 Prerequisites

- Lakka OS device (tested on Nintendo Switch Lite with modchip)
- MicroSD card with FAT32 partition
- Computer with SSH and SCP access
- Internet connection to download Java

## 🔧 Step 1: Setup Java on Lakka

### Java Installation Options

**NEW**: FreeJ2ME now supports custom Java paths via `config.ini`! You can install Java anywhere and configure the path.

#### Option A: Default Installation (Recommended for beginners)
- Install Java to `/storage/java/` (works without config)

#### Option B: Custom Installation (Flexible)
- Install Java anywhere and use `config.ini` to specify the path

### Download Java 8 for ARM64

1. **Visit Oracle Java Downloads:**
   - Go to: https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html
   - Find **"Linux ARM64 Compressed Archive"** section
   - Download: `jdk-8u*-linux-aarch64.tar.gz` (any Java 8 version)
   - **✅ COMPATIBLE**: Works with any Java 8 version and custom paths
   - Recommended: Use latest available Java 8 version for better security

2. **Extract Java Archive:**
   ```bash
   # Extract the downloaded file (replace filename with your downloaded version)
   tar -xzf jdk-8u*-linux-aarch64.tar.gz
   
   # This creates a JDK folder - move all contents to java directory
   mkdir -p java
   mv jdk*/* java/
   ls -la java/
   ```

### Enable Lakka Services

3. **Enable SSH and Samba on Lakka:**
   - Boot your Lakka device
   - Go to **Settings** → **Services**
   - Enable **SSH** (for command line access)
   - Enable **Samba** (for file sharing - optional but helpful)
   - Note your Lakka device's IP address

### Install Java on Lakka

4. **Copy Java to Lakka via SCP:**
   
   **Option A: Default Location (no config needed)**
   ```bash
   # Copy entire Java installation to default location
   scp -r java/* root@<lakka-ip>:/storage/java/
   ```
   
   **Option B: Custom Location (requires config.ini)**
   ```bash
   # Copy to custom location (example: /storage/jdk8)
   scp -r java/* root@<lakka-ip>:/storage/jdk8/
   ```
   
   **Replace `<lakka-ip>` with your actual Lakka IP address (e.g., 192.168.1.100)**

5. **Verify Java Installation:**
   ```bash
   # SSH into Lakka and test Java
   ssh root@<lakka-ip>
   
   # For default installation:
   /storage/java/bin/java -version
   
   # For custom installation (adjust path as needed):
   /storage/jdk8/bin/java -version
   ```
   
   You should see output like:
   ```
   java version "1.8.0_451"
   Java(TM) SE Runtime Environment (build 1.8.0_451-b10)
   Java HotSpot(TM) 64-Bit Server VM (build 25.451-b10, mixed mode)
   ```

## ⚙️ Step 2: Configure Custom Java Path (Optional)

**NEW FEATURE**: If you installed Java to a custom location, create a config file:

### Create config.ini (Only needed for custom Java paths)

6. **Create Configuration File:**
   ```bash
   # Create config.ini with your custom Java path
   cat > config.ini << EOF
   # FreeJ2ME Configuration
   # Java installation path (without /bin/java)
   java_path=/storage/jdk8
   
   # Examples for different installations:
   # java_path=/storage/java          (default location)
   # java_path=/storage/custom-java   (custom location)
   # java_path=/usr/lib/jvm/java-8-openjdk-amd64  (Linux system Java)
   EOF
   ```
   
   **Important**: 
   - Don't include `/bin/java` in the path
   - Use the directory that contains the `bin/` folder
   - If using default `/storage/java/`, no config.ini is needed

## 🎯 Step 3: Install FreeJ2ME Core

### Download FreeJ2ME Files

7. **Download Release Files:**
   - Download latest release from: https://github.com/KGBRecord/freej2me-plus-lakka/releases
   - Extract and locate these files:
     - `freej2me_libretro.so` (Libretro core)
     - `freej2me-lr.jar` (System BIOS)
     - `config.ini` (Configuration template - included in build)

### Copy to Lakka microSD

8. **Method A: Direct Copy to microSD Card**
   ```bash
   # Insert Lakka microSD into your computer
   # Copy files to correct directories
   cp freej2me_libretro.so /media/lakka/cores/
   cp freej2me-lr.jar /media/lakka/system/
   
   # Optional: Copy config.ini for custom Java paths
   cp config.ini /media/lakka/system/
   ```

9. **Method B: Copy via SCP (if Lakka is running)**
   ```bash
   # Copy libretro core
   scp freej2me_libretro.so root@<lakka-ip>:/storage/cores/
   
   # Copy system BIOS
   scp freej2me-lr.jar root@<lakka-ip>:/storage/system/
   
   # Optional: Copy config for custom Java paths
   scp config.ini root@<lakka-ip>:/storage/system/
   ```

## 📁 Final Directory Structure

### Option A: Default Java Installation
```
📁 /storage/
├── 📂 java/                    (default Java location)
│   ├── 📂 bin/
│   │   ├── java ✓
│   │   ├── javac ✓
│   │   └── javaw ✓
│   ├── 📂 lib/ ✓
│   ├── 📂 jre/ ✓
│   └── 📄 [other JDK files] ✓
├── 📂 cores/
│   └── 📄 freej2me_libretro.so ✓
├── 📂 system/
│   └── 📄 freej2me-lr.jar ✓
└── 📂 roms/
    └── 📂 [your J2ME games here]
```

### Option B: Custom Java Installation
```
📁 /storage/
├── 📂 jdk8/                    (custom Java location)
│   ├── 📂 bin/
│   │   ├── java ✓
│   │   ├── javac ✓
│   │   └── javaw ✓
│   ├── 📂 lib/ ✓
│   ├── 📂 jre/ ✓
│   └── 📄 [other JDK files] ✓
├── 📂 cores/
│   └── 📄 freej2me_libretro.so ✓
├── 📂 system/
│   ├── 📄 freej2me-lr.jar ✓
│   └── 📄 config.ini ✓         (contains java_path=/storage/jdk8)
└── 📂 roms/
    └── 📂 [your J2ME games here]
```

## 🔄 Step 4: Restart and Test

10. **Restart Lakka:**
   ```bash
   # Via SSH
   ssh root@<lakka-ip> "reboot"
   
   # Or power cycle your device
   ```

11. **Verify Installation:**
    - Boot into Lakka
    - Go to **Settings** → **Core Info**
    - Look for **"FreeJ2ME"** in the cores list
    - If visible, installation was successful!

## 🎮 Step 5: Playing Games

12. **Add J2ME Games:**
    - Place `.jar` or `.jad` files in the roms directory
    - Restart Lakka or refresh content

13. **Load Games:**
    - Go to **Load Content**
    - Navigate to your J2ME game
    - Select **"FreeJ2ME"** core when prompted
    - Start playing!

## 🔧 Troubleshooting

### Core not showing up?
- ✅ Verify `freej2me_libretro.so` is in `/storage/cores/`
- ✅ Check file permissions: `chmod +x /storage/cores/freej2me_libretro.so`
- ✅ Restart Lakka completely

### Games not loading?
- ✅ Ensure `freej2me-lr.jar` is in `/storage/system/`
- ✅ Verify Java is working: `/storage/java/bin/java -version`
- ✅ Check that game files are valid J2ME (.jar/.jad)

### Java not found errors?
- ✅ **Check config.ini**: If using custom Java path, verify `/storage/system/config.ini` exists
- ✅ **Verify config content**: `cat /storage/system/config.ini` should show `java_path=your_path`
- ✅ **Test custom Java**: `[your_custom_path]/bin/java -version`
- ✅ **Default fallback**: Remove config.ini to use default `/storage/java/`
- ✅ **Check RetroArch logs**: Look for "Java path from config:" or "Using default Java path:" messages
- ✅ Confirm Java directory exists: `ls -la /storage/java/bin/` (or your custom path)
- ✅ **Java compatibility**: Any Java 8 version should work with custom paths

### SSH Connection Issues?
- ✅ Ensure SSH is enabled in Lakka Services
- ✅ Check network connectivity
- ✅ Try default credentials: `root` (no password)
- ✅ Verify IP address is correct

## 💡 Tips

- **Tested Configuration:** Nintendo Switch Lite (modchip) + Lakka + Java 8
- **Network Access:** Keep SSH enabled for troubleshooting
- **Backup:** Save your working Java installation
- **Performance:** Any Java 8 version provides good performance on ARM64
- **🆕 Flexible Java Paths:** Use config.ini to install Java anywhere you prefer
- **🆕 Easy Migration:** Copy your Java installation between devices using config.ini
- **🆕 Multiple Java Versions:** Switch between Java installations by updating config.ini

## ⌨️ Virtual Keyboard

### Overview
FreeJ2ME Plus includes a built-in virtual keyboard overlay for text input in J2ME games. This feature provides a modern solution for games that require text entry (names, messages, etc.).

### Hotkey Controls
- **Toggle Virtual Keyboard:** `SELECT + START` (press both buttons simultaneously)
  - *Nintendo Switch Lite:* `- (Minus) + + (Plus)` buttons
- **Navigation:** Use D-pad (`↑↓←→`) to move cursor on keyboard
- **Select Character:** `A` button to type the selected character
  - *Nintendo Switch Lite:* `B` button (bottom face button)
- **Close Keyboard:** `SELECT + START` again

### Keyboard Layout
The virtual keyboard features a QWERTY-style layout with 4 rows:
```
Q W E R T Y U I O P
A S D F G H J K L  
Z X C V B N M , .  
  SPACE    0-9
```

### Usage in Games
1. **Open text input field in game** (name entry, password, etc.)
2. **Press `SELECT + START`** to open virtual keyboard overlay
   - *Nintendo Switch Lite:* Press `- (Minus) + + (Plus)` simultaneously
3. **Navigate with D-pad** to desired character
4. **Press `A`** to type the character into the game
   - *Nintendo Switch Lite:* Press `B` button
5. **Repeat for each character** you want to type
6. **Press `SELECT + START`** to close keyboard when done

### Features
- **Overlay Design:** Keyboard appears over game screen without interrupting gameplay
- **Responsive Navigation:** Single button press movement (no auto-repeat)
- **Game Compatibility:** Works with all J2ME games that support text input
- **Visual Feedback:** Current cursor position highlighted on keyboard
- **Input Isolation:** When keyboard is open, D-pad and A button are dedicated to keyboard control

### Notes
- Virtual keyboard bypasses the traditional J2ME phone-style T9 text input
- Provides faster, more intuitive text entry compared to classic phone keypads
- Keyboard state is preserved during gameplay sessions
- Works with both touch-based and traditional button-controlled games

---
**Enjoy your retro J2ME gaming experience on Lakka! 📱🎮**