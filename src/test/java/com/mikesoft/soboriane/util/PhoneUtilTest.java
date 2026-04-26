package com.mikesoft.soboriane.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PhoneUtilTest {

  @ParameterizedTest(name = "phoneToCanonical(''{0}'' -> ''{1}''")
  @CsvSource({
      "+79036152734, +79036152734",
      "8(903)615-27-34, +79036152734",
      "+7-903-615-2734, +79036152734"
  })
  void phoneToCanonicalTest(String rawPhone, String canonicalPhone) {
    String actualPhone = PhoneUtil.phoneToCanonical(rawPhone);
    Assertions.assertEquals(canonicalPhone, actualPhone);
  }

}
