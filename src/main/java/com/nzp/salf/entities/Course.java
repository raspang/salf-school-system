package com.nzp.salf.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="course")
public class Course {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	@Column(name = "title", unique = true)
	private String title;
	
	private String major;

	private Boolean enable;
	
	@OneToMany(mappedBy="course")
	private List<StudentRegistration> studentRegistrations;
	
	public Course() {
		this.enable = true;
	}

	public Course(String title, String major) {		
		this.title = title;
		this.major = major;
		this.enable = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public List<StudentRegistration> getStudentRegistrations() {
		return studentRegistrations;
	}

	public void setStudentRegistrations(List<StudentRegistration> studentRegistrations) {
		this.studentRegistrations = studentRegistrations;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", title=" + title + ", major=" + major + "]";
	}
	
	
	
}
