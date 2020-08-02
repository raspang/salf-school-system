package com.nzp.salf.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nzp.salf.entities.Student;


public interface StudentRepository extends JpaRepository<Student, Long>{
	
	@Query("SELECT s FROM Student s WHERE s.firstName LIKE %?1%"
            + " OR s.lastName LIKE %?1%"
            + " OR s.id LIKE %?1% order by s.id desc")
	Page<Student> findAll(String keyword, Pageable pageable);
	
	List<Student> findByIsRegistered(Boolean isRegistered);
}
	