package com.backend.unab.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.Customer;

public interface ICustomerDao extends JpaRepository<Customer, Long> {
}
