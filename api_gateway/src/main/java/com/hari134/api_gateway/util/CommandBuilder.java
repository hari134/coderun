package com.hari134.api_gateway.util;

import java.util.HashMap;

import com.hari134.api_gateway.dto.api.SubmissionRequest;

class LanguageScriptMappings {
  static final HashMap<String, String> RUN_SCRIPTS;
  static final HashMap<String, String> JUDGE_SCRIPTS;

  static {
    RUN_SCRIPTS= new HashMap<>();
    RUN_SCRIPTS.put("cpp", "scripts/run/cpp.sh");

    JUDGE_SCRIPTS= new HashMap<>();
    JUDGE_SCRIPTS.put("cpp", "scripts/judge/cpp.sh");
  }
}

public class CommandBuilder {
  public static String build(SubmissionRequest SubmissionRequest) {

  }
}