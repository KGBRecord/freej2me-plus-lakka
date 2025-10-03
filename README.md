
![BannerFinal](https://github.com/user-attachments/assets/ca82914c-e30e-406d-8d2e-487bda6263af)

# :warning: **Lakka OS Modified Version** :warning:

**This is a modified version of FreeJ2ME-Plus specifically adapted for Lakka OS with Docker build system.**

## ⚠️ **CRITICAL LAKKA LIMITATION** ⚠️

**Lakka is an extremely minimalized Linux distribution (based on OpenELEC) that makes installing Java VERY DIFFICULT:**
- ❌ No package managers (apt, yum, pacman)
- ❌ Read-only filesystem
- ❌ Missing system libraries
- ❌ No development tools

**🎯 SOLUTION: Pre-compile Java on a full Linux system and copy to Lakka manually.**

### Key features:
- **Docker build environment**: Uses OpenJDK 8 official image for consistent builds
- **Simplified build system**: Makefile-based build automation  
- **Lakka OS optimized**: Ready-to-use libretro core and system files
- **Cross-platform builds**: Build on Linux, macOS, Windows with Docker
- **Auto-resolution detection**: Automatically detects game resolution from JAR manifest
- **Intelligent scaling**: Multiple scaling modes (aspect fit, stretch, integer scaling)
- **Centered display**: Games are automatically centered with black bars when needed

### Latest Release: v0.1.1-dev
📦 **[Download Latest Release](https://github.com/KGBRecord/freej2me-plus-lakka/releases/tag/0.1.1-dev)**

#### New in v0.1.1-dev:
- ✨ **Auto-resolution detection** from JAR manifest files
- 🎮 **Intelligent scaling modes**: aspect_fit, stretch, integer scaling  
- 🖥️ **Centered display** with automatic black bars
- ⚙️ **New RetroArch core options** for easy configuration
- 🚀 **Optimized for full-screen gaming** on modern displays

### Quick Installation for Lakka:
1. Copy `freej2me_libretro.so` → `/cores/` directory on Lakka microSD
2. Copy `freej2me-lr.jar` → `/system/` directory on Lakka microSD  
3. Restart Lakka

📋 **[Full Installation Guide](LAKKA_INSTALL.md)**

## :gear: Configuration Options

### Auto-Resolution Detection
Enable this option to automatically detect game resolution from JAR files:
- **freej2me_auto_resolution** = `on` (recommended) / `off`

### Scaling Modes
Choose how games are displayed on your screen:
- **freej2me_scaling_mode** = `aspect_fit` / `stretch` / `integer`
  - **aspect_fit**: Maintains aspect ratio, adds black bars if needed (recommended)
  - **stretch**: Fills entire screen, may distort the image
  - **integer**: Pixel-perfect scaling, preserves original pixel clarity

### Recommended Settings for Lakka:
```
freej2me_auto_resolution = "on"
freej2me_scaling_mode = "aspect_fit" 
```

> **Note**: For general use, please refer to the original [FreeJ2ME-Plus repository](https://github.com/TASEmulators/freej2me-plus).

---

<h1 align="center"> Current status </h1>

<div align="center">

[![Docker Build](https://img.shields.io/badge/Docker-Build%20Ready-blue?style=for-the-badge&logo=docker)](https://github.com/KGBRecord/freej2me-plus-lakka)
[![Lakka Compatible](https://img.shields.io/badge/Lakka-Compatible-green?style=for-the-badge&logo=linux)](https://lakka.tv/)
![Java version](https://img.shields.io/badge/Java-8-orange?style=for-the-badge&label=Required%20Java)
![License](https://img.shields.io/badge/license-GPLv3-red?style=for-the-badge&label=Project%20License)
![Last Commit](https://img.shields.io/github/last-commit/KGBRecord/freej2me-plus-lakka?style=for-the-badge)
![Release](https://img.shields.io/github/v/release/KGBRecord/freej2me-plus-lakka?style=for-the-badge&label=Latest%20Release)

</div>

<h1 align="center"> Downloads for Lakka </h1>

<div align="center">

[![Latest Release](https://img.shields.io/github/v/release/KGBRecord/freej2me-plus-lakka?label=Latest%20Lakka%20Release:&style=for-the-badge&color=blue)](https://github.com/KGBRecord/freej2me-plus-lakka/releases/latest)
[![Docker Build](https://img.shields.io/badge/Docker%20Build-Available-green?style=for-the-badge&logo=docker)](https://github.com/KGBRecord/freej2me-plus-lakka#gear-coffee-building-freej2me-plus-for-lakka)

</div>

---

# :question: What is it?

### This is a **Lakka OS-optimized version** of FreeJ2ME-Plus, a J2ME emulator with libretro and AWT frontends. It has been specifically modified to work seamlessly with Lakka's environment and file system structure.

### Original FreeJ2ME-Plus authors:
#### - David Richardson [Recompile@retropie]
#### - Saket Dandawate  [Hex@retropie]

### Current maintainer of original project:
#### - Paulo Sousa [AShiningRay]

### Lakka OS modifications by:
#### - KGBRecord [KGBRecord]

---

# :bar_chart: Compatibility list

### For a general idea of what can or cannot run, look [HERE](https://tasemulators.github.io/freej2me-plus/)

----
# :penguin: Lakka OS Requirements

>**This version supports flexible Java installation for Lakka OS:**
>
>### Prerequisites:
>- **Java 8** (⚠️ manual installation required - see warning above)
>- **Apache Ant** for building (on development machine)
>- **Make tools** for libretro core compilation
>
>### Java Installation Reality on Lakka:
>1. ✅ **Custom location with config.ini**: Copy pre-compiled JDK anywhere (RECOMMENDED)
>2. ⚠️ **System Java fallback**: Tries `java` command when no config exists (will fail on stock Lakka)
>
>### Smart Java Detection (Priority Order):
>- **1st**: Custom path from config.ini (if exists)
>- **2nd**: System `java` command (automatic fallback, likely fails on Lakka)
>- **Reality**: Manual installation with config.ini is the practical solution

----
# :gear: :coffee: Building FreeJ2ME-Plus for Lakka

## Docker Build System

This project uses a Docker-based build system for consistent, cross-platform builds with Java 8, Apache Ant, and Make.

### System Requirements

- Docker
- Docker Compose  
- Make

### Quick Start

```bash
# Setup environment
make setup

# Build everything
make build-all

# Check build status
make status

# Show all available commands
make help
```

### Available Commands

| Target | Description |
|--------|-------------|
| `make setup` | Build Docker image with OpenJDK 8 |
| `make build-jar` | Build JAR files |
| `make build-so` | Build libretro core |
| `make build-all` | Build everything |
| `make clean` | Clean build artifacts |
| `make clean-docker` | Remove Docker containers and images |
| `make shell` | Start interactive shell |
| `make status` | Show build status |

### Output Files

After successful build, you will have:

```
build/
├── freej2me.jar          # Standalone AWT executable
└── freej2me-lr.jar       # Libretro executable (BIOS)

src/libretro/
└── freej2me_libretro.so  # Libretro core
```

### Using Docker Compose Directly

```bash
# Build image
docker-compose build

# Build JAR files
docker-compose run --rm freej2me-build ant

# Build libretro core
docker-compose run --rm freej2me-build sh -c "cd src/libretro && make"

# Start interactive shell
docker-compose run --rm freej2me-builder
```

### Java Environment

The Docker container includes:

- **Java 8 JDK**: Official OpenJDK 8 Docker image
- **Apache Ant**: Latest version from Debian repository
- **Build tools**: make, gcc, g++

### Troubleshooting

#### Docker Permission Issues
```bash
# If you encounter Docker permission issues
sudo usermod -aG docker $USER
# Then logout and login again
```

#### Clean Everything
```bash
# If you encounter issues, clean everything and start over
make clean-docker
make setup
make build-all
```

#### Interactive Debugging
```bash
# Start shell for debugging
make shell

# Inside container:
java -version                    # Check Java
ant -version                     # Check Ant
cd /workspace && ant             # Manual build
cd src/libretro && make          # Manual libretro build
```

### Cross-Platform Support

This Docker build system works on:
- **Linux** (x86_64, ARM64)
- **macOS** (Intel, Apple Silicon)  
- **Windows** (with Docker Desktop)

### CI/CD Integration

Can be used in CI/CD pipelines:

```yaml
# GitHub Actions example
- name: Build FreeJ2ME-Plus
  run: |
    make setup
    make build-all
```

----
# :memo: How to use the AWT frontend:

Launching the AWT frontend (freej2me.jar) directly will bring up the standalone GUI, where you can load your application through the `File` menu, or by **dragging and dropping your JAR/JAD/KJX/MSD file onto it**. 

You can also configure many aspects of the runtime, including debug options:

<img width="252" height="390" alt="image" src="https://github.com/user-attachments/assets/64737dd3-eea6-4437-b236-aeb163841f21" />
<img width="252" height="390" alt="image" src="https://github.com/user-attachments/assets/5b105d46-d83b-4d1f-9913-687d281523d4" />
<img width="254" height="390" alt="image" src="https://github.com/user-attachments/assets/95054a08-35fb-4e99-87f2-ba36a9a8629c" />

<img width="968" height="399" alt="image" src="https://github.com/user-attachments/assets/f28042c3-1d7f-47be-8942-0e5a5b2cf2b9" />

<h1> </h1>

Alternatively it can be launched from the command line with the following arguments:

- `fullscreen` :arrow_right: `1 = yes, 0 = no`
- `width` :arrow_right: `self explanatory, it's the virtual LCD's width`
- `height` :arrow_right: `also self explanatory, it's the virtual LCD's height`
- `scale` :arrow_right: `for windowed mode, dictates the scale that FreeJ2ME-Plus' window starts with.` 
  - '2' will make it 2X bigger than the original width and height size for example
- `keyLayout` :arrow_right: `specifies which device key layout should be used when booting up.` These can be:
  - `0 -> Default`
  - `1 -> LG`
  - `2 -> Motorola/Softbank`
  - `3 -> Motorola Triplets`
  - `4 -> Motorola V8`
  - `5 -> Nokia Keyboard`
  - `6 -> Sagem`
  - `7 -> Siemens`
  - `8 -> Sharp`
  - `9 -> SKT`
  - `10 -> KDDI`
- `framerate` :arrow_right: `sets the maximum FPS applications are allowed to run at.` 
  - Can be any value, although '10' to '60' is the expected ballpark
- `dojaversion` :arrow_right: `sets the DoJa/Star profile for the I-Appli to use.` These can be:
  - `10 -> Default`
  - `20 -> DoJa 2.0 & International 1.5`
  - `30 -> DoJa 3.0 & International 2.5`
  - `35 -> DoJa 3.5`
  - `40 -> DoJa 4.0`
  - `41 -> DoJa 4.1`
  - `50 -> DoJa 5.0`
  - `51 -> DoJa 5.1`
  - `100 -> Star 1.0`
  - `110 -> Star 1.1`
  - `120 -> Star 1.2`
  - `130 -> Star 1.3`
  - `150 -> Star 1.5`
  - `200 -> Star 2.0`

<h1> </h1>

Those are organized and read internally in this manner: `java -jar freej2me.jar 'file:///path/to/midlet.jar' fullscreen width height scale keyLayout framerate dojaversion`

Although all arguments aside from the path are optional to launch FreeJ2ME-Plus with any given app.

### _Notes:_

**When running under Microsoft Windows please do note paths require an additional `/` prefixed. For example, `C:\path\to\midlet.jar` should be passed as `file:///C:\path\to\midlet.jar`**

**FreeJ2ME keeps savedata and config at the working directory it is run from. Currently any settings specified at the config file take precedence over the values passed via command-line.**

---

# :mag: Modules and external dependencies used:

- #### JLayer(MPEG Player): - LGPLv2.1 License, compatible with GPLv3

- #### libsdl4j: zlib License, compatible with GPLv3

- #### ObjectWeb's ASM: BSD 3-Clause License, not directly compatible with GPLv3, but can be used as long as the original license is published alongside GPLv3 (check the 'License' tab)

- #### Libretro's API: MIT License, compatible with GPLv3

---

# :busts_in_silhouette: How to contribute

### If you're a developer:

  1) Open an Issue
  2) Try solving that issue
  3) Post on the Issue if you have a possible solution
  4) Submit a PR implementing the solution

### If you're an user:

  1) Open an Issue
  2) Explain it in as much detail as you can (FreeJ2ME-Plus version, jar used, md5 hash, as well as the issue with logs and images if possible)
  3) Post a save file close to where the issue manifests, or note the steps required to reproduce it

---

# :gear: Lakka OS Specific Changes

### Files Modified for Lakka Compatibility:

#### **src/libretro/freej2me_libretro.c**:
- **Smart Java Detection**: Uses config.ini → default path → system Java (automatic fallback)
- **Flexible Installation**: Supports system Java, default `/storage/java/`, or custom paths
- **NEW**: `read_java_path_from_config()` function for config.ini parsing

#### **build.xml**:
- Added `executable="/storage/java/bin/javac"` and `fork="true"` to javac tasks
- Changed `bootclasspath` from `${java.home}/lib/rt.jar` to `/storage/java/jre/lib/rt.jar`

### New Display Optimization Features:

#### **Auto-Resolution Detection**:
- Automatically reads game resolution from JAR manifest files
- Supports multiple manifest formats: Nokia-MIDlet-Canvas-Size, Canvas-Size, Screen-Size
- Eliminates need for manual resolution selection

#### **Intelligent Scaling Modes**:
- **Aspect Fit**: Maintains original aspect ratio with black bars (default)
- **Stretch**: Fills entire screen, may distort aspect ratio  
- **Integer**: Pixel-perfect integer scaling with black bars

#### **Core Options Added**:
- `freej2me_auto_resolution`: Enable/disable automatic resolution detection
- `freej2me_scaling_mode`: Choose scaling behavior (aspect_fit/stretch/integer)

#### **Files Modified for Display Optimization**:
- **src/libretro/freej2me_libretro.h**: Added new core options
- **src/libretro/freej2me_libretro.c**: Added scaling logic and argument passing
- **src/org/recompile/freej2me/MIDletLoader.java**: Added auto-detection methods
- **src/org/recompile/freej2me/MobilePlatform.java**: Integrated auto-detection calls
- **src/org/recompile/freej2me/Libretro.java**: Added argument processing

### Why These Changes?
- **Lakka Reality**: Accommodates Lakka's extreme limitations (no package manager, minimal system)
- **Flexibility**: Supports multiple Java installation methods when manually set up
- **Graceful Fallback**: Tries system Java but expects manual installation
- **Backward Compatible**: Still supports traditional `/storage/java/` setup
- **User Experience**: Auto-detection and intelligent scaling provide optimal display
- **Full-screen Gaming**: Games are properly centered and scaled for modern displays
- **Practical Approach**: Designed for reality of embedded/minimalized systems

### Upstream Compatibility:
These changes are **specific to Lakka OS** and may not be suitable for general use. For the original version, please visit: [TASEmulators/freej2me-plus](https://github.com/TASEmulators/freej2me-plus)
