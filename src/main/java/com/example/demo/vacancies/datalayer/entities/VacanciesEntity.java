package com.example.demo.vacancies.datalayer.entities;

import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.database.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name="vacancies")
@Getter
@Setter
public class VacanciesEntity extends BaseEntity {

    @Column(name = "job_title") // Явно связываем с колонкой в БД
    private String jobTitle;

    @Column(name = "location")
    private String location;

    @Column(name = "min_salary")
    private Float minSalary;

    @Column(name = "max_salary")
    private Float maxSalary;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vacancy_tech_stack", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Column(name = "technology")
    private List<String> techStack;

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "seniority_level")
    private String seniorityLevel;

    @Column(name = "source_link")
    private String sourceLink;

    @Column(name = "expire_at")
    private LocalDate expireAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;
}