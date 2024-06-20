package com.hari134.api_gateway.service;

import java.util.List;
import java.util.Optional;

import com.hari134.api_gateway.entity.Language;

public interface LanguageService {
    Language saveLanguage(Language language);
    Optional<Language> getLanguageById(Long id);
    List<Language> getAllLanguages();
    void deleteLanguage(Long id);
}
