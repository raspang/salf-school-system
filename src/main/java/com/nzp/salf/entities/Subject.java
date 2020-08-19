package com.nzp.salf.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="subject")
public class Subject {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="is required")
	@NotNull(message="is required")
	@Column(name="subjec_title", unique=true)
	@Size(min=3, message="is required")
	private String title;
	
	@NotNull(message="is required")
	private Integer units;
	
	private String descriptiveTitle;
	
	@NotEmpty(message="is required")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "subject_course", 
             joinColumns = { @JoinColumn(name = "subject_id") }, 
             inverseJoinColumns = { @JoinColumn(name = "course_id") })
	private List<Course> courses = new ArrayList<Course>();

	private Boolean enable;

	public Subject() {
		this.enable = true;
	}

	public Subject(String title, Integer units, String descriptiveTitle) {
		this.title = title;
		this.units = units;
		this.descriptiveTitle = descriptiveTitle;
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

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public String getDescriptiveTitle() {
		return descriptiveTitle;
	}

	public void setDescriptiveTitle(String descriptiveTitle) {
		this.descriptiveTitle = descriptiveTitle;
	}
	
	

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return title;
	}
	
	
	
	
}
