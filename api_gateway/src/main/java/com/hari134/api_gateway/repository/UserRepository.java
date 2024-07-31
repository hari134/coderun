package com.hari134.api_gateway.repository;


import org.springframework.stereotype.Repository;

import com.hari134.api_gateway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

