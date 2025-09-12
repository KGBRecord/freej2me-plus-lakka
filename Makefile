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
	@echo "JAR files built successfully!"
	@echo "Output files:"
	@ls -la $(BUILD_DIR)/*.jar 2>/dev/null || echo "No JAR files found in $(BUILD_DIR)"

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
		$(LIBRETRO_DIR)/*.so \
		README.md \
		LICENSE \
		KEYMAP.md
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
