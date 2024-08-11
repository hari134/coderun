FROM ubuntu:20.04

RUN export DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt-get install -y \
    pkgconf \
    libcap-dev \
    libsystemd-dev \
    g++ \
    gcc \
    git \
    make \
    build-essential \
    python3 \
    python3-pip \
    && rm -rf /var/lib/apt/lists/*

RUN git clone https://github.com/ioi/isolate.git /tmp/isolate

RUN cd /tmp/isolate && \
    make install

COPY scripts /scripts
RUN chmod -R +x /scripts

RUN chmod +x /usr/local/sbin/isolate-cg-keeper

ENTRYPOINT ["/usr/local/sbin/isolate-cg-keeper"]

CMD ["/bin/bash"]
