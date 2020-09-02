package com.nzp.salf.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nzp.salf.entities.AcademicYear;
import com.nzp.salf.entities.Student;
import com.nzp.salf.entities.StudentRegistration;


public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, Long>{

	
	@Query("SELECT sr FROM StudentRegistration sr INNER JOIN sr.student st INNER JOIN sr.academicYear ay "
			+ "WHERE (st.firstName LIKE %?1% OR st.lastName LIKE %?1% OR st.studentId LIKE %?1% ) "
			+ "AND CONCAT(ay.id, '') LIKE %?2% order by sr.id desc")
	Page<StudentRegistration> findAllOrderById(String keyword, String academicYear,  Pageable pageable);
	
	@Query("SELECT sr FROM StudentRegistration sr INNER JOIN sr.student st INNER JOIN sr.academicYear ay "
			+ "WHERE (st.firstName LIKE %?1% OR st.lastName LIKE %?1% OR st.studentId LIKE %?1% ) "
			+ "AND CONCAT(ay.id, '') LIKE %?2% order by sr.id desc")
	List<StudentRegistration> findAllOrderById(String keyword, String academicYear);
	
	
	List<StudentRegistration> findByStudentAndAcademicYear(Student student, AcademicYear academicYear);
	
}
	