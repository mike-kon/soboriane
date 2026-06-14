package com.mikesoft.soboriane.services.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.scheduling.annotation.Async;

public abstract class CacheDataSync<T,K> implements CacheData<T,K> {

  private AtomicReference<List<T>> data = new AtomicReference<>(new ArrayList<>());

  @Override
  @Async
  public void load() {
    List<T> newData = loadData();
    data.set(newData);
  }

  @Override
  public List<T> getAll() {
    return data.get();
  }

  public abstract List<T> loadData();
}
