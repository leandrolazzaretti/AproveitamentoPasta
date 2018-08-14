/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import dao.TipoInsumoDao;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import util.UpperCaseDocument;

/**
 *
 * @author Leandro
 */
public final class TelaOrdenacaoInsumo extends javax.swing.JInternalFrame {

    public static JInternalFrame framePesqInsumo;
    private String cbPesquisar = "nome";
    private Connection conexao = null;
    private final TipoInsumoDao tipInsDao = new TipoInsumoDao();
    private boolean confKeyEnter = true;

    /**
     * Creates new form TelaOrdenacaoInsumo
     */
    public TelaOrdenacaoInsumo() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        popupTabela();
        upperCase();
        pesquisarTipoInsumo();
    }

    public void pesquisarTipoInsumo() {
        String ordenacao = "";
        String sql = "select codigo, nome, ordenacao from tbTipoInsumo"
                + " where " + this.cbPesquisar + " like ?";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) this.tblOrdenacao.getModel();
        modelo.setNumRows(0);

        this.tblOrdenacao.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblOrdenacao.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblOrdenacao.getColumnModel().getColumn(2).setPreferredWidth(40);

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, "%" + this.txtPesquisarOrdenacao.getText() + "%");
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (rs.next()) {
                if (rs.getString(3).equals("999999999")) {
                    ordenacao = "";
                }else{
                    ordenacao = rs.getString(3);
                }
                modelo.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    ordenacao,});
                TipoInsumoDao.listaTipoInsumo.add(rs.getInt(3));
            }
            pst.close();
        } catch (java.sql.SQLException ex) {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void upperCase() {
        this.txtPesquisarOrdenacao.setDocument(new UpperCaseDocument());
    }

    private void fecharFramePesq() {
        if (this.framePesqInsumo != null) {
            this.framePesqInsumo.dispose();
        }
    }

    //abre o Popup com o botão direito do mouse e executa o metodo excluir
    private void popupTabela() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuItem1 = new JMenuItem("Remover");

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                removerTabela();
            }
        });

        popupMenu.add(menuItem1);
        this.tblOrdenacao.setComponentPopupMenu(popupMenu);
    }

    //evento para remover tblCadRecComponentes/ chama o metodo deletar componente
    private void removerTabela() {
        if (this.tblOrdenacao.getSelectedRow() != -1) {
            int setar = this.tblOrdenacao.getSelectedRow();
            int conf = Integer.parseInt(this.tblOrdenacao.getModel().getValueAt(setar, 0).toString());
            boolean confirmar = this.tipInsDao.contTipoInsumo(conf);
            if (confirmar == true) {
                JOptionPane.showMessageDialog(null, "Esse Tipo de insumo não pode ser removido.");
            } else {
                DefaultTableModel remov = (DefaultTableModel) this.tblOrdenacao.getModel();
                this.tipInsDao.deletarTipoInsumo();
                this.tblOrdenacao.removeAll();
                TipoInsumoDao.listaTipoInsumo.clear();
                pesquisarTipoInsumo();
            }
        }
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

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        cbPesquisarOrdenacao = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtPesquisarOrdenacao = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOrdenacao = new javax.swing.JTable();

        setBackground(new java.awt.Color(229, 247, 203));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        setClosable(true);
        setIconifiable(true);
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
                formInternalFrameDeiconified(evt);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBackground(new java.awt.Color(229, 247, 203));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Ordenação de Insumos");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jPanel2.setBackground(new java.awt.Color(229, 247, 203));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel2.add(btnFechar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 20));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 2, 40, 20));

        jPanel6.setBackground(new java.awt.Color(229, 247, 203));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnMinimi.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        btnMinimi.setForeground(new java.awt.Color(79, 79, 79));
        btnMinimi.setText("-");
        btnMinimi.setContentAreaFilled(false);
        btnMinimi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMinimi.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnMinimi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinimiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinimiMouseExited(evt);
            }
        });
        btnMinimi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinimiActionPerformed(evt);
            }
        });
        jPanel6.add(btnMinimi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 44, 20));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 2, 40, 20));

        cbPesquisarOrdenacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Descrição", "Código" }));
        cbPesquisarOrdenacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPesquisarOrdenacaoActionPerformed(evt);
            }
        });
        jPanel1.add(cbPesquisarOrdenacao, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 153, -1));

        jLabel7.setForeground(new java.awt.Color(79, 79, 79));
        jLabel7.setText("Pesquisar por:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        txtPesquisarOrdenacao.setVerifyInputWhenFocusTarget(false);
        txtPesquisarOrdenacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarOrdenacaoKeyReleased(evt);
            }
        });
        jPanel1.add(txtPesquisarOrdenacao, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 216, -1));

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/++++.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setContentAreaFilled(false);
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(428, 108, 20, -1));

        tblOrdenacao.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblOrdenacao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descrição", "Ordenação"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOrdenacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOrdenacaoMouseClicked(evt);
            }
        });
        tblOrdenacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblOrdenacaoKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblOrdenacao);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 583, 240));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBounds(129, 80, 602, 400);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseEntered
        // quando o mouse está em cima
        this.jPanel2.setBackground(new Color(211, 57, 33));
        this.btnFechar.setForeground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnFecharMouseEntered

    private void btnFecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseExited
        // quando o mouse sair de cima
        this.jPanel2.setBackground(new Color(229, 247, 203));
        this.btnFechar.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnFecharMouseExited

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // gerar mensagem de salvar antes de sair
//        if ((this.txtCadInsCodigo.getText().isEmpty()) && (this.txtCadInsDes.getText().isEmpty()) && ((this.txtCadInsPreco.getText().equals("0,00")) || (this.txtCadInsPreco.getText().equals(""))) && (this.txtCadInsQuant.getText().equals("0"))) {
//            this.setVisible(false);
//            this.dispose();
//        } else {
//            int result = JOptionPane.showConfirmDialog(null, "Deseja salvar antes de sair ?", "Atenção", JOptionPane.YES_NO_CANCEL_OPTION);
//            if (result == JOptionPane.YES_OPTION) {
//                confirmaAcao(true);
//            } else {
//                if (result == JOptionPane.NO_OPTION) {
        this.setVisible(false);
        this.dispose();
//                }
//            }
//        }
        fecharFramePesq();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnMinimiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimiMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel6, this.btnMinimi);
    }//GEN-LAST:event_btnMinimiMouseEntered

    private void btnMinimiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimiMouseExited
        // quando o mouse sair de cima
        this.jPanel6.setBackground(new Color(229, 247, 203));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnMinimiMouseExited

    private void btnMinimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimiActionPerformed
        try {
            // minimiza a tela
            this.setIcon(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(TelaCadInsumo.class.getName()).log(Level.SEVERE, null, ex);
        }
        fecharFramePesq();
    }//GEN-LAST:event_btnMinimiActionPerformed

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // quando o mouse sair de cima  
        this.jPanel6.setBackground(new Color(229, 247, 203));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameDeiconified

    private void cbPesquisarOrdenacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPesquisarOrdenacaoActionPerformed
        // adiciona um valor a variável cbPesquizar
        if (this.cbPesquisarOrdenacao.getSelectedItem().equals("Descrição")) {
            this.cbPesquisar = "nome";
        } else {
            if (this.cbPesquisarOrdenacao.getSelectedItem().equals("Código")) {
                this.cbPesquisar = "codigo";
            }
        }
    }//GEN-LAST:event_cbPesquisarOrdenacaoActionPerformed

    private void txtPesquisarOrdenacaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarOrdenacaoKeyReleased
        // chama o metodo pesquisar
        pesquisarTipoInsumo();
    }//GEN-LAST:event_txtPesquisarOrdenacaoKeyReleased

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // quando clicado adiciona o novo tipo
        if (!this.txtPesquisarOrdenacao.getText().trim().equals("")) {
            this.tipInsDao.addTipoInsumo(this.txtPesquisarOrdenacao.getText());
        } else {
            JOptionPane.showMessageDialog(null, "Tipo de insumo inválido.");
        }
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // quando o forme é inicializado
        this.txtPesquisarOrdenacao.requestFocus();
    }//GEN-LAST:event_formInternalFrameActivated

    private void tblOrdenacaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblOrdenacaoKeyReleased
        // Chama o metodo atualizar enquanto digita
        if (this.confKeyEnter == true) {
            int linha = this.tblOrdenacao.getSelectedRow();
            String nome = this.tblOrdenacao.getModel().getValueAt(linha, 1).toString();
            String confOrdenacao = this.tblOrdenacao.getModel().getValueAt(linha, 2).toString();
            if (nome.equals("")) {
                JOptionPane.showMessageDialog(null, "Descrição inválida.");
            } else {
                if (confOrdenacao.equals("")) {
                    JOptionPane.showMessageDialog(null, "Ordenação inválida.");
                } else {
                    int ordenacao = Integer.parseInt(this.tblOrdenacao.getModel().getValueAt(linha, 2).toString());
                    boolean confirma = false;
                    TipoInsumoDao.listaTipoInsumo.remove(linha);
                    for (int i = 0; i < TipoInsumoDao.listaTipoInsumo.size(); i++) {
                        if (TipoInsumoDao.listaTipoInsumo.get(i) == ordenacao) {
                            confirma = true;
                        }
                    }
                    if (confirma == true) {
                        JOptionPane.showMessageDialog(null, "Ordenação inválida.");
                    } else {
                        this.tipInsDao.atualizarTipoInsumo(Integer.parseInt(this.tblOrdenacao.getModel().getValueAt(linha, 0).toString()), nome, ordenacao);
                    }
                }
            }
            this.tblOrdenacao.removeAll();
            TipoInsumoDao.listaTipoInsumo.clear();
            pesquisarTipoInsumo();
            this.confKeyEnter = false;
        }
    }//GEN-LAST:event_tblOrdenacaoKeyReleased

    private void tblOrdenacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOrdenacaoMouseClicked
        // quando o mouse for clicado
        this.confKeyEnter = true;
    }//GEN-LAST:event_tblOrdenacaoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JComboBox<String> cbPesquisarOrdenacao;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable tblOrdenacao;
    private javax.swing.JTextField txtPesquisarOrdenacao;
    // End of variables declaration//GEN-END:variables
}
