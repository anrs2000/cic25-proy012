package es.cic.curso25.proy012;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Proy012ApplicationTests {

	@Test
	void contextLoads() {
	}

}
