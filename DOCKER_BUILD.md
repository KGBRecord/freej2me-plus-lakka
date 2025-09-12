# FreeJ2ME-Plus Docker Build System

This Docker build system provides a consistent build environment for FreeJ2ME-Plus with Java 8, Apache Ant, and Make using the official OpenJDK 8 Docker image.

## System Requirements

- Docker
- Docker Compose
- Make

## Quick Start

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

```bash
# Build image
docker-compose build

# Build JAR files
docker-compose run --rm freej2me-build ant

# Build libretro core
docker-compose run --rm freej2me-build sh -c "cd src/libretro && make"

# Start interactive shell
docker-compose run --rm freej2me-builder
## Using Docker Compose directly

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

## Available Commands

### Makefile Targets

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
| `make test` | Run basic tests |
| `make install` | Install to local system |
| `make package` | Create distribution package |
| `make help` | Show help |

## Output File Structure

After successful build, you will have:

```
build/
├── freej2me.jar          # Standalone AWT executable
└── freej2me-lr.jar       # Libretro executable (BIOS)

src/libretro/
└── freej2me_libretro.so  # Libretro core
```

## Mount Points

The Docker container will mount the following directories:

- **Source code**: `.` → `/workspace`
- **Build output**: `./build` → `/workspace/build`
- **Libretro output**: `./src/libretro` → `/workspace/src/libretro`

All build files will be saved directly to the host directory.

## Java Environment

The container is configured with:

- **Java 8 JDK**: Official OpenJDK 8 Docker image
- **Apache Ant**: Latest version from Debian repository
- **Build tools**: make, gcc, g++

## Troubleshooting

### Docker Permission Issues

```bash
# If you encounter Docker permission issues
sudo usermod -aG docker $USER
# Then logout and login again
```

### Clean Everything

```bash
# If you encounter issues, clean everything and start over
make clean-docker
make setup
make build-all
```

### Interactive Debugging

```bash
# Start shell for debugging
make shell

# Inside container:
java -version                    # Check Java
ant -version                     # Check Ant
cd /workspace && ant             # Manual build
cd src/libretro && make          # Manual libretro build
```

## CI/CD Integration

Can be used in CI/CD pipelines:

```yaml
# GitHub Actions example
- name: Build FreeJ2ME-Plus
  run: |
    make setup
    make build-all
    make test
```

## Performance Tips

1. **Build cache**: Docker will cache layers, first build will take longer
2. **Parallel builds**: Can build jar and so in parallel if needed
3. **Volume caching**: Use volume cache for dependencies

## Compatibility

This system is designed for:

- **Cross-platform**: Runs on Linux, macOS, Windows with Docker
- **CI/CD**: GitHub Actions, GitLab CI, Jenkins, etc.
- **Development**: Local development and testing
