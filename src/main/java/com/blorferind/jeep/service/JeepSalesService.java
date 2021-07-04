package com.blorferind.jeep.service;

import java.util.List;
import com.blorferind.jeep.entity.Jeep;

public interface JeepSalesService {

  List<Jeep> fetchJeeps(String model, String trim);

}
