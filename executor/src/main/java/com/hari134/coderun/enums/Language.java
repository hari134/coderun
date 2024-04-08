package com.hari134.coderun.enums;

public enum Language {
   CPP,
   JAVA,
   PYTHON;

   public static Language fromString(String language) {
        try {
            return Language.valueOf(language.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid programming language: " + language);
        }
    }
}
