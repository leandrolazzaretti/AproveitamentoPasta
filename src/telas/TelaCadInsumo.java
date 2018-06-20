/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.sun.glass.events.KeyEvent;
import conexao.ModuloConexao;
import dao.InsumoDao;
import dto.InsumoDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import util.SoNumeros;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaCadInsumo extends javax.swing.JInternalFrame {

    Connection conexao = null;
    public static JInternalFrame framePesqInsumo;
    Util util = new Util();
    TelaPesquisarInsumos insumo = new TelaPesquisarInsumos();

    /**
     * Creates new form TelaCadInsumos
     */
    public TelaCadInsumo() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        this.txtCadInsCodigo.setDocument(new SoNumeros());
        this.txtCadInsPreco.setDocument(new SoNumeros());
        this.txtCadInsQuant.setDocument(new SoNumeros());
        mascaraInsumo();
        limparCampos();       
    }

    private void confirmar(boolean confirmar) {
        InsumoDto insumoDto = new InsumoDto();
        InsumoDao insumoDao = new InsumoDao();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(2);

        insumoDto.setCodigo(Integer.parseInt(this.txtCadInsCodigo.getText()));
        insumoDto.setDescricao(this.txtCadInsDes.getText());
        insumoDto.setUm(this.cbCadInsUm.getSelectedItem().toString());

        String valorQuant = this.util.formatador(Double.parseDouble(this.txtCadInsQuant.getText().replace(",", ".")));
        BigDecimal valor = new BigDecimal(this.txtCadInsPreco.getText().replace(",", "."));
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String formatado = nf.format(valor);
        //System.out.println(formatado);

        insumoDto.setQuantidade(valorQuant);
        insumoDto.setPreco(formatado);

        if (confirmar == true) {
            insumoDao.adicionarInsumos(insumoDto);

        } else {
            insumoDao.atualizarInsumos(insumoDto, Integer.parseInt(this.txtCadInsCodigo.getText()));
        }
    }

    private void limparCampos() {
        this.txtCadInsCodigo.setEnabled(true);
        this.txtCadInsCodigo.setText(null);
        this.txtCadInsDes.setText(null);
        this.cbCadInsUm.setSelectedItem("kg");
        this.txtCadInsQuant.setText("0");
        this.txtCadInsPreco.setValue(null);

    }

    //confirma se o codigo já existe
    private boolean confirmaCodigo(String codigo) {
        String sql = "select count (codigo) as total from tbinsumos where codigo ='" + codigo + "';";

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

    // confirma se os campos estão setados
    private boolean verificaCampos() {
        if ((this.txtCadInsCodigo.getText().isEmpty()) || (this.txtCadInsDes.getText().isEmpty()) || (this.txtCadInsPreco.getText().isEmpty()) || (this.txtCadInsQuant.getText().isEmpty())) {
            return false;
        } else {
            return true;
        }
    }

    //mascara para o campo preço/ quantidade(foramato de moeda)
    private void mascaraInsumo() {
        DecimalFormat dFormat2 = new DecimalFormat("#,###.00");
        NumberFormatter formatter2 = new NumberFormatter(dFormat2);

        formatter2.setFormat(dFormat2);
        formatter2.setAllowsInvalid(false);

        this.txtCadInsPreco.setFormatterFactory(new DefaultFormatterFactory(formatter2));
    }

    private void confirmaAcao(boolean conf) {
        // chama o metodo adicionar
        boolean total;
        boolean campos;
        boolean conf2 = false;
        // verifica os campos
        campos = verificaCampos();
        if (campos == false) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            conf2 = true;
        } else {
            //chama o metodo para confirmar se o codigo já existe
            total = confirmaCodigo(this.txtCadInsCodigo.getText());
            if (total == false) {
                confirmar(false);
            } else {
                confirmar(true);
                limparCampos();
            }
        }
        if (conf == true) {
            if (conf2 == true) {
                this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
            } else {
                this.setVisible(false);
                this.dispose();
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

        btnCadInsAdicionar = new javax.swing.JButton();
        txtCadInsCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnCadInsDeletar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbCadInsUm = new javax.swing.JComboBox<>();
        btnCadInsLimpar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        txtCadInsDes = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCadInsPreco = new javax.swing.JFormattedTextField();
        txtCadInsQuant = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Insumos");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
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
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCadInsAdicionar.setText("Salvar");
        btnCadInsAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsAdicionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadInsAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 330, 79, -1));

        txtCadInsCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCadInsCodigoFocusLost(evt);
            }
        });
        txtCadInsCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCadInsCodigoKeyPressed(evt);
            }
        });
        getContentPane().add(txtCadInsCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 140, -1));

        jLabel2.setText("Descrição:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

        btnCadInsDeletar.setText("Eliminar");
        btnCadInsDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsDeletarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadInsDeletar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 330, 79, -1));

        jLabel3.setText("Unidade de Medida (UM):");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, -1, -1));

        jLabel4.setText("Quantidade:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, -1));

        jLabel5.setText("Preço:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, -1, -1));

        jLabel1.setText("Código:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, -1, -1));

        cbCadInsUm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "kg", "g", "mg", "L" }));
        getContentPane().add(cbCadInsUm, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 140, -1));

        btnCadInsLimpar.setText("Limpar");
        btnCadInsLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsLimparActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadInsLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, 79, -1));

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
        getContentPane().add(txtCadInsPreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 270, -1));

        txtCadInsQuant.setEnabled(false);
        getContentPane().add(txtCadInsQuant, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 270, -1));

        setBounds(148, 72, 564, 416);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadInsAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsAdicionarActionPerformed
        //chama o metodo adicionar/salvar
        confirmaAcao(false);
    }//GEN-LAST:event_btnCadInsAdicionarActionPerformed

    private void btnCadInsDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsDeletarActionPerformed
        // chama o metodo deletar
        if (this.txtCadInsCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um insumo válido.");
        } else {
            InsumoDao insumoDao = new InsumoDao();
            boolean conf = insumoDao.contInsumo(Integer.parseInt(this.txtCadInsCodigo.getText()));
            if (conf == true) {
                JOptionPane.showMessageDialog(null, "Este insumo não pode ser eliminado.");
            } else {
                insumoDao.deletarInsumos(Integer.parseInt(this.txtCadInsCodigo.getText()));
                limparCampos();
            }
        }

    }//GEN-LAST:event_btnCadInsDeletarActionPerformed

    private void btnCadInsLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsLimparActionPerformed
        // chama o metodo limpar    
        limparCampos();
    }//GEN-LAST:event_btnCadInsLimparActionPerformed
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Chama a TelePesquisarInsumos

        if (this.framePesqInsumo == null) {
            this.framePesqInsumo = new TelaPesquisarInsumos();
        } else {
            this.framePesqInsumo.dispose();
            this.framePesqInsumo = new TelaPesquisarInsumos();
        }
        this.util.comandoInternal(this.framePesqInsumo);
        this.insumo.pesquisarInsumos();
        TelaPesquisarInsumos.confimaTela = true;
        TelaPesquisarInsumos.confirmarEscolha = true;

//        TelaPesquisarInsumos insumos = new TelaPesquisarInsumos();
//        TelaPrincipal.Desktop.add(insumos);
//        insumos.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCadInsCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCadInsCodigoFocusLost
        //evento ao sair do campo ele busca os dados de determinado codigo
        if (!this.txtCadInsCodigo.getText().equals("")) {
            InsumoDao pesq = new InsumoDao();
            pesq.pesquisarInsumos(Integer.parseInt(this.txtCadInsCodigo.getText()));
        }
    }//GEN-LAST:event_txtCadInsCodigoFocusLost

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        // Chama o metodo para bloquear a movimentação do frame  
        this.util.bloquearMovimentacao(TelaPrincipal.frameInsumo, 148, 72);
    }//GEN-LAST:event_formComponentMoved

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // gerar mensagem de salvar antes de sair
        if ((this.txtCadInsCodigo.getText().isEmpty()) && (this.txtCadInsDes.getText().isEmpty()) && (this.txtCadInsPreco.getText().isEmpty()) && (this.txtCadInsQuant.getText().equals("0"))) {
            this.setVisible(false);
            this.dispose();
        } else {
            int result = JOptionPane.showConfirmDialog(null, "Deseja salvar antes de sair ?", "Atenção", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                confirmaAcao(true);
            } else {
                if (result == JOptionPane.NO_OPTION) {
                    this.setVisible(false);
                    this.dispose();
                } else {
                    this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        }
    }//GEN-LAST:event_formInternalFrameClosing

    private void txtCadInsCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadInsCodigoKeyPressed
        // chama o metodo atraves da tecla enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!this.txtCadInsCodigo.getText().equals("")) {
                InsumoDao pesq = new InsumoDao();
                pesq.pesquisarInsumos(Integer.parseInt(this.txtCadInsCodigo.getText()));
            }
        }
    }//GEN-LAST:event_txtCadInsCodigoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadInsAdicionar;
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
    public static javax.swing.JFormattedTextField txtCadInsPreco;
    public static javax.swing.JTextField txtCadInsQuant;
    // End of variables declaration//GEN-END:variables
}
