package com.blorferind.jeep.service;

import java.util.List;
import com.blorferind.jeep.entity.Jeep;
import com.blorferind.jeep.entity.jeepModel;

public interface JeepSalesService {

  List<Jeep> fetchJeeps(jeepModel model, String trim);

}
