package com.batch.batchdemo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInfoRepo extends JpaRepository<StudentInfo,Long> {
}
