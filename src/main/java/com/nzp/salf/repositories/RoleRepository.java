package com.nzp.salf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.salf.entities.Role;



public interface RoleRepository extends JpaRepository<Role, Long>{

	public Role findByName(String name);
}
