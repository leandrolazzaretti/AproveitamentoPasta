/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import dto.UsuarioDto;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import util.Util;

/**
 *
 * @author Leandro
 */
public final class TelaPesquisarUsuario extends javax.swing.JInternalFrame {

    private Connection conexao = null;
    private String cbPesquisar = "Nome";
    public UsuarioDto guardar = new UsuarioDto();
    private final Util frame = new Util();

    /**
     * Creates new form TelaPesquisarUsuario
     */
    public TelaPesquisarUsuario() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        pesquisarUsuario();
        retirarBordas();
        this.tblCadUsuario.getTableHeader().setReorderingAllowed(false);
    }

    // metodo para retirar as bordas do JinternalFrame
    private void retirarBordas() {
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null); //retirar o painel superior
        this.setBorder(null);//retirar bordas
    }

    // pesquizar/ carregar usuarios atraves da tabela
    public void pesquisarUsuario() {
        String sql = "select codigo, nome, login, perfil from tbusuarios where " + this.cbPesquisar + " like ?";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) this.tblCadUsuario.getModel();
        modelo.setNumRows(0);

        this.tblCadUsuario.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblCadUsuario.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblCadUsuario.getColumnModel().getColumn(2).setPreferredWidth(40);
        this.tblCadUsuario.getColumnModel().getColumn(3).setPreferredWidth(40);

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, "%" + this.txtCadUsuarioPesquisar.getText() + "%");
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)});
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setarCampos() {
        int setar = this.tblCadUsuario.getSelectedRow();
        TelaCadUsuario.txtCadUsuId.setEnabled(false);
        TelaCadUsuario.txtCadUsuLogin.setEnabled(false);
        TelaCadUsuario.txtCadUsuId.setText(this.tblCadUsuario.getModel().getValueAt(setar, 0).toString());
        TelaCadUsuario.txtCadUsuNome.setText(this.tblCadUsuario.getModel().getValueAt(setar, 1).toString());
        TelaCadUsuario.txtCadUsuLogin.setText(this.tblCadUsuario.getModel().getValueAt(setar, 2).toString());
        TelaCadUsuario.cbCadUsuPerfil.setSelectedItem(this.tblCadUsuario.getModel().getValueAt(setar, 3).toString());
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
        tblCadUsuario = new javax.swing.JTable();
        txtCadUsuarioPesquisar = new javax.swing.JTextField();
        cbCadUsuarioPesquisar = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(143, 165, 110));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(122, 122, 122)));
        setClosable(true);
        setIconifiable(true);
        setTitle("Tabela de Usuários");
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
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblCadUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "Login", "Perfil"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCadUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadUsuarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCadUsuario);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 400, 250));

        txtCadUsuarioPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCadUsuarioPesquisarKeyReleased(evt);
            }
        });
        getContentPane().add(txtCadUsuarioPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 260, -1));

        cbCadUsuarioPesquisar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nome", "Código", "Perfil" }));
        cbCadUsuarioPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCadUsuarioPesquisarActionPerformed(evt);
            }
        });
        getContentPane().add(cbCadUsuarioPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 150, -1));

        jLabel7.setForeground(new java.awt.Color(79, 79, 79));
        jLabel7.setText("Pesquisar por:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

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

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(386, 2, 40, 20));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Tabela de Usuários");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, -1));

        setBounds(209, 77, 441, 406);
    }// </editor-fold>//GEN-END:initComponents

    private void tblCadUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadUsuarioMouseClicked
        // seta os campos do formulário através da tabela
        //TelaCadUsuario.
        if (evt.getClickCount() > 1) {
            setarCampos();
            this.dispose();
        }

    }//GEN-LAST:event_tblCadUsuarioMouseClicked

    private void txtCadUsuarioPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadUsuarioPesquisarKeyReleased
        // chama o metodo pesquisar
        pesquisarUsuario();
    }//GEN-LAST:event_txtCadUsuarioPesquisarKeyReleased

    private void cbCadUsuarioPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCadUsuarioPesquisarActionPerformed
        // adiciona um valor a variável cbPesquizar
        if (this.cbCadUsuarioPesquisar.getSelectedItem().equals("Código")) {
            this.cbPesquisar = "Codigo";
        } else {
            if (this.cbCadUsuarioPesquisar.getSelectedItem().equals("Nome")) {
                this.cbPesquisar = "Nome";
            } else {
                this.cbPesquisar = "Perfil";
            }
        }
    }//GEN-LAST:event_cbCadUsuarioPesquisarActionPerformed

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
        this.txtCadUsuarioPesquisar.requestFocus();
    }//GEN-LAST:event_formInternalFrameActivated


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JComboBox<String> cbCadUsuarioPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable tblCadUsuario;
    private javax.swing.JTextField txtCadUsuarioPesquisar;
    // End of variables declaration//GEN-END:variables
}
