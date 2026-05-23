package com.helpdesk.config;


import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(AIConfig.class);

//   to make cutom implemntation of jdbc chat memory repository, we can create a bean for it and inject it to chat client bean. but for now we can use default implementation provided by spring ai starter.
//    public JdbcChatMemoryRepository jdbcChatMemoryRepository(){
//        return JdbcChatMemoryRepository.builder()
//                .jdbcTemplate()
//                .jdbcTemplate()
//                .build();
//    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {

        var chatMemory=MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(15)
                .build();

        logger.info("chat memory bean created. {}", chatMemory.getClass().getName());
        return builder

                .defaultSystem("Summerize the response within 300 words.")
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
    }

}
