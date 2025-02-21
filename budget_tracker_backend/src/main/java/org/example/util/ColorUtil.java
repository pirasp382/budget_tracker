package org.example.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class ColorUtil {

  private static final Random RANDOM = new Random();

  public static String generateRandomColor() {
    final int r = RANDOM.nextInt(256);
    final int g = RANDOM.nextInt(256);
    final int b = RANDOM.nextInt(256);
    return String.format("#%02X%02X%02X", r, g, b);
  }
}
