package com.nzp.salf.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="employee")
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	@Column(name="full_name")
	private String fullName;
	
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	@Column(name="position")
	private String positionTitle;
	
	@Column(name="position_id")
	private String positionId;
	
	@Column(name="selected")
	private Boolean selected;


	
	public Employee() {
		selected = true;

	}

	public Employee(String fullName, String positionTitle, String positionId) {
		this.fullName = fullName;
		this.positionTitle = positionTitle;
		this.positionId = positionId;
		this.selected = true;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}


	@Override
	public String toString() {
		return "Employee [id=" + id + ", fullName=" + fullName + ", positionTitle=" + positionTitle + ", positionId="
				+ positionId + ", select=" + selected + "]";
	}


	
}
