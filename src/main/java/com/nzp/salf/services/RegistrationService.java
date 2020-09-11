package com.nzp.salf.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nzp.salf.entities.AcademicYear;
import com.nzp.salf.entities.Billing;
import com.nzp.salf.entities.BillingDetail;
import com.nzp.salf.entities.Payment;
import com.nzp.salf.entities.Student;
import com.nzp.salf.entities.StudentRegistration;
import com.nzp.salf.repositories.BillingRepository;
import com.nzp.salf.repositories.StudentRegistrationRepository;

@Service
public class RegistrationService {

	@Autowired
	private StudentRegistrationRepository studentRegistrationRepository;
	
	@Autowired
	private BillingRepository billingRepository;
	
	public Boolean findExist(Student student, AcademicYear academicYear){
		
		List<StudentRegistration> studentRegistrations = studentRegistrationRepository.findByStudentAndAcademicYear(student, academicYear);
		
		if(studentRegistrations.size() > 0) {
			
			for(StudentRegistration studentRegistration : studentRegistrations) {
			
				Student studentTemp = studentRegistration.getStudent();
				AcademicYear academicYearTemp = studentRegistration.getAcademicYear();
			
				if(student.getId().equals(studentTemp.getId()) && academicYear.getId().equals(academicYearTemp.getId()))
					return true;
			}
				
		}
		return false;
		
	}
	
	public List<Payment> getPayments(StudentRegistration theStudentRegistration){
		return Arrays.asList(
		getPayment(BillingDetail.ENTRANCE.getDetail(), theStudentRegistration ),
		getPayment(BillingDetail.UNITS.getDetail(), theStudentRegistration ),
		getPayment(BillingDetail.MISCELLANEOUS.getDetail(), theStudentRegistration ),
		getPayment(BillingDetail.LABORATORY.getDetail(), theStudentRegistration ),
		getPayment(BillingDetail.EVALUATION.getDetail(), theStudentRegistration )
		);
	
	}
	
	private Payment getPayment(String detail,  StudentRegistration theStudentRegistration) {
		
		Billing billing = billingRepository.findFirstByPaymentDetailAndCourse(detail, theStudentRegistration.getCourse());	
		
		if(billing == null) {
			billing = billingRepository.findFirstByPaymentDetail(detail);	
		}

	
		if(billing == null) {
			return new Payment(detail, null, theStudentRegistration);
		}
		if(detail.equals(BillingDetail.UNITS.getDetail())) {
			return new Payment(detail, billing.getAmount()*theStudentRegistration.getTotalUnits(), theStudentRegistration);
		}

		return new Payment(detail, billing.getAmount(), theStudentRegistration);
		
		 
	}


}
