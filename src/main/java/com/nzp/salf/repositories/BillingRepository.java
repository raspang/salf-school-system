package com.nzp.salf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.salf.entities.Billing;
import com.nzp.salf.entities.Course;


public interface BillingRepository extends JpaRepository<Billing, Long>{

	Billing findFirstByPaymentDetailAndCourse(String paymentDetal, Course course);
	
	Billing findFirstByPaymentDetail(String paymentDetal);
	

}
	