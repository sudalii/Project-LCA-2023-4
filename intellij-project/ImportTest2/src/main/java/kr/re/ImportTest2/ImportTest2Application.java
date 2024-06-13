package kr.re.ImportTest2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

//@SpringBootApplication(scanBasePackages = {"java.util.function.Consumer"})
@SpringBootApplication
//@ComponentScan({"java.util.function.Consumer"})
public class ImportTest2Application {

	public static void main(String[] args) {

		String activeProfile = System.getProperty("spring.profiles.active", "server");
//		String homeDir = System.getProperty("user.home");
//		String projectLibPath = homeDir + "/server/olca-modules";
		System.out.println("Active profile: " + activeProfile);

		// Setting the system property
//		System.setProperty("project.lib.path", projectLibPath);



		SpringApplication.run(ImportTest2Application.class, args);


	}

}
