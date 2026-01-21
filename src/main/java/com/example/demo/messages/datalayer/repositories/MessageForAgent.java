package com.example.demo.messages.datalayer.repositories;

import com.example.demo.amq.dtos.response.toolcall.ToolCallDto;
import com.example.demo.messages.datalayer.enums.SenderType;

import java.util.List;

public interface MessageForAgent {
    String getContent();
    SenderType getRole();
    List<ToolCallDto> getToolCalls();
    String getToolCallId();
}
