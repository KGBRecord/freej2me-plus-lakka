#!/bin/bash

# FreeJ2ME-Plus Lakka Build Helper Script
# This script provides easy-to-use commands for building the project

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if Docker and Docker Compose are installed
check_prerequisites() {
    print_info "Checking prerequisites..."
    
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi
    
    print_success "Prerequisites check passed!"
}

# Function to show help
show_help() {
    echo "FreeJ2ME-Plus Lakka Build Helper"
    echo "================================="
    echo ""
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  setup [VARIANT]    Setup Docker environment"
    echo "                     VARIANT: default, ubuntu18, corretto"
    echo "  build-jar          Build Java JAR files"
    echo "  build-so           Build libretro shared library"
    echo "  build-all          Build everything"
    echo "  clean              Clean build artifacts"
    echo "  clean-all          Clean everything including Docker images"
    echo "  shell [VARIANT]    Start interactive shell in container"
    echo "  status             Show build status"
    echo "  help               Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 setup              # Setup with default (OpenJDK 8)"
    echo "  $0 setup ubuntu18     # Setup with Ubuntu 18.04"
    echo "  $0 setup corretto     # Setup with Amazon Corretto"
    echo "  $0 build-all          # Build everything"
    echo "  $0 shell ubuntu18     # Start shell with Ubuntu 18.04"
    echo ""
}

# Function to show build status
show_status() {
    print_info "Build Status:"
    echo ""
    
    # Check JAR files
    echo "JAR Files:"
    if [ -f "build/freej2me.jar" ]; then
        echo -e "  ${GREEN}✓${NC} freej2me.jar ($(stat -f%z build/freej2me.jar 2>/dev/null || stat -c%s build/freej2me.jar) bytes)"
    else
        echo -e "  ${RED}✗${NC} freej2me.jar (not found)"
    fi
    
    if [ -f "build/freej2me-lr.jar" ]; then
        echo -e "  ${GREEN}✓${NC} freej2me-lr.jar ($(stat -f%z build/freej2me-lr.jar 2>/dev/null || stat -c%s build/freej2me-lr.jar) bytes)"
    else
        echo -e "  ${RED}✗${NC} freej2me-lr.jar (not found)"
    fi
    
    echo ""
    
    # Check libretro core
    echo "Libretro Core:"
    if [ -f "src/libretro/freej2me_libretro.so" ]; then
        echo -e "  ${GREEN}✓${NC} freej2me_libretro.so ($(stat -f%z src/libretro/freej2me_libretro.so 2>/dev/null || stat -c%s src/libretro/freej2me_libretro.so) bytes)"
    else
        echo -e "  ${RED}✗${NC} freej2me_libretro.so (not found)"
    fi
    
    echo ""
    
    # Check Docker images
    echo "Docker Images:"
    if docker images | grep -q "freej2me-plus-lakka"; then
        echo -e "  ${GREEN}✓${NC} Docker image exists"
    else
        echo -e "  ${RED}✗${NC} Docker image not found"
    fi
}

# Function to setup environment
setup_environment() {
    local variant=${1:-default}
    print_info "Setting up build environment with variant: $variant..."
    check_prerequisites
    
    case $variant in
        ubuntu18)
            print_info "Using Ubuntu 18.04 variant..."
            docker-compose --profile ubuntu18 build freej2me-builder-ubuntu18
            ;;
        corretto)
            print_info "Using Amazon Corretto variant..."
            docker-compose --profile corretto build freej2me-builder-corretto
            ;;
        default|"")
            print_info "Using default variant (OpenJDK 8)..."
            docker-compose build freej2me-builder
            ;;
        *)
            print_error "Unknown variant: $variant"
            print_info "Available variants: default, ubuntu18, corretto"
            exit 1
            ;;
    esac
    
    print_success "Environment setup completed!"
}

# Function to build JAR files
build_jar() {
    print_info "Building JAR files..."
    make build-jar
    print_success "JAR files built successfully!"
}

# Function to build libretro core
build_so() {
    print_info "Building libretro core..."
    make build-so
    print_success "Libretro core built successfully!"
}

# Function to build everything
build_all() {
    print_info "Building everything..."
    make build-all
    print_success "All builds completed successfully!"
    show_status
}

# Function to clean builds
clean_builds() {
    print_info "Cleaning build artifacts..."
    make clean
    print_success "Build artifacts cleaned!"
}

# Function to clean everything
clean_all() {
    print_warning "This will remove all build artifacts and Docker images."
    read -p "Are you sure? (y/N): " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "Cleaning everything..."
        make clean-docker
        print_success "Everything cleaned!"
    else
        print_info "Operation cancelled."
    fi
}

# Function to start interactive shell
start_shell() {
    local variant=${1:-default}
    print_info "Starting interactive shell with variant: $variant..."
    
    case $variant in
        ubuntu18)
            docker-compose --profile ubuntu18 run --rm freej2me-builder-ubuntu18
            ;;
        corretto)
            docker-compose --profile corretto run --rm freej2me-builder-corretto
            ;;
        default|"")
            docker-compose run --rm freej2me-builder
            ;;
        *)
            print_error "Unknown variant: $variant"
            print_info "Available variants: default, ubuntu18, corretto"
            exit 1
            ;;
    esac
}

# Main script logic
case "${1:-help}" in
    setup)
        setup_environment "$2"
        ;;
    build-jar)
        build_jar
        ;;
    build-so)
        build_so
        ;;
    build-all)
        build_all
        ;;
    clean)
        clean_builds
        ;;
    clean-all)
        clean_all
        ;;
    shell)
        start_shell "$2"
        ;;
    status)
        show_status
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        print_error "Unknown command: $1"
        echo ""
        show_help
        exit 1
        ;;
esac
