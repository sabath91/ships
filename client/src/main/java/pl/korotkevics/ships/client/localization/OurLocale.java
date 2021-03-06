package pl.korotkevics.ships.client.localization;

/**
 * Locale representation.
 * Implemented ourselves because JDK has just a few whereas Polish, Russian are not available.
 *
 * @author Sandor Korotkevics
 * @since 2018-01-15
 */

public enum OurLocale {
  ENGLISH {
    @Override
    public String toString() {
      return "en";
    }
  },
  POLISH {
    @Override
    public String toString() {
      return "pl";
    }
  },
  RUSSIAN {
    @Override
    public String toString() {
      return "ru";
    }
  }
}
