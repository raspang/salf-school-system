package com.nzp.salf.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nzp.salf.utils.DateUtils;


@Entity
@Table(name="student")
public class Student {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="is required")
	@Column(name="student_id")
	private String studentId;

	@NotEmpty(message="is required")
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="middle_name")
	private String middleName;

	@NotEmpty(message="is required")
	@Column(name="last_name")
	private String lastName;
	
	@NotEmpty(message="is required")
	@Column(name="address")
	private String address;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate dob;
	
	@NotNull(message="is required")
	private String sex;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate dateOfRegistration;
	
	@JsonIgnore
	@OneToMany(mappedBy="student")
	private List<StudentRegistration> studentRegistrations;
	
	@NotNull(message="is required")
	@OneToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name="course_id")
	private Course course;
	
	private Boolean active;
	
	@Column(name="is_registered")
	private Boolean isRegistered;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	@Column(name="date_deactivate")
	private LocalDate deactivateDate;
	
	private Boolean enable;
	
	@Transient
	private String dobStr;
	
	@Transient
	private String dateOfRegistrationStr;
	
	@Transient
	private String deactivateDateStr;

	@Transient
	private String fullName;
	
	
	public Student() {
		this.active = true;
		this.dateOfRegistration = LocalDate.now();
		this.isRegistered  = false;
		this.enable = true;
	}

	
	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public LocalDate getDateOfRegistration() {
		return dateOfRegistration;
	}

	public void setDateOfRegistration(LocalDate dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
	}

	public List<StudentRegistration> getStudentRegistrations() {
		return studentRegistrations;
	}

	public void setStudentRegistrations(List<StudentRegistration> studentRegistrations) {
		this.studentRegistrations = studentRegistrations;
	}
	
	

	public String getDobStr() {
		return DateUtils.displayDate(dob);
	}

	public void setDobStr(String dobStr) {
		this.dobStr = dobStr;
	}

	public String getDateOfRegistrationStr() {
		return DateUtils.displayDate(dateOfRegistration);
	}

	public void setDateOfRegistrationStr(String dateOfRegistrationStr) {
		this.dateOfRegistrationStr = dateOfRegistrationStr;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDate getDeactivateDate() {
		return deactivateDate;
	}

	public void setDeactivateDate(LocalDate deactivateDate) {
		this.deactivateDate = deactivateDate;
	}

	
	public String getDeactivateDateStr() {
		return DateUtils.displayDate( deactivateDate);
	}

	public void setDeactivateDateStr(String deactivateDateStr) {
		this.deactivateDateStr = deactivateDateStr;
	}

	
	public Boolean getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	
	public String getFullName() {
		return lastName+", "+firstName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", address=" + address + ", dob=" + dob + ", sex=" + sex + ", dateOfRegistration="
				+ dateOfRegistration + "]";
	}
	
	
	
}
