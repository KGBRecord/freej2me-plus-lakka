# FreeJ2ME-Plus for Lakka OS - Complete Installation Guide

## ⚠️ **IMPORTANT WARNING** ⚠️

**Lakka OS is a minimalized Linux distribution based on OpenELEC/LibreELEC that has been stripped down to only essential components for RetroArch. Installing Java directly on Lakka is EXTREMELY DIFFICULT due to:**

- ❌ **No package manager** (no apt, yum, pacman, etc.)
- ❌ **Read-only filesystem** in most areas
- ❌ **Missing system libraries** required by Java
- ❌ **Limited storage space** on embedded devices
- ❌ **No development tools** for compilation
- ❌ **Restricted user permissions**

### 🎯 **RECOMMENDED APPROACH**:
**Pre-compile Java on a full Linux system, then copy the entire JDK to Lakka.**

*This guide assumes you will prepare Java externally and transfer it to Lakka.*

---

## 📋 Prerequisites

- Lakka OS device (tested on Nintendo Switch Lite with modchip)
- MicroSD card with FAT32 partition  
- **Full Linux system for Java preparation** (Ubuntu, Debian, etc.)
- Computer with SSH and SCP access
- Internet connection to download Java

## 🔧 Step 1: Setup Java on Lakka

### Java Installation Options

**NEW**: FreeJ2ME now supports flexible Java installation with automatic fallback!

#### Option A: System Java ⚠️ **VERY DIFFICULT ON LAKKA**
- **NOT RECOMMENDED**: Installing Java via package manager is nearly impossible on Lakka
- Lakka has no package manager and missing system dependencies
- Only viable if you've modified Lakka extensively (advanced users only)

#### Option B: Default Installation (RECOMMENDED)
- Copy pre-compiled Java to `/storage/java/` (works without config)
- **This is the standard approach for Lakka**

#### Option C: Custom Installation (Flexible)
- Copy pre-compiled Java anywhere and use `config.ini` to specify the path
- Useful for devices with limited `/storage/` space

### Download Java 8 for ARM64

**⚠️ CRITICAL**: Download and prepare Java on a **FULL LINUX SYSTEM**, not on Lakka!

1. **Visit Oracle Java Downloads (on your main computer):**
   - Go to: https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html
   - Find **"Linux ARM64 Compressed Archive"** section
   - Download: `jdk-8u*-linux-aarch64.tar.gz` (any Java 8 version)
   - **✅ COMPATIBLE**: Works with any Java 8 version and custom paths
   - Recommended: Use latest available Java 8 version for better security
   
   **🚫 DO NOT**: Try to download or install Java directly on Lakka - it won't work!

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

4. **Install Java (Choose one option):**
   
   **Option A: System Java ⚠️ EXTREMELY DIFFICULT**
   ```bash
   # WARNING: This will likely FAIL on stock Lakka!
   # Lakka is too minimalized to support Java installation
   ssh root@<lakka-ip> "java -version"
   # Expected result: "java: not found" or "command not found"
   ```
   
   **💡 If you see Java already available, you have a heavily modified Lakka setup.**
   
   **Option B: Default Location (no config needed)**
   ```bash
   # Copy entire Java installation to default location
   scp -r java/* root@<lakka-ip>:/storage/java/
   ```
   
   **Option C: Custom Location (requires config.ini)**
   ```bash
   # Copy to custom location (example: /storage/jdk8)
   scp -r java/* root@<lakka-ip>:/storage/jdk8/
   ```
   
   **Replace `<lakka-ip>` with your actual Lakka IP address (e.g., 192.168.1.100)**

5. **Verify Java Installation:**
   ```bash
   # SSH into Lakka and test Java
   ssh root@<lakka-ip>
   
   # For system Java:
   java -version
   
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

**NEW FEATURE**: Configure Java path or let FreeJ2ME auto-detect system Java:

### Create config.ini (Only needed for custom Java paths)

6. **Create Configuration File (Optional):**
   ```bash
   # Create config.ini only if you need custom Java path
   # (Skip this step to use system Java or default /storage/java/)
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
   
   **Priority Order**: 
   1. **Custom path** from config.ini (if specified)
   2. **Default path** `/storage/java/bin/java` (if exists)
   3. **System Java** from PATH (automatic fallback)
   
   **Important**: 
   - Don't include `/bin/java` in the path
   - Use the directory that contains the `bin/` folder
   - **No config needed** for system Java or default location

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

### Option A: System Java Installation (Simplest)
```
📁 /storage/
├── 📂 cores/
│   └── 📄 freej2me_libretro.so ✓
├── 📂 system/
│   └── 📄 freej2me-lr.jar ✓
└── 📂 roms/
    └── 📂 [your J2ME games here]

# Java installed in system PATH (e.g., via package manager)
# No additional files needed!
```

### Option B: Default Java Installation
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

### Option C: Custom Java Installation
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
- ⚠️ **Lakka Limitation**: System Java (`java -version`) will likely fail - this is NORMAL
- ✅ **Check manual installation**: `/storage/java/bin/java -version`
- ✅ **Check config.ini**: If using custom Java path, verify `/storage/system/config.ini` exists
- ✅ **Verify config content**: `cat /storage/system/config.ini` should show `java_path=your_path`
- ✅ **Test custom Java**: `[your_custom_path]/bin/java -version`
- ✅ **Manual installation required**: Lakka cannot install Java via package manager
- ✅ **Check RetroArch logs**: Look for "Java path from config:", "Using default Java path:", or "using system Java command:" messages
- ✅ **Confirm Java directory exists**: `ls -la /storage/java/bin/` (or your custom path)
- ✅ **Java compatibility**: Any Java 8 version should work
- 🚫 **Don't expect system Java**: Lakka is too minimalized for native Java support

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
- **⚠️ Lakka Reality:** Don't expect system Java to work - manual installation required
- **🆕 Flexible Java Paths:** Use config.ini to install Java anywhere you prefer
- **🆕 Easy Migration:** Copy your Java installation between devices using config.ini
- **🆕 Multiple Java Versions:** Switch between Java installations by updating config.ini
- **🆕 Automatic Fallback:** Tries system Java if available (rare on stock Lakka)
- **💡 Lakka Tip:** Prepare everything on a full Linux system first
- **🔧 Advanced Users:** Only attempt system Java if you've heavily modified Lakka

---
**Enjoy your retro J2ME gaming experience on Lakka! 📱🎮**