import org.w3c.dom.Document;

import java.util.ArrayList;

import javafx.util.Pair;

/**
 * Created by arianfarahani on 4/12/17.
 */
public class CharacterGenerator
{
      private Document      raceDoc;
      private WeightedTable homelandTable;

      public CharacterGenerator() throws IllegalStateException {
            raceDoc = XMLParser.openFile("data/races.xml");
            if(raceDoc == null) {
                  throw new IllegalStateException();
            }

            homelandTable = new WeightedTable();
            homelandTable.addFromNodeTag(raceDoc, "Homeland");
      }

      public ArrayList<Pair<String,String>> rollHomeland() {
            return homelandTable.roll();
      }

      public void print() {
            homelandTable.print();
      }
}
