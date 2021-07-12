package com.blorferind.jeep.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.blorferind.jeep.dao.JeepSalesDao;
import com.blorferind.jeep.entity.Jeep;
import com.blorferind.jeep.entity.jeepModel;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class DefaultJeepSalesService implements JeepSalesService {
  
  @Autowired
  private JeepSalesDao jeepSalesDao;

  @Transactional(readOnly = true)
  @Override
  public List<Jeep> fetchJeeps(jeepModel model, String trim) {
    log.info("The fetchJeeps method was called with model={} and trim={}",
        model, trim);
    List<Jeep> jeeps = jeepSalesDao.fetchJeeps(model, trim);
    
    
    
    if(jeeps.isEmpty()) {
      String msg = String.format("No jeeps found with model=%s and trim=%s",
          model,
          trim);
      throw new NoSuchElementException(msg);
    }
    Collections.sort(jeeps);
    return jeeps;
   
  }

}
