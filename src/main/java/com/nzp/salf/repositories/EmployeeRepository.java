package com.nzp.salf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.salf.entities.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	public Employee findFirstByPositionIdAndSelected(String positionId, Boolean selected);
	
}
	