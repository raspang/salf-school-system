package com.nzp.salf.web;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nzp.salf.entities.Course;
import com.nzp.salf.repositories.CourseRepository;


@Controller
@RequestMapping("/courses")
public class CourseController{

	
	@Autowired
	private CourseRepository courseRepository;
	
	@GetMapping("/list")
	public String showCourses(HttpServletRequest request,Model theModel) {
		String keyword = request.getParameter("keyword");
		List<Course> courses = courseRepository.findAll(keyword, Sort.by(Sort.Direction.DESC, "id"));
		theModel.addAttribute("courses", courses);
		theModel.addAttribute("keyword",keyword);
		return "course/courses";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		Course course = new Course();
		theModel.addAttribute("course", course);
		return "course/course-form";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("courseId") Long theId, Model theModel) {
		Optional<Course> course = courseRepository.findById(theId);
		theModel.addAttribute("course", course);
		return "course/course-form";	
	}
	
	@PostMapping("/save")
	public String saveCourse(@Valid @ModelAttribute("course") Course theCourse,
			BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
			return "course/course-form";	

		String success = "created";
		if(theCourse.getId() != null) {
			success = "updated";
		}
		
		courseRepository.save(theCourse);
		return "redirect:/courses/list?success="+success;
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("courseId") Long theId) {
		Optional<Course> temp = courseRepository.findById(theId);
		Course theCourse = temp.orElse(null);
		if(theCourse != null) theCourse.setEnable(false); 
		courseRepository.save(theCourse);
		return "redirect:/courses/list";
	}

}
