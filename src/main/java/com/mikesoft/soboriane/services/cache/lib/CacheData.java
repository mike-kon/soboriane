package com.mikesoft.soboriane.services.cache;

import java.util.List;

/**
 * Интерфейс кэшируемых запросов.
 *
 * @param <T> кэшируемый тип.
 * @param <K> ключевой тип.
 */
public interface CacheData<T, K> {

  /** Загрузка. */
  void load();

  /** Получить элемент по ключу. */
  T get(K key);

  /** Получить все элементы. */
  List<T> getAll();
}
