package com.example.demo.vacancies.businesslayer.services;

import com.example.demo.amq.dtos.response.ToolFilter;
import com.example.demo.amq.dtos.response.enums.LookupsEnum;
import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.infrastructure.errors.EntityNotFoundException;
import com.example.demo.vacancies.businesslayer.controllers.VacanciesWebSocketController;
import com.example.demo.vacancies.businesslayer.dtos.CreateVacancyRequestDto;
import com.example.demo.vacancies.businesslayer.dtos.VacancyGetResponseDto;
import com.example.demo.vacancies.datalayer.repositories.VacanciesRepository;
import com.example.demo.chats.datalayer.repositories.ChatRepository;
import com.example.demo.vacancies.datalayer.entities.VacanciesEntity;
import com.example.demo.globals.PageResponseDto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class VacanciesService {
    private final VacanciesRepository vacanciesRepository;
    private final ChatRepository chatRepository;
    private final VacanciesWebSocketController vacanciesWebSocketController;

    @Transactional
    public void saveVacancies(UUID chatId, List<CreateVacancyRequestDto> vacancies) {
        ChatEntity chat = chatRepository.findById(chatId)
                .orElseThrow();
        System.out.println(vacancies);
        List<VacanciesEntity> vacanciesEntities = vacancies.stream()
                .map(dto -> {
                    VacanciesEntity vacanciesEntity = new VacanciesEntity();
                    vacanciesEntity.setJobTitle(dto.jobTitle());
                    vacanciesEntity.setDescription(dto.description());
                    vacanciesEntity.setCompanyName(dto.companyName());
                    vacanciesEntity.setLocation(dto.location());
                    vacanciesEntity.setExpireAt(LocalDate.parse(dto.expireAt()));
                    vacanciesEntity.setMinSalary(dto.minSalary());
                    vacanciesEntity.setMaxSalary(dto.maxSalary());
                    vacanciesEntity.setSourceLink(dto.sourceLink());
                    vacanciesEntity.setTechStack(dto.techStack());
                    vacanciesEntity.setSeniorityLevel(dto.seniorityLevel());
                    vacanciesEntity.setChat(chat);
                    return vacanciesEntity;
                }).toList();

        vacanciesRepository.saveAll(vacanciesEntities);

        vacanciesWebSocketController.sendVacanciesUpdate(chatId, vacanciesEntities.size());
    }

    public PageResponseDto<VacancyGetResponseDto> getVacancies(
            UUID chatId,
            int page,
            int size) {
        ChatEntity chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat with id " + chatId + " not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<VacanciesEntity> vacanciesPage = vacanciesRepository.findByChat_Id(chat.getId(), pageable);

        Page<VacancyGetResponseDto> dtoPage = vacanciesPage.map(entity -> {
            VacancyGetResponseDto dto = new VacancyGetResponseDto(
                    entity.getId(),
                    entity.getJobTitle(),
                    entity.getLocation(),
                    entity.getMinSalary(),
                    entity.getMaxSalary(),
                    entity.getTechStack(),
                    entity.getDescription(),
                    entity.getCompanyName(),
                    entity.getSeniorityLevel(),
                    entity.getSourceLink()
                    );
            return dto;
        });

        return new PageResponseDto<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages()
        );
    }

    public List<VacancyGetResponseDto> executeAgentCall(UUID chatId, List<ToolFilter> filters) {
        // 1. Проверка чата
        if (!chatRepository.existsById(chatId)) {
            throw new EntityNotFoundException("Chat with id " + chatId + " not found");
        }
        Specification<VacanciesEntity> spec = (root, query, cb) ->
                cb.equal(root.get("chat").get("id"), chatId);

        // Добавляем динамические фильтры
        for (ToolFilter filter : filters) {
            spec = spec.and(createSpecification(filter));
        }

        return vacanciesRepository.findAll(spec).stream()
                .map(this::mapToDto)
                .toList();
    }

    private Specification<VacanciesEntity> createSpecification(ToolFilter filter) {
        return (root, query, cb) -> {
            // Получаем имя поля из FieldsEnum (например, "minSalary")
            String field = filter.field().getEntityField();
            String val = filter.value();
            LookupsEnum lookup = filter.lookup();


            if (field.equals("techStack")) {
                return cb.isMember(val, root.get("techStack"));
            }

            return switch (lookup) {
                case eq -> cb.equal(root.get(field), val);
                case neq -> cb.notEqual(root.get(field), val);
                case gt -> cb.greaterThan(root.get(field).as(String.class), val);
                case gte -> cb.greaterThanOrEqualTo(root.get(field).as(String.class), val);
                case lt -> cb.lessThan(root.get(field).as(String.class), val);
                case lte -> cb.lessThanOrEqualTo(root.get(field).as(String.class), val);
                case contains -> cb.like(root.get(field), "%" + val + "%");
                case icontains -> cb.like(cb.lower(root.get(field)), "%" + val.toLowerCase() + "%");
                case startswith -> cb.like(root.get(field), val + "%");
                case istartswith -> cb.like(cb.lower(root.get(field)), val.toLowerCase() + "%");
                case endswith -> cb.like(root.get(field), "%" + val);
                case iendswith -> cb.like(cb.lower(root.get(field)), "%" + val.toLowerCase());
                case isnull -> cb.isNull(root.get(field));
                default -> cb.conjunction(); // "Пустое" условие (1=1)
            };
        };
    }

    // Вспомогательный метод маппинга
    private VacancyGetResponseDto mapToDto(VacanciesEntity entity) {
        return new VacancyGetResponseDto(
                entity.getId(), entity.getJobTitle(), entity.getLocation(),
                entity.getMinSalary(), entity.getMaxSalary(), entity.getTechStack(),
                entity.getDescription(), entity.getCompanyName(),
                entity.getSeniorityLevel(), entity.getSourceLink()
        );
    }

}
