package com.nzp.salf.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nzp.salf.entities.AcademicYear;
import com.nzp.salf.entities.Student;
import com.nzp.salf.exception.ResourceNotFoundException;
import com.nzp.salf.repositories.AcademicYearRepository;
import com.nzp.salf.repositories.StudentRepository;


@Controller
@RequestMapping("/academicyears")
public class AcademicYearController{

	
	@Autowired
	private AcademicYearRepository academicYearRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("/list")
	public String showCourses(Model theModel) {
		List<AcademicYear> academicYears = academicYearRepository.findByEnableOrderByIdDesc(true);
		theModel.addAttribute("academicYears", academicYears);
		return "academicyear/academicyears";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		AcademicYear academicYear = new AcademicYear();
		theModel.addAttribute("academicYear", academicYear);
		theModel.addAttribute("edit", false);
		return "academicyear/academicyear-form";
	}
	
	/*
	 * @GetMapping("/showFormForUpdate") public String
	 * showFormForUpdate(@RequestParam("academicYearId") Long theId, Model theModel)
	 * { Optional<AcademicYear> academicYear =
	 * academicYearRepository.findById(theId); theModel.addAttribute("academicYear",
	 * academicYear); theModel.addAttribute("edit", true); return
	 * "academicyear/academicyear-form"; }
	 */
	

	@PostMapping("/save")
	public String saveCourse(@Valid @ModelAttribute("academicYear") AcademicYear theAcademicYear,
			BindingResult bindingResult, Model theModel) {
		

		
		if(bindingResult.hasErrors() || academicYearRepository.existsByYearAndSemester(theAcademicYear.getYear(), theAcademicYear.getSemester())) {
			
			theModel.addAttribute("edit", false);
			
			if(academicYearRepository.existsByYearAndSemester(theAcademicYear.getYear(), theAcademicYear.getSemester()) )
				theModel.addAttribute("alreadyExist", true);
			else 
				theModel.addAttribute("alreadyExist", false);
			
			return "academicyear/academicyear-form";
		}
		
		
		if(theAcademicYear.getCurrent()) {
			for(AcademicYear academicYear: academicYearRepository.findAll()) {
				if(academicYear.getCurrent()) {
					academicYear.setCurrent(false);
					academicYearRepository.save(academicYear);
				}
			}
				
		}

		String success = "created";
		if(theAcademicYear.getId() != null) {
			success = "updated";
		}
		
			
		academicYearRepository.save(theAcademicYear);
		
		/* Update all students to not registered */
		if(theAcademicYear.getCurrent()) {
			List<Student> students = studentRepository.findByIsRegistered(true);
			for(Student student: students) {
				student.setIsRegistered(false);
				studentRepository.save(student);
			}		
		}
		
		return "redirect:/academicyears/list?success="+success;
	}
	
	@GetMapping("/select/{academicYearId}")
	public String selectAY(@PathVariable Long academicYearId, Model theModel) {
		
		AcademicYear theAcademicYear = academicYearRepository.findById(academicYearId).orElseThrow(
				() -> new ResourceNotFoundException("AcademicYear", "id", academicYearId));
		
		if(!theAcademicYear.getCurrent()) {
			// turn off the current
			for(AcademicYear academicYear: academicYearRepository.findAll()) {
				if(academicYear.getCurrent()) {
					academicYear.setCurrent(false);
					academicYearRepository.save(academicYear);
				}
			}
				
		}
		theAcademicYear.setCurrent(true);
		
		String success = "created";
		if(theAcademicYear.getId() != null) {
			success = "updated";
		}

		academicYearRepository.save(theAcademicYear);
		
	
		
		
		/* Change the setting for students list: registration are now open*/

		List<Student> students = studentRepository.findByIsRegistered(true);
		for(Student student: students) {
			student.setIsRegistered(false);
			studentRepository.save(student);
		}		

		
		
		return "redirect:/academicyears/list?success="+success;
	}
	
	
	@GetMapping("/delete")
	public String delete(@RequestParam("academicYearId") Long theId) {
		Optional<AcademicYear> ay = academicYearRepository.findById(theId);
		AcademicYear theay = ay.orElse(null);
		if(theay != null) theay.setEnable(false);
		academicYearRepository.save(theay);
		return "redirect:/academicyears/list";
	}
	
	@ModelAttribute("currentAcademicYear")
	public AcademicYear getAcademicYear() {
		return academicYearRepository.findByCurrent(true);
	}
	
}
