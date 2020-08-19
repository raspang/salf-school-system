package com.nzp.salf.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nzp.salf.entities.Course;


public interface CourseRepository extends JpaRepository<Course, Long>{
	
	@Query("SELECT c FROM Course c WHERE c.title LIKE %?1% and c.enable = true")
	List<Course> findAll(String keyword, Sort sort);
	
	List<Course> findByEnableOrderByIdDesc(Boolean enable);
}
	