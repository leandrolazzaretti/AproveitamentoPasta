/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dao.MovimentacaoEstoqueDao;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Leandro
 */
public class CoresAlternadasTabela extends DefaultTableCellRenderer {

    private double quantidade;
    

    public CoresAlternadasTabela() {
    }

    public void CorNaLinhaQuantidade(JTable tabela) {
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object values,
                    boolean isSelected, boolean hasFocus, int row, int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, values, isSelected, hasFocus, row, column);
                Object texto = table.getValueAt(row, 3);
                if (Double.parseDouble(texto.toString().replace(" kg", "").replace(" g", "").replace(" mg", "").replace(" L", "").replace(".", "").replace(",", ".")) <= 0) {
                    setForeground(Color.red);
                } else {
                    setForeground(Color.BLACK);
                }
                tabela.setSelectionBackground(new java.awt.Color(51, 153, 255));
                
                return label;
            }
        ;
    }


);
        
    }
    
    public void CorNaLinhaValidade(JTable tabela) {
        MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();
        Util util = new Util();
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object values,
                    boolean isSelected, boolean hasFocus, int row, int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, values, isSelected, hasFocus, row, column);
                Object texto = table.getValueAt(row, 5);
              
                if (movEstDao.dataComparar(movEstDao.dataAtual(),util.inverterData(texto.toString().replace("/", "-"))) == true) {
                    setForeground(Color.red);
                } else {
                    setForeground(Color.BLACK);
                }
                tabela.setSelectionBackground(new java.awt.Color(51, 153, 255));
                
                return label;
            }
        ;
    }


);
        
    }
}
