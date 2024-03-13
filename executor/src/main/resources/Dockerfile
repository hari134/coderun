# Use a base image with necessary tools
FROM ubuntu:latest

# Install necessary packages
RUN export DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt-get install -y \
    pkgconf -y \ 
    libcap-dev -y \
    libsystemd-dev -y \
    g++ -y \
    gcc -y \
    git -y \
    make -y \
    build-essential -y \
    && rm -rf /var/lib/apt/lists/*

# Clone isolate repository
RUN git clone https://github.com/ioi/isolate.git /tmp/isolate

# Build and install isolate
RUN cd /tmp/isolate && \
    make install 

COPY scripts /scripts
CMD tail -f /dev/null
