package com.blorferind.jeep.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.blorferind.jeep.entity.Jeep;
import com.blorferind.jeep.service.JeepSalesService;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
public class BasicJeepSalesController implements JeepSalesController {
  
  @Autowired
  private JeepSalesService jeepSalesService;

  public List<Jeep> fetchJeeps(String model, String trim) {
    log.debug("model={}, trim ={}", model, trim);
    return jeepSalesService.fetchJeeps(model, trim);
  }

}