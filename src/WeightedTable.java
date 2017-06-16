import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javafx.util.Pair;

/**
 * Created by arianfarahani on 4/12/17.
 */
public class WeightedTable
{
      ArrayList<WeightedOption> options;
      private int totalWeight;

      private class WeightedOption
      {
            private int                             upperBound;
            private ArrayList<Pair<String, String>> values;

            private WeightedOption()
            {
                  this.upperBound = 0;
                  values = new ArrayList<>();
            }

            private void setUpperBound(int bound)
            {
                  this.upperBound = bound;
            }

            private void setValues(ArrayList<Pair<String, String>> vals)
            {
                  this.values = vals;
            }

            private void addValue(String key, String value)
            {
                  values.add(new Pair(key, value));
            }

            private int getUpperBound()
            {
                  return upperBound;
            }

            private ArrayList<Pair<String, String>> getValues()
            {
                  return values;
            }

            public void print()
            {
                  System.out.println("\tbound: " + upperBound);

                  for (Pair<String, String> pair : values) {
                        System.out.println("\t" + pair.getKey() + ": " + pair.getValue());
                  }
            }
      }

      public WeightedTable()
      {
            totalWeight = 0;
            options = new ArrayList<>();
      }

      public void addSubnodes(NodeList parentNode)
      {

            for (int i = 0; i < parentNode.getLength(); i++) {
                  NodeList list = parentNode.item(i).getChildNodes();
                  WeightedOption option = null;
                  for (int j = 0; j < list.getLength(); j++) {
                        String key = list.item(j).getNodeName();

                        if (key.equals("#text"))
                              continue;

                        if (option == null)
                              option = new WeightedOption();

                        String value = list.item(j).getTextContent();
                        option.addValue(key, value);

                        if (key.equals("weight")) {
                              totalWeight += Integer.parseInt(value);
                              option.setUpperBound(totalWeight);
                        }
                  }
                  if (option != null)
                        options.add(option);
            }
      }

      public void addFromNodeTag(Document doc, String tagName)
      {
            NodeList nodes = doc.getElementsByTagName(tagName);


            for (int i = 0; i < nodes.getLength(); i++) {
                  NodeList list = nodes.item(i).getChildNodes();
                  WeightedOption option = null;
                  for (int j = 0; j < list.getLength(); j++) {
                        String key = list.item(j).getNodeName();

                        if (key.equals("#text"))
                              continue;

                        if (option == null)
                              option = new WeightedOption();

                        String value = list.item(j).getTextContent();
                        option.addValue(key, value);

                        if (key.equals("weight")) {
                              totalWeight += Integer.parseInt(value);
                              option.setUpperBound(totalWeight);
                        }
                  }
                  if (option != null)
                        options.add(option);
            }
      }

      public ArrayList<Pair<String, String>> roll()
      {
            int num = Dice.roll(totalWeight);
            for (WeightedOption option : options) {
                  if (num <= option.upperBound)
                        return option.getValues();
            }

            return null;
      }

      public void print()
      {
            System.out.println("Total weight: " + totalWeight + "\n ---------------------");
            for (int i = 0; i < options.size(); i++) {
                  System.out.println("Option: " + i);
                  options.get(i).print();
            }
            System.out.println("\n");
      }


}
