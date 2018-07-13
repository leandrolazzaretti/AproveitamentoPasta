/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import dao.MovimentacaoEstoqueDao;
import dao.ReceitaDao;
import java.awt.Color;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaPesquisarPastaEstoque extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    Util util = new Util();
    MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();
    
    private String cbPesquisar = "ep.codigoReceita";
    
    /**
     * Creates new form TelaPesquisarPastaEstoque
     */
    public TelaPesquisarPastaEstoque() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        retirarBordas();
        setarTabelaPasta();
    }
    
     // metodo para retirar as bordas do JinternalFrame
    private void retirarBordas() {
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null); //retirar o painel superior
        this.setBorder(null);//retirar bordas
    }

    //Seta a tabela de estoque de pastas
    public void setarTabelaPasta() {
        String sql = "select ep.codigoReceita, r.descricao, ep.quantidade, r.datavencimento, ep.data, ep.dataVencimento from tbEstoquePasta as ep"
                + " inner join tbreceita as r on ep.codigoReceita = r.codigorec"
                + " where " + this.cbPesquisar + " like ?";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) this.tblEstPasta.getModel();
        modelo.setNumRows(0);

        this.tblEstPasta.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(1).setPreferredWidth(30);
        this.tblEstPasta.getColumnModel().getColumn(2).setPreferredWidth(30);
        this.tblEstPasta.getColumnModel().getColumn(3).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(4).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(5).setPreferredWidth(20);

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtMovEst.getText() + "%");
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    this.util.formatadorQuant(rs.getDouble(3)),
                    rs.getString(4),
                    inverterData(rs.getString(5)).replace("-", "/"),
                    inverterData(rs.getString(6)).replace("-", "/")
                        
                });
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }
    
     private String inverterData(String data) {
        String[] dataInvertida = data.split("-");
        return dataInvertida[2] + "-" + dataInvertida[1] + "-" + dataInvertida[0];
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbPesMovEst = new javax.swing.JComboBox<>();
        txtMovEst = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEstPasta = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();

        setBackground(new java.awt.Color(143, 165, 110));
        setClosable(true);
        setIconifiable(true);
        setPreferredSize(new java.awt.Dimension(858, 386));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameDeiconified(evt);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(79, 79, 79));
        jLabel8.setText("Tabela Estoque de Pastas");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Pesquisar por:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 53, -1, -1));

        cbPesMovEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Pasta", "Quantidade", "Validade" }));
        cbPesMovEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPesMovEstActionPerformed(evt);
            }
        });
        getContentPane().add(cbPesMovEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 74, 140, -1));

        txtMovEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMovEstKeyReleased(evt);
            }
        });
        getContentPane().add(txtMovEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 74, 228, -1));

        tblEstPasta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Pasta", "Quantidade ", "Validade", "Data", "Data Vencimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEstPasta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEstPastaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEstPasta);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 840, 190));

        jPanel6.setBackground(new java.awt.Color(143, 165, 110));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnMinimi.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        btnMinimi.setForeground(new java.awt.Color(79, 79, 79));
        btnMinimi.setText("-");
        btnMinimi.setContentAreaFilled(false);
        btnMinimi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMinimi.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnMinimi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinimiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinimiMouseExited(evt);
            }
        });
        btnMinimi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinimiActionPerformed(evt);
            }
        });
        jPanel6.add(btnMinimi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 44, 20));

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(776, 2, 40, 20));

        jPanel1.setBackground(new java.awt.Color(143, 165, 110));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnFechar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnFechar.setForeground(new java.awt.Color(79, 79, 79));
        btnFechar.setText("X");
        btnFechar.setContentAreaFilled(false);
        btnFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFecharMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFecharMouseExited(evt);
            }
        });
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });
        jPanel1.add(btnFechar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(816, 2, 40, 20));

        setBounds(0, 89, 858, 316);
    }// </editor-fold>//GEN-END:initComponents

    private void cbPesMovEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPesMovEstActionPerformed
        //  // adiciona um valor a variável cbPesquizar
        if (this.cbPesMovEst.getSelectedItem().equals("Código")) {
            this.cbPesquisar = "ep.codigoReceita";
        } else {
            if (this.cbPesMovEst.getSelectedItem().equals("Pasta")) {
                this.cbPesquisar = "r.descricao";
            } else {
                if (this.cbPesMovEst.getSelectedItem().equals("Quantidade")) {
                    this.cbPesquisar = "ep.quantidade";
                } else {
                    this.cbPesquisar = "r.datavencimento";
                }
            }
        }
    }//GEN-LAST:event_cbPesMovEstActionPerformed

    private void txtMovEstKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMovEstKeyReleased
        // chama metodo pesquisar enquanto digita
        setarTabelaPasta();
    }//GEN-LAST:event_txtMovEstKeyReleased

    private void tblEstPastaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEstPastaMouseClicked
        // seta a descrição da pasta nno campo Receita, através de duplo click com o mouse

        if (evt.getClickCount() > 1) {
            ReceitaDao receitaDao = new ReceitaDao();
            int setar = this.tblEstPasta.getSelectedRow();
            TelaMovimentacaoEstoque.txtDescricao.setText((String) this.tblEstPasta.getModel().getValueAt(setar, 1));
            TelaMovimentacaoEstoque.txtCodigo.setText(receitaDao.buscaCodigo((String) this.tblEstPasta.getModel().getValueAt(setar, 1)));
            TelaMovimentacaoEstoque.txtCodigo.setEnabled(false);
            TelaMovimentacaoEstoque.txtEstQuantidade.setEnabled(true);
            TelaMovimentacaoEstoque.txtEstData.setEnabled(true);
            if (TelaMovimentacaoEstoque.cbTipo.getSelectedItem().equals("Saída")) {
                TelaMovimentacaoEstoque.txtEstData.setText(this.movEstDao.inverterData(this.movEstDao.dataAtual()).replace("-", "/"));
            }
            TelaMovimentacaoEstoque.txtEstQuantidade.requestFocus();
            this.dispose();
        }
    }//GEN-LAST:event_tblEstPastaMouseClicked

    private void btnMinimiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimiMouseEntered
        /// quando o mouse está em cima
        this.jPanel6.setBackground(new Color(210, 226, 186));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnMinimiMouseEntered

    private void btnMinimiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimiMouseExited
        // quando o mouse sair de cima
        this.jPanel6.setBackground(new Color(143, 165, 110));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnMinimiMouseExited

    private void btnMinimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimiActionPerformed
        try {
            // minimiza a tela
            this.setIcon(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(TelaCadInsumo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnMinimiActionPerformed

    private void btnFecharMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseEntered
        // quando o mouse está em cima
        this.jPanel1.setBackground(new Color(211, 57, 33));
        this.btnFechar.setForeground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnFecharMouseEntered

    private void btnFecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseExited
        // quando o mouse sair de cima
        this.jPanel1.setBackground(new Color(143, 165, 110));
        this.btnFechar.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnFecharMouseExited

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // chama o metodo sair
        this.dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // passa o foco para o campo de texto 
        this.txtMovEst.requestFocus();
        // quando o mouse sair de cima  
        this.jPanel6.setBackground(new Color(143, 165, 110));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // passa o foco para o campo de texto 
        this.txtMovEst.requestFocus();
        // quando o mouse sair de cima  
        this.jPanel6.setBackground(new Color(143, 165, 110));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameDeiconified


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JComboBox<String> cbPesMovEst;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblEstPasta;
    private javax.swing.JTextField txtMovEst;
    // End of variables declaration//GEN-END:variables
}