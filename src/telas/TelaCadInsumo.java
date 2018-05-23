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
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Leandro
 */
public class TelaCadInsumo extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCadInsumos
     */
    public TelaCadInsumo() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        addUMComboBox();
    }

    private void limparCampos() {
        this.txtCadInsCodigo.setEnabled(true);
        this.txtCadInsCodigo.setText(null);
        this.txtCadInsDes.setText(null);
        this.cbCadInsUm.setSelectedItem("mg");
        this.txtCadInsQuant.setText(null);
        this.txtCadInsPreco.setText(null);

    }

    public void adicionarInsumos() {

        String sql = "insert into tbinsumos(codigo,descricao,UM,quantidade,preco) values(?,?,?,?,?)";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadInsCodigo.getText());
            this.pst.setString(2, this.txtCadInsDes.getText());
            this.pst.setString(3, this.cbCadInsUm.getSelectedItem().toString());
            this.pst.setString(4, this.txtCadInsQuant.getText());
            this.pst.setString(5, this.txtCadInsPreco.getText());

            //Validação dos campos obirgatórios
            if ((this.txtCadInsCodigo.getText().isEmpty()) || (this.txtCadInsDes.getText().isEmpty()) || (this.txtCadInsQuant.getText().isEmpty()) || (this.txtCadInsPreco.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            } else {
                //Atualiza a tabela insumos
                int adicionado = this.pst.executeUpdate();
                //Linha abaixo serve de apoio
                //System.out.println(adicionado);
                //confirma se realmente foi atualizada
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Insumo cadastrado com sucesso!");
                    limparCampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void pesquisarInsumos() {
        String sql = "select * from tbinsumos where codigo like ?";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadInsPesquisar.getText() + "%");
            this.rs = this.pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            this.tblCadInsumos.setModel(DbUtils.resultSetToTableModel(this.rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //seta os campos do formulário com o coteúdo da tabela
    private void setarCampos() {
        int setar = this.tblCadInsumos.getSelectedRow();
        this.txtCadInsCodigo.setEnabled(false);
        this.txtCadInsCodigo.setText(this.tblCadInsumos.getModel().getValueAt(setar, 0).toString());
        this.txtCadInsDes.setText(this.tblCadInsumos.getModel().getValueAt(setar, 1).toString());
        this.cbCadInsUm.setSelectedItem(this.tblCadInsumos.getModel().getValueAt(setar, 2).toString());
        this.txtCadInsQuant.setText(this.tblCadInsumos.getModel().getValueAt(setar, 3).toString());
        this.txtCadInsPreco.setText(this.tblCadInsumos.getModel().getValueAt(setar, 4).toString());
    }

    public void atualizarInsumos() {
        String sql = "update tbinsumos set descricao=?, UM=?, quantidade=?, preco=? where codigo=?";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadInsDes.getText());
            this.pst.setString(2, this.cbCadInsUm.getSelectedItem().toString());
            this.pst.setString(3, this.txtCadInsQuant.getText());
            this.pst.setString(4, this.txtCadInsPreco.getText());
            this.pst.setString(5, this.txtCadInsCodigo.getText());

            //Validação dos campos obirgatórios
            if ((this.txtCadInsCodigo.getText().isEmpty()) || (this.txtCadInsQuant.getText().isEmpty()) || (this.txtCadInsPreco.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            } else {
                //Atualiza a tabela insumos
                int adicionado = this.pst.executeUpdate();
                //Linha abaixo serve de apoio
                //System.out.println(adicionado);
                //confirma se realmente foi atualizada
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Insumo Atualizado com sucesso!");
                    limparCampos();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void deletarInsumos() {
        if (this.txtCadInsCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um insumo válido.");
        } else {
            int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar este insumo?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {

                String sql = "delete from tbinsumos where codigo=?";

                try {
                    this.pst = this.conexao.prepareStatement(sql);
                    this.pst.setString(1, this.txtCadInsCodigo.getText());
                    int deletado = this.pst.executeUpdate();

                    if (deletado > 0) {
                        JOptionPane.showMessageDialog(null, "Insumo deletado com sucesso!");
                        limparCampos();
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }

    //metodo para adicionar uma nova unidade de medida (UM)
    private void addUM() {
        String adicionar = JOptionPane.showInputDialog("Digite uma nova UM:");
        if (adicionar.equals("")) {
            JOptionPane.showMessageDialog(null, "Digite uma UM válida.");
        } else {
            String sql = "insert into tbUM(UM) values('" + adicionar + "')";

            try {
                this.pst = this.conexao.prepareStatement(sql);
                int adicionado = this.pst.executeUpdate();
                this.cbCadInsUm.addItem(adicionar);
                this.cbCadInsUm.updateUI();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "UM adicionada.");
                }
            } catch (java.sql.SQLException e2) {
                JOptionPane.showMessageDialog(null, "UM já existe.");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                //System.out.println(e);
            }
        }
    }

    //metodo para remover uma nova unidade de medida (UM)
    private void removerUM() {
        String adicionar = JOptionPane.showInputDialog("Digite uma UM para ser apagada:");

        String sql = "delete from tbUM where UM='" + adicionar + "'";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            int deletado = this.pst.executeUpdate();
            if (deletado > 0) {
                this.cbCadInsUm.removeItem(adicionar);
                this.cbCadInsUm.updateUI();
                JOptionPane.showMessageDialog(null, "UM apagada.");

            } else {
                JOptionPane.showMessageDialog(null, "UM inválida.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    //Adiciona as UM do bando no combo box
    private void addUMComboBox() {
        String sql = "Select * from tbUM";
        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                this.cbCadInsUm.addItem(this.rs.getString(1));
            }
            this.cbCadInsUm.updateUI();
        } catch (Exception e) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCadInsumos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtCadInsPesquisar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtCadInsPreco = new javax.swing.JTextField();
        btnCadInsAdicionar = new javax.swing.JButton();
        txtCadInsCodigo = new javax.swing.JTextField();
        btnCadInsAtualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnCadInsDeletar = new javax.swing.JButton();
        txtCadInsDes = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCadInsQuant = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbCadInsUm = new javax.swing.JComboBox<>();
        btnCadInsLimpar = new javax.swing.JButton();
        btnCadInsAddUm = new javax.swing.JButton();
        btnCadInsRemoverUm = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Insumos");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblCadInsumos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblCadInsumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Código", "Descrição", "UM", "Quantidade", "Preço"
            }
        ));
        tblCadInsumos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadInsumosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCadInsumos);

        jLabel6.setText("Código");

        txtCadInsPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCadInsPesquisarKeyReleased(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(txtCadInsPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCadInsPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnCadInsAdicionar.setText("Adicionar");
        btnCadInsAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsAdicionarActionPerformed(evt);
            }
        });

        btnCadInsAtualizar.setText("Atualizar");
        btnCadInsAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsAtualizarActionPerformed(evt);
            }
        });

        jLabel2.setText("Descrição");

        btnCadInsDeletar.setText("Deletar");
        btnCadInsDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsDeletarActionPerformed(evt);
            }
        });

        jLabel3.setText("Unidade de Medida (UM)");

        jLabel4.setText("Quantidade");

        jLabel5.setText("Preço");

        jLabel1.setText("Código");

        btnCadInsLimpar.setText("Limpar");
        btnCadInsLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsLimparActionPerformed(evt);
            }
        });

        btnCadInsAddUm.setText("+");
        btnCadInsAddUm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsAddUmActionPerformed(evt);
            }
        });

        btnCadInsRemoverUm.setText("-");
        btnCadInsRemoverUm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsRemoverUmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtCadInsCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(txtCadInsDes))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(160, 160, 160)
                                .addComponent(jLabel2)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbCadInsUm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCadInsAddUm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCadInsRemoverUm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtCadInsQuant, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(txtCadInsPreco, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel4))))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(btnCadInsAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCadInsAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCadInsDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCadInsLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCadInsDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCadInsQuant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCadInsPreco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCadInsCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbCadInsUm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCadInsAddUm)
                            .addComponent(btnCadInsRemoverUm))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCadInsAdicionar)
                    .addComponent(btnCadInsAtualizar)
                    .addComponent(btnCadInsDeletar)
                    .addComponent(btnCadInsLimpar))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 267, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadInsAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsAdicionarActionPerformed
        // chama o metodo adicionar
        adicionarInsumos();
    }//GEN-LAST:event_btnCadInsAdicionarActionPerformed

    // evento do tipo "enquanto digita"
    private void txtCadInsPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadInsPesquisarKeyReleased
        // chama o metodo pesquisar
        pesquisarInsumos();
    }//GEN-LAST:event_txtCadInsPesquisarKeyReleased
    //evento do tipo "clicar com o mouse"
    private void tblCadInsumosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadInsumosMouseClicked
        // chama o metodo setarCampos
        setarCampos();
    }//GEN-LAST:event_tblCadInsumosMouseClicked

    private void btnCadInsAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsAtualizarActionPerformed
        // chama o metodo atualizar
        atualizarInsumos();
    }//GEN-LAST:event_btnCadInsAtualizarActionPerformed

    private void btnCadInsDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsDeletarActionPerformed
        // chama o metodo deletar
        deletarInsumos();
    }//GEN-LAST:event_btnCadInsDeletarActionPerformed

    private void btnCadInsLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsLimparActionPerformed
        // chama o metodo limpar    
        limparCampos();
    }//GEN-LAST:event_btnCadInsLimparActionPerformed

    private void btnCadInsAddUmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsAddUmActionPerformed
        // chama o metodo adicionar UM
        addUM();
    }//GEN-LAST:event_btnCadInsAddUmActionPerformed

    private void btnCadInsRemoverUmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsRemoverUmActionPerformed
        // chama o metodo remover UM
        removerUM();
    }//GEN-LAST:event_btnCadInsRemoverUmActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadInsAddUm;
    private javax.swing.JButton btnCadInsAdicionar;
    private javax.swing.JButton btnCadInsAtualizar;
    private javax.swing.JButton btnCadInsDeletar;
    private javax.swing.JButton btnCadInsLimpar;
    private javax.swing.JButton btnCadInsRemoverUm;
    private javax.swing.JComboBox<String> cbCadInsUm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCadInsumos;
    private javax.swing.JTextField txtCadInsCodigo;
    private javax.swing.JTextField txtCadInsDes;
    private javax.swing.JTextField txtCadInsPesquisar;
    private javax.swing.JTextField txtCadInsPreco;
    private javax.swing.JTextField txtCadInsQuant;
    // End of variables declaration//GEN-END:variables
}
