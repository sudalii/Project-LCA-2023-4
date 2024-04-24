package kr.re.ImportTest2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(scanBasePackages = {"java.util.function.Consumer"})
@SpringBootApplication
//@ComponentScan({"java.util.function.Consumer"})
public class ImportTest2Application {

	public static void main(String[] args) {
		SpringApplication.run(ImportTest2Application.class, args);
	}

}
