package es.mcpworkshop.client.Client;

import java.util.Arrays;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }

  @Bean
  public CommandLineRunner predefinedQuestions(
      ChatClient.Builder chatClientBuilder,
      ToolCallbackProvider tools,
      ConfigurableApplicationContext context) {

    return args -> {
      if (args.length > 0) {
        var chatClient = chatClientBuilder.defaultToolCallbacks(tools).build();

        System.out.printf("%n>>> QUESTION: %s", Arrays.toString(args));
        System.out.printf(
            "%n>>> ASSISTANT: %s", chatClient.prompt(Arrays.toString(args)).call().content());
        context.close();
      }
    };
  }
}
