# Use a base image with necessary tools
FROM ubuntu:20.04

# Install necessary packages
RUN export DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt-get install -y \
    pkgconf \
    libcap-dev \
    libsystemd-dev -y \
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
    make install


COPY scripts /scripts


# Make the script executable
RUN chmod +x /usr/local/sbin/isolate-cg-keeper

# Run isolate-cg-keeper script in the background
ENTRYPOINT ["/usr/local/sbin/isolate-cg-keeper"]

CMD ["/bin/bash"]
