package com.nzp.salf.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.nzp.salf.utils.DateUtils;

@Entity
@Table(name="student_registration")
public class StudentRegistration {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@NotNull(message="is required")
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="student_id")
	private Student student;
	
	@NotNull(message="is required")
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="course_id")
	private Course course;
	
	
	@NotNull(message="is required")
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="academic_year_id")
	private AcademicYear academicYear;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="registrar_id")
	private Employee registrar;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="assessment_officer_id")
	private Employee assessmentOfficer;
	
	@NotEmpty(message="is required")
	@NotNull(message="is required")
	@Column(name="curriculum_year")
	private String curriculumYear;

	@NotNull(message="is required")
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate dateOfRegistration;
	
	@NotEmpty(message="cannot be empty")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "student_subject", 
             joinColumns = { @JoinColumn(name = "student_id") }, 
             inverseJoinColumns = { @JoinColumn(name = "subject_id") })
	private List<Subject> subjects = new ArrayList<Subject>();

	private Boolean enable;
	
	@Transient
	private String dateOfRegistrationStr;
	

	
	@Transient
	private Integer totalUnits;
	
	public StudentRegistration() {
		this.dateOfRegistration= LocalDate.now();
		this.enable = true;
	}


	public StudentRegistration(Student student, Course course, AcademicYear academicYear, String curriculumYear) {
		this.student = student;
		this.course = course;
		this.academicYear = academicYear;
		this.curriculumYear = curriculumYear;
		this.dateOfRegistration= LocalDate.now();
		this.enable = true;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Student getStudent() {
		return student;
	}


	public void setStudent(Student student) {
		this.student = student;
	}


	public Course getCourse() {
		return course;
	}


	public void setCourse(Course course) {
		this.course = course;
	}


	public AcademicYear getAcademicYear() {
		return academicYear;
	}


	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
	}

	public String getCurriculumYear() {
		return curriculumYear;
	}


	public void setCurriculumYear(String curriculumYear) {
		this.curriculumYear = curriculumYear;
	}


	public LocalDate getDateOfRegistration() {
		return dateOfRegistration;
	}


	public void setDateOfRegistration(LocalDate dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
	}


	public List<Subject> getSubjects() {
		return subjects;
	}


	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	

	public String getDateOfRegistrationStr() {
		return DateUtils.displayDate(dateOfRegistration);
	}


	public void setDateOfRegistrationStr(String dateOfRegistrationStr) {
		this.dateOfRegistrationStr = dateOfRegistrationStr;
	}

	

	public Integer getTotalUnits() {
		int count = 0;
		for(Subject s : subjects)
			count += s.getUnits();
		return count;
	}


	public void setTotalUnits(Integer totalUnits) {
		this.totalUnits = totalUnits;
	}


	public Employee getRegistrar() {
		return registrar;
	}


	public void setRegistrar(Employee registrar) {
		this.registrar = registrar;
	}


	public Employee getAssessmentOfficer() {
		return assessmentOfficer;
	}


	public void setAssessmentOfficer(Employee assessmentOfficer) {
		this.assessmentOfficer = assessmentOfficer;
	}


	

	public Boolean getEnable() {
		return enable;
	}


	public void setEnable(Boolean enable) {
		this.enable = enable;
	}


	@Override
	public String toString() {
		return "StudentRegistration [id=" + id + ", student=" + student + ", course=" + course + ", academicYear="
				+ academicYear + ", curriculumnYear=" + curriculumYear + ", dateOfRegistration=" + dateOfRegistration
				+ ", subjects=" + subjects + "]";
	}


	
	
}
