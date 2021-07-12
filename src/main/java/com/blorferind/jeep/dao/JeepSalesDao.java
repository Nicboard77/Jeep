package com.blorferind.jeep.dao;

import java.util.List;
import com.blorferind.jeep.entity.Jeep;
import com.blorferind.jeep.entity.jeepModel;

public interface JeepSalesDao {

  List<Jeep> fetchJeeps(jeepModel model, String trim);

}
