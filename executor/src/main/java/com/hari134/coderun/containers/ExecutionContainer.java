package com.hari134.coderun.containers;

import com.hari134.coderun.dto.container.ContainerResponse;

public interface ExecutionContainer {
    public ContainerResponse execute(ExecutionConfig executionConfig); 
}