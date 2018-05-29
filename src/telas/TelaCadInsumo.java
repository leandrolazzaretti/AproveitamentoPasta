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
import util.SoNumeros;

/**
 *
 * @author Leandro
 */
public class TelaCadInsumo extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    private String cbPesquisar = "codigo";

    /**
     * Creates new form TelaCadInsumos
     */
    public TelaCadInsumo() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        this.txtCadInsCodigo.setDocument(new SoNumeros());
        this.txtCadInsPreco.setDocument(new SoNumeros());
        this.txtCadInsQuant.setDocument(new SoNumeros());
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
            this.pst.setString(4, this.txtCadInsQuant.getText().replace(",", "."));
            this.pst.setString(5, this.txtCadInsPreco.getText().replace(",", "."));
            //Atualiza a tabela insumos
            int adicionado = this.pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Insumo cadastrado com sucesso!");
                limparCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void atualizarInsumos() {
        String sql = "update tbinsumos set descricao=?, UM=?, quantidade=?, preco=? where codigo=?";

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.pst.setString(1, this.txtCadInsDes.getText());
            this.pst.setString(2, this.cbCadInsUm.getSelectedItem().toString());
            this.pst.setString(3, this.txtCadInsQuant.getText().replace(",", "."));
            this.pst.setString(4, this.txtCadInsPreco.getText().replace(",", "."));
            this.pst.setString(5, this.txtCadInsCodigo.getText());
            //Atualiza a tabela insumos
            int adicionado = this.pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Insumo Atualizado com sucesso!");
                limparCampos();
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

    //confirma se o codigo já existe
    private boolean confirmaCodigo(String codigo) {
        String sql = "select count (codigo) as total from tbinsumos where codigo ='" + codigo + "';";
        int total = 0;

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.rs = this.pst.executeQuery();
            total = Integer.parseInt(this.rs.getString(1));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        if (total > 0) {
            return false;
        } else {
            return true;
        }

    }

    // confirma se os campos estão setados
    private boolean verificaCampos() {
        if ((this.txtCadInsCodigo.getText().isEmpty()) || (this.txtCadInsDes.getText().isEmpty()) || (this.txtCadInsPreco.getText().isEmpty()) || (this.txtCadInsQuant.getText().isEmpty())) {
            return false;
        } else {
            return true;
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

        txtCadInsPreco = new javax.swing.JTextField();
        btnCadInsAdicionar = new javax.swing.JButton();
        txtCadInsCodigo = new javax.swing.JTextField();
        btnCadInsAtualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnCadInsDeletar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCadInsQuant = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbCadInsUm = new javax.swing.JComboBox<>();
        btnCadInsLimpar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        txtCadInsDes = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Insumos");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(txtCadInsPreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 270, -1));

        btnCadInsAdicionar.setText("Salvar");
        btnCadInsAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsAdicionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadInsAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 330, 79, -1));
        getContentPane().add(txtCadInsCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 140, -1));

        btnCadInsAtualizar.setText("Atualizar");
        btnCadInsAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsAtualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadInsAtualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 330, 79, -1));

        jLabel2.setText("Descrição:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

        btnCadInsDeletar.setText("Deletar");
        btnCadInsDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsDeletarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadInsDeletar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 330, 79, -1));

        jLabel3.setText("Unidade de Medida (UM):");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, -1, -1));

        jLabel4.setText("Quantidade:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, -1));
        getContentPane().add(txtCadInsQuant, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 270, -1));

        jLabel5.setText("Preço:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, -1, -1));

        jLabel1.setText("Código:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, -1, -1));

        cbCadInsUm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "kg", "g", "mg", "L" }));
        getContentPane().add(cbCadInsUm, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 140, -1));

        btnCadInsLimpar.setText("Novo");
        btnCadInsLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsLimparActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadInsLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, 79, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        jButton1.setToolTipText("Pesquisar");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, 30, 30));
        getContentPane().add(txtCadInsDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 270, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Cadastro de Insumos");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, -1, -1));

        setBounds(148, 72, 564, 416);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadInsAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsAdicionarActionPerformed
        // chama o metodo adicionar
        boolean total;
        boolean campos;
        // verifica os campos
        campos = verificaCampos();
        if (campos == false) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
        } else {
            //chama o metodo para confirmar se o codigo já existe
            total = confirmaCodigo(this.txtCadInsCodigo.getText());
            if (total == false) {
                JOptionPane.showMessageDialog(null, "Código já existe.");
            } else {
                adicionarInsumos();
            }

        }
    }//GEN-LAST:event_btnCadInsAdicionarActionPerformed

    private void btnCadInsAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsAtualizarActionPerformed
        // cahama o metodo atualizar
        boolean total;
        boolean campos;
        // verifica os campos
        campos = verificaCampos();
        if (campos == false) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
        } else {
            total = confirmaCodigo(this.txtCadInsCodigo.getText());
            if (total == true) {
                JOptionPane.showMessageDialog(null, "Código inválido.");
            } else {
                atualizarInsumos();
            }
        }

    }//GEN-LAST:event_btnCadInsAtualizarActionPerformed

    private void btnCadInsDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsDeletarActionPerformed
        // chama o metodo deletar
        deletarInsumos();
    }//GEN-LAST:event_btnCadInsDeletarActionPerformed

    private void btnCadInsLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsLimparActionPerformed
        // chama o metodo limpar    
        limparCampos();
    }//GEN-LAST:event_btnCadInsLimparActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Chama a TelePesquisarInsumos
        TelaPesquisarInsumos insumos = new TelaPesquisarInsumos();
        TelaPrincipal.Desktop.add(insumos);
        insumos.setVisible(true);
        insumos.confimaTela = true;

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadInsAdicionar;
    private javax.swing.JButton btnCadInsAtualizar;
    private javax.swing.JButton btnCadInsDeletar;
    private javax.swing.JButton btnCadInsLimpar;
    public static javax.swing.JComboBox<String> cbCadInsUm;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    public static javax.swing.JTextField txtCadInsCodigo;
    public static javax.swing.JTextField txtCadInsDes;
    public static javax.swing.JTextField txtCadInsPreco;
    public static javax.swing.JTextField txtCadInsQuant;
    // End of variables declaration//GEN-END:variables
}
