package org.gbif.data.controller;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.slf4j.Slf4j;
import org.gbif.data.pipelines.service.CSV2AvroPipelineService;
import org.gbif.data.service.DataTransformerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = {DataTransformerController.class, DataTransformerService.class, CSV2AvroPipelineService.class})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class DataTransformerControllerTest {

  private static final MockMultipartFile MOCK_INPUT_FILE = new MockMultipartFile("file",
      "input.csv",
      MediaType.APPLICATION_OCTET_STREAM_VALUE,
      "1145306333;Animalia;Chordata;Mammalia;Carnivora;Mustelidae;Gulo;Gulo gulo (Linnaeus, 1758);63.2;-135.5".getBytes());

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void transformDataZip_test() throws Exception {

    mockMvc.perform(multipart("/data/transform").file(MOCK_INPUT_FILE))
        .andExpect(status().isOk())
        .andExpect(result -> content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
  }
}
