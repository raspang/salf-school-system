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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nzp.salf.entities.Employee;
import com.nzp.salf.repositories.EmployeeRepository;


@Controller
@RequestMapping("/employees")
public class EmployeeController{

	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@GetMapping("/list")
	public String showCourses(Model theModel) {
		List<Employee> employees = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		theModel.addAttribute("employees", employees);
		return "employee/employees";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		Employee employee = new Employee();
		theModel.addAttribute("employee", employee);
		return "employee/employee-form";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") Long theId, Model theModel) {
		Optional<Employee> employee = employeeRepository.findById(theId);
		theModel.addAttribute("employee", employee);
		return "employee/employee-form";	
	}
	
	@PostMapping("/save")
	public String saveCourse(@Valid @ModelAttribute("employee") Employee theEmployee,
			BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
			return "employee/employee-form";

		String success = "created";
		if(theEmployee.getId() != null) {
			success = "updated";
		}
		
		if(theEmployee.getSelected()) {
			Employee theOther = employeeRepository.findFirstByPositionIdAndSelected(theEmployee.getPositionId(), true);
			theOther.setSelected(false);
			employeeRepository.save(theOther);
		}
		
		employeeRepository.save(theEmployee);
		return "redirect:/employees/list?success="+success;
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("employeeId") Long theId) {
		employeeRepository.deleteById(theId);
		return "redirect:/employees/list";
	}
	
	
}
