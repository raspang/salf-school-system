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
	

	
	@GetMapping("/list")
	public String showSubjectsPageByPage(HttpServletRequest request, Model theModel) {
		int page = 0; //default page number is 0 (yes it is weird)
        int size = 15; //default page size is 10
        
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
		theModel.addAttribute("courses", courseRepository.findByEnableOrderByIdDesc(true));
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
			if(theSubject.getId() != null)
				theModel.addAttribute("courses", courseRepository.findAll());
			else
				theModel.addAttribute("courses", courseRepository.findByEnableOrderByIdDesc(true));
			return "subject/subject-form";	
		}
		
		String success = "created";
		if(theSubject.getId() != null) {
			success = "updated";
		}
		
		
		subjectRepository.save(theSubject);
		return "redirect:/subjects/list?success="+success;
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("subjectId") Long theId) {
		
		Optional<Subject> temp = subjectRepository.findById(theId);
		Subject theSubject = temp.orElse(null);
		if(theSubject != null) theSubject.setEnable(false); 
		subjectRepository.save(theSubject);
		
		return "redirect:/subjects/list";
	}
	


}
