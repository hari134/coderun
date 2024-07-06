package com.hari134.api_gateway.repository;

import org.springframework.stereotype.Repository;

import com.hari134.api_gateway.entity.AuthMethod;
import com.hari134.api_gateway.repository.base.BaseRepository;;

@Repository
public interface AuthMethodRepository extends BaseRepository<AuthMethod,Long>{

}

