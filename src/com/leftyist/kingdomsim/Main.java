package com.leftyist.kingdomsim;

import javax.swing.JFrame;
import com.leftyist.kingdomsim.forms.formWeather;

/**
 * Created by arianfarahani on 4/12/17.
 */
public class Main
{
      public static void main(String[] args) {
            //Weather weather = new Weather();
            loadFrame();
            //weather.rollWeather();
      }

      public static void loadFrame() {
            formWeather form = new formWeather();


            JFrame frame = new JFrame("Weather window");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setContentPane(form.getPanel());

            frame.setSize(200, 250);
            //frame.pack();
            frame.setVisible(true);
      }
}
