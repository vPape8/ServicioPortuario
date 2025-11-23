package com.portuario.repository;

import com.portuario.model.Buque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuqueRepository extends JpaRepository<Buque, String> {
}
