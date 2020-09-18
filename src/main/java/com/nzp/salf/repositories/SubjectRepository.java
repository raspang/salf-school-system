package com.nzp.salf.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nzp.salf.entities.Course;
import com.nzp.salf.entities.Subject;


public interface SubjectRepository extends JpaRepository<Subject, Long>{

	@Query("SELECT s FROM Subject s WHERE s.title LIKE %?1% and s.enable = true")
	Page<Subject> findAll(String keyword, Pageable pageable);

	List<Subject> findByCurriculumYearAndSemesterAndEnable(String curriculumYear, String semester, Boolean enable);
	
	Boolean existsByTitle(String title);
}
	