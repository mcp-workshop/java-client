package es.mcpworkshop.client.Client;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);
  @Autowired private ChatClient chatClient;

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }

  @Override
  public void run(String... args) {
    String comando;
    Scanner scanner = new Scanner(System.in);
    do {
      System.out.println("Ask a question: ( quit to terminate )");
      comando = scanner.nextLine().trim();
      if (!comando.equalsIgnoreCase("quit")) {
        String response = "An Error Happened, please try again";
        try {
          response = chatClient.prompt(comando).call().content();
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
        System.out.printf("%n>>> ASSISTANT: %s%n", response);
      }
    } while (!comando.equalsIgnoreCase("quit"));
    System.out.println("Bye bye !");
    System.exit(0);
  }
}
