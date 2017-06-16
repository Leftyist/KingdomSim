
/**
 * Created by arianfarahani on 4/12/17.
 */
import java.util.concurrent.ThreadLocalRandom;

public class Dice
{
      private Dice()
      {
      }

      private static int rollDice(int count, int sides) {
            int total = 0;
            for(int i = 0; i < count; i++) {
                  total += ThreadLocalRandom.current().nextInt(1, sides + 1);
            }
            return total;
      }

      private static int rollRange(int start, int end) {
            return ThreadLocalRandom.current().nextInt(start, end + 1);
      }

      public static int roll(String input) {
            int bonus = 0;
            String base;
            if(input.contains("+")) {
                  String[] mod = input.split("\\+");
                  bonus += Integer.parseInt(mod[1]);
                  base = mod[0];
            } else if(input.contains("-")) {
                  String[] mod = input.split("-");
                  bonus -= Integer.parseInt(mod[1]);
                  base = mod[0];
            } else {
                  base = input;
            }

            String[] dice = base.split("d");

            int count = Integer.parseInt(dice[0]);
            int sides = Integer.parseInt(dice[1]);
            int result = rollDice(count, sides);
            result += bonus;

            //always min 1
            if(result < 1)
                  result = 1;

            return result;
      }

      public static int roll(int input) {
            return rollDice(1, input);
      }

      public static int roll(int start, int end) {
            return rollRange(start, end);
      }

}
