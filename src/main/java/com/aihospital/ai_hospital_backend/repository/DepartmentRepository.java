package com.aihospital.ai_hospital_backend.repository;

import com.aihospital.ai_hospital_backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}