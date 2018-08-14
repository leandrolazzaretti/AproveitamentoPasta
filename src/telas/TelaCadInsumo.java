/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.sun.glass.events.KeyEvent;
import conexao.ModuloConexao;
import dao.InsumoDao;
import dao.TipoInsumoDao;
import dto.InsumoDto;
import java.awt.Color;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import util.ComboKeyHandler;
import util.MascaraMoeda;
import util.SoNumeros;
import util.UpperCaseDocument;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaCadInsumo extends javax.swing.JInternalFrame {

    private Connection conexao = null;
    public static JInternalFrame framePesqInsumo;
    private final Util util = new Util();
    private final TipoInsumoDao tipoInsumoDao = new TipoInsumoDao();
    private final TelaPesquisarInsumos insumo = new TelaPesquisarInsumos();
    private boolean confirmaMascaraMoeda = false;

    /**
     * Creates new form TelaCadInsumos
     */
    public TelaCadInsumo() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        this.txtCadInsCodigo.setDocument(new SoNumeros());
        this.txtCadInsPreco.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        this.txtCadInsPreco.setDocument(new MascaraMoeda());
        //this.txtCadInsPreco.setDocument(new SoNumeros()); 
        this.txtCadInsQuant.setDocument(new SoNumeros());
        limparCampos();
        upperCase();
        this.tipoInsumoDao.setarComboBox(cbTipoInsumo);
        this.cbTipoInsumo.setSelectedItem(null);
        cbAtivar();
    }

    private void confirmar(boolean confirmar) {
        InsumoDto insumoDto = new InsumoDto();
        InsumoDao insumoDao = new InsumoDao();

        insumoDto.setCodigo(Integer.parseInt(this.txtCadInsCodigo.getText()));
        insumoDto.setCodigoTipoInsumo(this.tipoInsumoDao.buscaCodTipoInsumo(this.cbTipoInsumo.getSelectedItem().toString()));
        insumoDto.setDescricao(this.txtCadInsDes.getText());
        insumoDto.setUM(this.cbCadInsUm.getSelectedItem().toString());

        insumoDto.setQuantidade(this.txtCadInsQuant.getText().replace(".", "").replace(",", "."));
        insumoDto.setPreco(this.txtCadInsPreco.getText().replace(".", "").replace(",", "."));

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
        this.cbTipoInsumo.setSelectedItem("");
        this.cbCadInsUm.setSelectedItem("kg");
        this.txtCadInsQuant.setText("0");
        this.txtCadInsPreco.setText(null);
        this.txtCadInsPreco.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        this.txtCadInsPreco.setDocument(new MascaraMoeda());
        this.txtCadInsCodigo.requestFocus();
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
        if ((this.txtCadInsCodigo.getText().isEmpty()) || (this.txtCadInsDes.getText().isEmpty()) || (this.cbTipoInsumo.getSelectedItem().equals("")) || (this.txtCadInsPreco.getText().isEmpty()) || (this.txtCadInsQuant.getText().isEmpty())) {
            return false;
        } else {
            return true;
        }
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

    private void upperCase() {
        this.txtCadInsDes.setDocument(new UpperCaseDocument());
    }

    private void fecharFramePesq() {
        if (this.framePesqInsumo != null) {
            this.framePesqInsumo.dispose();
        }
    }

    private void cbAtivar() {
        JTextField text = (JTextField) this.cbTipoInsumo.getEditor().getEditorComponent();
        text.addKeyListener(new ComboKeyHandler(this.cbTipoInsumo));
        text.setDocument(new UpperCaseDocument());
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

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        txtCadInsPreco = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCadInsDes = new javax.swing.JTextField();
        txtCadInsQuant = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        cbCadInsUm = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCadInsCodigo = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnCadInsAdicionar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnCadInsLimpar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnCadInsDeletar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cbTipoInsumo = new javax.swing.JComboBox<>();
        btnAdicionarTipoInsumo = new javax.swing.JButton();
        btnExcluirTipoInsumo = new javax.swing.JButton();

        setBackground(new java.awt.Color(229, 247, 203));
        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Insumos");
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

        jPanel2.setBackground(new java.awt.Color(229, 247, 203));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel2.setForeground(new java.awt.Color(79, 79, 79));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(229, 247, 203));
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

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 2, 40, 20));

        txtCadInsPreco.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCadInsPrecoFocusLost(evt);
            }
        });
        txtCadInsPreco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCadInsPrecoKeyPressed(evt);
            }
        });
        jPanel2.add(txtCadInsPreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, 270, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Cadastro de Insumos");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, -1, -1));

        txtCadInsDes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCadInsDesKeyPressed(evt);
            }
        });
        jPanel2.add(txtCadInsDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 270, -1));

        txtCadInsQuant.setEnabled(false);
        jPanel2.add(txtCadInsQuant, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 270, -1));

        jButton1.setForeground(new java.awt.Color(53, 53, 53));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        jButton1.setToolTipText("Pesquisar");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, 30, 30));

        cbCadInsUm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "kg", "L" }));
        jPanel2.add(cbCadInsUm, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 180, -1));

        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Código:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, -1, -1));

        jLabel5.setForeground(new java.awt.Color(79, 79, 79));
        jLabel5.setText("Preço:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, -1, -1));

        jLabel4.setForeground(new java.awt.Color(79, 79, 79));
        jLabel4.setText("Quantidade:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, -1, -1));

        jLabel3.setForeground(new java.awt.Color(79, 79, 79));
        jLabel3.setText("Unidade de Medida (UM):");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, -1));

        jLabel2.setForeground(new java.awt.Color(79, 79, 79));
        jLabel2.setText("Descrição:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

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
        jPanel2.add(txtCadInsCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 140, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCadInsAdicionar.setForeground(new java.awt.Color(66, 66, 66));
        btnCadInsAdicionar.setText("Salvar");
        btnCadInsAdicionar.setContentAreaFilled(false);
        btnCadInsAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadInsAdicionarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadInsAdicionarMouseExited(evt);
            }
        });
        btnCadInsAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsAdicionarActionPerformed(evt);
            }
        });
        jPanel3.add(btnCadInsAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 360, 80, 25));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCadInsLimpar.setForeground(new java.awt.Color(66, 66, 66));
        btnCadInsLimpar.setText("Limpar");
        btnCadInsLimpar.setContentAreaFilled(false);
        btnCadInsLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadInsLimparMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadInsLimparMouseExited(evt);
            }
        });
        btnCadInsLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsLimparActionPerformed(evt);
            }
        });
        jPanel4.add(btnCadInsLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 360, 80, 25));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCadInsDeletar.setForeground(new java.awt.Color(66, 66, 66));
        btnCadInsDeletar.setText("Eliminar");
        btnCadInsDeletar.setContentAreaFilled(false);
        btnCadInsDeletar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadInsDeletarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadInsDeletarMouseExited(evt);
            }
        });
        btnCadInsDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInsDeletarActionPerformed(evt);
            }
        });
        jPanel5.add(btnCadInsDeletar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 360, 80, 25));

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
        btnMinimi.getAccessibleContext().setAccessibleDescription("");

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 2, 40, 20));

        jLabel7.setForeground(new java.awt.Color(79, 79, 79));
        jLabel7.setText("Tipo de insumo:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, -1, -1));

        cbTipoInsumo.setEditable(true);
        jPanel2.add(cbTipoInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 180, -1));

        btnAdicionarTipoInsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/++++.png"))); // NOI18N
        btnAdicionarTipoInsumo.setToolTipText("Adicionar");
        btnAdicionarTipoInsumo.setContentAreaFilled(false);
        btnAdicionarTipoInsumo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionarTipoInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarTipoInsumoActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdicionarTipoInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 188, 20, -1));

        btnExcluirTipoInsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/x.png"))); // NOI18N
        btnExcluirTipoInsumo.setToolTipText("Apagar");
        btnExcluirTipoInsumo.setContentAreaFilled(false);
        btnExcluirTipoInsumo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluirTipoInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirTipoInsumoActionPerformed(evt);
            }
        });
        jPanel2.add(btnExcluirTipoInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 188, 20, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
        );

        setBounds(148, 72, 564, 416);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadInsAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsAdicionarActionPerformed
        //chama o metodo adicionar/salvar
        // altera a cor quando pressionado
        //alteraCorPressionado(this.jPanel3, this.btnCadInsAdicionar);
        confirmaAcao(false);
    }//GEN-LAST:event_btnCadInsAdicionarActionPerformed

    private void btnCadInsDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInsDeletarActionPerformed
        // chama o metodo deletar
        // altera a cor quando pressionado
        //alteraCorPressionado(this.jPanel5, this.btnCadInsDeletar);
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
        this.txtCadInsCodigo.requestFocus();
    }//GEN-LAST:event_btnCadInsLimparActionPerformed
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Chama a TelePesquisarInsumos

        if (this.framePesqInsumo == null) {
            this.framePesqInsumo = new TelaPesquisarInsumos();
        } else {
            this.framePesqInsumo.dispose();
            this.framePesqInsumo = new TelaPesquisarInsumos();
        }
        this.util.comandoInternal2(this.framePesqInsumo);
        this.insumo.pesquisarInsumos();
        TelaPesquisarInsumos.confimaTela = true;
        TelaPesquisarInsumos.confirmarEscolha = true;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCadInsCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCadInsCodigoFocusLost
        //evento ao sair do campo ele busca os dados de determinado codigo
        if (!this.txtCadInsCodigo.getText().equals("")) {
            InsumoDao pesq = new InsumoDao();
            pesq.pesquisarInsumos(Integer.parseInt(this.txtCadInsCodigo.getText()));
        }
    }//GEN-LAST:event_txtCadInsCodigoFocusLost

    private void txtCadInsCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadInsCodigoKeyPressed
        // chama o metodo atraves da tecla enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!this.txtCadInsCodigo.getText().equals("")) {
                InsumoDao pesq = new InsumoDao();
                pesq.pesquisarInsumos(Integer.parseInt(this.txtCadInsCodigo.getText()));
            }
            this.txtCadInsDes.requestFocus();
        }
    }//GEN-LAST:event_txtCadInsCodigoKeyPressed

    private void btnFecharMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseEntered
        // quando o mouse está em cima
        this.jPanel1.setBackground(new Color(211, 57, 33));
        this.btnFechar.setForeground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnFecharMouseEntered

    private void btnFecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseExited
        // quando o mouse sair de cima
        this.jPanel1.setBackground(new Color(229, 247, 203));
        this.btnFechar.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnFecharMouseExited

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // gerar mensagem de salvar antes de sair
        if ((this.txtCadInsCodigo.getText().isEmpty()) && (this.txtCadInsDes.getText().isEmpty()) && ((this.txtCadInsPreco.getText().equals("0,00")) || (this.txtCadInsPreco.getText().equals(""))) && (this.txtCadInsQuant.getText().equals("0"))) {
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
                }
            }
        }
        fecharFramePesq();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnCadInsLimparMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadInsLimparMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel4, this.btnCadInsLimpar);
    }//GEN-LAST:event_btnCadInsLimparMouseEntered

    private void btnCadInsLimparMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadInsLimparMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel4, this.btnCadInsLimpar);
    }//GEN-LAST:event_btnCadInsLimparMouseExited

    private void btnCadInsAdicionarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadInsAdicionarMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel3, this.btnCadInsAdicionar);
    }//GEN-LAST:event_btnCadInsAdicionarMouseEntered

    private void btnCadInsAdicionarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadInsAdicionarMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel3, this.btnCadInsAdicionar);
    }//GEN-LAST:event_btnCadInsAdicionarMouseExited

    private void btnCadInsDeletarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadInsDeletarMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel5, this.btnCadInsDeletar);
    }//GEN-LAST:event_btnCadInsDeletarMouseEntered

    private void btnCadInsDeletarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadInsDeletarMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel5, this.btnCadInsDeletar);
    }//GEN-LAST:event_btnCadInsDeletarMouseExited

    private void btnMinimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimiActionPerformed
        try {
            // minimiza a tela
            this.setIcon(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(TelaCadInsumo.class.getName()).log(Level.SEVERE, null, ex);
        }
        fecharFramePesq();
    }//GEN-LAST:event_btnMinimiActionPerformed

    private void btnMinimiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimiMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel6, this.btnMinimi);
    }//GEN-LAST:event_btnMinimiMouseEntered

    private void btnMinimiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimiMouseExited
        // quando o mouse sair de cima  
        this.jPanel6.setBackground(new Color(229, 247, 203));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnMinimiMouseExited

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // passa o foco para o campo de texto 
        this.txtCadInsCodigo.requestFocus();
    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // passa o foco para o campo de texto 
        this.txtCadInsCodigo.requestFocus();
        // quando o mouse sair de cima  
        this.jPanel6.setBackground(new Color(229, 247, 203));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameDeiconified

    private void txtCadInsDesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadInsDesKeyPressed
        // quando ENTER é pressionado
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.cbTipoInsumo.requestFocus();
        }
    }//GEN-LAST:event_txtCadInsDesKeyPressed

    private void txtCadInsPrecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadInsPrecoKeyPressed
        // quando ENTER é pressionado
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            confirmaAcao(false);
            this.confirmaMascaraMoeda = true;
        }
        if (this.confirmaMascaraMoeda == false) {
            this.txtCadInsPreco.setDocument(new MascaraMoeda());
            this.confirmaMascaraMoeda = true;
        }
    }//GEN-LAST:event_txtCadInsPrecoKeyPressed

    private void txtCadInsPrecoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCadInsPrecoFocusLost
        // quando o campo perder o foco
        this.confirmaMascaraMoeda = false;
    }//GEN-LAST:event_txtCadInsPrecoFocusLost

    private void btnAdicionarTipoInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarTipoInsumoActionPerformed
        // chama o metodo adicionar tipo de pasta
        if (this.cbTipoInsumo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Tipo de insumo inválido.");
        } else {

            boolean conf = this.tipoInsumoDao.buscaCodTipoInsumo(this.cbTipoInsumo.getSelectedItem().toString()) <= 0;
            if (conf == false) {
                JOptionPane.showMessageDialog(null, "Tipo de insumo já existe.");
            } else {
                String pastaAdicionada = this.cbTipoInsumo.getSelectedItem().toString();
                this.tipoInsumoDao.addTipoInsumo(this.cbTipoInsumo.getSelectedItem().toString());
                this.cbTipoInsumo.removeAllItems();
                this.tipoInsumoDao.setarComboBox(cbTipoInsumo);
                cbAtivar();
                this.cbTipoInsumo.setSelectedItem(pastaAdicionada);
            }
        }
    }//GEN-LAST:event_btnAdicionarTipoInsumoActionPerformed

    private void btnExcluirTipoInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirTipoInsumoActionPerformed
        //Chama o metodo remover tipo de pasta
        if (this.cbTipoInsumo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Tipo de insumo inválido.");
        } else {
            int conf = this.tipoInsumoDao.buscaCodTipoInsumo(this.cbTipoInsumo.getSelectedItem().toString());
            boolean confirmar = this.tipoInsumoDao.contTipoInsumo(conf);
            if (confirmar == true) {
                JOptionPane.showMessageDialog(null, "Esse Tipo de insumo não pode ser removido.");
            } else {
                this.tipoInsumoDao.removeTipoInsumo(this.cbTipoInsumo.getSelectedItem().toString());
                this.cbTipoInsumo.removeAllItems();
                this.tipoInsumoDao.setarComboBox(cbTipoInsumo);
                cbAtivar();
            }
        }
    }//GEN-LAST:event_btnExcluirTipoInsumoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarTipoInsumo;
    private javax.swing.JButton btnCadInsAdicionar;
    private javax.swing.JButton btnCadInsDeletar;
    private javax.swing.JButton btnCadInsLimpar;
    private javax.swing.JButton btnExcluirTipoInsumo;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnMinimi;
    public static javax.swing.JComboBox<String> cbCadInsUm;
    public static javax.swing.JComboBox<String> cbTipoInsumo;
    private javax.swing.JButton jButton1;
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
    public static javax.swing.JTextField txtCadInsCodigo;
    public static javax.swing.JTextField txtCadInsDes;
    public static javax.swing.JFormattedTextField txtCadInsPreco;
    public static javax.swing.JTextField txtCadInsQuant;
    // End of variables declaration//GEN-END:variables
}
