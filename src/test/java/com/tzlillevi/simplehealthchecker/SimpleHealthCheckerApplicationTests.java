package com.tzlillevi.simplehealthchecker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
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
	public void getHealth_allFailed()throws Exception{
		var targetResponse = ResponseEntity.status(500).body("");
		Mockito.when(webCallsService.call(anyString())).thenReturn(targetResponse);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/health")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isServiceUnavailable());
	}

	@Test
	public void getHealth_onlyOneFailed()throws Exception{
		var targetResponse1 = ResponseEntity.status(500).body("");
		var targetResponse2 = ResponseEntity.status(200).body("");

		Mockito.when(webCallsService.call("url1")).thenReturn(targetResponse1);
		Mockito.when(webCallsService.call("url2")).thenReturn(targetResponse2);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/health")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isServiceUnavailable());
	}




}
