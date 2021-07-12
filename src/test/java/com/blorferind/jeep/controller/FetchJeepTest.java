package com.blorferind.jeep.controller;


import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.blorferind.jeep.Constants;
import com.blorferind.jeep.controller.support.FetchJeepTestSupport;
import com.blorferind.jeep.entity.Jeep;
import com.blorferind.jeep.entity.jeepModel;
import com.blorferind.jeep.service.JeepSalesService;

class FetchJeepTest{
  
  @Nested
  @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
  @ActiveProfiles("test")
  @Sql(scripts = {
      "classpath:flyway/migrations/jeepSchema.sql",
      "classpath:flyway/migrations/jeepData.sql"}, 
      config = @SqlConfig(encoding = "utf-8"))
  class TestsThatDoNotPolluteTheApplicationContext extends FetchJeepTestSupport{
    
    @Test
    void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
      //System.out.println(getBaseUri());
      
      //Given: a valid model, trim and URI
      jeepModel model = jeepModel.WRANGLER;
      String trim = "Sport";
      String uri = 
          String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
      
      // When: a connection is made to the URI
      //getRestTemplate().getForEntity(uri, Jeep.class);
      
      ResponseEntity<List<Jeep>> response = 
         getRestTemplate().exchange(uri, HttpMethod.GET, null,
             new ParameterizedTypeReference<>() {});
      //Then: a success (Ok - 200) status code is returned
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      
      // And: the actual list returned is the same as the expected list
      List<Jeep> actual = response.getBody();
      List<Jeep> expected = buildExpected();
      
      actual.forEach(jeep -> jeep.setModelPK(null));
       
      assertThat(actual).isEqualTo(expected);
    }


  @Test
  void testThatAnErrorMessageIsReturnedWhenAnUnknownTrimIsSupplied() {
    //System.out.println(getBaseUri());
    
    //Given: a valid model, trim and URI
    jeepModel model = jeepModel.WRANGLER;
    String trim = "Unknown Value";
    String uri = 
        String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
    
    // When: a connection is made to the URI
    //getRestTemplate().getForEntity(uri, Jeep.class);
    
    ResponseEntity<Map<String, Object>> response = 
       getRestTemplate().exchange(uri, HttpMethod.GET, null,
           new ParameterizedTypeReference<>() {});
    //Then: a not found (404) status code is returned
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    
    // And: an error message is returned
    Map<String, Object> error = response.getBody();
    
  }
  @ParameterizedTest
  @MethodSource("com.blorferind.jeep.controller.FetchJeepTest#parametersForInvalidInput")
  void testThatAnErrorMessageIsReturnedWhenAnInvalidValueIsSupplied(String model, String trim, String reason) {
    //System.out.println(getBaseUri());
    
    //Given: a valid model, trim and URI

    String uri = 
        String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
    
    // When: a connection is made to the URI
    //getRestTemplate().getForEntity(uri, Jeep.class);
    
    ResponseEntity<Map<String, Object>> response = 
       getRestTemplate().exchange(uri, HttpMethod.GET, null,
           new ParameterizedTypeReference<>() {});
    //Then: a not found (400) status code is returned
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    
    // And: an error message is returned
    Map<String, Object> error = response.getBody();
    
    assertErrorMessageValid(error, HttpStatus.BAD_REQUEST);
  }

}
  static Stream<Arguments> parametersForInvalidInput() {
    return Stream.of(
        arguments("WRANGLER", "@*&^$*&#@", "Trim contains non-alphanumeric chars"),
        arguments("WRANGLER", "C".repeat(Constants.TRIM_MAX_LENGTH + 1), "Trim length too long"),
        arguments("INVALID", "Sport", "Model is not enum value")
        
    );
  }

  @Nested
  @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
  @ActiveProfiles("test")
  @Sql(scripts = {
      "classpath:flyway/migrations/jeepSchema.sql",
      "classpath:flyway/migrations/jeepData.sql"}, 
      config = @SqlConfig(encoding = "utf-8"))
  class TestsThatPolluteTheApplicationContext extends FetchJeepTestSupport{
    @MockBean
    private JeepSalesService jeepSalesService;
    
    @Test
    void testThatAnUnplannedErrorResultsInA500Status() {
      //System.out.println(getBaseUri());
      
      //Given: a valid model, trim and URI
      jeepModel model = jeepModel.WRANGLER;
      String trim = "Invalid";
      String uri = 
          String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
      
      
      doThrow(new RuntimeException("Ouch!")).when(jeepSalesService).fetchJeeps(model, trim);
      
      

      // When: a connection is made to the URI
      ResponseEntity<Map<String, Object>> response = 
         getRestTemplate().exchange(uri, HttpMethod.GET, null,
             new ParameterizedTypeReference<>() {});
      //Then: an INTERNAL SERVER ERROR (500) status code is returned
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
      
      // And: an error message is returned
      Map<String, Object> error = response.getBody();
      
      assertErrorMessageValid(error, HttpStatus.INTERNAL_SERVER_ERROR);
      
    }
    
  }




}
