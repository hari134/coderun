package com.hari134.executor.judge;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.hari134.executor.dto.judge.ContainerResponse;
import com.hari134.executor.dto.judge.ExecutionConfig;
import com.hari134.executor.dto.queue.SubmissionResponseQueueMessage;
import com.hari134.executor.judge.impl.CoderunJudge;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true",
    "spring.main.lazy-initialization=true"
})
public class CoderunJudgeTest {

  @Autowired
  private CoderunJudge coderunJudge;

  @Test
  public void testPlainExecution() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "\n#include <iostream>\nusing namespace std;\nint main() {\n for(int i=0;i<5;i++){\n cout<<i;\n }\n\n return 0;\n}";
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
        System.out.println("output");
        System.out.println(result.getStdOut());
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),result.getStdErr(),correlationId);
        System.out.println(executionResult.toString());

        System.out.println("error");
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

  @Test
  public void testExecutionWithMLE() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "#include <iostream>\n#include <cstdlib>\n\nint main() {\n    const size_t totalSize = 200 * 1024 * 1024;\n    const size_t incrementSize = 1024 * 1024*10; // 1 MB\n    size_t allocatedSize = 0;\n    char* buffer = nullptr;\n\n    try {\n        buffer = new char[totalSize];\n        while (allocatedSize < totalSize) {\n            std::fill(buffer + allocatedSize, buffer + allocatedSize + incrementSize, 1);\n            allocatedSize += incrementSize;\n            std::cout << \"Allocated \" << allocatedSize / (1024 * 1024) << \" MB\" << std::endl;\n        }\n        std::cout << \"Memory allocated successfully.\" << std::endl;\n    } catch (const std::bad_alloc& e) {\n        std::cerr << \"Memory allocation failed: \" << e.what() << std::endl;\n        delete[] buffer;\n        return 1;\n    }\n\n    std::cout << \"Using the allocated memory...\" << std::endl;\n\n    delete[] buffer;\n    std::cout << \"Memory deallocated successfully.\" << std::endl;\n\n    return 0;\n}\n";
    ExecutionConfig executionConfig = new ExecutionConfig(
        correlationId,
        "cpp",
        code,
        "2",
        "128000",
        boxId,
        "",
        "");

    CompletableFuture<ContainerResponse> future = coderunJudge.executeAsync(executionConfig);
    future.thenAccept(result -> {
      try {
        System.out.println("output");
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),result.getStdErr(),correlationId);
        System.out.println(executionResult.toString());
        System.out.println("error");
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

  @Test
  public void testExecutionWithTLE() {

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
        System.out.println("output");
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),result.getStdErr(),correlationId);
        System.out.println(executionResult.toString());
        System.out.println("error");
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

  @Test
  public void testExecutionWithCompileTimeError() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "\n#include <iostream>\nusing namespace std;\nint {\n for(int i=0;i<10000000;i++){\n cout<<i<<endl;\n }\n\n return 0;\n}";
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
        System.out.println("output");
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),result.getStdErr(),correlationId);
        System.out.println(executionResult.toString());

        System.out.println("error");
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

  @Test
  public void testExecutionWithRunTimeError() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "#include <iostream>\n\nusing namespace std;\n\nint main() {\n    int numerator = 10;\n    int denominator = 0;\n    \n    cout << \"Before division\" << endl;\n    \n    int result = numerator / denominator;\n    \n    cout << \"After division\" << endl;\n    cout << \"Result: \" << result << endl;\n    \n    return 0;\n}\n";
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
        System.out.println("output");
        System.out.println(result.getStdOut());
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),result.getStdErr(),correlationId);
        System.out.println(executionResult.toString());

        System.out.println("error");
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
