package com.backend.unab.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.MedicalRecord;

public interface IMedicalRecordDao extends JpaRepository<MedicalRecord, Long> {
}
