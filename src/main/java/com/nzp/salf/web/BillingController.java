package com.nzp.salf.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nzp.salf.entities.Billing;
import com.nzp.salf.entities.BillingDetail;
import com.nzp.salf.entities.Course;
import com.nzp.salf.repositories.BillingRepository;
import com.nzp.salf.repositories.CourseRepository;


@Controller
@RequestMapping("/billings")
public class BillingController{

	
	@Autowired
	private BillingRepository billingRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@GetMapping("/list")
	public String showCourses(HttpServletRequest request,Model theModel) {
		List<Billing> billings = billingRepository.findAll();
		theModel.addAttribute("billings", billings);
		return "billing/billings";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		Billing billing = new Billing();
		theModel.addAttribute("paymentDetails", listOfBillings());
		theModel.addAttribute("courses",  courseRepository.findByEnableOrderByIdDesc(true));
		theModel.addAttribute("billing", billing);
		return "billing/billing-form";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("billingId") Long theId, Model theModel) {
		Optional<Billing> billing = billingRepository.findById(theId);
		theModel.addAttribute("paymentDetails", listOfBillings());
		theModel.addAttribute("courses",  courseRepository.findByEnableOrderByIdDesc(true));
		theModel.addAttribute("billing", billing);
		return "billing/billing-form";
	}
	
	@PostMapping("/save")
	public String saveCourse(@Valid @ModelAttribute("billing") Billing theBilling,
			BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
			return "billing/billing-form";

		String success = "created";
		if(theBilling.getId() != null) {
			success = "updated";
		}
		
		billingRepository.save(theBilling);
		return "redirect:/billings/list?success="+success;
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("billingId") Long theId) {
		billingRepository.deleteById(theId);
		return "redirect:/billings/list";
	}
	
	
	private List<String> listOfBillings(){
		return Arrays.asList(
				BillingDetail.ENTRANCE.getDetail(),
				BillingDetail.UNITS.getDetail(),
				BillingDetail.MISCELLANEOUS.getDetail(),
				BillingDetail.LABORATORY.getDetail(),
				BillingDetail.EVALUATION.getDetail()

				);
		
	}
	


}
