package com.leftyist.kingdomsim.forms;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.BorderUIResource;

public class formNextTurn
{
      private JPanel  nextTurnPanel;
      private JButton finishTurnButton;

      private formKingdom parentForm;

      public JPanel getPanel() {
            return nextTurnPanel;
      }

      public formNextTurn(formKingdom parent) {
            parentForm = parent;
            nextTurnPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Income Phase"));

            JPanel thisPanel = this.getPanel();
            finishTurnButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                        parentForm.nextTurnButton.setEnabled(true);
                        parentForm.kingdomPanel.remove(thisPanel);
                        SwingUtilities.getWindowAncestor(parentForm.kingdomPanel).pack();
                  }
            });
      }
}
