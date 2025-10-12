# 🚨 CRITICAL: Backup Your Games Before Installing Custom Lakka

## ⚠️ **WARNING: DATA LOSS RISK** ⚠️

Installing a custom Lakka OS (or any new operating system) **WILL COMPLETELY WIPE YOUR DEVICE**:
- 🗑️ All games, saves, settings will be **PERMANENTLY DELETED**
- 🗑️ RetroArch configurations will be lost
- 🗑️ Custom cores and BIOS files will be erased
- 🗑️ WiFi passwords and system settings will be reset

**📱 This applies to ALL platforms**: Raspberry Pi, Generic PC, handheld devices, etc.

---

## 🛡️ **BEFORE DOING ANYTHING - BACKUP EVERYTHING**

### Method 1: Complete SD Card/Drive Backup (Recommended)

#### For SD Card Devices (Pi, etc.)
```bash
# Linux/macOS - backup entire SD card
sudo dd if=/dev/sdX of=lakka_backup_$(date +%Y%m%d).img bs=4M status=progress
gzip lakka_backup_$(date +%Y%m%d).img  # Compress to save space

# Windows - use Win32DiskImager or Raspberry Pi Imager
# Select "Read" mode to create backup image
```

#### For Internal Storage Devices
```bash
# Create complete drive image before installation
sudo dd if=/dev/sdX of=complete_backup_$(date +%Y%m%d).img bs=4M status=progress
```

### Method 2: Selective File Backup

#### Essential Directories to Backup:
```bash
# Connect to Lakka via SSH or remove SD card
# Copy these critical directories:

/storage/roms/               # All your game ROMs
/storage/saves/              # Save states and saves  
/storage/savefiles/          # Battery saves
/storage/screenshots/        # Screenshots
/storage/playlists/          # Game playlists
/storage/.config/retroarch/  # RetroArch settings
/storage/bios/               # BIOS files
/storage/cores/              # Custom cores
/storage/overlays/           # Custom overlays
/storage/remappings/         # Controller configs
```

#### SSH Backup Commands:
```bash
# Create backup directory on your computer
mkdir lakka_backup_$(date +%Y%m%d)
cd lakka_backup_$(date +%Y%m%d)

# Copy files from Lakka (replace <lakka-ip> with device IP)
scp -r root@<lakka-ip>:/storage/roms ./
scp -r root@<lakka-ip>:/storage/saves ./
scp -r root@<lakka-ip>:/storage/savefiles ./
scp -r root@<lakka-ip>:/storage/.config/retroarch ./retroarch_config
scp -r root@<lakka-ip>:/storage/bios ./
scp -r root@<lakka-ip>:/storage/cores ./
scp -r root@<lakka-ip>:/storage/playlists ./
scp -r root@<lakka-ip>:/storage/screenshots ./
scp -r root@<lakka-ip>:/storage/overlays ./
scp -r root@<lakka-ip>:/storage/remappings ./
```

### Method 3: Physical SD Card Copy
```bash
# Remove SD card from device
# Insert into computer
# Copy entire contents to backup folder
cp -r /media/LAKKA/ ~/lakka_backup_$(date +%Y%m%d)/
```

---

## 📁 What to Backup by Platform

### Raspberry Pi / ARM Devices
- **Essential**: `/storage/roms/`, `/storage/saves/`, `/storage/.config/`
- **Important**: `/storage/bios/`, `/storage/cores/`
- **Optional**: `/storage/screenshots/`, `/storage/playlists/`

### Generic PC / x86 Devices  
- **Essential**: Same as above + any custom drivers
- **Important**: WiFi passwords, network configs
- **Optional**: Custom themes, overlays

### Handheld Devices (Steam Deck, etc.)
- **Essential**: All game data + device-specific configs
- **Critical**: Controller mappings, display settings
- **Optional**: Custom firmware configurations

---

## 🔄 Restoring After Installation

### After installing custom Lakka with J2ME:

```bash
# Copy backed up files to new installation
scp -r ./roms root@<lakka-ip>:/storage/
scp -r ./saves root@<lakka-ip>:/storage/
scp -r ./savefiles root@<lakka-ip>:/storage/
scp -r ./retroarch_config root@<lakka-ip>:/storage/.config/retroarch
scp -r ./bios root@<lakka-ip>:/storage/
# ... etc for all backed up directories
```

### Verification:
```bash
# SSH into new Lakka and verify files
ssh root@<lakka-ip>
ls -la /storage/roms/     # Check your games are there
ls -la /storage/saves/    # Check save files restored
```

---

## 💾 Storage Space Planning

### Backup Size Estimates:
- **Small Collection**: 2-5GB (few systems, no PSX/N64)
- **Medium Collection**: 10-20GB (multiple systems, some CD games)  
- **Large Collection**: 50-200GB+ (full ROM sets, PSX, Dreamcast, etc.)
- **Complete SD Image**: Same size as your SD card (32GB, 64GB, etc.)

### Where to Store Backups:
- ✅ **External USB drive** (recommended)
- ✅ **Network storage** (NAS, cloud)  
- ✅ **Another computer** via network
- ❌ **Same SD card** (will be wiped!)

---

## ⏱️ Time Requirements

### Backup Process:
- **SSH method**: 30 minutes - 2 hours (depends on collection size)
- **SD card copy**: 15-60 minutes (depends on card speed)
- **Complete image**: 1-3 hours (depends on card size and USB speed)

### Build + Install Process:
- **Build custom Lakka**: 3-8 hours (depends on hardware)
- **Flash new image**: 10-30 minutes
- **Restore games**: 30 minutes - 2 hours

**Total Time Commitment**: Plan for a full day (8-12 hours)

---

## 🚨 Final Warnings

1. **💾 BACKUP FIRST** - No backup = permanent data loss
2. **🔌 Stable Power** - Don't lose power during builds/flashing
3. **📱 Test First** - Try on spare SD card if possible
4. **⏰ Time Budget** - This is NOT a quick 30-minute process
5. **📞 Support** - Have technical knowledge or help available

**Remember**: The custom Lakka build with integrated J2ME is cool, but losing years of game saves is not worth it. Always backup first!

---

## 🆘 Emergency Recovery

### If Something Goes Wrong:
```bash
# Restore from complete backup image
sudo dd if=lakka_backup_YYYYMMDD.img of=/dev/sdX bs=4M status=progress

# Or flash original Lakka and restore files manually
# Download original Lakka from https://lakka.tv/get/
```

### Prevention:
- Keep multiple backup copies
- Test backup integrity before proceeding
- Have original Lakka image ready for recovery

**Happy gaming! 🎮** (After backing up safely! 🛡️)