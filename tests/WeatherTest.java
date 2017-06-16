import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by arianfarahani on 4/12/17.
 */
class WeatherTest
{
      @Test
      void rollWeather()
      {
            Weather weather = new Weather();

            for(int i = 0; i < 500; i++) {
                  weather.rollWeather();

                  assertTrue(weather.getTemperatureDay() - weather.getTemperatureNight() <= 20);

                  int day = weather.getTemperatureDay();
                  if(day < 0)
                        System.out.println("Temp: " + day + " on roll " + i);

                  if(day > 40)
                        System.out.println("Temp: " + day + " on roll " + i);

            }
      }

}