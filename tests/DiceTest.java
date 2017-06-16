/**
 * Created by arianfarahani on 4/12/17.
 */

import static org.junit.jupiter.api.Assertions.assertTrue;

class DiceTest
{
      @org.junit.jupiter.api.Test
      void rollPositive()
      {
            String roll = "1d6+2";
            int min = 3;
            int max = 8;

            for(int i = 0; i < 100; i++) {
                  int val = Dice.roll(roll);
                  assertTrue(val <= max && val >= min);
            }
      }

      @org.junit.jupiter.api.Test
      void rollNegative()
      {
            String roll = "1d20-5";
            int min = 1;
            int max = 15;

            for(int i = 0; i < 100; i++) {
                  int val = Dice.roll(roll);
                  assertTrue(val <= max && val >= min);
            }
      }
}