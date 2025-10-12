# FreeJ2ME-Plus for Lakka OS - Installation Guide

## 🚨 **BACKUP YOUR GAMES FIRST!** 🚨

**⚠️ CRITICAL WARNING**: Installing any custom OS will **COMPLETELY WIPE YOUR DEVICE**. Read our **[Backup Guide](BACKUP_GUIDE.md)** BEFORE doing anything!

## ⚠️ **CRITICAL LIMITATION** ⚠️

**Lakka cannot install Java directly** - it's too minimalized (no package manager, read-only filesystem, missing libraries). 

**🎯 SOLUTIONS:**
1. **Manual Setup** (this guide): Prepare Java on a full Linux system, then copy to Lakka
2. **Easy Alternative** (coming soon): Use KGBRecord's Lakka fork with built-in Java + FreeJ2ME

## 🚀 **EASIER ALTERNATIVE - KGBRecord's Lakka Fork** 🚀

**🎯 For users who want a simpler experience:**

**Repository:** [KGBRecord/Lakka-LibreELEC](https://github.com/KGBRecord/Lakka-LibreELEC)

### What's Different:
- ✅ **Java 8 built-in** - no manual Java installation needed
- ✅ **FreeJ2ME-Plus pre-installed** - ready to use out of the box
- ✅ **Zero configuration** - just flash and play J2ME games
- ✅ **Regular Lakka features** - all standard cores and functionality

### Build Requirements:
- 💾 **30-60GB** free disk space
- ⏱️ **Several hours** build time
- 🐳 **Docker** (Windows/macOS/Linux compatible)
- 🔧 **Git** for source download

### Current Status:
✅ **Available for Self-Build** - The Lakka fork is ready but requires you to build it yourself:
- 📖 **Build Guide**: See [BUILD.md](https://github.com/KGBRecord/Lakka-LibreELEC/blob/Lakka-v5.x/BUILD.md) in the Lakka-LibreELEC repo
- ⏱️ **Time Required**: Several hours build time (depends on hardware)
- 💾 **Storage Required**: 30-60GB disk space for building
- 🖥️ **Build Platform**: Any OS with Docker (Windows, macOS, Linux)
- ⚠️ **Important**: This takes much longer than manual setup method below

**💡 Choose Your Method:**
- ⚡ **Quick Setup**: Use manual installation below (30 minutes)
- 🔧 **Custom Build**: Build your own Lakka image with integrated J2ME (several hours)

**🚨 CRITICAL WARNING**: Always backup your game saves before installing any new operating system!

---

## 📋 Manual Installation (Current Method)

## 📋 What You Need

- Lakka device with SSH enabled  
- Full Linux system (Ubuntu/Debian) to prepare Java
- Java 8 JDK for ARM64

## 🚀 Quick Installation

### Step 1: Prepare Java (on your Linux computer)

```bash
# Download Java 8 ARM64 from Oracle
# Extract and prepare
tar -xzf jdk-8u*-linux-aarch64.tar.gz
mkdir java && mv jdk*/* java/
```

### Step 2: Copy to Lakka

```bash
# Copy Java to Lakka (replace <lakka-ip> with your device IP)
scp -r java/* root@<lakka-ip>:/storage/jdk8/

# Copy FreeJ2ME files  
scp freej2me_libretro.so root@<lakka-ip>:/storage/cores/
scp freej2me-lr.jar root@<lakka-ip>:/storage/system/
```

### Step 3: Create Config

```bash
# Create config.ini on Lakka
ssh root@<lakka-ip>
cat > /storage/system/config.ini << EOF
java_path=/storage/jdk8
EOF
```

### Step 4: Restart and Play

```bash
reboot
```

**Done!** Look for "FreeJ2ME" in RetroArch cores, load your .jar games.

---

## 📂 Alternative: MicroSD Method

If SSH is difficult, remove Lakka microSD and copy directly:

```
📁 [microSD]/
├── 📂 cores/
│   └── 📄 freej2me_libretro.so
├── 📂 system/
│   ├── 📄 freej2me-lr.jar  
│   └── 📄 config.ini (java_path=/storage/jdk8)
└── 📂 jdk8/
    └── 📂 bin/java (+ all JDK files)
```

---

## 🔧 Troubleshooting

### Core not appearing?
- Check `/storage/cores/freej2me_libretro.so` exists
- Restart Lakka completely

### Java errors?
- Test: `ssh root@<lakka-ip> "/storage/jdk8/bin/java -version"`
- Check config: `cat /storage/system/config.ini`
- Verify path in config matches Java location

### Games not loading?
- Ensure `/storage/system/freej2me-lr.jar` exists
- Use valid J2ME files (.jar/.jad)

---

## 💡 Key Points

- **System Java won't work** on stock Lakka - always use custom installation
- **Any Java 8 version** works if properly configured
- **Config.ini is required** - no automatic detection on Lakka
- **Prepare externally** - don't try to install Java on Lakka directly

## 🔗 Resources

- **FreeJ2ME-Plus Releases:** [Latest Release](https://github.com/KGBRecord/freej2me-plus-lakka/releases)
- **KGBRecord's Lakka Fork:** [Lakka-LibreELEC](https://github.com/KGBRecord/Lakka-LibreELEC) *(built-in Java + FreeJ2ME - in development)*
- **Too complex?** Consider building the custom Lakka image instead, but note it requires significant time and storage

---
**Happy gaming! 🎮**