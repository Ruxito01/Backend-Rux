package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendRuxApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendRuxApplication.class, args);
    System.out.println("""
         .--.              .--.
        : (\\ ". _......_ ." /) :
         '.    `        `    .'
          /'   _        _   `\\
         /     0        0     \\
        |       /      \\       |
        |     /'        `\\     |
         \\   | .  .==.  . |   /
          '._ \\ '  ""  ' / _.'
             /  /      \\  \\
            /  /        \\  \\
            \\  \\        /  /
             `\\ `\\    /' /`
               \\  |  |  /
                | |  | |
                | |  | |
               /' |  | `\\
              ( . \\  / . )
               `._/  \\_.'
        """);
  }

}
