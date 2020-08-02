package com.nzp.salf.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.nzp.salf.entities.StudentRegistration;
import com.nzp.salf.entities.Subject;
import com.nzp.salf.repositories.StudentRegistrationRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {
	
	@Autowired
	private StudentRegistrationRepository studentRegistrationRepository;
	
	public String exportReport(HttpServletResponse resp, Long id) throws JRException, IOException {
		
		byte[] bytes = null;
	
		Optional<StudentRegistration> studentRegistration = studentRegistrationRepository.findById(id);
		StudentRegistration theStudentRegistration = studentRegistration.orElse(null);
		
		List<Subject> subjects = new ArrayList<>();
		
		if(theStudentRegistration != null)
			subjects = theStudentRegistration.getSubjects();
		
        File file = ResourceUtils.getFile("classpath:students.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjects);
        Map<String, Object> parameters = new HashMap<>();
        
        if(theStudentRegistration != null) {
	        parameters.put("lastName", theStudentRegistration.getStudent().getLastName());
	        parameters.put("firstName", theStudentRegistration.getStudent().getFirstName());
	        parameters.put("middleName", theStudentRegistration.getStudent().getMiddleName());
	        parameters.put("address", theStudentRegistration.getStudent().getAddress());
	        parameters.put("dateOfBirth", theStudentRegistration.getStudent().getDobStr());
	        parameters.put("sex" , theStudentRegistration.getStudent().getSex());
	    	parameters.put("courseAndYear", theStudentRegistration.getCourse().getTitle()+" "+theStudentRegistration.getCurriculumYear());
	    	parameters.put("major", theStudentRegistration.getCourse().getMajor());
	    	parameters.put("dateOfRegistration", theStudentRegistration.getDateOfRegistrationStr());
	    	parameters.put("totalUnits", String.valueOf(theStudentRegistration.getTotalUnits()));
	    	parameters.put("curriculumYear", theStudentRegistration.getCurriculumYear());
	    	parameters.put("semester", theStudentRegistration.getAcademicYear().getSemester());
	    	parameters.put("academicYear", theStudentRegistration.getAcademicYear().getYear());
	    	parameters.put("principal", theStudentRegistration.getRegistrar() != null ? theStudentRegistration.getRegistrar().getFullName() : "");
	    	parameters.put("assessmentOfficer", theStudentRegistration.getAssessmentOfficer() != null ? theStudentRegistration.getAssessmentOfficer().getFullName() : "");
        }
        
        	bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, dataSource);
        	
    		resp.reset();
    		resp.resetBuffer();
    		resp.setContentType("application/pdf");
    		resp.setContentLength(bytes.length);
    		ServletOutputStream ouputStream = resp.getOutputStream();
    		ouputStream.write(bytes, 0, bytes.length);
    		ouputStream.flush();
    		ouputStream.close();

        return "report generated";
		
	}
}
