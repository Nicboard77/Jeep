package com.blorferind.jeep.controller;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.blorferind.jeep.controller.support.FetchJeepTestSupport;
import com.blorferind.jeep.entity.Jeep;
import com.blorferind.jeep.entity.jeepModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
    config = @SqlConfig(encoding = "utf-8"))

class FetchJeepTest extends FetchJeepTestSupport{

  @Test
  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
    //System.out.println(getBaseUri());
    
    //Given: a valid model, trim and URI
    jeepModel model = jeepModel.WRANGLER;
    String trim = "Sport";
    String uri = 
        String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
    System.out.println(uri);
    
    // When: a connection is made to the URI
    //getRestTemplate().getForEntity(uri, Jeep.class);
    
    ResponseEntity<Jeep> response = 
       getRestTemplate().getForEntity(uri, Jeep.class);
    
    //Then: a success (Ok - 200) status code is returned
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

}