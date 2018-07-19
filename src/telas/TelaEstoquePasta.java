/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.sun.glass.events.KeyEvent;
import dao.EstoquePastaDao;
import dao.EstoquePastaFinalDao;
import dao.MovimentacaoEstoqueDao;
import dao.ReceitaDao;
import java.awt.Color;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import util.MascaraMoeda;
import util.SoNumeros;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaEstoquePasta extends javax.swing.JInternalFrame {

    public static JInternalFrame framePesReceita;
    TelaPesquisarReceita rec = new TelaPesquisarReceita();
    Util util = new Util();
    EstoquePastaDao estPas = new EstoquePastaDao();
    EstoquePastaFinalDao estPasFinal = new EstoquePastaFinalDao();
    public static boolean jDialogSobre;

    /**
     * Creates new form TelaEstoquePasta
     */
    public TelaEstoquePasta() {
        initComponents();
        this.txtCodigo.setDocument(new SoNumeros());
        this.txtQuantidade.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        this.txtQuantidade.setDocument(new MascaraMoeda());
    }

    private void limparCampos() {
        this.txtCodigo.setText(null);
        this.txtCodigo.setEnabled(true);
        this.txtCodigo.requestFocus();
        this.txtQuantidade.setDocument(new MascaraMoeda());
        this.txtQuantidade.setEnabled(false);
        ((DefaultTableModel) this.tblProducaoPastaOp1.getModel()).setRowCount(0);
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
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtQuantidade = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        btnLimpar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnConfirmar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProducaoPastaOp2 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducaoPastaOp1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Estoque Pasta");
        setToolTipText("");
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
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(776, 2, 40, 20));

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

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(816, 2, 40, 20));

        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Qual pasta pruduzir ?");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 54, -1, 30));

        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });
        jPanel1.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 150, -1));

        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnPesquisar.setToolTipText("Pesquisar");
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });
        jPanel1.add(btnPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 30, 30));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(79, 79, 79));
        jLabel2.setText("Produção de Pasta");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jLabel3.setForeground(new java.awt.Color(79, 79, 79));
        jLabel3.setText("Quantidade:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, -1, -1));

        txtQuantidade.setEnabled(false);
        txtQuantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQuantidadeKeyPressed(evt);
            }
        });
        jPanel1.add(txtQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 130, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLimpar.setForeground(new java.awt.Color(79, 79, 79));
        btnLimpar.setText("Limpar");
        btnLimpar.setContentAreaFilled(false);
        btnLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLimparMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLimparMouseExited(evt);
            }
        });
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        jPanel4.add(btnLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 25));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 180, 100, 25));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnConfirmar.setForeground(new java.awt.Color(79, 79, 79));
        btnConfirmar.setText("Confirmar");
        btnConfirmar.setContentAreaFilled(false);
        btnConfirmar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConfirmarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConfirmarMouseExited(evt);
            }
        });
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });
        jPanel3.add(btnConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 24));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 100, 25));

        jLabel4.setForeground(new java.awt.Color(79, 79, 79));
        jLabel4.setText("Código:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jPanel5.setBackground(new java.awt.Color(236, 255, 209));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(155, 155, 155)));

        tblProducaoPastaOp2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblProducaoPastaOp2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Código", "Descrição", "Estoque ", "Usar", "Equivale à (%)", "Vencimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProducaoPastaOp2.setPreferredSize(new java.awt.Dimension(520, 0));
        jScrollPane2.setViewportView(tblProducaoPastaOp2);

        tblProducaoPastaOp1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblProducaoPastaOp1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Código ", "Descrição", "Estoque", "Usar ", "Equivale à (%)", "Vencimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblProducaoPastaOp1);

        jLabel6.setText("Primeira opção - Utilizando apenas Pastas do estoque:");

        jLabel7.setText("Segunda opção - Utilizando Pastas mais Insumos:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(170, 170, 170)
                .addComponent(jLabel7)
                .addContainerGap(180, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 858, 330));

        jLabel5.setForeground(new java.awt.Color(79, 79, 79));
        jLabel5.setText("Kg");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 134, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 861, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

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
    }//GEN-LAST:event_btnMinimiActionPerformed

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
//        // gerar mensagem de salvar antes de sair
        this.dispose();
//        }
    }//GEN-LAST:event_btnFecharActionPerformed

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // quando a tela sair do minimizar
        this.jPanel6.setBackground(new Color(229, 247, 203));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
        this.txtCodigo.requestFocus();
    }//GEN-LAST:event_formInternalFrameDeiconified

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        // chama a tela de pesquisa de resceita
        if (this.framePesReceita == null) {
            this.framePesReceita = new TelaPesquisarReceita();
        } else {
            this.framePesReceita.dispose();
            this.framePesReceita = new TelaPesquisarReceita();
        }
        this.rec.pesquisarReceita();
        this.util.comandoInternal(this.framePesReceita);
        TelaPesquisarReceita.confirmarEscolha = 3;
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void btnLimparMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel4, this.btnLimpar);
    }//GEN-LAST:event_btnLimparMouseEntered

    private void btnLimparMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel4, this.btnLimpar);
    }//GEN-LAST:event_btnLimparMouseExited

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // chama o metodo limpar
        //alteraCorPressionado(this.jPanel4, this.btnLimpar);
        limparCampos();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnConfirmarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmarMouseEntered
        /// quando o mouse estiver em cima
        alteraCor(this.jPanel3, this.btnConfirmar);
    }//GEN-LAST:event_btnConfirmarMouseEntered

    private void btnConfirmarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmarMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel3, this.btnConfirmar);
    }//GEN-LAST:event_btnConfirmarMouseExited

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        //chama o metodo para dar entrado ou saida em insumo ou pasta
        //alteraCorPressionado(this.jPanel3, this.btnConfirmar);
        if (!this.txtQuantidade.getText().equals("") && (!this.txtCodigo.getText().equals(""))) {
            int codigo = Integer.parseInt(this.txtCodigo.getText());
            double quantidade = Double.parseDouble(this.txtQuantidade.getText().replace(".", "").replace(",", "."));

            this.tblProducaoPastaOp1.removeAll();
            this.tblProducaoPastaOp2.removeAll();
            this.estPasFinal.limparVariaveis();
            this.estPasFinal.pastaCompativel(this.estPasFinal.buscaCodigoInsumos(codigo));
            this.estPasFinal.buscarInsumos(codigo, quantidade, true);
            this.estPasFinal.subtrairInsumos(codigo);
            this.estPasFinal.setarTabela(tblProducaoPastaOp1);

            this.tblProducaoPastaOp1.setEnabled(true);
            this.tblProducaoPastaOp1.setVisible(true);
//            MovimentacaoEstoqueDao movDao = new MovimentacaoEstoqueDao();
//            movDao.producaoPasta(this.tblProducaoPastaOp1, movDao.buscaCodigoInsumos(Integer.parseInt(this.txtCodigo.getText())), false, Integer.parseInt(this.txtCodigo.getText()));
//            double[] confirma = movDao.quantoUsarOpc1(this.tblProducaoPastaOp1, Integer.parseInt(this.txtCodigo.getText()), false);
//            this.tblProducaoPastaOp1.setEnabled(true);
//            this.tblProducaoPastaOp1.setVisible(true);
//            if (confirma[4] > 0) {
//                movDao.quantoUsarOpc1_2(this.tblProducaoPastaOp1, confirma);
//            }
////            if (confirma[4] > 0) {
////                this.tblProducaoPastaOp1.setEnabled(false);
////                this.tblProducaoPastaOp1.setVisible(false);
////            } else {
////                this.tblProducaoPastaOp1.setEnabled(true);
////                this.tblProducaoPastaOp1.setVisible(true);
////            }
////            movDao.quantoUsarOpc1_2(this.tblProducaoPastaOp1);
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }

        //confirmaAcao(false);
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        // chama metodo ao pressionar a tecla Enter  
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!this.txtCodigo.getText().equals("")) {
                ReceitaDao pesq = new ReceitaDao();
                pesq.pesquisarProducaoPasta(Integer.parseInt(this.txtCodigo.getText()));

            }
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // quando abrir a tela o campo de código recebe o foco
        this.txtCodigo.requestFocus();
    }//GEN-LAST:event_formInternalFrameActivated

    private void txtQuantidadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantidadeKeyPressed
        // quando ENTER é pressionado
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!this.txtQuantidade.getText().equals("")) {
                int codigo = Integer.parseInt(this.txtCodigo.getText());
                double quantidade = Double.parseDouble(this.txtQuantidade.getText().replace(".", "").replace(",", "."));

                this.tblProducaoPastaOp1.removeAll();
                this.tblProducaoPastaOp2.removeAll();
                this.estPasFinal.limparVariaveis();
                this.estPasFinal.pastaCompativel(this.estPasFinal.buscaCodigoInsumos(codigo));
                this.estPasFinal.buscarInsumos(codigo, quantidade, true);
                this.estPasFinal.subtrairInsumos(codigo);
                this.estPasFinal.setarTabela(tblProducaoPastaOp1);

                this.tblProducaoPastaOp1.setEnabled(true);
                this.tblProducaoPastaOp1.setVisible(true);

//                MovimentacaoEstoqueDao movDao = new MovimentacaoEstoqueDao();
//                movDao.producaoPasta(this.tblProducaoPastaOp1, movDao.buscaCodigoInsumos(Integer.parseInt(this.txtCodigo.getText())), false, Integer.parseInt(this.txtCodigo.getText()));
//                double[] confirma = movDao.quantoUsarOpc1(this.tblProducaoPastaOp1, Integer.parseInt(this.txtCodigo.getText()), false);
//                this.tblProducaoPastaOp1.setEnabled(true);
//                this.tblProducaoPastaOp1.setVisible(true);
//                if (confirma[4] > 0) {
//                    movDao.quantoUsarOpc1_2(this.tblProducaoPastaOp1, confirma);
//                }
//
////                if (confirma[4] > 0) {
////                    this.tblProducaoPastaOp1.setEnabled(false);
////                    this.tblProducaoPastaOp1.setVisible(false);
////                } else {
////                    this.tblProducaoPastaOp1.setEnabled(true);
////                    this.tblProducaoPastaOp1.setVisible(true);
////                }
////                movDao.quantoUsarOpc1_2(this.tblProducaoPastaOp1);
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            }
        }
    }//GEN-LAST:event_txtQuantidadeKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable tblProducaoPastaOp1;
    public static javax.swing.JTable tblProducaoPastaOp2;
    public static javax.swing.JTextField txtCodigo;
    public static javax.swing.JFormattedTextField txtQuantidade;
    // End of variables declaration//GEN-END:variables
}
