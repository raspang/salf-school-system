package com.nzp.salf.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nzp.salf.entities.AcademicYear;
import com.nzp.salf.entities.Student;
import com.nzp.salf.entities.StudentRegistration;
import com.nzp.salf.repositories.StudentRegistrationRepository;

@Service
public class RegistrationService {

	@Autowired
	private StudentRegistrationRepository studentRegistrationRepository;
	
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

}
