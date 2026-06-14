package com.mikesoft.soboriane.services.cache;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheService {

  private final List<CacheData<?, ?>> allData;

  @PostConstruct
  public void init() {
    allData.forEach(CacheData::load);
  }
}
