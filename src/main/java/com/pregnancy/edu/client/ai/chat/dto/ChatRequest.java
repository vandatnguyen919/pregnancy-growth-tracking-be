package com.pregnancy.edu.client.ai.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The ChatRequest class encapsulates the input to an AI model.
 * The input includes the AI model to use and a list of Message objects, AKA a prompt.
 * Each message in the prompt plays a different role in the conversation, from user questions and instructions to
 * examples and pertinent contextual information.
 * <p>
 * In a nutshell, the prompt helps the AI model "understand" the structure and intent of the user's query, leading to more precise and relevant responses.
 */
@Data
@NoArgsConstructor
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int maxTokens;
    private boolean stream;

    public ChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }
}
