package com.nzp.salf.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.nzp.salf.repositories.StudentRepository;

public class UniqueConstraintValidator 
	implements ConstraintValidator<Unique, String> {

	@Autowired
	private StudentRepository studentRepository;
	
    @Override
    public void initialize(Unique unique) {
        unique.message();
    }

	@Override
	public boolean isValid(String studentId, 
						ConstraintValidatorContext theConstraintValidatorContext) {
		return studentRepository != null && studentRepository.existsByStudentId(studentId) ? false : true;
		
	}
}
