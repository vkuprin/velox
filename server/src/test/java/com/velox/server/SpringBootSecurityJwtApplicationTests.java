package com.velox.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.velox.server.controller.MyController;
import com.velox.server.service.MyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {

  @Autowired
  private MyController myController;

  @MockBean
  private MyService myService;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
      mockMvc = MockMvcBuilders.standaloneSetup(myController).build();
  }

  @Test
  public void contextLoads() {
      assertThat(myController).isNotNull();
  }

  @Test
  public void testJwtTokenCreationAndValidation() {
      // ToDo
  }

  @Test
  public void testPublicEndpoint() throws Exception {
      mockMvc.perform(get("/public-endpoint"))
          .andExpect(status().isOk());
  }

  @Test
  public void testPrivateEndpointUnauthorized() throws Exception {
      mockMvc.perform(get("/private-endpoint"))
          .andExpect(status().isUnauthorized());
  }

  @Test
  public void testPrivateEndpointAuthorized() throws Exception {
      String token = "valid-jwt-token";
      mockMvc.perform(get("/private-endpoint")
          .header("Authorization", "Bearer " + token))
          .andExpect(status().isOk());
  }

  @Test
  public void testServiceLayer() {
      // ToDo
  }
}
