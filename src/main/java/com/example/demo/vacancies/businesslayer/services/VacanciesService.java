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

    @Transactional(readOnly = true)
    public List<VacancyGetResponseDto> executeAgentCall(UUID chatId, List<ToolFilter> filters) {
        // 1. Проверка чата
        if (!chatRepository.existsById(chatId)) {
            throw new EntityNotFoundException("Chat with id " + chatId + " not found");
        }
        System.out.println(chatId);
        Specification<VacanciesEntity> spec = (root, query, cb) ->
                cb.equal(root.get("chat").get("id"), chatId);

        // Добавляем динамические фильтры

        for (ToolFilter filter : filters) {
            spec = spec.or(createSpecification(filter));
        }


        return vacanciesRepository.findAll(spec).stream()
                .map(this::mapToDto)
                .toList();
    }

    private Object castToType(String value, Class<?> type) {
        if (value == null || type == String.class) return value;
        if (type == Float.class || type == float.class) return Float.parseFloat(value);
        if (type == Double.class || type == double.class) return Double.parseDouble(value);
        if (type == Integer.class || type == int.class) return Integer.parseInt(value);
        if (type == Long.class || type == long.class) return Long.parseLong(value);
        if (type == LocalDate.class) return LocalDate.parse(value);
        if (type.isEnum()) {
            return Enum.valueOf((Class<Enum>) type, value);
        }
        return value;
    }

    private Specification<VacanciesEntity> createSpecification(ToolFilter filter) {
        return (root, query, cb) -> {
            String field = filter.field().getEntityField();
            String val = filter.value();
            LookupsEnum lookup = filter.lookup();

            System.out.println(field + " " + val + " "  + lookup);
            if (field.equals("techStack")) {

                return cb.like(cb.lower(root.join("techStack")), "%" + val.toLowerCase() + "%");
            }


            Class<?> fieldType = root.get(field).getJavaType();

            Object castedValue = castToType(val, fieldType);

            return switch (lookup) {
                case eq -> cb.equal(root.get(field), castedValue);
                case neq -> cb.notEqual(root.get(field), castedValue);
                case gt -> cb.greaterThan(root.get(field).as((Class<Comparable>) fieldType), (Comparable) castedValue);
                case gte -> cb.greaterThanOrEqualTo(root.get(field).as((Class<Comparable>) fieldType), (Comparable) castedValue);
                case lt -> cb.lessThan(root.get(field).as((Class<Comparable>) fieldType), (Comparable) castedValue);
                case lte -> cb.lessThanOrEqualTo(root.get(field).as((Class<Comparable>) fieldType), (Comparable) castedValue);
                case contains, icontains -> {
                    if (fieldType != String.class) yield cb.conjunction();
                    String cleanedVal = val.trim().toLowerCase();
                    String pattern = "%" + cleanedVal + "%";
                    yield cb.like(cb.lower(root.get(field)), pattern);
                }
                case isnull -> cb.isNull(root.get(field));
                default -> cb.conjunction();
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
