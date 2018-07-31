/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import dao.ReceitaInsumoDao;
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
public class TelaPesquisarReceita extends javax.swing.JInternalFrame {

    Connection conexao = null;

    private String cbPesquisar = "codigorec";
    public int codRecIns = 0;
    public int codIns = 0;
    public static int confirmarEscolha;
    Util util = new Util();

    /**
     * Creates new form TelaPesquisarReceita
     */
    public TelaPesquisarReceita() {
        initComponents();

        this.conexao = ModuloConexao.conector();
        pesquisarReceita();
        retirarBordas();
        this.tblPesquisarReceita.getTableHeader().setReorderingAllowed(false);
    }

    // metodo para retirar as bordas do JinternalFrame
    private void retirarBordas() {
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null); //retirar o painel superior
        this.setBorder(null);//retirar bordas
    }

    public void pesquisarReceita() {
        String sql = "select r.codigorec, r.descricao, r.pantone, t.descricao, r.datavencimento "
                + "from tbreceita as r "
                + "inner join tbTipoPasta as t on r.codigoTipoPasta = t.codigo "
                + "where " + this.cbPesquisar + " like ?";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) this.tblPesquisarReceita.getModel();
        modelo.setNumRows(0);

        this.tblPesquisarReceita.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblPesquisarReceita.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblPesquisarReceita.getColumnModel().getColumn(2).setPreferredWidth(40);
        this.tblPesquisarReceita.getColumnModel().getColumn(3).setPreferredWidth(40);
        this.tblPesquisarReceita.getColumnModel().getColumn(4).setPreferredWidth(40);

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtPesquisarReceita.getText() + "%");
            ResultSet rs = pst.executeQuery();
            this.codRecIns = rs.getInt(1);
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)});
            }
            pst.close();
        } catch (java.sql.SQLException ex) {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //seta a tabela tblCadRecComponentes com os dados do banco
    public void setarTbComponentes(int codigoRec) {
        String sql = "select ri.codigoInsumo , i.descricao, ri.consumo from tbreceita as r "
                + "inner join tbReceitaInsumo as ri on ri.codigoReceita = r.codigorec "
                + "inner join tbinsumos as i on i.codigo = ri.codigoInsumo "
                + "where r.codigorec ='" + codigoRec + "'";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) TelaCadReceita.tblCadRecComponentes.getModel();
        modelo.setNumRows(0);

        this.tblPesquisarReceita.getColumnModel().getColumn(0).setPreferredWidth(40);
        this.tblPesquisarReceita.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblPesquisarReceita.getColumnModel().getColumn(2).setPreferredWidth(40);

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar            
            while (rs.next()) {

                int codg = rs.getInt(1);
                String comp = rs.getString(2);
                String cons = this.util.formatadorQuant3(rs.getDouble(3));
                modelo.addRow(new Object[]{codg, comp, cons});
                ReceitaInsumoDao.consumoTotal2.add(rs.getDouble(3));
                ReceitaInsumoDao.consumoTotal3.add(rs.getDouble(3));
                ReceitaInsumoDao.consumoTotal += rs.getDouble(3);
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            //System.out.println(e);
        }
    }

    //seta os campos na tela de cadastro de receita
    public void setarCampos() {

        int setar = this.tblPesquisarReceita.getSelectedRow();
        TelaCadReceita.txtCadRecCodigo.setEnabled(false);
        TelaCadReceita.txtCadRecCodigo.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 0).toString());
        TelaCadReceita.txtCadRecDes.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 1).toString());
        TelaCadReceita.txtCadRecPan.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 2).toString());
        TelaCadReceita.cbCadReceitaTipo.setSelectedItem(this.tblPesquisarReceita.getModel().getValueAt(setar, 3).toString());
        TelaCadReceita.txtCadRecVal.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 4).toString());
        
        TelaCadReceita.txtCadRecComponentesCodigo.setEnabled(true);
        TelaCadReceita.txtCadRecComponentesCodigo.requestFocus();
        TelaCadReceita.txtCadRecConsumo.setEnabled(true);
        TelaCadReceita.btnInsumoPesquisar.setEnabled(true);
        TelaCadReceita.btnAddConsumo.setEnabled(true);
        TelaCadReceita.tblCadRecComponentes.setVisible(true);
        
        this.codRecIns = Integer.parseInt(TelaCadReceita.txtCadRecCodigo.getText());
        setarTbComponentes(this.codRecIns);
        
    }

    //seta os campos na tela de movimentação de estoque
    public void setarCampos2(){
        int setar = this.tblPesquisarReceita.getSelectedRow();
        TelaMovimentacaoEstoque.txtCodigo.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 0).toString());
        TelaMovimentacaoEstoque.txtDescricao.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 1).toString());
        TelaMovimentacaoEstoque.txtCodigo.setEnabled(false);
        TelaMovimentacaoEstoque.txtEstQuantidade.setEnabled(true);
        TelaMovimentacaoEstoque.txtEstData.setEnabled(true);
        TelaMovimentacaoEstoque.txtEstQuantidade.requestFocus();
    }
    
    //seta os campos na tela de Produção Pasta
    public void setarCampos3(){
        int setar = this.tblPesquisarReceita.getSelectedRow();
         TelaEstoquePasta.txtCodigo.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 0).toString());
         TelaEstoquePasta.txtCodigo.setEnabled(false);
         TelaEstoquePasta.txtQuantidade.setEnabled(true);
         TelaEstoquePasta.txtQuantidade.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        txtPesquisarReceita = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPesquisarReceita = new javax.swing.JTable();
        cbPesquisarReceita = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(143, 165, 110));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(122, 122, 122)));
        setClosable(true);
        setIconifiable(true);
        setTitle("Pesquisar Receita");
        setToolTipText("");
        setMaximumSize(null);
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

        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Pesquisar por:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        txtPesquisarReceita.setVerifyInputWhenFocusTarget(false);
        txtPesquisarReceita.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarReceitaKeyReleased(evt);
            }
        });
        getContentPane().add(txtPesquisarReceita, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 216, -1));

        tblPesquisarReceita.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblPesquisarReceita.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descrição", "Pantone", "Tipo", "Validade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPesquisarReceita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesquisarReceitaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPesquisarReceita);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 838, 190));

        cbPesquisarReceita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Descrição", "Pantone", "Tipo", "Validade" }));
        cbPesquisarReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPesquisarReceitaActionPerformed(evt);
            }
        });
        getContentPane().add(cbPesquisarReceita, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 153, -1));

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Tabela de Receitas");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, -1));

        setBounds(0, 89, 858, 316);
    }// </editor-fold>//GEN-END:initComponents

    private void tblPesquisarReceitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisarReceitaMouseClicked
        // seta os campos do formulário através da tabela
        //TelaCadReceita.       

        if (evt.getClickCount() > 1) {
            if (this.confirmarEscolha == 1) {
                setarCampos();
            } else {
                if (this.confirmarEscolha == 2) {
                    setarCampos2();
                    
                } else {
                    setarCampos3();
                }
            }
            this.dispose();
        }
    }//GEN-LAST:event_tblPesquisarReceitaMouseClicked

    private void txtPesquisarReceitaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarReceitaKeyReleased
        // chama o metodo pesquisar
        pesquisarReceita();
    }//GEN-LAST:event_txtPesquisarReceitaKeyReleased

    private void cbPesquisarReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPesquisarReceitaActionPerformed
        // adiciona um valor a variável cbPesquizar
        if (this.cbPesquisarReceita.getSelectedItem().equals("Código")) {
            this.cbPesquisar = "r.codigorec";
        } else {
            if (this.cbPesquisarReceita.getSelectedItem().equals("Descrição")) {
                this.cbPesquisar = "r.descricao";
            } else {
                if (this.cbPesquisarReceita.getSelectedItem().equals("Pantone")) {
                    this.cbPesquisar = "r.pantone";
                } else {
                    if (this.cbPesquisarReceita.getSelectedItem().equals("Tipo")) {
                        this.cbPesquisar = "t.descricao";
                    } else {
                        this.cbPesquisar = "r.datavencimento";
                    }
                }
            }
        }
    }//GEN-LAST:event_cbPesquisarReceitaActionPerformed

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
        this.txtPesquisarReceita.requestFocus();
        // quando o mouse sair de cima  
        this.jPanel6.setBackground(new Color(143, 165, 110));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // passa o foco para o campo de texto 
        this.txtPesquisarReceita.requestFocus();
        // quando o mouse sair de cima  
        this.jPanel6.setBackground(new Color(143, 165, 110));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameDeiconified


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JComboBox<String> cbPesquisarReceita;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable tblPesquisarReceita;
    private javax.swing.JTextField txtPesquisarReceita;
    // End of variables declaration//GEN-END:variables
}
