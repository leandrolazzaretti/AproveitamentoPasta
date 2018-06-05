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
import java.sql.SQLException;
import javax.swing.JOptionPane;
import util.HashUtil;

/**
 *
 * @author Leandro
 */
public class TelaCadUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;

    UsuarioDao lista = new UsuarioDao();
    private int index = -1;
    private int codigo;

    /**
     * Creates new form TelaCadUsuario
     */
    public TelaCadUsuario() {
        initComponents();

        this.conexao = ModuloConexao.conector();
        //this.txtCadUsuId.setDocument(new SoNumeros());

        setarCodigo();
    }

    // adiciona um novo usuario
    public void adicionar() {
        String sql = "insert into tbusuarios(codigo,nome,login,senha,perfil) values(?,?,?,?,?)";

        PreparedStatement pst;
        try {

            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtCadUsuId.getText());
            pst.setString(2, this.txtCadUsuNome.getText());
            pst.setString(3, this.txtCadUsuLogin.getText());
            pst.setString(4, HashUtil.stringMD5(this.txtCadUsuSenha.getText()));
            pst.setString(5, this.cbCadUsuPerfil.getSelectedItem().toString());

            //Atualiza a tabela usuarios                       
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                limparCampos();
            }
            pst.close();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, sqle);
            System.out.println(sqle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }
    // atualiza o cadastro de um usuário

    private void atualizar() {
        String sql = "update tbusuarios set nome=?, login=?, senha=?, perfil=? where codigo=?";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtCadUsuNome.getText());
            pst.setString(2, this.txtCadUsuLogin.getText());
            pst.setString(3, HashUtil.stringMD5(this.txtCadUsuSenha.getText()));
            pst.setString(4, this.cbCadUsuPerfil.getSelectedItem().toString());
            pst.setString(5, this.txtCadUsuId.getText());

            //Atualiza a tabela usuarios
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Usuário Atualizado com sucesso!");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Deleta um usuário
    private void deletar() {
        if ((this.txtCadUsuId.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Código inválido.");
        } else {

            int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                String sql = "delete from tbusuarios where codigo=?";

                PreparedStatement pst;

                try {
                    pst = this.conexao.prepareStatement(sql);
                    pst.setString(1, this.txtCadUsuId.getText());
                    int deleta = pst.executeUpdate();
                    //confirma o deleta
                    if (deleta > 0) {
                        JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
                        limparCampos();
                    }
                    pst.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }
            }
        }
    }

    //verifica se existem campos em branco
    private boolean verificaCampos() {
        if ((this.txtCadUsuId.getText().isEmpty()) || (this.txtCadUsuNome.getText().isEmpty()) || (this.txtCadUsuLogin.getText().isEmpty()) || (this.txtCadUsuSenha.getText().isEmpty()) || (this.txtCadUsuConfirmarSenha.getText().isEmpty())) {
            return false;
        } else {
            return true;
        }
    }

    //confirma se o login já existe
    private boolean confirmaLogin(String login) {
        String sql = "select count (login) as total from tbusuarios where login ='" + login + "';";

        PreparedStatement pst;
        int total = 0;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            total = Integer.parseInt(rs.getString(1));

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        if (total > 0) {
            return false;
        } else {
            return true;
        }

    }
    
      //confirma se o login já existe
    private boolean confirmaCodigo(String codigo) {
        String sql = "select count (codigo) as total from tbusuarios where codigo ='" + codigo + "';";

        PreparedStatement pst;
        int total = 0;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            total = Integer.parseInt(rs.getString(1));

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return total <=0;
    }

    //seta os campos através da lista
    private void setarCamposLista(UsuarioDto user) {
        this.txtCadUsuId.setEnabled(false);
        this.txtCadUsuId.setText(String.valueOf(user.getIduser()));
        this.txtCadUsuNome.setText(user.getNome());
        this.txtCadUsuLogin.setText(user.getLogin());
        this.cbCadUsuPerfil.setSelectedItem(user.getPerfil());
    }

    //seta o código do usuário
    private void setarCodigo() {
        String sql = "select max(codigo)+1 from tbusuarios";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            this.txtCadUsuId.setText(rs.getString(1));

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        this.txtCadUsuId.setEnabled(false);
    }

    //limpa todos os campos da tela
    private void limparCampos() {
        this.txtCadUsuId.setEnabled(true);
        this.txtCadUsuLogin.setEnabled(true);
        this.txtCadUsuId.setText(null);
        this.txtCadUsuNome.setText(null);
        this.txtCadUsuLogin.setText(null);
        this.txtCadUsuSenha.setText(null);
        this.txtCadUsuConfirmarSenha.setText(null);
        this.cbCadUsuPerfil.setSelectedItem("Administrador");
        this.btnProximo.setEnabled(true);
        this.btnUltimo.setEnabled(true);
        this.btnPrimeiro.setEnabled(true);
        this.btnAnterior.setEnabled(true);
        this.index = -1;
        setarCodigo();
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
        cbCadUsuPerfil = new javax.swing.JComboBox<>();
        btnAdi = new javax.swing.JButton();
        btnExc = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtCadUsuId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnLimpar = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnProximo = new javax.swing.JButton();
        btnPrimeiro = new javax.swing.JButton();
        btnUltimo = new javax.swing.JButton();
        btnCadUsePesquisar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtCadUsuSenha = new javax.swing.JPasswordField();
        txtCadUsuConfirmarSenha = new javax.swing.JPasswordField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Usuários");
        setMaximumSize(new java.awt.Dimension(417, 409));
        setPreferredSize(new java.awt.Dimension(420, 406));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText(" Nome:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        jLabel2.setText(" Login:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        jLabel3.setText(" Senha:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        jLabel4.setText(" Perfil:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));
        getContentPane().add(txtCadUsuNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 213, -1));
        getContentPane().add(txtCadUsuLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 213, -1));

        cbCadUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Usuário" }));
        getContentPane().add(cbCadUsuPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 213, -1));

        btnAdi.setText("Salvar");
        btnAdi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdiActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdi, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 80, -1));

        btnExc.setText("Eliminar");
        btnExc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcActionPerformed(evt);
            }
        });
        getContentPane().add(btnExc, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, 80, -1));

        jLabel5.setText(" Código:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));
        getContentPane().add(txtCadUsuId, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 150, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Cadastro de Usuários");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        btnLimpar.setText("Novo");
        btnLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 79, -1));

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

        btnCadUsePesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnCadUsePesquisar.setToolTipText("Pesquisar");
        btnCadUsePesquisar.setContentAreaFilled(false);
        btnCadUsePesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadUsePesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadUsePesquisarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadUsePesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 30, 30));

        jLabel7.setText("Confirmar Senha:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 221, -1, -1));
        getContentPane().add(txtCadUsuSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 210, -1));
        getContentPane().add(txtCadUsuConfirmarSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 210, -1));

        setBounds(221, 77, 417, 409);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadUsePesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadUsePesquisarActionPerformed
        // chama a TelaPesquisarUsuario
        TelaPesquisarUsuario pesq = new TelaPesquisarUsuario();
        TelaPrincipal.Desktop.add(pesq);
        pesq.setVisible(true);
    }//GEN-LAST:event_btnCadUsePesquisarActionPerformed

    private void btnUltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUltimoActionPerformed
        // ultimo registro
        this.index = this.lista.list().size() - 1;
        setarCamposLista(this.lista.list().get(this.index));
        this.btnProximo.setEnabled(false);
        this.btnUltimo.setEnabled(false);
        this.btnPrimeiro.setEnabled(true);
        this.btnAnterior.setEnabled(true);
    }//GEN-LAST:event_btnUltimoActionPerformed

    private void btnPrimeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrimeiroActionPerformed
        // primeiro registro
        this.index = 0;

        setarCamposLista(this.lista.list().get(this.index));
        this.btnProximo.setEnabled(true);
        this.btnUltimo.setEnabled(true);
        this.btnPrimeiro.setEnabled(false);
        this.btnAnterior.setEnabled(false);
    }//GEN-LAST:event_btnPrimeiroActionPerformed

    private void btnProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProximoActionPerformed
        // proximo registro
        this.index++;
        if (this.index < this.lista.list().size() - 1) {
            setarCamposLista(this.lista.list().get(this.index));
            this.btnAnterior.setEnabled(true);
            this.btnPrimeiro.setEnabled(true);
        } else {
            setarCamposLista(this.lista.list().get(this.index));
            this.btnProximo.setEnabled(false);
            this.btnUltimo.setEnabled(false);
        }
    }//GEN-LAST:event_btnProximoActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        // registro anterior
        this.index--;
        if (this.index > 0) {
            setarCamposLista(this.lista.list().get(this.index));
            this.btnProximo.setEnabled(true);
            this.btnUltimo.setEnabled(true);
        } else {
            this.index = 0;
            setarCamposLista(this.lista.list().get(this.index));
            this.btnAnterior.setEnabled(false);
            this.btnPrimeiro.setEnabled(false);
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // chama o metodo limpar
        limparCampos();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnExcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcActionPerformed
        // chamad o metodo deletar
        deletar();
    }//GEN-LAST:event_btnExcActionPerformed

    private void btnAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdiActionPerformed
        // chama o metodo adicionar
        boolean total;
        boolean campos;
        // verifica os campos
        campos = verificaCampos();
        if (campos == false) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
        } else {
            total = confirmaCodigo(this.txtCadUsuId.getText());
            if (total == false) {
                if (this.txtCadUsuSenha.getText().equals(this.txtCadUsuConfirmarSenha.getText())) {
                    atualizar();
                } else {
                    JOptionPane.showMessageDialog(null, "As senhas não são correspondentes.");
                }
            } else {
                //chama o metodo para confirmar se o login já existe
                total = confirmaLogin(this.txtCadUsuLogin.getText());
                if (total == false) {
                    JOptionPane.showMessageDialog(null, "Login já existe.");

                } else {
                    //verifica a compatibilidade das senhas
                    if (this.txtCadUsuSenha.getText().equals(this.txtCadUsuConfirmarSenha.getText())) {
                        adicionar();
                    } else {
                        JOptionPane.showMessageDialog(null, "As senhas não são correspondentes.");
                    }
                }
            }
        }
    }//GEN-LAST:event_btnAdiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdi;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnCadUsePesquisar;
    private javax.swing.JButton btnExc;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPrimeiro;
    private javax.swing.JButton btnProximo;
    private javax.swing.JButton btnUltimo;
    public static javax.swing.JComboBox<String> cbCadUsuPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPasswordField txtCadUsuConfirmarSenha;
    public static javax.swing.JTextField txtCadUsuId;
    public static javax.swing.JTextField txtCadUsuLogin;
    public static javax.swing.JTextField txtCadUsuNome;
    public static javax.swing.JPasswordField txtCadUsuSenha;
    // End of variables declaration//GEN-END:variables

}
