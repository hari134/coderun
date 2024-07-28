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

  // @Test
  public void testPlainExecution() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "\n#include <iostream>\nusing namespace std;\nint main() {\n for(int i=0;i<5;i++){\n cout<<i;\n }\n\n return 0;\n}";
    ExecutionConfig executionConfig = new ExecutionConfig(
        correlationId,
        "cpp",
        code,
        "2",
        "3",
        "256000",
        boxId,
        "",
        "");

    CompletableFuture<ContainerResponse> future = coderunJudge.executeAsync(executionConfig);
    future.thenAccept(result -> {
      try {
        System.out.println("output");
        System.out.println(result.getStdOut());
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),
            result.getStdErr(), correlationId);
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
    String code = "#include <iostream>\n"
        + "int main() {\n"
        + "    const size_t numIntegers = 10 * 1024 * 1024 / sizeof(int);\n"
        + "    int* array = new int[numIntegers];\n"
        + "    for (size_t i = 0; i < numIntegers; ++i) {\n"
        + "        array[i] = 0;\n"
        + "    }\n"
        + "    std::cout << \"Array of \" << numIntegers << \" integers initialized.\" << std::endl;\n"
        + "    return 0;\n"
        + "}\n";

    ExecutionConfig executionConfig = new ExecutionConfig(
        correlationId,
        "cpp",
        code,
        "2", // CPU limit
        "3",
        String.valueOf(1024 * 9), // Memory limit (in KB), 9 MB for 10 MB allocated in code
        boxId,
        "",
        "");

    CompletableFuture<ContainerResponse> future = coderunJudge.executeAsync(executionConfig);
    future.thenAccept(result -> {
      try {
        System.out.println("Output:");
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),
            result.getStdErr(), correlationId);
        System.out.println(executionResult.toString());
        System.out.println("Error:");
        System.out.println(result.getStdErr());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).exceptionally(exception -> {
      exception.printStackTrace();
      return null;
    }).join();
  }

  // @Test
  public void testStreamingExecution() {
    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "\n#include <iostream>\nusing namespace std;\nint main() {\n for(int i=0;i<5000000;i++){\n cout<<i<<endl;\n }\n\n return 0;\n}";
    ExecutionConfig executionConfig = new ExecutionConfig(
        correlationId,
        "cpp",
        code,
        "1",
        "3",
        "256000",
        boxId,
        "",
        "");

    CompletableFuture<ContainerResponse> future = coderunJudge.executeAsyncStreaming(executionConfig);
    future.thenAccept(result -> {
      try {
        // System.out.println("Output:");
        // System.out.println(result.getStdOut());

        // System.out.println("Error:");
        // System.out.println(result.getStdErr());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).exceptionally(exception -> {
      exception.printStackTrace();
      return null;
    }).join();
  }

  // @Test
  public void testExecutionWithTLE() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "\n#include <iostream>\nusing namespace std;\nint main() {\n for(int i=0;i<10000000;i++){\n cout<<i<<endl;\n }\n\n return 0;\n}";
    ExecutionConfig executionConfig = new ExecutionConfig(
        correlationId,
        "cpp",
        code,
        "2",
        "3",
        "256000",
        boxId,
        "",
        "");

    CompletableFuture<ContainerResponse> future = coderunJudge.executeAsync(executionConfig);
    future.thenAccept(result -> {
      try {
        System.out.println("output");
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),
            result.getStdErr(), correlationId);
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

  // @Test
  public void testExecutionWithCompileTimeError() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "\n#include <iostream>\nusing namespace std;\nint {\n for(int i=0;i<10000000;i++){\n cout<<i<<endl;\n }\n\n return 0;\n}";
    ExecutionConfig executionConfig = new ExecutionConfig(
        correlationId,
        "cpp",
        code,
        "2",
        "3",
        "256000",
        boxId,
        "",
        "");

    CompletableFuture<ContainerResponse> future = coderunJudge.executeAsync(executionConfig);
    future.thenAccept(result -> {
      try {
        System.out.println("output");
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),
            result.getStdErr(), correlationId);
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

  // @Test
  public void testExecutionWithRunTimeError() {

    String correlationId = "test-id";
    String boxId = coderunJudge.getUniqueBoxId();
    String code = "#include <iostream>\n\nusing namespace std;\n\nint main() {\n    int numerator = 10;\n    int denominator = 0;\n    \n    cout << \"Before division\" << endl;\n    \n    int result = numerator / denominator;\n    \n    cout << \"After division\" << endl;\n    cout << \"Result: \" << result << endl;\n    \n    return 0;\n}\n";
    ExecutionConfig executionConfig = new ExecutionConfig(
        correlationId,
        "cpp",
        code,
        "2",
        "3",
        "256000",
        boxId,
        "",
        "");

    CompletableFuture<ContainerResponse> future = coderunJudge.executeAsync(executionConfig);
    future.thenAccept(result -> {
      try {
        System.out.println("output");
        System.out.println(result.getStdOut());
        SubmissionResponseQueueMessage executionResult = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),
            result.getStdErr(), correlationId);
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
