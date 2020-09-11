package com.nzp.salf.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.nzp.salf.entities.AcademicYear;
import com.nzp.salf.entities.Payment;
import com.nzp.salf.entities.Report;
import com.nzp.salf.entities.StudentRegistration;
import com.nzp.salf.entities.Subject;
import com.nzp.salf.exception.ResourceNotFoundException;
import com.nzp.salf.repositories.AcademicYearRepository;
import com.nzp.salf.repositories.PaymentRepository;
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
	
	@Autowired
	private AcademicYearRepository academicYearRepository;
	
	@Autowired 
	private PaymentRepository paymentRepository;
	
	public String exportReport(HttpServletResponse resp, Long id) throws JRException, IOException {
		
		byte[] bytes = null;
	
		Optional<StudentRegistration> studentRegistration = studentRegistrationRepository.findById(id);
		StudentRegistration theStudentRegistration = studentRegistration.orElse(null);
		
		List<Subject> subjects = new ArrayList<>();
		
		if(theStudentRegistration != null)
			subjects = theStudentRegistration.getSubjects();
		
		
		
        File file = ResourceUtils.getFile("classpath:cor.jrxml");
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
	    	parameters.put("name", theStudentRegistration.getStudent().getFullName());
	    	
	    	List<Payment> studentPayments = paymentRepository.findByStudentRegistration(theStudentRegistration);
	    	Double totalFees = 0d;
	    	for(Payment payment: studentPayments) {
	    		String title = payment.getPaymentDetail();
	    		title = title.replaceAll("\\s+","").toLowerCase();
	    		
	    		parameters.put(title, String.valueOf(payment.getAmount()));
	    		totalFees += payment.getAmount();
	    	}
	    	parameters.put("totalfees", String.valueOf(totalFees));
	    	if(theStudentRegistration.getLess() != null && theStudentRegistration.getLess() > 0d)
	    		parameters.put("less", String.valueOf(theStudentRegistration.getLess()));
			
			 if(theStudentRegistration.getBalance() != null && theStudentRegistration.getBalance() > 0d)
				 parameters.put("balance", String.valueOf(theStudentRegistration.getBalance()));
			 
			 if(theStudentRegistration.getPaymentPerExam() != null && theStudentRegistration.getPaymentPerExam() > 0d)
			  parameters.put("paymentperexam",  String.valueOf(theStudentRegistration.getPaymentPerExam()));	
			 
			 
			 if(theStudentRegistration.getRegistrar() != null) {
				 
				 parameters.put("registrar", theStudentRegistration.getRegistrar().getFullName());
				 
				 String name = theStudentRegistration.getRegistrar().getFullName().replaceAll("\\s+","").toLowerCase();
				 
				 if(name.equalsIgnoreCase("jhonaq.baillespin")) {
					BufferedImage image1 =  null;
					try {
					 File initialImage = ResourceUtils.getFile("classpath:jhona.png");
					 image1 = ImageIO.read(initialImage);
					}catch(Exception e) {}
					parameters.put("jhona", image1);
				 }
				
			 }
			 if(theStudentRegistration.getCashier() != null) {
				 parameters.put("cashier", theStudentRegistration.getCashier().getFullName());
				 
				 String name = theStudentRegistration.getCashier().getFullName().replaceAll("\\s+","").toLowerCase();
				 
				 if(name.equalsIgnoreCase("raidak.salibo")) {
					BufferedImage image1 =  null;
					try {
					 File initialImage = ResourceUtils.getFile("classpath:raida.png");
					 image1 = ImageIO.read(initialImage);
					}catch(Exception e) {}
					parameters.put("raida", image1);
				 }
			 }
				
				
        }
        

		
		  for(int i = 1; i <= subjects.size(); i++) {
			  parameters.put("courseNo"+String.valueOf(i), subjects.get(i-1).getTitle());
			  parameters.put("courseDesc"+String.valueOf(i), subjects.get(i-1).getDescriptiveTitle()); 
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
	

	public String exportReports(HttpServletResponse resp, List<StudentRegistration> studentRegistrations, String academicYear) throws JRException, IOException {

        AcademicYear aY = academicYearRepository.findById(Long.parseLong(academicYear)).orElseThrow(
        		() -> new ResourceNotFoundException("AcademicYear", "id", academicYear));
        
		List<Report> reports = new ArrayList<>();
		for(StudentRegistration sR: studentRegistrations) {
			Report report = new Report();
			report.setStudentId(sR.getStudent().getStudentId());
			report.setLastName(sR.getStudent().getLastName());
			report.setFirstName(sR.getStudent().getFirstName());
			report.setGender(sR.getStudent().getSex());
			report.setCourse(sR.getCourse().getTitle());
			report.setMajor(sR.getCourse().getMajor());
			report.setYear(sR.getCurriculumYear());
			report.setSy(sR.getAcademicYear().getYear());
			report.setSemester(sR.getAcademicYear().getSemester());
			report.setDateOfRegistration(sR.getDateOfRegistrationStr());
			report.setSubjects(sR.getSubjects().toString());
			report.setUnits(String.valueOf(sR.getTotalUnits()));
			reports.add(report);
		}
		
		
		byte[] bytes = null;
        File file = ResourceUtils.getFile("classpath:registered.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reports);
        Map<String, Object> parameters = new HashMap<>();

           parameters.put("academicYear", aY.getDisplayAcademicYear());
        
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
