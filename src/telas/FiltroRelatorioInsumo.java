/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JRException;
import util.Relatorio;

/**
 *
 * @author Leandro
 */
public class FiltroRelatorioInsumo extends javax.swing.JDialog {

    private String cod = "";
    private String insu = "";
    private String um = "";
    private String quant = "";

    private int xMouse;
    private int yMouse;

    /**
     * Creates new form FiltroRelatorioInsumo
     * @param parent
     * @param modal
     */
    public FiltroRelatorioInsumo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.rbNao.setSelected(true);
    }

    private void emitirRelatorio(boolean confFiltro) {
        Relatorio relatorio = new Relatorio();

        if (confFiltro == true) {
            verificaFiltro();
            relatorio.relatorioInsumoSetar(this.cod, this.insu, this.um, this.quant);
        } else {
            relatorio.relatorioInsumoSetar("", "", "", "");
        }
        try {
            relatorio.gerarRelatorio();
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    private void verificaFiltro() {
        if (this.chCodigo.isSelected()) {
            this.cod = this.txtFiltroCodigo.getText();
        }
        if (this.chInsumo.isSelected()) {
            this.insu = this.txtFiltroInsumo.getText();
        }
        if (this.chUM.isSelected()) {
            this.um = this.cbFiltroUM.getSelectedItem().toString();
        }
        if (this.chQuant.isSelected()) {
            this.quant = this.txtFiltroQuant.getText();
        }
    }

    private void ativarCampos() {
        this.chCodigo.setEnabled(true);
        this.chInsumo.setEnabled(true);
        this.chQuant.setEnabled(true);
        this.chUM.setEnabled(true);
    }

    private void desativarCampos() {
        this.chCodigo.setEnabled(false);
        this.chInsumo.setEnabled(false);
        this.chQuant.setEnabled(false);
        this.chUM.setEnabled(false);

        this.txtFiltroCodigo.setEnabled(false);
        this.txtFiltroInsumo.setEnabled(false);
        this.cbFiltroUM.setEnabled(false);
        this.txtFiltroQuant.setEnabled(false);
    }

    // quando o mouse estiver em cima
    private void alteraCor(JPanel painel, JButton botao) {
        painel.setBackground(new Color(192, 221, 147));
        botao.setForeground(new Color(66, 66, 66));
    }

    // retorna a cor original do botão
    public void retornaCor(JPanel painel, JButton botao) {
        painel.setBackground(new Color(255, 255, 255));
        botao.setForeground(new Color(66, 66, 66));

    }

    // quando o botão for pressionado
    private void alteraCorPressionado(JPanel painel, JButton botao) {
        painel.setBackground(new Color(172, 198, 132));
        botao.setForeground(new Color(66, 66, 66));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        txtFiltroInsumo = new javax.swing.JTextField();
        txtFiltroQuant = new javax.swing.JTextField();
        txtFiltroCodigo = new javax.swing.JTextField();
        cbFiltroUM = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        rbNao = new javax.swing.JRadioButton();
        rbSim = new javax.swing.JRadioButton();
        chUM = new javax.swing.JCheckBox();
        chCodigo = new javax.swing.JCheckBox();
        chInsumo = new javax.swing.JCheckBox();
        chQuant = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnEmitir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(143, 165, 110));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(122, 122, 122)));
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtFiltroInsumo.setEnabled(false);
        jPanel1.add(txtFiltroInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 170, -1));

        txtFiltroQuant.setEnabled(false);
        jPanel1.add(txtFiltroQuant, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 170, -1));

        txtFiltroCodigo.setEnabled(false);
        jPanel1.add(txtFiltroCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 170, -1));

        cbFiltroUM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "kg", "g", "mg", "L" }));
        cbFiltroUM.setEnabled(false);
        jPanel1.add(cbFiltroUM, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 170, -1));

        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Quantidade:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 60, -1));

        jLabel2.setForeground(new java.awt.Color(79, 79, 79));
        jLabel2.setText("Código:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 40, -1));

        jLabel3.setForeground(new java.awt.Color(79, 79, 79));
        jLabel3.setText("Insumo:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 40, -1));

        jLabel4.setForeground(new java.awt.Color(79, 79, 79));
        jLabel4.setText("UM:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 40, -1));

        jPanel3.setBackground(new java.awt.Color(143, 165, 110));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel3.add(btnFechar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 20));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 2, 40, 20));

        jPanel2.setBackground(new java.awt.Color(143, 165, 110));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), "Adicionar Filtro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(79, 79, 79))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroup1.add(rbNao);
        rbNao.setForeground(new java.awt.Color(79, 79, 79));
        rbNao.setText("Não");
        rbNao.setContentAreaFilled(false);
        rbNao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNaoActionPerformed(evt);
            }
        });
        jPanel2.add(rbNao, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        buttonGroup1.add(rbSim);
        rbSim.setForeground(new java.awt.Color(79, 79, 79));
        rbSim.setText("Sim");
        rbSim.setContentAreaFilled(false);
        rbSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSimActionPerformed(evt);
            }
        });
        jPanel2.add(rbSim, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 300, 50));

        chUM.setContentAreaFilled(false);
        chUM.setEnabled(false);
        chUM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chUMActionPerformed(evt);
            }
        });
        jPanel1.add(chUM, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 186, -1, -1));

        chCodigo.setContentAreaFilled(false);
        chCodigo.setEnabled(false);
        chCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chCodigoActionPerformed(evt);
            }
        });
        jPanel1.add(chCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 126, -1, 20));

        chInsumo.setContentAreaFilled(false);
        chInsumo.setEnabled(false);
        chInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chInsumoActionPerformed(evt);
            }
        });
        jPanel1.add(chInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 156, -1, -1));

        chQuant.setContentAreaFilled(false);
        chQuant.setEnabled(false);
        chQuant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chQuantActionPerformed(evt);
            }
        });
        jPanel1.add(chQuant, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 216, -1, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCancelar.setForeground(new java.awt.Color(79, 79, 79));
        btnCancelar.setText("Cancelar");
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelarMouseExited(evt);
            }
        });
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel4.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 270, 80, 25));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEmitir.setForeground(new java.awt.Color(79, 79, 79));
        btnEmitir.setText("Emitir");
        btnEmitir.setContentAreaFilled(false);
        btnEmitir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEmitirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEmitirMouseExited(evt);
            }
        });
        btnEmitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmitirActionPerformed(evt);
            }
        });
        jPanel5.add(btnEmitir, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 270, 80, 25));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(79, 79, 79));
        jLabel5.setText("Relatório Insumo Filtro");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(338, 331));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseEntered
        // quando o mouse está em cima
        this.jPanel3.setBackground(new Color(211, 57, 33));
        this.btnFechar.setForeground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnFecharMouseEntered

    private void btnFecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseExited
        // quando o mouse sair de cima
        this.jPanel3.setBackground(new Color(143, 165, 110));
        this.btnFechar.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnFecharMouseExited

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        //fecha a tela
        this.dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void rbNaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNaoActionPerformed
        // chama o metodo para desativar os campos
        desativarCampos();
    }//GEN-LAST:event_rbNaoActionPerformed

    private void rbSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSimActionPerformed
        // chama o metodo para ativar os campos
        ativarCampos();
    }//GEN-LAST:event_rbSimActionPerformed

    private void chUMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chUMActionPerformed
        // ativa/desativa filtro codigo
        if (this.chUM.isSelected()) {
            this.cbFiltroUM.setEnabled(true);
        } else {
            this.cbFiltroUM.setEnabled(false);
        }
    }//GEN-LAST:event_chUMActionPerformed

    private void chCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chCodigoActionPerformed
        // ativa/desativa filtro codigo
        if (this.chCodigo.isSelected()) {
            this.txtFiltroCodigo.setEnabled(true);
        } else {
            this.txtFiltroCodigo.setEnabled(false);
        }
    }//GEN-LAST:event_chCodigoActionPerformed

    private void chInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chInsumoActionPerformed
        // ativa/desativa filtro nome
        if (this.chInsumo.isSelected()) {
            this.txtFiltroInsumo.setEnabled(true);
        } else {
            this.txtFiltroInsumo.setEnabled(false);
        }
    }//GEN-LAST:event_chInsumoActionPerformed

    private void chQuantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chQuantActionPerformed
        // ativa/desativa filtro login
        if (this.chQuant.isSelected()) {
            this.txtFiltroQuant.setEnabled(true);
        } else {
            this.txtFiltroQuant.setEnabled(false);
        }
    }//GEN-LAST:event_chQuantActionPerformed

    private void btnCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel4, this.btnCancelar);
    }//GEN-LAST:event_btnCancelarMouseEntered

    private void btnCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel4, this.btnCancelar);
    }//GEN-LAST:event_btnCancelarMouseExited

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // quando ativado
        alteraCorPressionado(this.jPanel4, this.btnCancelar);
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEmitirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmitirMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel5, this.btnEmitir);
    }//GEN-LAST:event_btnEmitirMouseEntered

    private void btnEmitirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmitirMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel5, this.btnEmitir);
    }//GEN-LAST:event_btnEmitirMouseExited

    private void btnEmitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmitirActionPerformed
        // quando ativado
        alteraCorPressionado(this.jPanel5, this.btnEmitir);
        if (this.rbSim.isSelected()) {
            emitirRelatorio(true);
        } else {
            emitirRelatorio(false);
        }
        this.dispose();
    }//GEN-LAST:event_btnEmitirActionPerformed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        // evento para mover o frame sem a barra superior
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();

        this.setLocation(x - this.xMouse, y - this.yMouse);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        // evento para mover o frame sem a barra superior
        this.xMouse = evt.getX();
        this.yMouse = evt.getY();
    }//GEN-LAST:event_jPanel1MousePressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // faz ação quando a tela é fechada
        TelaPrincipal.jDialogInsumo = false;
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FiltroRelatorioInsumo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FiltroRelatorioInsumo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FiltroRelatorioInsumo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FiltroRelatorioInsumo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FiltroRelatorioInsumo dialog = new FiltroRelatorioInsumo(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEmitir;
    private javax.swing.JButton btnFechar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbFiltroUM;
    private javax.swing.JCheckBox chCodigo;
    private javax.swing.JCheckBox chInsumo;
    private javax.swing.JCheckBox chQuant;
    private javax.swing.JCheckBox chUM;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton rbNao;
    private javax.swing.JRadioButton rbSim;
    private javax.swing.JTextField txtFiltroCodigo;
    private javax.swing.JTextField txtFiltroInsumo;
    private javax.swing.JTextField txtFiltroQuant;
    // End of variables declaration//GEN-END:variables
}
