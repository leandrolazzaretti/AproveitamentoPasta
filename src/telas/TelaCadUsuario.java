/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import dao.UsuarioDao;
import dto.UsuarioDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Leandro
 */
public class TelaCadUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    UsuarioDao lista = new UsuarioDao();
    private int index;

    /**
     * Creates new form TelaCadUsuario
     */
    public TelaCadUsuario() {
        initComponents();

        this.conexao = ModuloConexao.conector();
        mostarUsuario();
    }

    //limpa todos os campos da tela
    private void limparCampos() {
        this.txtCadUsuId.setText(null);
        this.txtCadUsuNome.setText(null);
        this.txtCadUsuLogin.setText(null);
        this.txtCadUsuSenha.setText(null);
        this.cbCadUsuPerfil.setSelectedItem("Administrador");
    }

    // adiciona um novo usuario
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
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
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

    //mostra todas as informações de um usuario nos campos da tela
    private void mostarUsuario() {

        String sql = "select * from tbusuarios";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.rs = this.pst.executeQuery();

            if (rs.next()) {
                this.txtCadUsuId.setText(this.rs.getString(1));
                this.txtCadUsuNome.setText(this.rs.getString(2));
                this.txtCadUsuLogin.setText(this.rs.getString(3));
                this.txtCadUsuSenha.setText(this.rs.getString(4));
                this.cbCadUsuPerfil.setSelectedItem(this.rs.getString(5));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            //System.out.println(e);
        }
    }

    // atualiza o cadastro de um usuário
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
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
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

    //Deleta um usuário
    private void deletar() {
        if (this.txtCadUsuId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário válido.");
        } else {

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
    }

    private void setarCampos(UsuarioDto user) {
        this.txtCadUsuId.setText(String.valueOf(user.getIduser()));
        this.txtCadUsuNome.setText(user.getNome());
        this.txtCadUsuLogin.setText(user.getLogin());
        this.txtCadUsuSenha.setText(user.getSenha());
        this.cbCadUsuPerfil.setSelectedItem(user.getPerfil());
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
        btnExc = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtCadUsuId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnLimpar = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnProximo = new javax.swing.JButton();
        btnPrimeiro = new javax.swing.JButton();
        btnUltimo = new javax.swing.JButton();
        btnPesquisar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Usuários");
        setPreferredSize(new java.awt.Dimension(420, 406));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText(" Nome:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 127, -1, -1));

        jLabel2.setText(" Login:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 165, -1, -1));

        jLabel3.setText(" Senha:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 203, -1, -1));

        jLabel4.setText(" Perfil:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 241, -1, -1));
        getContentPane().add(txtCadUsuNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 124, 213, -1));
        getContentPane().add(txtCadUsuLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 162, 213, -1));
        getContentPane().add(txtCadUsuSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 200, 213, -1));

        cbCadUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Usuário" }));
        getContentPane().add(cbCadUsuPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 238, 213, -1));

        btnAdi.setText("Adicionar");
        btnAdi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdiActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdi, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 288, -1, -1));

        btnAtu.setText("Atualizar");
        btnAtu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAtu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtuActionPerformed(evt);
            }
        });
        getContentPane().add(btnAtu, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 288, 79, -1));

        btnExc.setText("Deletar");
        btnExc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcActionPerformed(evt);
            }
        });
        getContentPane().add(btnExc, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 288, 73, -1));

        jLabel5.setText(" Código:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 89, -1, -1));
        getContentPane().add(txtCadUsuId, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 86, 150, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Cadastro de Usuários");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        btnLimpar.setText("Limpar");
        btnLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(286, 288, 79, -1));

        btnAnterior.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnterior.setText("<");
        btnAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, -1, -1));

        btnProximo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnProximo.setText(">");
        btnProximo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProximoActionPerformed(evt);
            }
        });
        getContentPane().add(btnProximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 330, -1, -1));

        btnPrimeiro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrimeiro.setText("<<");
        btnPrimeiro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrimeiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrimeiroActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrimeiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        btnUltimo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUltimo.setText(">>");
        btnUltimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUltimoActionPerformed(evt);
            }
        });
        getContentPane().add(btnUltimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 330, -1, -1));

        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnPesquisar.setToolTipText("Pesquisar");
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });
        getContentPane().add(btnPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 35, 34));

        setBounds(220, 77, 420, 406);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdiActionPerformed
        // chama o metodo adicionar
        adicionar();
    }//GEN-LAST:event_btnAdiActionPerformed

    private void btnAtuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtuActionPerformed
        // chama o metodo atualizar
        atualizar();
    }//GEN-LAST:event_btnAtuActionPerformed

    private void btnExcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcActionPerformed
        // chamad o metodo deletar
        deletar();
    }//GEN-LAST:event_btnExcActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // chama o metodo limpar
        limparCampos();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        // registro anterior
        this.index--;
        if (index >= 0) {
            setarCampos(this.lista.list().get(index));
            this.btnProximo.setEnabled(true);
            this.btnUltimo.setEnabled(true);
        } else {
            this.index = 0;
            setarCampos(this.lista.list().get(index));
            this.btnAnterior.setEnabled(false);
            this.btnPrimeiro.setEnabled(false);
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProximoActionPerformed
        // proximo registro
        this.index++;
        if (index <= ) {
            setarCampos(this.lista.list().get(index));
            this.btnAnterior.setEnabled(true);
            this.btnPrimeiro.setEnabled(true);
        } else {
            this.btnProximo.setEnabled(false);
            this.btnUltimo.setEnabled(false);
        }
    }//GEN-LAST:event_btnProximoActionPerformed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        // TODO add your handling code here:
        new TelaProcurarUsuarios().setVisible(true);
        //painel.setVisible(true);
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void btnPrimeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrimeiroActionPerformed
        // primeiro registro
        this.index = 0;
        if (index == 0) {
            setarCampos(this.lista.list().get(index));
            this.btnProximo.setEnabled(true);
            this.btnUltimo.setEnabled(true);
        } else {           
            this.btnPrimeiro.setEnabled(false);
        }
        setarCampos(this.lista.list().get(index));
    }//GEN-LAST:event_btnPrimeiroActionPerformed

    private void btnUltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUltimoActionPerformed
//        // ultimo registro
//        this.index = 
//        setarCampos(this.lista.list().get(index));
    }//GEN-LAST:event_btnUltimoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdi;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnAtu;
    private javax.swing.JButton btnExc;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnPrimeiro;
    private javax.swing.JButton btnProximo;
    private javax.swing.JButton btnUltimo;
    private javax.swing.JComboBox<String> cbCadUsuPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtCadUsuId;
    private javax.swing.JTextField txtCadUsuLogin;
    private javax.swing.JTextField txtCadUsuNome;
    private javax.swing.JTextField txtCadUsuSenha;
    // End of variables declaration//GEN-END:variables
}
