package com.blorferind.jeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.blorferind.ComponentScanMarker;

@SpringBootApplication(scanBasePackageClasses = { ComponentScanMarker.class })

public class jeepSales {

  public static void main(String[] args) {
    SpringApplication.run(jeepSales.class, args);
  }
}
