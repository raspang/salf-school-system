package com.nzp.salf.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.salf.entities.User;



public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);
	


}
