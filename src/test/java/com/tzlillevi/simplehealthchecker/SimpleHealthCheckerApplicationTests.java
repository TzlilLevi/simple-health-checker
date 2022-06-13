package com.tzlillevi.simplehealthchecker;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class SimpleHealthCheckerApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WebCallsService webCallsService;

	@Test
	public void getHealth_success()throws Exception{
		var targetResponse = ResponseEntity.status(200).body("");
		Mockito.when(webCallsService.call(anyString())).thenReturn(targetResponse);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/health")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getHealth_failed()throws Exception{
		var targetResponse = ResponseEntity.status(500).body("");
		Mockito.when(webCallsService.call(anyString())).thenReturn(targetResponse);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/health")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isServiceUnavailable());
	}



}
