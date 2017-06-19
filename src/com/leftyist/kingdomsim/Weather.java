package com.leftyist.kingdomsim;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import com.leftyist.kingdomsim.utils.*;
import javafx.util.Pair;

import static com.leftyist.kingdomsim.utils.DiceKt.roll;
import static com.leftyist.kingdomsim.utils.DiceKt.rollRange;

public class Weather
{
      private Document      weatherDoc;
      private WeightedTable weatherTable;

      private String climate;
      private String season;
      private String weatherType;

      private int    temperatureDay;
      private int    temperatureNight;
      private String temperatureEffects;

      private int    windSpeed;
      private String windEffects;

      private String precipitationDescription;
      private String precipitationEffects;
      private int    precipitationTimeMinutes;

      private int snowfallInches;
      private int snowAccumulationInches;


      public String getPrecipitationDescription()
      {
            return precipitationDescription;
      }

      public int getPrecipitationTimeMinutes()
      {
            return precipitationTimeMinutes;
      }

      public int getSnowfallInches()
      {
            return snowfallInches;
      }

      public int getSnowAccumulationInches()
      {
            return snowAccumulationInches;
      }

      public int getTemperatureDay()
      {
            return temperatureDay;
      }

      public int getTemperatureNight()
      {
            return temperatureNight;
      }

      public int getWindSpeed()
      {
            return windSpeed;
      }

      public String getWeatherType()
      {
            return weatherType;
      }

      public String getClimate()
      {
            return climate;
      }

      public String getSeason()
      {
            return season;
      }

      public Weather() throws IllegalStateException
      {
            weatherDoc = XMLParser.openFile("data/weather.xml");
            if (weatherDoc == null) {
                  throw new IllegalStateException();
            }

            climate = "Temperate";
            buildTable();
      }

      private void initValues()
      {
            weatherType = "";
            temperatureDay = 0;
            temperatureNight = 0;
            windSpeed = 0;
            precipitationDescription = "";
            precipitationTimeMinutes = 0;

            //changeClimate();
            changeSeason();
      }

      private void changeSeason() {
            String[] seasons = getSeasons();
            int num = rollRange(0, seasons.length-1);
            season = seasons[num];
      }

      private void changeClimate() {
            String[] climates = getClimates();
            int num = rollRange(0, climates.length-1);
            climate = climates[num];

            buildTable();
      }

      private String[] getClimates() {
            NodeList nodes = weatherDoc.getElementsByTagName("Climate");
            String[] ret = new String[nodes.getLength()];
            for(int i = 0; i < nodes.getLength(); i++) {
                  ret[i] = nodes.item(i).getTextContent();
            }
            return ret;
      }

      private String[] getSeasons() {
            NodeList nodes = weatherDoc.getElementsByTagName("Seasons");
            nodes = nodes.item(0).getChildNodes();
            ArrayList<String> ret = new ArrayList<>();
            for(int i = 0; i < nodes.getLength(); i++) {
                  if(nodes.item(i).getNodeName().equals("#text"))
                        continue;

                  ret.add(nodes.item(i).getNodeName());
            }
            String[] converted = new String[ret.size()];
            for(int i = 0; i < ret.size(); i++) {
                  converted[i] = ret.get(i);
            }
            return converted;
      }


      public void rollWeather()
      {
            initValues();
            ArrayList<Pair<String, String>> values = weatherTable.roll();

            for (Pair<String, String> p : values) {

                  if (p.getKey().equals("temp"))
                        getTemperature(p.getValue());

                  else if (p.getKey().equals("wind"))
                        getWind(p.getValue());

                  else if (p.getKey().equals("type")) {
                        weatherType = p.getValue();
                        if (weatherType.equals("Cold snap")) {
                              temperatureNight -= 10;
                              temperatureDay -= 10;
                        } else if (weatherType.equals("Heat wave")) {
                              temperatureDay += 10;
                              temperatureNight += 10;
                        }
                  } else if(p.getKey().equals("precipitation")) {
                        getPrecipitation(p.getValue());
                        weatherType = p.getValue();
                  }

            }
      }

      private void buildTable()
      {
            weatherTable = new WeightedTable();
            NodeList nodes = weatherDoc.getElementsByTagName(climate);
            nodes = nodes.item(nodes.getLength() - 1).getChildNodes();
            weatherTable.addSubnodes(nodes);
      }

      private void getPrecipitation(String precip) {
            NodeList nodes = weatherDoc.getElementsByTagName("Precipitations");
            nodes = nodes.item(0).getChildNodes();
            nodes = XMLParser.findChild(nodes, precip);

            if (nodes == null)
                  throw new IllegalArgumentException();

            String description = XMLParser.findValueByTag(nodes, "description");

            nodes = XMLParser.findChild(nodes, "Duration");

            String dice = XMLParser.findValueByTag(nodes, "dice");
            String units = XMLParser.findValueByTag(nodes, "units");

            precipitationTimeMinutes = roll(dice);
            precipitationDescription = description;

            if(units.equals("hours"))
                  precipitationTimeMinutes *= 60;


      }

      private void getTemperature(String temp) throws IllegalArgumentException
      {
            if (temp.equals("Normal")) {
                  NodeList tempLookup = weatherDoc.getElementsByTagName("Seasons");
                  tempLookup = tempLookup.item(0).getChildNodes();
                  tempLookup = XMLParser.findChild(tempLookup, season);
                  temp = XMLParser.findValueByTag(tempLookup, "temp");
            }

            NodeList nodes = weatherDoc.getElementsByTagName("Temperatures");
            nodes = nodes.item(0).getChildNodes();
            nodes = XMLParser.findChild(nodes, temp);
            if (nodes == null)
                  throw new IllegalArgumentException();

            int min = Integer.parseInt(XMLParser.findValueByTag(nodes, "min"));
            int max = Integer.parseInt(XMLParser.findValueByTag(nodes, "max"));

            temperatureDay += rollRange(min, max);
            temperatureNight = temperatureDay - rollRange(10, 20);
      }

      private void getWind(String wind) throws IllegalArgumentException
      {
            NodeList nodes = weatherDoc.getElementsByTagName("Winds");
            nodes = nodes.item(0).getChildNodes();

            nodes = XMLParser.findChild(nodes, wind);
            if (nodes == null)
                  throw new IllegalArgumentException();

            int min = Integer.parseInt(XMLParser.findValueByTag(nodes, "min"));
            int max = Integer.parseInt(XMLParser.findValueByTag(nodes, "max"));

            windSpeed = rollRange(min, max);
      }

      public void print()
      {
            weatherTable.print();
      }
}
