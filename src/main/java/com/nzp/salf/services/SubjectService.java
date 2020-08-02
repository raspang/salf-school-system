package com.nzp.salf.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nzp.salf.entities.Course;
import com.nzp.salf.entities.Subject;
import com.nzp.salf.repositories.SubjectRepository;

@Service
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;
	
	public List<Subject> filterCourse(Course course){
		
		List<Subject> subjects = new ArrayList<>();
		
		for(Subject s : subjectRepository.findAll()) {
			for(Course c : s.getCourses())
				if(c.getId() == course.getId()) {
					subjects.add(s);
					break;
				}					
		}
		return subjects;
		
	}

}
