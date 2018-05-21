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

/**
 *
 * @author Leandro
 */
public class TelaCadUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCadUsuario
     */
    public TelaCadUsuario() {
        initComponents();

        this.conexao = ModuloConexao.conector();
    }

    private void limparCampos() {
        this.txtCadUsuId.setText(null);
        this.txtCadUsuNome.setText(null);
        this.txtCadUsuLogin.setText(null);
        this.txtCadUsuSenha.setText(null);
        this.cbCadUsuPerfil.setSelectedItem("Administrador");
    }

    public void adicionar() {
        String sql = "insert into tbusuarios(iduser,nome,login,senha,perfil) values(?,?,?,?,?)";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadUsuId.getText());
            this.pst.setString(2, this.txtCadUsuNome.getText());
            this.pst.setString(3, this.txtCadUsuLogin.getText());
            this.pst.setString(4, this.txtCadUsuSenha.getText());
            this.pst.setString(5, this.cbCadUsuPerfil.getSelectedItem().toString());

            //Validação dos campos obirgatórios
            if ((this.txtCadUsuId.getText().isEmpty()) || (this.txtCadUsuNome.getText().isEmpty()) || (this.txtCadUsuLogin.getText().isEmpty()) || (this.txtCadUsuSenha.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {
                //Atualiza a tabela usuarios
                int adicionado = this.pst.executeUpdate();
                //Linha abaixo serve de apoio
                //System.out.println(adicionado);
                //confirma se realmente foi atualizada
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                    limparCampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            //System.out.println(e);
        }

    }

    private void pesquisar() {

        String sql = "select * from tbusuarios where iduser=?";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadUsuId.getText());
            this.rs = this.pst.executeQuery();

            if (rs.next()) {
                this.txtCadUsuNome.setText(this.rs.getString(2));
                this.txtCadUsuLogin.setText(this.rs.getString(3));
                this.txtCadUsuSenha.setText(this.rs.getString(4));
                this.cbCadUsuPerfil.setSelectedItem(this.rs.getString(5));

            } else {
                JOptionPane.showMessageDialog(null, "Digite um [ id ] válido.");
                limparCampos();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    private void atualizar() {
        String sql = "update tbusuarios set nome=?, login=?, senha=?, perfil=? where iduser=?";

        try {
            this.pst = this.conexao.prepareStatement(sql);  
            this.pst.setString(1, this.txtCadUsuNome.getText());
            this.pst.setString(2, this.txtCadUsuLogin.getText());
            this.pst.setString(3, this.txtCadUsuSenha.getText());
            this.pst.setString(4, this.cbCadUsuPerfil.getSelectedItem().toString());
            this.pst.setString(5, this.txtCadUsuId.getText());

            //Validação dos campos obirgatórios
            if ((this.txtCadUsuId.getText().isEmpty()) || (this.txtCadUsuNome.getText().isEmpty()) || (this.txtCadUsuLogin.getText().isEmpty()) || (this.txtCadUsuSenha.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {
                //Atualiza a tabela usuarios
                int adicionado = this.pst.executeUpdate();
                //Linha abaixo serve de apoio
                //System.out.println(adicionado);
                //confirma se realmente foi atualizada
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário Atualizado com sucesso!");
                    limparCampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void deletar() {      
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            String sql = "delete from tbusuarios where iduser=?";

            try {
                this.pst = this.conexao.prepareStatement(sql);
                this.pst.setString(1, this.txtCadUsuId.getText());
                int deleta = pst.executeUpdate();                          
                //confirma o deleta
                if (deleta > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
                    limparCampos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCadUsuNome = new javax.swing.JTextField();
        txtCadUsuLogin = new javax.swing.JTextField();
        txtCadUsuSenha = new javax.swing.JTextField();
        cbCadUsuPerfil = new javax.swing.JComboBox<>();
        btnAdi = new javax.swing.JButton();
        btnAtu = new javax.swing.JButton();
        btnPes = new javax.swing.JButton();
        btnExc = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtCadUsuId = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setPreferredSize(new java.awt.Dimension(860, 560));

        jLabel1.setText("* Nome");

        jLabel2.setText("* Login");

        jLabel3.setText("* Senha");

        jLabel4.setText(" Perfil");

        cbCadUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Usuário" }));

        btnAdi.setText("Adicionar");
        btnAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdiActionPerformed(evt);
            }
        });

        btnAtu.setText("Atualizar");
        btnAtu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtuActionPerformed(evt);
            }
        });

        btnPes.setText("Pesquisar");
        btnPes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesActionPerformed(evt);
            }
        });

        btnExc.setText("Excluir");
        btnExc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcActionPerformed(evt);
            }
        });

        jLabel5.setText("*  id");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtCadUsuLogin)
                        .addComponent(txtCadUsuSenha)
                        .addComponent(txtCadUsuNome, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbCadUsuPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCadUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(btnAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAtu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExc, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCadUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCadUsuNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCadUsuLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCadUsuSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbCadUsuPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdi)
                    .addComponent(btnPes)
                    .addComponent(btnExc)
                    .addComponent(btnAtu))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        setBounds(220, 100, 420, 379);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdiActionPerformed
        // chama o metodo adicionar
        adicionar();
    }//GEN-LAST:event_btnAdiActionPerformed

    private void btnAtuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtuActionPerformed
        // chama o metodo atualizar
        atualizar();
    }//GEN-LAST:event_btnAtuActionPerformed

    private void btnPesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesActionPerformed
        // chama o metodo pesquisar
        pesquisar();
    }//GEN-LAST:event_btnPesActionPerformed

    private void btnExcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcActionPerformed
        // chamad o metodo deletar
        deletar();
    }//GEN-LAST:event_btnExcActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdi;
    private javax.swing.JButton btnAtu;
    private javax.swing.JButton btnExc;
    private javax.swing.JButton btnPes;
    private javax.swing.JComboBox<String> cbCadUsuPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtCadUsuId;
    private javax.swing.JTextField txtCadUsuLogin;
    private javax.swing.JTextField txtCadUsuNome;
    private javax.swing.JTextField txtCadUsuSenha;
    // End of variables declaration//GEN-END:variables
}
