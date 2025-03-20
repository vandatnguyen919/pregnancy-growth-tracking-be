package com.pregnancy.edu.client.ai.chat;

import com.pregnancy.edu.client.ai.chat.dto.ChatRequest;
import com.pregnancy.edu.client.ai.chat.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * The MetaLlamaChatClient class is an implementation of the ChatClient interface.
 */
@Component
public class MetaLlamaChatClient implements ChatClient {

    private final RestClient restClient;

    public MetaLlamaChatClient(
            @Value("${ai.meta-llama.endpoint}") String endpoint,
            @Value("${ai.meta-llama.api-key}") String apiKey,
            RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(endpoint)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    @Override
    public ChatResponse generate(ChatRequest chatRequest) {
        return this.restClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chatRequest)
                .retrieve()
                .body(ChatResponse.class);
    }
}
