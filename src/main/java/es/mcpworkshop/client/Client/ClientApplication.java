package es.mcpworkshop.client.Client;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }

  @Autowired
  private ChatClient chatClient;

  @Override
  public void run(String... args) {
    String comando;
    Scanner scanner = new Scanner(System.in);
    do {
      System.out.println("Ask a question: ( quit to terminate )");
      comando = scanner.nextLine().trim();
      if (!comando.equalsIgnoreCase("quit")) {
        System.out.printf("%n>>> ASSISTANT: %s%n", chatClient.prompt(comando).call().content());
      }
    } while (!comando.equalsIgnoreCase("quit"));
    System.out.println("Bye bye !");
    System.exit(0);
  }

  @Configuration
  static class AIConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AIConfig.class);

    @Value("classpath:enhanced-system-prompt.txt")
    private Resource systemPrompt;

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, ToolCallbackProvider tools)
            throws Exception {
      LOG.info("Initializing ChatClient");
      String systemText =
              systemPrompt.getContentAsString(StandardCharsets.UTF_8).formatted(LocalDateTime.now());

      return chatClientBuilder
              .defaultAdvisors(
                      List.of(
                              new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build()))
              .defaultSystem(systemText)
              .defaultToolCallbacks(tools)
              .build();
    }
  }
}
