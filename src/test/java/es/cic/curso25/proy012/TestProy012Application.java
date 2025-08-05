package es.cic.curso25.proy012;

import org.springframework.boot.SpringApplication;

public class TestProy012Application {

	public static void main(String[] args) {
		SpringApplication.from(Proy012Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
