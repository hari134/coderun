package com.hari134.coderun.judge;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hari134.coderun.dto.judge.ContainerResponse;
import com.hari134.coderun.dto.judge.ExecutionConfig;
import com.hari134.coderun.judge.impl.CoderunJudge;

@SpringBootTest
public class CoderunJudgeTest {

  @Autowired
  private CoderunJudge coderunJudge;

  @Test
  public void testPlainExecution() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "\n#include <iostream>\nusing namespace std;\nint main() {\n for(int i=0;i<10000000;i++){\n cout<<i<<endl;\n }\n\n return 0;\n}";
    ExecutionConfig executionConfig = new ExecutionConfig(
        correlationId,
        "cpp",
        code,
        "2",
        "256000",
        boxId,
        "",
        "");

    CompletableFuture<ContainerResponse> future = coderunJudge.executeAsync(executionConfig);
    future.thenAccept(result -> {
      try {
        System.out.println("............\n............");
        System.out.println(result.getStdOut());
        System.out.println(result.getStdErr());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).exceptionally(exception -> {
      try {
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }).join();
  }
}
