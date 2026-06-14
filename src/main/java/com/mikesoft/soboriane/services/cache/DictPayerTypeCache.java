package com.mikesoft.soboriane.services.cache;

import com.mikesoft.soboriane.dao.DictPayerTypeDao;
import com.mikesoft.soboriane.dto.db.DictPrayerTypeDto;
import com.mikesoft.soboriane.services.cache.lib.CacheDataSync;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DicktPayerTypeCache extends CacheDataSync<DictPrayerTypeDto, Integer> {

  private final DictPayerTypeDao dictPayerTypeDao;

  @Override
  public boolean findByKey(DictPrayerTypeDto item, Integer key) {
    return item.;
  }

  @Override
  public List<DictPrayerTypeDto> loadData() {
    return dictPayerTypeDao.getAll();
  }

}
