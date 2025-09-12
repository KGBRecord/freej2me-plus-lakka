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
   - Download: `jdk-8u*-linux-aarch64.tar.gz` (any Java 8 version)
   - **✅ COMPATIBLE**: Works with any Java 8 version since paths are now simplified
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
   ```bash
   # Copy entire Java installation to Lakka
   scp -r java/* root@<lakka-ip>:/storage/java/
   ```
   
   **Replace `<lakka-ip>` with your actual Lakka IP address (e.g., 192.168.1.100)**

5. **Verify Java Installation:**
   ```bash
   # SSH into Lakka and test Java
   ssh root@<lakka-ip>
   /storage/java/bin/java -version
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
- ✅ Verify Java is working: `/storage/java/bin/java -version`
- ✅ Check that game files are valid J2ME (.jar/.jad)

### Java not found errors?
- ✅ Confirm Java directory: `ls -la /storage/java/bin/`
- ✅ Test Java manually: `/storage/java/bin/java -version`
- ✅ **Java compatibility**: Any Java 8 version should work with simplified paths
- ✅ Reinstall Java following Step 1 with any Java 8 version

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

---
**Enjoy your retro J2ME gaming experience on Lakka! 📱🎮**