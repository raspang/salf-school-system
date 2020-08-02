package com.nzp.salf.web;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nzp.salf.entities.Subject;
import com.nzp.salf.repositories.CourseRepository;
import com.nzp.salf.repositories.SubjectRepository;


@Controller
@RequestMapping("/subjects")
public class SubjectController{

	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	/*
	 * @GetMapping("/list") public String showSubjects(Model theModel) {
	 * List<Subject> subjects =
	 * subjectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	 * theModel.addAttribute("subjects", subjects); return "subject/subjects"; }
	 */
	
	@GetMapping("/list")
	public String showSubjectsPageByPage(HttpServletRequest request, Model theModel) {
		int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
      
		theModel.addAttribute("subjects", subjectRepository.findAll(request.getParameter("keyword"), PageRequest.of(page, size)));
        theModel.addAttribute("keyword", request.getParameter("keyword"));
		return "subject/subjects";
	}
	
	
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		Subject subject = new Subject();
		theModel.addAttribute("courses", courseRepository.findAll());
		theModel.addAttribute("subject", subject);
		return "subject/subject-form";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("subjectId") Long theId, Model theModel) {
		Optional<Subject> subject = subjectRepository.findById(theId);
		theModel.addAttribute("courses", courseRepository.findAll());
		theModel.addAttribute("subject", subject);
		return "subject/subject-form";	
	}
	
	@PostMapping("/save")
	public String saveSubject(@Valid @ModelAttribute("subject") Subject theSubject,
			BindingResult bindingResult, Model theModel) {
		
		if(bindingResult.hasErrors()) {
			theModel.addAttribute("courses", courseRepository.findAll());
			return "subject/subject-form";	
		}
		subjectRepository.save(theSubject);
		return "redirect:/subjects/list";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("subjectId") Long theId) {
		subjectRepository.deleteById(theId);
		return "redirect:/subjects/list";
	}
	
	/*
	 * public String getPrincipal() { String username; Object principal =
	 * SecurityContextHolder.getContext().getAuthentication().getPrincipal(); if
	 * (principal instanceof UserDetails) { username =
	 * ((UserDetails)principal).getUsername(); } else { username =
	 * principal.toString(); } return username; }
	 */

}