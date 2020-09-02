package com.nzp.salf.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="academic_year")
public class AcademicYear {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="is required")
	@NotNull(message="is required")
	@Size(min=9, message="is required")
	@Column(name = "year")
	private String year;
	
	@NotEmpty(message="is required")
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	@Column(name = "semester")
	private String semester;
	
	@Column(name = "school_year")
	private Boolean current;

	@OneToMany(mappedBy="academicYear")
	List<StudentRegistration> studentRegistrations;
	
	private Boolean enable;
	
	
	@Transient
	private String displayAcademicYear;
	
	
	public AcademicYear() {
		this.current = true;
		this.enable = true;
	}

	public AcademicYear(String year, String semester) {
		this.year = year;
		this.semester = semester;
		this.current = true;
		this.enable = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	
	public Boolean getCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}

	public String getDisplayAcademicYear() {
		return year+", "+semester;
	}

	public void setDisplayAcademicYear(String displayAcademicYear) {
		this.displayAcademicYear = displayAcademicYear;
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
		AcademicYear other = (AcademicYear) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AcademicYear [id=" + id + ", year=" + year + ", semester=" + semester + "]";
	}
	
	
	
	
}
