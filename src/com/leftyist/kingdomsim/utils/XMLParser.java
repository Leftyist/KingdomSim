package com.leftyist.kingdomsim.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javafx.util.Pair;

public class XMLParser
{
      public XMLParser() {
      }

      public static Document openFile(String fileName) {
            try {
                  //init
                  File file = new File(fileName);
                  DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                  documentBuilderFactory.setIgnoringElementContentWhitespace(true);
                  DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                  return(documentBuilder.parse(file));
            } catch (Exception e) {
                  return null;
            }
      }

      private static String[] getNodeValues(NodeList nodeList) {
            String[] ret = new String[nodeList.getLength()];
            for(int i = 0; i < nodeList.getLength(); i++)
                  ret[i] = nodeList.item(i).getTextContent();

            return ret;
      }

      private static ArrayList<Pair<String, String>> getNodeSubAttributes(NodeList node) {
            ArrayList<Pair<String,String>> values = new ArrayList<>();

            for(int i = 0; i < node.getLength(); i++) {
                  String key = node.item(i).getNodeName();

                  if(key.equals("#text"))
                        continue;

                  Node n = node.item(i);

                  values.add(new Pair<>(n.getNodeName(), n.getTextContent()));
            }
            return values;
      }

      public static String findValueByTag(NodeList nodes, String tagName) {

            for (int i = 0; i < nodes.getLength(); i++) {
                  String nodeName = nodes.item(i).getNodeName();
                  if (nodeName.equals(tagName))
                        return nodes.item(i).getTextContent();
            }
            return null;
      }

      public static NodeList findChild(NodeList nodes, String childName) {
            if (nodes == null)
                  return null;

            for(int i = 0; i < nodes.getLength(); i++) {
                  if(nodes.item(i).getNodeName().equals(childName))
                        return nodes.item(i).getChildNodes();
            }
            return null;
      }
}
