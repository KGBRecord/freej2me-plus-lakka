# FreeJ2ME-Plus Build Environment
FROM openjdk:8-jdk

# Install required packages
RUN apt-get update && apt-get install -y \
    ant \
    make \
    gcc \
    g++ \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /workspace

# Create build output directory
RUN mkdir -p /workspace/build

# Default command
CMD ["/bin/bash"]
