/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import dao.UsuarioDao;
import dto.SoNumeros;
import dto.UsuarioDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Leandro
 */
public class TelaCadUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    UsuarioDao lista = new UsuarioDao();
    private int index = -1;
    private String cbPesquisar = "codigo";

    /**
     * Creates new form TelaCadUsuario
     */
    public TelaCadUsuario() {
        initComponents();

        this.conexao = ModuloConexao.conector();
        carregarTabela();
        //this.txtCadUsuId.setDocument(new SoNumeros());
        
        setarCodigo();
        
    }

    //limpa todos os campos da tela
    private void limparCampos() {
        this.txtCadUsuId.setEnabled(true);
        this.txtCadUsuId.setText(null);
        this.txtCadUsuNome.setText(null);
        this.txtCadUsuLogin.setText(null);
        this.txtCadUsuSenha.setText(null);
        this.cbCadUsuPerfil.setSelectedItem("Administrador");
        this.btnProximo.setEnabled(true);
        this.btnUltimo.setEnabled(true);
        this.btnPrimeiro.setEnabled(true);
        this.btnAnterior.setEnabled(true);
        this.index = -1;
        setarCodigo();
    }

    // adiciona um novo usuario
    public void adicionar() {
        String sql = "insert into tbusuarios(codigo,nome,login,senha,perfil) values(?,?,?,?,?)";

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
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, sqle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    // atualiza o cadastro de um usuário
    private void atualizar() {
        String sql = "update tbusuarios set nome=?, login=?, senha=?, perfil=? where codigo=?";

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

                String sql = "delete from tbusuarios where codigo=?";

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

    private void setarCamposLista(UsuarioDto user) {
        this.txtCadUsuId.setEnabled(false);
        this.txtCadUsuId.setText(String.valueOf(user.getIduser()));
        this.txtCadUsuNome.setText(user.getNome());
        this.txtCadUsuLogin.setText(user.getLogin());
        this.txtCadUsuSenha.setText(user.getSenha());
        this.cbCadUsuPerfil.setSelectedItem(user.getPerfil());
    }

    //carregar tabela
    private void carregarTabela() {
        String sql = "select * from tbusuarios";

        DefaultTableModel modelo = (DefaultTableModel) this.tblCadUse.getModel();
        modelo.setNumRows(0);

        this.tblCadUse.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblCadUse.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblCadUse.getColumnModel().getColumn(2).setPreferredWidth(40);
        this.tblCadUse.getColumnModel().getColumn(3).setPreferredWidth(40);
        this.tblCadUse.getColumnModel().getColumn(4).setPreferredWidth(40);

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.rs = this.pst.executeQuery();

            while (this.rs.next()) {
                modelo.addRow(new Object[]{
                    this.rs.getInt(1),
                    this.rs.getString(2),
                    this.rs.getString(3),
                    this.rs.getString(4),
                    this.rs.getString(5)});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //pesquisa feita atraves da tabela
    public void pesquisarUsuario() {
        String sql = "select * from tbusuarios where " + this.cbPesquisar + " like ?";

        DefaultTableModel modelo = (DefaultTableModel) this.tblCadUse.getModel();
        modelo.setNumRows(0);

        this.tblCadUse.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblCadUse.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblCadUse.getColumnModel().getColumn(2).setPreferredWidth(40);
        this.tblCadUse.getColumnModel().getColumn(3).setPreferredWidth(40);
        this.tblCadUse.getColumnModel().getColumn(4).setPreferredWidth(40);

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadUsePesquisar.getText() + "%");
            this.rs = this.pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (this.rs.next()) {
                modelo.addRow(new Object[]{
                    this.rs.getInt(1),
                    this.rs.getString(2),
                    this.rs.getString(3),
                    this.rs.getString(4),
                    this.rs.getString(5)});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    
    private void setarCampos() {
        int setar = this.tblCadUse.getSelectedRow();
        this.txtCadUsuId.setEnabled(false);
        this.txtCadUsuId.setText(this.tblCadUse.getModel().getValueAt(setar, 0).toString());
        this.txtCadUsuNome.setText(this.tblCadUse.getModel().getValueAt(setar, 1).toString());
        this.txtCadUsuLogin.setText(this.tblCadUse.getModel().getValueAt(setar, 2).toString());
        this.txtCadUsuSenha.setText(this.tblCadUse.getModel().getValueAt(setar, 3).toString());
        this.cbCadUsuPerfil.setSelectedItem(this.tblCadUse.getModel().getValueAt(setar, 4).toString());
    }
    
    private void setarCodigo(){
        int codig = this.lista.list().size()+1;
        this.txtCadUsuId.setEnabled(false);
        this.txtCadUsuId.setText(String.valueOf(codig));
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblCadUse = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        cbCadUsePesquisar = new javax.swing.JComboBox<>();
        txtCadUsePesquisar = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
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
        btnCadUsePesquisar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Usuários");
        setPreferredSize(new java.awt.Dimension(420, 406));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblCadUse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "Login", "Senha", "Perfil"
            }
        ));
        tblCadUse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadUseMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCadUse);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 390, 250));

        jLabel7.setText("Pesquisar por:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, -1, -1));

        cbCadUsePesquisar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Nome", "Perfil" }));
        cbCadUsePesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCadUsePesquisarActionPerformed(evt);
            }
        });
        getContentPane().add(cbCadUsePesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 150, -1));

        txtCadUsePesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCadUsePesquisarKeyReleased(evt);
            }
        });
        getContentPane().add(txtCadUsePesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, 260, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText(" Nome:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, -1));

        jLabel2.setText(" Login:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        jLabel3.setText(" Senha:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        jLabel4.setText(" Perfil:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, -1));
        jPanel1.add(txtCadUsuNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 213, -1));
        jPanel1.add(txtCadUsuLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 213, -1));
        jPanel1.add(txtCadUsuSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 213, -1));

        cbCadUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Usuário" }));
        jPanel1.add(cbCadUsuPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, 213, -1));

        btnAdi.setText("Salvar");
        btnAdi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdiActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdi, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 280, 80, -1));

        btnAtu.setText("Atualizar");
        btnAtu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAtu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtuActionPerformed(evt);
            }
        });
        jPanel1.add(btnAtu, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 79, -1));

        btnExc.setText("Deletar");
        btnExc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcActionPerformed(evt);
            }
        });
        jPanel1.add(btnExc, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 280, 73, -1));

        jLabel5.setText(" Código:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));
        jPanel1.add(txtCadUsuId, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 150, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Cadastro de Usuários");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        btnLimpar.setText("Novo");
        btnLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 79, -1));

        btnAnterior.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnterior.setText("<");
        btnAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, -1, -1));

        btnProximo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnProximo.setText(">");
        btnProximo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProximoActionPerformed(evt);
            }
        });
        jPanel1.add(btnProximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, -1, -1));

        btnPrimeiro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrimeiro.setText("<<");
        btnPrimeiro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrimeiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrimeiroActionPerformed(evt);
            }
        });
        jPanel1.add(btnPrimeiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, -1, -1));

        btnUltimo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUltimo.setText(">>");
        btnUltimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUltimoActionPerformed(evt);
            }
        });
        jPanel1.add(btnUltimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, -1, -1));

        btnCadUsePesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnCadUsePesquisar.setToolTipText("Pesquisar");
        btnCadUsePesquisar.setContentAreaFilled(false);
        btnCadUsePesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadUsePesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadUsePesquisarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCadUsePesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 90, 30, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 360));

        setBounds(17, 77, 826, 407);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdiActionPerformed
        // chama o metodo adicionar
        adicionar();
        carregarTabela();
    }//GEN-LAST:event_btnAdiActionPerformed

    private void btnAtuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtuActionPerformed
        // chama o metodo atualizar
        atualizar();
        carregarTabela();
    }//GEN-LAST:event_btnAtuActionPerformed

    private void btnExcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcActionPerformed
        // chamad o metodo deletar
        deletar();
        carregarTabela();
    }//GEN-LAST:event_btnExcActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // chama o metodo limpar
        limparCampos();
    }//GEN-LAST:event_btnLimparActionPerformed

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

    private void btnPrimeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrimeiroActionPerformed
        // primeiro registro
        this.index = 0;

        setarCamposLista(this.lista.list().get(this.index));
        this.btnProximo.setEnabled(true);
        this.btnUltimo.setEnabled(true);
        this.btnPrimeiro.setEnabled(false);
        this.btnAnterior.setEnabled(false);
    }//GEN-LAST:event_btnPrimeiroActionPerformed

    private void btnUltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUltimoActionPerformed
        // ultimo registro
        this.index = this.lista.list().size() - 1;
        setarCamposLista(this.lista.list().get(this.index));
        this.btnProximo.setEnabled(false);
        this.btnUltimo.setEnabled(false);
        this.btnPrimeiro.setEnabled(true);
        this.btnAnterior.setEnabled(true);
    }//GEN-LAST:event_btnUltimoActionPerformed

    private void txtCadUsePesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadUsePesquisarKeyReleased
        // chama o metodo pesquisar
        pesquisarUsuario();
    }//GEN-LAST:event_txtCadUsePesquisarKeyReleased

    private void tblCadUseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadUseMouseClicked
        // seta os campos do formulário através da tabela
        setarCampos();
    }//GEN-LAST:event_tblCadUseMouseClicked

    private void cbCadUsePesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCadUsePesquisarActionPerformed
        // adiciona um valor a variável cbPesquizar
        if (this.cbCadUsePesquisar.getSelectedItem().equals("Código")) {
            this.cbPesquisar = "Codigo";
        } else {
            if (this.cbCadUsePesquisar.getSelectedItem().equals("Nome")) {
                this.cbPesquisar = "Nome";
            } else {
                this.cbPesquisar = "Perfil";
            }
        }
    }//GEN-LAST:event_cbCadUsePesquisarActionPerformed

    private void btnCadUsePesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadUsePesquisarActionPerformed
        // chama a TelaPesquisarUsuario
//        TelaPrincipal principal = new TelaPrincipal();
//        principal.comandoInternal(new NewJInternalFrame());
        TelaPesquisarUsuario pesq = new TelaPesquisarUsuario();
        TelaPrincipal.Desktop.add(pesq);
        pesq.setVisible(true);
    }//GEN-LAST:event_btnCadUsePesquisarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdi;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnAtu;
    private javax.swing.JButton btnCadUsePesquisar;
    private javax.swing.JButton btnExc;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPrimeiro;
    private javax.swing.JButton btnProximo;
    private javax.swing.JButton btnUltimo;
    private javax.swing.JComboBox<String> cbCadUsePesquisar;
    private javax.swing.JComboBox<String> cbCadUsuPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCadUse;
    private javax.swing.JTextField txtCadUsePesquisar;
    private javax.swing.JTextField txtCadUsuId;
    private javax.swing.JTextField txtCadUsuLogin;
    private javax.swing.JTextField txtCadUsuNome;
    private javax.swing.JTextField txtCadUsuSenha;
    // End of variables declaration//GEN-END:variables
}
