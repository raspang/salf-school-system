package com.nzp.salf.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.salf.entities.AcademicYear;


public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long>{

	List<AcademicYear> findByEnableOrderByIdDesc(Boolean enable);
	
	AcademicYear findFirstByCurrent(Boolean current);
	
	Boolean existsByYearAndSemester(String year, String semester);
	
}
	