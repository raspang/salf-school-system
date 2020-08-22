
package com.nzp.salf.web;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import com.nzp.salf.entities.Course;
import com.nzp.salf.entities.Employee;
import com.nzp.salf.entities.Student;
import com.nzp.salf.entities.StudentRegistration;
import com.nzp.salf.exception.ResourceNotFoundException;
import com.nzp.salf.repositories.AcademicYearRepository;
import com.nzp.salf.repositories.EmployeeRepository;
import com.nzp.salf.repositories.StudentRegistrationRepository;
import com.nzp.salf.repositories.StudentRepository;
import com.nzp.salf.services.RegistrationService;
import com.nzp.salf.services.ReportService;
import com.nzp.salf.services.SubjectService;

import net.sf.jasperreports.engine.JRException;


@Controller
@RequestMapping("/registrations")
public class RegistrationController {

	
	@Autowired
	private StudentRegistrationRepository studentRegistrationRepository;
	

	@Autowired
	private AcademicYearRepository academicYearRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private RegistrationService registrationService;

	
	@GetMapping("/list")
	public String showStudentRegistrations(HttpServletRequest request, Model theModel) {
		int page = 0; //default page number is 0 (yes it is weird)
        int size = 15; //default page size is 1
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        String keyword = request.getParameter("keyword");
        String academicYear = request.getParameter("academicYear");
        
		theModel.addAttribute("studentRegistrations", studentRegistrationRepository.findAllOrderById(keyword, academicYear, PageRequest.of(page, size)));
		theModel.addAttribute("keyword", keyword);
		theModel.addAttribute("academicYear", academicYear);
		theModel.addAttribute("academicYears", academicYearRepository.findByEnableOrderByIdDesc(true));
		
		return "registration/registrations";
	}
	
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(@RequestParam(name="studentId", required=true) Long theId, Model theModel) {
		StudentRegistration studentRegistration = new StudentRegistration();
		
		Employee registrar = employeeRepository.findFirstByPositionIdAndSelected("Registrar", true);
		Employee assessmentOfficer = employeeRepository.findFirstByPositionIdAndSelected("Assessment Officer", true);
		Student theStudent = studentRepository.findById(theId).orElseThrow( () -> new ResourceNotFoundException("Student", "id", theId));
		Course theCourse = theStudent.getCourse();
		
		studentRegistration.setRegistrar(registrar);
		studentRegistration.setAssessmentOfficer(assessmentOfficer);
		studentRegistration.setCourse(theCourse);
		studentRegistration.setStudent(theStudent);
		studentRegistration.setAcademicYear(getCurrentAcademicYear());
		
		theModel.addAttribute("subjects", subjectService.filterCourse(theCourse));
		theModel.addAttribute("edit", false);
		theModel.addAttribute("studentStr", theStudent.getFullName());
		theModel.addAttribute("courseStr", theCourse.getTitle());
		theModel.addAttribute("academicYearStr", getCurrentAcademicYear() != null ? getCurrentAcademicYear().getDisplayAcademicYear():"");
		
		theModel.addAttribute("studentRegistration", studentRegistration);
		
		return "registration/registration-form";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("studentRegistrationId") Long theId, Model theModel) {
		
		Optional<StudentRegistration> sReg = studentRegistrationRepository.findById(theId);
		
		StudentRegistration studentRegistration = sReg.orElse(null);
		
		theModel.addAttribute("edit", true);
		theModel.addAttribute("studentStr", studentRegistration.getStudent().getFullName());
		theModel.addAttribute("courseStr", studentRegistration.getCourse().getTitle());
		theModel.addAttribute("academicYearStr", getCurrentAcademicYear() != null ? getCurrentAcademicYear().getDisplayAcademicYear():"");
		
		theModel.addAttribute("subjects", subjectService.filterCourse(studentRegistration.getCourse()));
		theModel.addAttribute("studentRegistration", studentRegistration);
		
		return "registration/registration-form";	
	}
	
	@PostMapping("/save")
	public String saveRegistered(@Valid @ModelAttribute("studentRegistration") StudentRegistration theStudentRegistration,
			BindingResult bindingResult, Model theModel) {
		
		String success = "created";
		if(theStudentRegistration.getId() != null) {
			success = "updated";
		}

		
		boolean isRegistered = false;
		if(theStudentRegistration.getId() == null )
			isRegistered = registrationService.findExist(theStudentRegistration.getStudent(), getCurrentAcademicYear());

		if(bindingResult.hasErrors() || isRegistered) {
			theModel.addAttribute("studentStr", theStudentRegistration.getStudent().getFullName());
			theModel.addAttribute("courseStr", theStudentRegistration.getCourse().getTitle());
			theModel.addAttribute("academicYearStr", getCurrentAcademicYear() != null ? getCurrentAcademicYear().getDisplayAcademicYear():"");
			theModel.addAttribute("subjects", subjectService.filterCourse(theStudentRegistration.getCourse()));
			if(isRegistered)
				theModel.addAttribute("registered",true);
			return "registration/registration-form";	
		}
		
		studentRegistrationRepository.save(theStudentRegistration);
		
		/* Update the status of student registered */
		Student theStudent = theStudentRegistration.getStudent();
		theStudent.setIsRegistered(true);
		
		
		studentRepository.save(theStudent);
		
		return "redirect:/registrations/list?success="+success;
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("studentRegistrationId") Long theId) {
		
		Optional<StudentRegistration> studentRegistration = studentRegistrationRepository.findById(theId);
		StudentRegistration theStudentRegistration = studentRegistration.orElse(null);
		if(theStudentRegistration != null) {
			Optional<Student> student = studentRepository.findById(theStudentRegistration.getStudent().getId());
			Student theStudent = student.orElse(null);
			if(theStudent != null ) {
				theStudent.setIsRegistered(false);
				studentRepository.save(theStudent);
			}
		}
		studentRegistrationRepository.deleteById(theId);
		
		return "redirect:/registrations/list";
	}


    @GetMapping("/report/pdf/COR{id}-{lastname}")
    public String generateReport(HttpServletResponse resp, @PathVariable(name="id", required=true) Long id, @PathVariable String lastname) throws JRException, IOException {
        reportService.exportReport(resp, id);
        return "registration/registrations";
    }
	
	@ModelAttribute("currentAcademicYear")
	public AcademicYear getCurrentAcademicYear() {
		return academicYearRepository.findByCurrent(true);
	}
	
	
}
