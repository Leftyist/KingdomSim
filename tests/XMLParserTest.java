
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.leftyist.kingdomsim.utils.*;

/**
 * Created by arianfarahani on 4/12/17.
 */
class XMLParserTest
{
      @Test
      void openFile()
      {
            Document doc = XMLParser.openFile("data/buildings.xml");
            NodeList list = doc.getElementsByTagName("Building");
            for(int i = 0; i < list.getLength(); i++) {
                  Node n = list.item(i);
                  NamedNodeMap map = n.getAttributes();
                  map.getLength();
            }

      }

}