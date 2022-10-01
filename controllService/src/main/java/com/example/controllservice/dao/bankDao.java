package com.example.controllservice.dao;

import com.example.controllservice.Entiy.bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface bankDao extends JpaRepository<bank, String> {
}
