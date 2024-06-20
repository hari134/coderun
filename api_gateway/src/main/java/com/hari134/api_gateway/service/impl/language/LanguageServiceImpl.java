package com.hari134.api_gateway.service.impl.language;

import com.hari134.api_gateway.entity.Language;
import com.hari134.api_gateway.repository.LanguageRepository;
import com.hari134.api_gateway.service.LanguageService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    @Transactional
    public Language saveLanguage(Language language) {
        return languageRepository.save(language);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Language> getLanguageById(Long id) {
        return languageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteLanguage(Long id) {
        languageRepository.deleteById(id);
    }
}
