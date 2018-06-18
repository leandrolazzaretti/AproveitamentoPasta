/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
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
    public static boolean confirmarEscolha;
    Util frame = new Util();

    /**
     * Creates new form TelaPesquisarReceita
     */
    public TelaPesquisarReceita() {
        initComponents();

        this.conexao = ModuloConexao.conector();
        pesquisarReceita();
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
        String sql = "select i.descricao, ri.consumo from tbreceita as r "
                + "inner join tbReceitaInsumo as ri on ri.codigoReceita = r.codigorec "
                + "inner join tbinsumos as i on i.codigo = ri.codigoInsumo "
                + "where r.codigorec ='" + codigoRec + "'";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) TelaCadReceita.tblCadRecComponentes.getModel();
        modelo.setNumRows(0);

        this.tblPesquisarReceita.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.tblPesquisarReceita.getColumnModel().getColumn(1).setPreferredWidth(40);

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar            
            while (rs.next()) {

                String comp = rs.getString(1);
                String cons = rs.getString(2);

                modelo.addRow(new Object[]{comp, cons});
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
        this.codRecIns = Integer.parseInt(TelaCadReceita.txtCadRecCodigo.getText());
        setarTbComponentes(this.codRecIns);
    }

    //seta os campos na tela de movimentação de estoque
    private void setarCampos2() {
        int setar = this.tblPesquisarReceita.getSelectedRow();
        TelaMovimentacaoEstoque.txtCodigo.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 0).toString());
        TelaMovimentacaoEstoque.txtDescricao.setText(this.tblPesquisarReceita.getModel().getValueAt(setar, 1).toString());
        TelaMovimentacaoEstoque.txtCodigo.setEnabled(false);
        TelaMovimentacaoEstoque.txtEstQuantidade.setEnabled(true);
        TelaMovimentacaoEstoque.txtEstData.setEnabled(true);
    }

    private void setarCampos3(int codigoRec) {

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

        setClosable(true);
        setIconifiable(true);
        setTitle("Pesquisar Receita");
        setToolTipText("");
        setMaximumSize(null);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
        });

        jLabel6.setText("Pesquisar por:");

        txtPesquisarReceita.setVerifyInputWhenFocusTarget(false);
        txtPesquisarReceita.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarReceitaKeyReleased(evt);
            }
        });

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

        cbPesquisarReceita.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Descrição", "Pantone", "Tipo", "Validade" }));
        cbPesquisarReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPesquisarReceitaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(cbPesquisarReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPesquisarReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbPesquisarReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(txtPesquisarReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 96, 860, 367);
    }// </editor-fold>//GEN-END:initComponents

    private void tblPesquisarReceitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisarReceitaMouseClicked
        // seta os campos do formulário através da tabela
        //TelaCadReceita.       
        if (evt.getClickCount() > 1) {
            if (this.confirmarEscolha == true) {
                setarCampos();
            } else {
                setarCampos2();
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

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        // Chama o metodo para bloquear a movimentação do frame
        if (this.confirmarEscolha == true) {
            this.frame.bloquearMovimentacao(TelaCadReceita.framePesReceita, 0, 96);
        } else {
            this.frame.bloquearMovimentacao(TelaMovimentacaoEstoque.framePesReceita, 0, 96);
        }
    }//GEN-LAST:event_formComponentMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbPesquisarReceita;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable tblPesquisarReceita;
    private javax.swing.JTextField txtPesquisarReceita;
    // End of variables declaration//GEN-END:variables
}
