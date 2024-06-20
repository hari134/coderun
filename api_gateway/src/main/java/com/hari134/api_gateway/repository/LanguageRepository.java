package com.hari134.api_gateway.repository;


import com.hari134.api_gateway.entity.Language;
import com.hari134.api_gateway.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends BaseRepository<Language,Long> {
  
}

