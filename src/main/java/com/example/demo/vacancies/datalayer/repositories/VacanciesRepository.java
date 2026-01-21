package com.example.demo.vacancies.datalayer.repositories;

import com.example.demo.vacancies.datalayer.entities.VacanciesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface VacanciesRepository extends JpaRepository<VacanciesEntity, UUID>,
        JpaSpecificationExecutor<VacanciesEntity> {
    Page<VacanciesEntity> findByChat_Id(UUID chatId, Pageable pageable);
}
