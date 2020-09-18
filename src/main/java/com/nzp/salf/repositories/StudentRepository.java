package com.nzp.salf.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nzp.salf.entities.Student;


public interface StudentRepository extends JpaRepository<Student, Long>{
	
	/*
	 * @Query("SELECT s FROM Student s WHERE (s.firstName LIKE %?3%" +
	 * " OR s.lastName LIKE %?2%" +
	 * " OR s.studentId LIKE %?1%) and s.enable = true")
	 */
	Page<Student> findByStudentIdStartingWithAndEnable(String studentId,  Boolean enable, Pageable pageable);
	
	Page<Student> findByLastNameStartingWithAndEnable( String lastName,  Boolean enable, Pageable pageable);
	
	Page<Student> findByFirstNameStartingWithAndEnable(String firstName, Boolean enable, Pageable pageable);
	
	Page<Student> findByEnable( Boolean enable, Pageable pageable);
	
	List<Student> findByIsRegistered(Boolean isRegistered);
	
	Boolean existsByStudentId(String studentId);
}
	