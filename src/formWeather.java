import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Created by arianfarahani on 4/14/17.
 */
public class formWeather
{
      private JButton rollWeatherButton;
      private JLabel  daytimeTempLabel;
      private JLabel  nighttimeTempLabel;
      private JPanel  weatherPanel;
      private JLabel  windSpeedLabel;
      private JLabel  weatherLabel;
      private JLabel  climateLabel;
      private JLabel  seasonLabel;
      private JLabel  durationLabel;
      private JButton button1;

      private Weather weather;

      public formWeather()
      {
            //weather = new Weather();

            try {
                  File file = new File("resources/ic_exclamation.png");
                  Image img = ImageIO.read(file);
                  button1.setSize(30, 30);
                  button1.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                  System.out.println(ex.toString());
            }

            rollWeatherButton.addActionListener(new ActionListener()
            {
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                        weather.rollWeather();
                        daytimeTempLabel.setText(weather.getTemperatureDay() + " ");
                        nighttimeTempLabel.setText(weather.getTemperatureNight() + " ");
                        windSpeedLabel.setText(weather.getWindSpeed() + " mph");
                        weatherLabel.setText(weather.getWeatherType());
                        climateLabel.setText(weather.getClimate());
                        seasonLabel.setText(weather.getSeason());

                        if(weather.getPrecipitationTimeMinutes() >= 60)
                              durationLabel.setText(weather.getPrecipitationTimeMinutes()/60 + " hours");
                        else
                              durationLabel.setText(weather.getPrecipitationTimeMinutes() + " minutes");
                  }
            });
      }

      public JPanel getPanel()
      {
            return weatherPanel;
      }
}
