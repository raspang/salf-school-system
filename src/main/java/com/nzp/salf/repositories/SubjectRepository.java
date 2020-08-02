package com.nzp.salf.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nzp.salf.entities.Subject;


public interface SubjectRepository extends JpaRepository<Subject, Long>{

	@Query("SELECT s FROM Subject s WHERE s.title LIKE %?1%")
	Page<Subject> findAll(String keyword, Pageable pageable);

}
	