package com.example.demo.users.businesslayer.services;

import com.example.demo.users.datalayer.entities.UserEntity;
import com.example.demo.users.datalayer.repositories.UserRepository;
import com.example.demo.infrastructure.errors.EntityNotFoundException;
import com.example.demo.users.businesslayer.dtos.CreateUserRequestDto;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service // Регистрирует класс как Бин в контексте Spring
@RequiredArgsConstructor // Создает конструктор для всех final-полей (Dependency Injection)
public class UsersService {

    // final гарантирует, что зависимость будет передана через конструктор
    private final UserRepository userRepository;

    // Метод для получения пользователя
    public UserEntity getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional // Оборачивает метод в транзакцию БД
    public UserEntity createUser(CreateUserRequestDto createUserRequestDto, String encodedPassword) {
        UserEntity userEntity = new UserEntity();

        userEntity.setPassword(encodedPassword);
        userEntity.setUsername(createUserRequestDto.username());
        userEntity.setEmail(createUserRequestDto.email());

        return userRepository.save(userEntity);
    }


}