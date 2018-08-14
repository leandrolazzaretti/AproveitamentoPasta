/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import dao.MovimentacaoEstoqueDao;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import util.UpperCaseDocument;
import util.Util;

/**
 *
 * @author Leandro
 */
public class FiltroRelatorioEstoquePasta extends javax.swing.JDialog {

    private int xMouse;
    private int yMouse;
    private final Util util = new Util();

    private String tipo = "";
    private String id = "";
    private String desc = "";
    private String cod = "";
    private String dataDe = "";
    private String dataAte = "";
    private String dataValidade = "= ''";
    private boolean confirmaValidade = false;

    /**
     * Creates new form FiltroRelatorioMovimentacao
     *
     * @param parent
     * @param modal
     */
    public FiltroRelatorioEstoquePasta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.rbNao.setSelected(true);
        upperCase();
    }

    private void emitirRelatorio(boolean confFiltro) {
        Relatorio relatorio = new Relatorio();

        if (confFiltro == true) {
            verificaFiltro();
            relatorio.relatorioEstoquePastaSetar(this.id, this.cod, this.desc, this.dataDe, this.dataAte, this.dataValidade, this.confirmaValidade);
        } else {
            relatorio.relatorioEstoquePastaSetar("", "", "", "", "", "= ''", false);
        }
        try {
            relatorio.gerarRelatorio();
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    private void verificaFiltro() {

        if (this.chDentroPrazo.isSelected() && this.chValidadeVencida.isSelected()) {
            this.dataValidade = "= ''";
            this.confirmaValidade = false;
        } else {
            if (this.chDentroPrazo.isSelected() && this.chVencimentoHoje.isSelected()) {
                this.dataValidade = ">= '" + this.util.dataAtual() + "'";
                this.confirmaValidade = false;
            } else {
                if (this.chValidadeVencida.isSelected() && this.chVencimentoHoje.isSelected()) {
                    this.dataValidade = "<= '" + this.util.dataAtual() + "'";
                    this.confirmaValidade = true;
                } else {
                    if (this.chDentroPrazo.isSelected()) {
                        this.dataValidade = ">='" + this.util.dataAtual() + "'";
                        this.confirmaValidade = false;
                    }
                    if (this.chValidadeVencida.isSelected()) {
                        this.dataValidade = "< '" + this.util.dataAtual() + "'";
                        this.confirmaValidade = true;
                    }
                    if (this.chVencimentoHoje.isSelected()) {
                        this.dataValidade = "= '" + this.util.dataAtual() + "'";
                        this.confirmaValidade = false;
                    }
                }
            }
        }

        if (this.chId.isSelected()) {
            this.id = this.txtFiltroId.getText();
        }
        if (this.chCodigo.isSelected()) {
            this.cod = this.txtFiltroCodigo.getText();
        }
        if (this.chDescricao.isSelected()) {
            this.desc = this.txtFiltroDescricao.getText();
        }
        if (this.chData.isSelected()) {
            MovimentacaoEstoqueDao movDao = new MovimentacaoEstoqueDao();
            this.dataDe = movDao.inverterData(this.txtDataDe.getText().replace("/", "-"));
            this.dataAte = movDao.inverterData(this.txtDataAte.getText().replace("/", "-"));
        }
    }

    private void ativarCampos() {
        this.chId.setEnabled(true);
        this.chCodigo.setEnabled(true);
        this.chDescricao.setEnabled(true);
        this.chData.setEnabled(true);
        this.chDentroPrazo.setEnabled(true);
        this.chValidadeVencida.setEnabled(true);
        this.chVencimentoHoje.setEnabled(true);
    }

    private void desativarCampos() {
        this.chId.setEnabled(false);
        this.chCodigo.setEnabled(false);
        this.chDescricao.setEnabled(false);
        this.chData.setEnabled(false);
        this.chDentroPrazo.setEnabled(false);
        this.chValidadeVencida.setEnabled(false);
        this.chVencimentoHoje.setEnabled(false);

        this.txtFiltroId.setEnabled(false);
        this.txtFiltroCodigo.setEnabled(false);
        this.txtFiltroDescricao.setEnabled(false);
        this.txtDataDe.setEnabled(false);
        this.txtDataAte.setEnabled(false);
    }
    
    //chama o método para tornar as letras do campo Maiusculas
    private void upperCase(){
        this.txtFiltroDescricao.setDocument(new UpperCaseDocument());
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
        txtFiltroCodigo = new javax.swing.JTextField();
        txtFiltroDescricao = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        rbNao = new javax.swing.JRadioButton();
        rbSim = new javax.swing.JRadioButton();
        chData = new javax.swing.JCheckBox();
        chId = new javax.swing.JCheckBox();
        chCodigo = new javax.swing.JCheckBox();
        chDescricao = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnEmitir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtDataAte = new javax.swing.JFormattedTextField();
        txtDataDe = new javax.swing.JFormattedTextField();
        txtFiltroId = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        chDentroPrazo = new javax.swing.JCheckBox();
        chValidadeVencida = new javax.swing.JCheckBox();
        chVencimentoHoje = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        txtFiltroCodigo.setEnabled(false);
        jPanel1.add(txtFiltroCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 170, -1));

        txtFiltroDescricao.setEnabled(false);
        jPanel1.add(txtFiltroDescricao, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 170, -1));

        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Data:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 40, -1));

        jLabel2.setForeground(new java.awt.Color(79, 79, 79));
        jLabel2.setText("ID:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 60, -1));

        jLabel3.setForeground(new java.awt.Color(79, 79, 79));
        jLabel3.setText("Codigo:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 40, -1));

        jLabel4.setForeground(new java.awt.Color(79, 79, 79));
        jLabel4.setText("Descrição:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 50, -1));

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

        chData.setContentAreaFilled(false);
        chData.setEnabled(false);
        chData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chDataActionPerformed(evt);
            }
        });
        jPanel1.add(chData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        chId.setContentAreaFilled(false);
        chId.setEnabled(false);
        chId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chIdActionPerformed(evt);
            }
        });
        jPanel1.add(chId, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, 20));

        chCodigo.setContentAreaFilled(false);
        chCodigo.setEnabled(false);
        chCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chCodigoActionPerformed(evt);
            }
        });
        jPanel1.add(chCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        chDescricao.setContentAreaFilled(false);
        chDescricao.setEnabled(false);
        chDescricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chDescricaoActionPerformed(evt);
            }
        });
        jPanel1.add(chDescricao, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, -1));

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

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 360, 80, 25));

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

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 80, 25));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(79, 79, 79));
        jLabel5.setText("Relatório Estoque Pasta");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel6.setBackground(new java.awt.Color(143, 165, 110));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Até:");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, -1, -1));

        jLabel7.setForeground(new java.awt.Color(79, 79, 79));
        jLabel7.setText("De:");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, -1, -1));

        try {
            txtDataAte.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataAte.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDataAte.setEnabled(false);
        jPanel6.add(txtDataAte, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 48, 110, -1));

        try {
            txtDataDe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataDe.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDataDe.setEnabled(false);
        jPanel6.add(txtDataDe, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 110, -1));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 170, 80));

        txtFiltroId.setEnabled(false);
        jPanel1.add(txtFiltroId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 170, -1));

        jPanel7.setBackground(new java.awt.Color(143, 165, 110));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), "Situação validade", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(79, 79, 79))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chDentroPrazo.setBackground(new java.awt.Color(143, 165, 110));
        chDentroPrazo.setForeground(new java.awt.Color(79, 79, 79));
        chDentroPrazo.setText("Dentro do prazo");
        chDentroPrazo.setEnabled(false);
        jPanel7.add(chDentroPrazo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        chValidadeVencida.setBackground(new java.awt.Color(143, 165, 110));
        chValidadeVencida.setForeground(new java.awt.Color(79, 79, 79));
        chValidadeVencida.setText("Vencida");
        chValidadeVencida.setEnabled(false);
        jPanel7.add(chValidadeVencida, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        chVencimentoHoje.setBackground(new java.awt.Color(143, 165, 110));
        chVencimentoHoje.setForeground(new java.awt.Color(79, 79, 79));
        chVencimentoHoje.setText("Vencimento hoje");
        chVencimentoHoje.setEnabled(false);
        jPanel7.add(chVencimentoHoje, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 300, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 338, 410));

        setSize(new java.awt.Dimension(338, 411));
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

    private void chDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chDataActionPerformed
        // ativa/desativa filtro codigo
        if (this.chData.isSelected()) {
            this.txtDataDe.setEnabled(true);
            this.txtDataAte.setEnabled(true);
        } else {
            this.txtDataDe.setEnabled(false);
            this.txtDataAte.setEnabled(false);
        }
    }//GEN-LAST:event_chDataActionPerformed

    private void chIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chIdActionPerformed
        // ativa/desativa filtro codigo
        if (this.chId.isSelected()) {
            this.txtFiltroId.setEnabled(true);
        } else {
            this.txtFiltroId.setEnabled(false);
        }
    }//GEN-LAST:event_chIdActionPerformed

    private void chCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chCodigoActionPerformed
        // ativa/desativa filtro nome
        if (this.chCodigo.isSelected()) {
            this.txtFiltroCodigo.setEnabled(true);
        } else {
            this.txtFiltroCodigo.setEnabled(false);
        }
    }//GEN-LAST:event_chCodigoActionPerformed

    private void chDescricaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chDescricaoActionPerformed
        // ativa/desativa filtro login
        if (this.chDescricao.isSelected()) {
            this.txtFiltroDescricao.setEnabled(true);
        } else {
            this.txtFiltroDescricao.setEnabled(false);
        }
    }//GEN-LAST:event_chDescricaoActionPerformed

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
        // faz ação quando ao fechar a tela
        TelaPrincipal.jDialogEstoquePasta = false;
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
            java.util.logging.Logger.getLogger(FiltroRelatorioEstoquePasta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FiltroRelatorioEstoquePasta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FiltroRelatorioEstoquePasta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FiltroRelatorioEstoquePasta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FiltroRelatorioEstoquePasta dialog = new FiltroRelatorioEstoquePasta(new javax.swing.JFrame(), true);
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
    private javax.swing.JCheckBox chCodigo;
    private javax.swing.JCheckBox chData;
    private javax.swing.JCheckBox chDentroPrazo;
    private javax.swing.JCheckBox chDescricao;
    private javax.swing.JCheckBox chId;
    private javax.swing.JCheckBox chValidadeVencida;
    private javax.swing.JCheckBox chVencimentoHoje;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton rbNao;
    private javax.swing.JRadioButton rbSim;
    private javax.swing.JFormattedTextField txtDataAte;
    private javax.swing.JFormattedTextField txtDataDe;
    private javax.swing.JTextField txtFiltroCodigo;
    private javax.swing.JTextField txtFiltroDescricao;
    private javax.swing.JTextField txtFiltroId;
    // End of variables declaration//GEN-END:variables
}
