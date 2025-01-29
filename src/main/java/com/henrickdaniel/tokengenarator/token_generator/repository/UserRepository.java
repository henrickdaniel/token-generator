package com.henrickdaniel.tokengenarator.token_generator.repository;


import com.henrickdaniel.tokengenarator.token_generator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    Boolean existsByName(String name);

}
