package com.hari134.api_gateway.repository;


import org.springframework.stereotype.Repository;

import com.hari134.api_gateway.entity.User;
import com.hari134.api_gateway.repository.base.BaseRepository;;

@Repository
public interface UserRepository extends BaseRepository<User,Long>{

}
