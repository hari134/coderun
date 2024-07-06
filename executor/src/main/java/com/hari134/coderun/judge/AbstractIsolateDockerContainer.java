package com.hari134.coderun.judge;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractIsolateDockerContainer {
  private static final AtomicInteger boxIdCounter = new AtomicInteger(0);

  public String getUniqueBoxId() {
    return String.valueOf(boxIdCounter.incrementAndGet());
  }

  protected void decrementBoxId(){
    boxIdCounter.decrementAndGet();
  }
}
