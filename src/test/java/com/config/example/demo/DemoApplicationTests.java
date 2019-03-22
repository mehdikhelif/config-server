package com.config.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void overridesLoadedTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/serviceA/dev"))
				.andExpect(status().isOk())
				.andReturn();
		Environment environment = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Environment.class);
		assertThat(environment).isNotNull();
		Optional<PropertySource> overrides = environment.getPropertySources().stream()
				.filter(ps -> "overrides".equalsIgnoreCase(ps.getName())).findAny();
		assertThat(overrides).isPresent();
		assertThat(overrides.get().getSource().get("test")).isEqualTo("test");
	}

}
