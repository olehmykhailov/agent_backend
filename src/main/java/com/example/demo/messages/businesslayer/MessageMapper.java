package com.example.demo.messages.businesslayer;

import com.example.demo.messages.businesslayer.dtos.MessageGetResponseDto;
import com.example.demo.messages.datalayer.entities.MessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageGetResponseDto toDto(MessageEntity message);
}
