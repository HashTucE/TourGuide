package tourGuide;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) {

        System.out.println(" ");
        System.out.println("  _______                _____       _     _      ");
        System.out.println(" |__   __|              / ____|     (_)   | |     ");
        System.out.println("    | | ___  _   _ _ __| |  __ _   _ _  __| | ___ ");
        System.out.println("    | |/ _ \\| | | | '__| | |_ | | | | |/ _` |/ _ \\");
        System.out.println("    | | (_) | |_| | |  | |__| | |_| | | (_| |  __/");
        System.out.println("    |_|\\___/ \\__,_|_|   \\_____|\\__,_|_|\\__,_|\\___|");
        System.out.println(" =================================================");
        System.out.println(" :: APP ::                                (v1.0.0)");
        System.out.println(" ");
    }
}
