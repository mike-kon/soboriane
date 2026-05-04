package com.mikesoft.soboriane.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Утилиты для обработки номера телефона.
 */
public class PhoneUtil {

  public static final String FULL_PHONE_PATTERN
      = "^(8|\\+7)[-( ]?(\\d{3})[-) ]?(\\d{3})[- ]?(\\d{2})[- ]?(\\d{2})$";
  public static final String CANONICAL_PHONE_PATTERN = "^\\+7\\d{10}$";
  private static final Pattern fullPhonePatter = Pattern.compile(FULL_PHONE_PATTERN);

  private PhoneUtil() {
  }

  /**
   * Приведение телефона к каноническому виду.
   *
   * @param rawPhone Изначальный номер.
   * @return Канонический вид телефона.
   */
  public static String phoneToCanonical(String rawPhone) {
    if (rawPhone.matches(CANONICAL_PHONE_PATTERN)) {
      return rawPhone;
    }
    Matcher matcher = fullPhonePatter.matcher(rawPhone);
    if (matcher.find()) {
      if (matcher.groupCount() < 5) {
        throw new IllegalArgumentException("Это не телефон:" + rawPhone);
      }
      StringBuilder stringBuilder = new StringBuilder("+7");
      for (int i = 2; i <= matcher.groupCount(); i++) {
        String str = matcher.group(i);
        stringBuilder.append(str);
      }
      return stringBuilder.toString();
    }
    throw new IllegalArgumentException("Это не телефон:" + rawPhone);
  }

}
