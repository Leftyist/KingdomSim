package com.leftyist.kingdomsim.forms;

import com.leftyist.kingdomsim.structures.Kingdom;
import com.leftyist.kingdomsim.structures.WorldManager;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class formKingdom
{
      public  JPanel  kingdomPanel;
      private JLabel  value_stability;
      private JLabel  value_economy;
      private JLabel  value_loyalty;
      private JLabel  value_treasury;
      private JTable  table_building_queue;
      public  JButton nextTurnButton;

      private String kingdomName;

      public JPanel getPanel()
      {
            return kingdomPanel;
      }

      public formKingdom(String kingdomName)
      {
            this.kingdomName = kingdomName;
            Kingdom kingdom = WorldManager.INSTANCE.getKingdom(kingdomName);

            table_building_queue.setModel(new MyTableModel());
            ((MyTableModel) table_building_queue.getModel()).updateQueue(kingdom);

            TableColumn column = null;
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table_building_queue.getModel().getColumnCount(); i++) {
                  column = table_building_queue.getColumnModel().getColumn(i);
                  if (i == 2 || i == 3) {
                        column.setPreferredWidth(50);
                        column.setCellRenderer(centerRenderer);
                  } else {
                        column.setPreferredWidth(100);
                  }
            }

            updateValues();

            formKingdom form = this;

            nextTurnButton.addActionListener(new ActionListener()
            {
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                        if (kingdom.getKingdomStatString("playercontrolled").equals("true")) {
                              nextTurnButton.setEnabled(false);
                              formNextTurn nextTurn = new formNextTurn(form);
                              kingdomPanel.add(nextTurn.getPanel());
                              SwingUtilities.getWindowAncestor(kingdomPanel).pack();
                        }
                  }
            });
      }


      private void updateValues()
      {
            Kingdom kingdom = WorldManager.INSTANCE.getKingdom(kingdomName);
            value_stability.setText(kingdom.getKingdomStatString("stability"));
            value_economy.setText(kingdom.getKingdomStatString("economy"));
            value_loyalty.setText(kingdom.getKingdomStatString("loyalty"));
            value_treasury.setText(kingdom.getKingdomStatString("bp") + " BP");
      }
}

class MyTableModel extends AbstractTableModel
{
      private String[] columnNames = {"Settlement",
              "Building",
              "Cost",
              "Turns"};

      private ArrayList<Kingdom.buildingInConstruction> data;

      public void updateQueue(Kingdom kingdom)
      {
            data = kingdom.getBuildQueue();
      }

      public int getColumnCount()
      {
            return columnNames.length;
      }

      public int getRowCount()
      {
            return data.size();
      }

      public String getColumnName(int col)
      {
            return columnNames[col];
      }

      public Object getValueAt(int row, int col)
      {
            switch (col) {
                  case 0:
                        return data.get(row).getSettlement();
                  case 1:
                        return data.get(row).getBuilding();
                  case 2:
                        return data.get(row).getCost();
                  case 3:
                        return data.get(row).getTurns();
                  default:
                        return null;
            }
      }

      /**
       * JTable uses this method to determine the default renderer/
       * editor for each cell.  If we didn't implement this method,
       * then the last column would contain text ("true"/"false"),
       * rather than a check box.
       */
      public Class getColumnClass(int c)
      {
            return getValueAt(0, c).getClass();
      }

      public boolean isCellEditable(int row, int col)
      {
            return false;
      }
}
