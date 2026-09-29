package vn.iotstar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1","spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc @ActiveProfiles("demo")
class AuthenticationTest {
 @Autowired MockMvc mvc; @Autowired ObjectMapper mapper;
 @Test void fullFlow()throws Exception {
  String signup="{\"fullName\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"Password123!\"}";
  mvc.perform(get("/users/me")).andExpect(status().isUnauthorized());
  mvc.perform(post("/auth/signup").contentType("application/json").content(signup)).andExpect(status().isOk()).andExpect(jsonPath("$.password").doesNotExist());
  mvc.perform(post("/auth/signup").contentType("application/json").content(signup)).andExpect(status().isConflict());
  mvc.perform(post("/auth/login").contentType("application/json").content("{\"email\":\"test@example.com\",\"password\":\"wrong\"}")).andExpect(status().isUnauthorized());
  String response=mvc.perform(post("/auth/login").contentType("application/json").content(signup)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  String token=mapper.readTree(response).get("token").asText();
  mvc.perform(get("/users/me").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("test@example.com")).andExpect(jsonPath("$.password").doesNotExist());
  mvc.perform(get("/users/").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$[0].password").doesNotExist());
  mvc.perform(get("/users/me").header("Authorization","Bearer invalid.token.value")).andExpect(status().isUnauthorized());
  mvc.perform(post("/auth/signup").contentType("application/json").content("{\"email\":\"invalid\",\"password\":\"1\",\"fullName\":\"\"}")).andExpect(status().isBadRequest());
  mvc.perform(get("/login")).andExpect(status().isOk());
 }
}
