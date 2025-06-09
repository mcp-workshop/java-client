package es.mcpworkshop.client.Client;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AIConfig {

  @Value("classpath:basic-prompt.txt")
  private Resource basicResource;

  @Value("classpath:enhanced-prompt.txt")
  private Resource enhancedResource;

  @Bean
  ChatClient chatClient(
      ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, ToolCallbackProvider tools) {

    String prompt = null;
    try {
      prompt =
          basicResource.getContentAsString(StandardCharsets.UTF_8).formatted(LocalDateTime.now());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return chatClientBuilder
        .defaultAdvisors(
            List.of(
                new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build()))
        .defaultSystem(prompt)
        .defaultToolCallbacks(tools)
        .build();
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

    return builder -> {

      // formatter
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

      // deserializers
      builder.deserializers(new LocalDateDeserializer(dateFormatter));
      builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));

      // serializers
      builder.serializers(new LocalDateSerializer(dateFormatter));
      builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
    };
  }
}
