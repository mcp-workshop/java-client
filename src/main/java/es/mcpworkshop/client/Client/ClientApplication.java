package es.mcpworkshop.client.Client;

import java.util.Scanner;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

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
        System.out.printf("%n>>> ASSISTANT: %s%n", chatClient.prompt(comando).call().content());
      }
    } while (!comando.equalsIgnoreCase("quit"));
    System.out.println("Bye bye !");
    System.exit(0);
  }
}
