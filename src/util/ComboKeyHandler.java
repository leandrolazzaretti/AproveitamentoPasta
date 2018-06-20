/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author Leandro
 */
public class ComboKeyHandler extends KeyAdapter {

    private final JComboBox<String> comboBox;
    private final List<String> list = new ArrayList<>();
    private boolean shouldHide;

    public ComboKeyHandler(JComboBox<String> combo) {
        this.comboBox = combo;
        for (int i = 0; i < this.comboBox.getModel().getSize(); i++) {
            list.add(this.comboBox.getItemAt(i));
        }
    }

    private static void setSuggestionModel(JComboBox<String> comboBox, ComboBoxModel<String> cml, String str) {
        comboBox.setModel(cml);
        comboBox.setSelectedItem(-1);
        ((JTextField) comboBox.getEditor().getEditorComponent()).setText(str);
    }

    private static ComboBoxModel<String> getSuggestedModel(List<String> list, String text) {

        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        for (String s : list) {
            if (s.toLowerCase().startsWith(text.toLowerCase())) 
                m.addElement(s);            
        }
        return m;
    }
    
    @Override public void keyTyped(final KeyEvent e) {
    EventQueue.invokeLater(new Runnable() {
      @Override public void run() {
        String text = ((JTextField) e.getComponent()).getText();
        ComboBoxModel<String> m;
        if (text.isEmpty()) {
          String[] array = list.toArray(new String[list.size()]);
          m = new DefaultComboBoxModel<String>(array);
          setSuggestionModel(comboBox, m, "");
          comboBox.hidePopup();
        } else {
          m = getSuggestedModel(list, text);
          if (m.getSize() == 0 || shouldHide) {
            comboBox.hidePopup();
          } else {
            setSuggestionModel(comboBox, m, text);
            comboBox.showPopup();
          }
        }
      }
    });
  }
    
}
