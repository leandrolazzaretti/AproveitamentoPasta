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
public class TelaCadReceita extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCadReceita
     */
    public TelaCadReceita() {
        initComponents();
        this.conexao = ModuloConexao.conector();
    }

    private void limparCampos() {
        this.txtCadRecCodigo.setEnabled(true);
        this.txtCadRecCodigo.setText(null);
        this.txtCadRecDes.setText(null);
        this.txtCadRecPan.setText(null);
        this.txtCadRecTipo.setText(null);
        this.txtCadRecVal.setText(null);

    }

    public void adicionarReceita() {

        String sql = "insert into tbreceita(codigorec,descricao,pantone,tipodepasta,datavencimento) values(?,?,?,?,?)";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadRecCodigo.getText());
            this.pst.setString(2, this.txtCadRecDes.getText());
            this.pst.setString(3, this.txtCadRecPan.getText());
            this.pst.setString(4, this.txtCadRecTipo.getText());
            this.pst.setString(5, this.txtCadRecVal.getText());

            //Validação dos campos obirgatórios
            if ((this.txtCadRecCodigo.getText().isEmpty()) || (this.txtCadRecDes.getText().isEmpty()) || (this.txtCadRecPan.getText().isEmpty()) || (this.txtCadRecTipo.getText().isEmpty()) || (this.txtCadRecVal.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            } else {
                //Atualiza a tabela receita
                int adicionado = this.pst.executeUpdate();
                //Linha abaixo serve de apoio
                //System.out.println(adicionado);
                //confirma se realmente foi atualizada
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Receita cadastrada com sucesso!");
                    limparCampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void pesquisarReceita() {
        String sql = "select * from tbreceita where codigorec like ?";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadRecPesquisar.getText() + "%");
            this.rs = this.pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            this.tblCadReceita.setModel(DbUtils.resultSetToTableModel(this.rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //seta os campos do formulário com o coteúdo da tabela
    private void setarCampos() {
        int setar = this.tblCadReceita.getSelectedRow();
        this.txtCadRecCodigo.setEnabled(false);
        this.txtCadRecCodigo.setText(this.tblCadReceita.getModel().getValueAt(setar, 0).toString());
        this.txtCadRecDes.setText(this.tblCadReceita.getModel().getValueAt(setar, 1).toString());
        this.txtCadRecPan.setText(this.tblCadReceita.getModel().getValueAt(setar, 2).toString());
        this.txtCadRecTipo.setText(this.tblCadReceita.getModel().getValueAt(setar, 3).toString());
        this.txtCadRecVal.setText(this.tblCadReceita.getModel().getValueAt(setar, 4).toString());
    }
    
    public void atualizarReceita() {
        String sql = "update tbreceita set descricao=?, pantone=?, tipodepasta=?, datavencimento=? where codigorec=?";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadRecDes.getText());
            this.pst.setString(2, this.txtCadRecPan.getText());
            this.pst.setString(3, this.txtCadRecTipo.getText());
            this.pst.setString(4, this.txtCadRecVal.getText());
            this.pst.setString(5, this.txtCadRecCodigo.getText());

            //Validação dos campos obirgatórios
            if ((this.txtCadRecCodigo.getText().isEmpty()) || (this.txtCadRecDes.getText().isEmpty()) || (this.txtCadRecPan.getText().isEmpty()) || (this.txtCadRecTipo.getText().isEmpty()) || (this.txtCadRecVal.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            } else {
                //Atualiza a tabela Receita
                int adicionado = this.pst.executeUpdate();
                //Linha abaixo serve de apoio
                //System.out.println(adicionado);
                //confirma se realmente foi atualizada
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Receita Atualizado com sucesso!");
                    limparCampos();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void deletarReceita() {
        if (this.txtCadRecCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione uma receita válida.");
        } else {
            int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar esta receita?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {

                String sql = "delete from tbreceita where codigorec=?";

                try {
                    this.pst = this.conexao.prepareStatement(sql);
                    this.pst.setString(1, this.txtCadRecCodigo.getText());
                    int deletado = this.pst.executeUpdate();

                    if (deletado > 0) {
                        JOptionPane.showMessageDialog(null, "Receita deletada com sucesso!");
                        limparCampos();
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCadRecDes = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCadRecTipo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCadRecVal = new javax.swing.JTextField();
        btnCadRecAdicionar = new javax.swing.JButton();
        btnCadRecAtualizar = new javax.swing.JButton();
        btnCadRecDeletar = new javax.swing.JButton();
        btnCadRecLimpar = new javax.swing.JButton();
        txtCadRecCodigo = new javax.swing.JTextField();
        txtCadRecPan = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCadRecComponentes = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCadReceita = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtCadRecPesquisar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Receitas");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 225));

        jLabel1.setText("Código");

        jLabel2.setText("Pantone");

        jLabel3.setText("Tipo de pasta");

        jLabel4.setText("Descrição");

        jLabel5.setText("Validade");

        btnCadRecAdicionar.setText("Adicionar");
        btnCadRecAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecAdicionarActionPerformed(evt);
            }
        });

        btnCadRecAtualizar.setText("Atualizar");
        btnCadRecAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecAtualizarActionPerformed(evt);
            }
        });

        btnCadRecDeletar.setText("Deletar");
        btnCadRecDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecDeletarActionPerformed(evt);
            }
        });

        btnCadRecLimpar.setText("Limpar");
        btnCadRecLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecLimparActionPerformed(evt);
            }
        });

        jLabel8.setText("Componentes");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tblCadRecComponentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Insumos"
            }
        ));
        jScrollPane2.setViewportView(tblCadRecComponentes);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jLabel1)
                            .addGap(127, 127, 127)
                            .addComponent(jLabel4)
                            .addGap(317, 317, 317)
                            .addComponent(jLabel8))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(txtCadRecCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(38, 38, 38)
                            .addComponent(txtCadRecDes, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(33, 33, 33)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(jLabel2))
                                    .addComponent(txtCadRecPan, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txtCadRecTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel5))
                                    .addComponent(txtCadRecVal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(btnCadRecAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCadRecAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCadRecDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCadRecLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8))))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtCadRecCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCadRecDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCadRecPan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(6, 6, 6)
                                .addComponent(txtCadRecTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(6, 6, 6)
                                .addComponent(txtCadRecVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCadRecAdicionar)
                            .addComponent(btnCadRecAtualizar)
                            .addComponent(btnCadRecDeletar)
                            .addComponent(btnCadRecLimpar))
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 830, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblCadReceita.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblCadReceita.setModel(new javax.swing.table.DefaultTableModel(
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
                "Código", "Descrição", "Pantone", "Tipo", "Validade"
            }
        ));
        tblCadReceita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadReceitaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCadReceita);

        jLabel6.setText("Código");

        txtCadRecPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCadRecPesquisarKeyReleased(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(txtCadRecPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCadRecPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 830, 250));

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

    private void tblCadReceitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadReceitaMouseClicked
        // chama o metodo setarCampos
        setarCampos();
    }//GEN-LAST:event_tblCadReceitaMouseClicked

    private void txtCadRecPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadRecPesquisarKeyReleased
        // chama o metodo pesquisar
        pesquisarReceita();
    }//GEN-LAST:event_txtCadRecPesquisarKeyReleased

    private void btnCadRecAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecAdicionarActionPerformed
        // chama o metodo adicionar
        adicionarReceita();
    }//GEN-LAST:event_btnCadRecAdicionarActionPerformed

    private void btnCadRecLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecLimparActionPerformed
        // chama o metodo limpar campos
        limparCampos();
    }//GEN-LAST:event_btnCadRecLimparActionPerformed

    private void btnCadRecAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecAtualizarActionPerformed
        // cahama o metodo atualizar
        atualizarReceita();
    }//GEN-LAST:event_btnCadRecAtualizarActionPerformed

    private void btnCadRecDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecDeletarActionPerformed
        // chama o metodo deletar
        deletarReceita();
    }//GEN-LAST:event_btnCadRecDeletarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadRecAdicionar;
    private javax.swing.JButton btnCadRecAtualizar;
    private javax.swing.JButton btnCadRecDeletar;
    private javax.swing.JButton btnCadRecLimpar;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblCadRecComponentes;
    private javax.swing.JTable tblCadReceita;
    private javax.swing.JTextField txtCadRecCodigo;
    private javax.swing.JTextField txtCadRecDes;
    private javax.swing.JTextField txtCadRecPan;
    private javax.swing.JTextField txtCadRecPesquisar;
    private javax.swing.JTextField txtCadRecTipo;
    private javax.swing.JTextField txtCadRecVal;
    // End of variables declaration//GEN-END:variables
}
