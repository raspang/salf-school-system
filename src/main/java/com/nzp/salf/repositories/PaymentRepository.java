package com.nzp.salf.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nzp.salf.entities.Payment;
import com.nzp.salf.entities.StudentRegistration;


public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	List<Payment> findByStudentRegistration(StudentRegistration studentRegistration);
	
	Payment findFirstByPaymentDetailAndStudentRegistration(String paymentDetail, StudentRegistration studentRegistration);
	
}
	