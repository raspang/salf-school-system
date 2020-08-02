package com.nzp.salf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.salf.entities.AcademicYear;


public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long>{

	AcademicYear findByCurrent(Boolean current);
	
}
	