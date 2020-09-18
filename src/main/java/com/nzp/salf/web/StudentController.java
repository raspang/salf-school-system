package com.nzp.salf.web;

import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nzp.salf.entities.AcademicYear;
import com.nzp.salf.entities.Student;
import com.nzp.salf.repositories.AcademicYearRepository;
import com.nzp.salf.repositories.CourseRepository;
import com.nzp.salf.repositories.StudentRepository;


@Controller
@RequestMapping("/students")
public class StudentController{

	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private AcademicYearRepository academicYearRepository;
	
	
	@GetMapping("/list")
	public String showStudents(HttpServletRequest request, Model theModel) {

		int page = 0; 
        int size = 15; 
        	
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        
        String studentId = request.getParameter("studentId");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");

   
        Pageable sortedBy;
 
        
		theModel.addAttribute("studentId", studentId);
		
		if(studentId != null && !studentId.isEmpty()) {
			sortedBy =  PageRequest.of(page, size, Sort.by("studentId"));
			theModel.addAttribute("students", studentRepository.findByStudentIdStartingWithAndEnable(studentId, true, sortedBy));
			theModel.addAttribute("studentId", studentId);
		}else if(lastName != null && !lastName.isEmpty()) {
			sortedBy =  PageRequest.of(page, size, Sort.by("lastName"));
			theModel.addAttribute("students", studentRepository.findByLastNameStartingWithAndEnable(lastName, true, sortedBy));
			theModel.addAttribute("lastName", lastName);
		}else if(firstName != null && !firstName.isEmpty()) {
			sortedBy =  PageRequest.of(page, size, Sort.by("firstName"));
			theModel.addAttribute("students", studentRepository.findByFirstNameStartingWithAndEnable(firstName, true, sortedBy));
			theModel.addAttribute("firstName", firstName);
		}else {
			sortedBy =  PageRequest.of(page, size, Sort.by("lastName"));
			theModel.addAttribute("students", studentRepository.findByEnable( true, sortedBy));
		}
		return "student/students";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		Student student = new Student();
		theModel.addAttribute("student", student);
		theModel.addAttribute("courses", courseRepository.findByEnableOrderByIdDesc(true));
		return "student/student-form";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("studentId") Long theId, Model theModel) {
		Optional<Student> student = studentRepository.findById(theId);
		theModel.addAttribute("student", student);
		theModel.addAttribute("courses", courseRepository.findAll());
		return "student/student-form";	
	}
	
	@PostMapping("/save")
	public String saveStudent(@Valid @ModelAttribute("student") Student theStudent,
			BindingResult bindingResult, Model theModel) {
		
		String success = "created";
		if(theStudent.getId() != null) {
			success = "updated";
		}
		
		
		if(bindingResult.hasErrors()) {
			theModel.addAttribute("courses", courseRepository.findAll());
			return "student/student-form";
		}
		
		if(!theStudent.getActive())
			theStudent.setDeactivateDate(LocalDate.now());
		if(theStudent.getDeactivateDate() != null && theStudent.getActive())
			theStudent.setDeactivateDate(null);
		
		studentRepository.save(theStudent);
		return "redirect:/students/list?success="+success;
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("studentId") Long theId) {
		Optional<Student> temp = studentRepository.findById(theId);
		Student theStudent = temp.orElse(null);
		if(theStudent != null) theStudent.setEnable(false); 
		studentRepository.save(theStudent);
		return "redirect:/students/list";
	}
	
	public String getPrincipal() {
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		} else {
		   username = principal.toString();
		}
		return username;
	}
	@ModelAttribute("academicYear")
	public AcademicYear getSchoolYear() {
		return academicYearRepository.findFirstByCurrent(true);

	}

}
