# FreeJ2ME-Plus Lakka Build System
# This Makefile provides commands to build the project using Docker

# Variables
DOCKER_COMPOSE = docker-compose
DOCKER_BUILD_SERVICE = freej2me-build
DOCKER_INTERACTIVE_SERVICE = freej2me-builder
BUILD_DIR = build
LIBRETRO_DIR = src/libretro

# Default target
.DEFAULT_GOAL := help

# Help target
.PHONY: help
help:
	@echo "FreeJ2ME-Plus Docker Build System"
	@echo "=================================="
	@echo ""
	@echo "Available targets:"
	@echo "  setup          - Build Docker image and setup environment"
	@echo "  build-jar      - Build Java JAR files (freej2me.jar, freej2me-lr.jar)"
	@echo "  build-so       - Build libretro shared library (.so file)"
	@echo "  build-all      - Build both JAR files and libretro core"
	@echo "  create-config  - Create sample config.ini template"
	@echo "  clean          - Clean build artifacts"
	@echo "  clean-docker   - Remove Docker containers and images"
	@echo "  shell          - Start interactive shell in build container"
	@echo "  status         - Show build status"
	@echo "  test           - Run basic tests"
	@echo "  install        - Install to local system"
	@echo "  package        - Create distribution package"
	@echo "  help           - Show this help message"
	@echo ""

# Setup Docker environment
.PHONY: setup
setup:
	@echo "Setting up Docker environment..."
	@$(DOCKER_COMPOSE) build
	@echo "Docker environment ready!"

# Build JAR files using Docker
.PHONY: build-jar
build-jar: setup
	@echo "Building JAR files..."
	@mkdir -p $(BUILD_DIR)
	@$(DOCKER_COMPOSE) run --rm $(DOCKER_BUILD_SERVICE) ant
	@$(MAKE) create-config
	@echo "JAR files built successfully!"
	@echo "Output files:"
	@ls -la $(BUILD_DIR)/*.jar 2>/dev/null || echo "No JAR files found in $(BUILD_DIR)"
	@ls -la $(BUILD_DIR)/config.ini 2>/dev/null || echo "Config template not found"

# Create sample config.ini file
.PHONY: create-config
create-config:
	@echo "Creating sample config.ini..."
	@mkdir -p $(BUILD_DIR)
	@echo "# FreeJ2ME Configuration File" > $(BUILD_DIR)/config.ini
	@echo "# =============================" >> $(BUILD_DIR)/config.ini
	@echo "# This file should be placed in the same directory as freej2me-lr.jar" >> $(BUILD_DIR)/config.ini
	@echo "# (typically the RetroArch system directory)" >> $(BUILD_DIR)/config.ini
	@echo "" >> $(BUILD_DIR)/config.ini
	@echo "# Java Installation Path" >> $(BUILD_DIR)/config.ini
	@echo "# ----------------------" >> $(BUILD_DIR)/config.ini
	@echo "# Specify the path to your Java installation (without /bin/java)" >> $(BUILD_DIR)/config.ini
	@echo "# The core will automatically append /bin/java (Linux) or /bin/javaw (Windows)" >> $(BUILD_DIR)/config.ini
	@echo "" >> $(BUILD_DIR)/config.ini
	@echo "# Default installation (no config needed if using this path):" >> $(BUILD_DIR)/config.ini
	@echo "# java_path=/storage/java" >> $(BUILD_DIR)/config.ini
	@echo "" >> $(BUILD_DIR)/config.ini
	@echo "# Custom installation examples:" >> $(BUILD_DIR)/config.ini
	@echo "java_path=/storage/jdk8" >> $(BUILD_DIR)/config.ini
	@echo "" >> $(BUILD_DIR)/config.ini
	@echo "# Other examples for different systems:" >> $(BUILD_DIR)/config.ini
	@echo "# java_path=/opt/homebrew/opt/openjdk@8              # macOS Homebrew" >> $(BUILD_DIR)/config.ini
	@echo "# java_path=/usr/lib/jvm/java-8-openjdk-amd64       # Linux system Java" >> $(BUILD_DIR)/config.ini
	@echo "# java_path=/home/user/java/jdk8                    # Custom Linux path" >> $(BUILD_DIR)/config.ini
	@echo "# java_path=C:\\Program Files\\Java\\jdk1.8.0_XXX      # Windows" >> $(BUILD_DIR)/config.ini
	@echo "" >> $(BUILD_DIR)/config.ini
	@echo "# Notes:" >> $(BUILD_DIR)/config.ini
	@echo "# - Remove the '#' from the java_path line you want to use" >> $(BUILD_DIR)/config.ini
	@echo "# - Only one java_path should be uncommented" >> $(BUILD_DIR)/config.ini
	@echo "# - If this file doesn't exist, the core uses /storage/java by default" >> $(BUILD_DIR)/config.ini
	@echo "# - Check RetroArch logs for \"Java path from config:\" messages" >> $(BUILD_DIR)/config.ini
	@echo "Sample config.ini created in $(BUILD_DIR)/"

# Build libretro shared library using Docker
.PHONY: build-so
build-so: setup
	@echo "Building libretro shared library..."
	@$(DOCKER_COMPOSE) run --rm $(DOCKER_BUILD_SERVICE) sh -c "cd $(LIBRETRO_DIR) && make clean && make"
	@echo "Libretro core built successfully!"
	@echo "Output files:"
	@ls -la $(LIBRETRO_DIR)/*.so 2>/dev/null || echo "No .so files found in $(LIBRETRO_DIR)"

# Build everything
.PHONY: build-all
build-all: build-jar build-so
	@echo "All builds completed successfully!"
	@echo ""
	@echo "Build artifacts:"
	@echo "================"
	@echo "JAR files:"
	@ls -la $(BUILD_DIR)/*.jar 2>/dev/null || echo "  No JAR files found"
	@echo ""
	@echo "Libretro core:"
	@ls -la $(LIBRETRO_DIR)/*.so 2>/dev/null || echo "  No .so files found"

# Clean build artifacts
.PHONY: clean
clean:
	@echo "Cleaning build artifacts..."
	@rm -rf $(BUILD_DIR)/*
	@$(DOCKER_COMPOSE) run --rm $(DOCKER_BUILD_SERVICE) sh -c "cd $(LIBRETRO_DIR) && make clean" || true
	@echo "Build artifacts cleaned!"

# Clean Docker environment
.PHONY: clean-docker
clean-docker:
	@echo "Cleaning Docker environment..."
	@$(DOCKER_COMPOSE) down --rmi all --volumes --remove-orphans
	@docker system prune -f
	@echo "Docker environment cleaned!"

# Start interactive shell
.PHONY: shell
shell: setup
	@echo "Starting interactive shell..."
	@$(DOCKER_COMPOSE) run --rm $(DOCKER_INTERACTIVE_SERVICE)

# Build and test (for CI/CD)
.PHONY: test
test: build-all
	@echo "Running basic tests..."
	@test -f $(BUILD_DIR)/freej2me.jar || (echo "ERROR: freej2me.jar not found!" && exit 1)
	@test -f $(BUILD_DIR)/freej2me-lr.jar || (echo "ERROR: freej2me-lr.jar not found!" && exit 1)
	@test -f $(LIBRETRO_DIR)/freej2me_libretro.so || (echo "ERROR: freej2me_libretro.so not found!" && exit 1)
	@echo "All tests passed!"

# Install (copy files to system directories)
.PHONY: install
install: build-all
	@echo "Installing FreeJ2ME-Plus..."
	@mkdir -p ~/.local/share/freej2me
	@cp $(BUILD_DIR)/*.jar ~/.local/share/freej2me/
	@echo "JAR files installed to ~/.local/share/freej2me/"
	@echo "Libretro core location: $(PWD)/$(LIBRETRO_DIR)/freej2me_libretro.so"
	@echo "Installation completed!"

# Package for distribution
.PHONY: package
package: build-all
	@echo "Creating distribution package..."
	@mkdir -p dist
	@tar -czf dist/freej2me-plus-lakka-$(shell date +%Y%m%d).tar.gz \
		$(BUILD_DIR)/*.jar \
		$(BUILD_DIR)/config.ini \
		$(LIBRETRO_DIR)/*.so \
		README.md \
		LICENSE \
		KEYMAP.md \
		LAKKA_INSTALL.md
	@echo "Package created in dist/ directory"

# Show build status
.PHONY: status
status:
	@echo "Build Status:"
	@echo "============="
	@echo ""
	@echo "JAR Files:"
	@if [ -f "$(BUILD_DIR)/freej2me.jar" ]; then \
		echo "  ✓ freej2me.jar ($$(stat -f%z $(BUILD_DIR)/freej2me.jar 2>/dev/null || stat -c%s $(BUILD_DIR)/freej2me.jar 2>/dev/null || echo 'unknown') bytes)"; \
	else \
		echo "  ✗ freej2me.jar (not found)"; \
	fi
	@if [ -f "$(BUILD_DIR)/freej2me-lr.jar" ]; then \
		echo "  ✓ freej2me-lr.jar ($$(stat -f%z $(BUILD_DIR)/freej2me-lr.jar 2>/dev/null || stat -c%s $(BUILD_DIR)/freej2me-lr.jar 2>/dev/null || echo 'unknown') bytes)"; \
	else \
		echo "  ✗ freej2me-lr.jar (not found)"; \
	fi
	@if [ -f "$(BUILD_DIR)/config.ini" ]; then \
		echo "  ✓ config.ini (configuration template)"; \
	else \
		echo "  ✗ config.ini (template not found)"; \
	fi
	@echo ""
	@echo "Libretro Core:"
	@if [ -f "$(LIBRETRO_DIR)/freej2me_libretro.so" ]; then \
		echo "  ✓ freej2me_libretro.so ($$(stat -f%z $(LIBRETRO_DIR)/freej2me_libretro.so 2>/dev/null || stat -c%s $(LIBRETRO_DIR)/freej2me_libretro.so 2>/dev/null || echo 'unknown') bytes)"; \
	else \
		echo "  ✗ freej2me_libretro.so (not found)"; \
	fi
	@echo ""
	@echo "Docker Images:"
	@if docker images | grep -q "freej2me-plus-lakka"; then \
		echo "  ✓ Docker image exists"; \
	else \
		echo "  ✗ Docker image not found (run 'make setup')"; \
	fi
