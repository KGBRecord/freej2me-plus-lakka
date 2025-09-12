# FreeJ2ME-Plus for Lakka OS - Complete Installation Guide

## 📋 Prerequisites

- Lakka OS device (tested on Nintendo Switch Lite with modchip)
- MicroSD card with FAT32 partition
- Computer with SSH and SCP access
- Internet connection to download Java

## 🔧 Step 1: Setup Java on Lakka

### Download Java 8 for ARM64

1. **Visit Oracle Java Downloads:**
   - Go to: https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html
   - Find **"Linux ARM64 Compressed Archive"** section
   - Download: `jdk-8u451-linux-aarch64.tar.gz`
   - **⚠️ IMPORTANT**: Must use exactly version **8u451** as this build is hardcoded for this version
   - If you use a different version, you must modify the source code and rebuild

2. **Extract Java Archive:**
   ```bash
   # Extract the downloaded file
   tar -xzf jdk-8u451-linux-aarch64.tar.gz
   
   # This creates a folder named jdk1.8.0_451
   ls -la jdk1.8.0_451/
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
   ```bash
   # Create java directory on Lakka and copy JDK
   ssh root@<lakka-ip> "mkdir -p /storage/java"
   scp -r jdk1.8.0_451 root@<lakka-ip>:/storage/java/
   ```
   
   **Replace `<lakka-ip>` with your actual Lakka IP address (e.g., 192.168.1.100)**

5. **Verify Java Installation:**
   ```bash
   # SSH into Lakka and test Java
   ssh root@<lakka-ip>
   /storage/java/jdk1.8.0_451/bin/java -version
   ```
   
   You should see output like:
   ```
   java version "1.8.0_451"
   Java(TM) SE Runtime Environment (build 1.8.0_451-b10)
   Java HotSpot(TM) 64-Bit Server VM (build 25.451-b10, mixed mode)
   ```

## 🎯 Step 2: Install FreeJ2ME Core

### Download FreeJ2ME Files

6. **Download Release Files:**
   - Download latest release from: https://github.com/KGBRecord/freej2me-plus-lakka/releases
   - Extract and locate these files:
     - `freej2me_libretro.so` (Libretro core)
     - `freej2me-lr.jar` (System BIOS)

### Copy to Lakka microSD

7. **Method A: Direct Copy to microSD Card**
   ```bash
   # Insert Lakka microSD into your computer
   # Copy files to correct directories
   cp freej2me_libretro.so /media/lakka/cores/
   cp freej2me-lr.jar /media/lakka/system/
   ```

8. **Method B: Copy via SCP (if Lakka is running)**
   ```bash
   # Copy libretro core
   scp freej2me_libretro.so root@<lakka-ip>:/storage/cores/
   
   # Copy system BIOS
   scp freej2me-lr.jar root@<lakka-ip>:/storage/system/
   ```

## 📁 Final Directory Structure

Your Lakka should have this structure:
```
📁 /storage/
├── 📂 java/
│   └── 📂 jdk1.8.0_451/
│       └── 📂 bin/
│           ├── java ✓
│           ├── javac ✓
│           └── javaw ✓
├── 📂 cores/
│   └── 📄 freej2me_libretro.so ✓
├── 📂 system/
│   └── 📄 freej2me-lr.jar ✓
└── 📂 roms/
    └── 📂 [your J2ME games here]
```

## 🔄 Step 3: Restart and Test

9. **Restart Lakka:**
   ```bash
   # Via SSH
   ssh root@<lakka-ip> "reboot"
   
   # Or power cycle your device
   ```

10. **Verify Installation:**
    - Boot into Lakka
    - Go to **Settings** → **Core Info**
    - Look for **"FreeJ2ME"** in the cores list
    - If visible, installation was successful!

## 🎮 Step 4: Playing Games

11. **Add J2ME Games:**
    - Place `.jar` or `.jad` files in the roms directory
    - Restart Lakka or refresh content

12. **Load Games:**
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
- ✅ Verify Java is working: `/storage/java/jdk1.8.0_451/bin/java -version`
- ✅ Check that game files are valid J2ME (.jar/.jad)

### Java not found errors?
- ✅ Confirm Java directory: `ls -la /storage/java/jdk1.8.0_451/bin/`
- ✅ Test Java manually: `/storage/java/jdk1.8.0_451/bin/java -version`
- ✅ **Version mismatch**: If using different Java version, path must be updated in source code
- ✅ Reinstall Java following Step 1 with exact version 8u451

### SSH Connection Issues?
- ✅ Ensure SSH is enabled in Lakka Services
- ✅ Check network connectivity
- ✅ Try default credentials: `root` (no password)
- ✅ Verify IP address is correct

## 💡 Tips

- **Tested Configuration:** Nintendo Switch Lite (modchip) + Lakka + Java 8u451
- **Network Access:** Keep SSH enabled for troubleshooting
- **Backup:** Save your working Java installation
- **Performance:** Java 8u451 provides good performance on ARM64

---
**Enjoy your retro J2ME gaming experience on Lakka! 📱🎮**