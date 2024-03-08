# Use a base image with necessary tools
FROM ubuntu:latest

# Install necessary packages
RUN apt-get update && \
    apt-get install -y \
    libcap-dev \
    asciidoc-base \
    libsystemd-dev \
    g++ \
    gcc \
    git \
    make \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Clone isolate repository
RUN git clone https://github.com/ioi/isolate.git /tmp/isolate

# Build and install isolate
RUN cd /tmp/isolate && \
    make && \
    make install 

# Start isolate-cg-keeper daemon
CMD ["isolate-cg-keeper"]
