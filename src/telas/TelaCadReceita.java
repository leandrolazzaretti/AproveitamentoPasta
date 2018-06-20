/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.sun.glass.events.KeyEvent;
import conexao.ModuloConexao;
import dao.ReceitaDao;
import dao.ReceitaInsumoDao;
import dao.TipoPastaDao;
import dto.ReceitaDto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import util.SoNumeros;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaCadReceita extends javax.swing.JInternalFrame {

    Connection conexao = null;
    public int codigoTipo;
    private String descIns;
    TipoPastaDao pasta = new TipoPastaDao();
    ReceitaInsumoDao recInsDao = new ReceitaInsumoDao();
    TelaPesquisarReceita rec = new TelaPesquisarReceita();
    Util frame = new Util();

    public static JInternalFrame framePesInsumo;
    public static JInternalFrame framePesReceita;

    /**
     * Creates new form TelaCadReceita
     */
    public TelaCadReceita() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        this.txtCadRecCodigo.setDocument(new SoNumeros());
        this.txtCadRecVal.setDocument(new SoNumeros());
        this.txtCadRecConsumo.setDocument(new SoNumeros());
        this.tblCadRecComponentes.getColumnModel().getColumn(1).setPreferredWidth(20);
        popupTabela();
        mascaraConsu();
        this.pasta.setarComboBox();
        AutoCompleteDecorator.decorate(cbCadReceitaTipo);
        this.cbCadReceitaTipo.setSelectedItem(null);

    }

    private void confirmar(boolean confirmar) {
        ReceitaDto receitaDto = new ReceitaDto();
        ReceitaDao receitaDao = new ReceitaDao();

        receitaDto.setCodigo(Integer.parseInt(this.txtCadRecCodigo.getText()));
        receitaDto.setDescricao(this.txtCadRecDes.getText());
        receitaDto.setPantone(this.txtCadRecPan.getText());
        receitaDto.setTipo(this.pasta.buscaCodTipoPasta(this.cbCadReceitaTipo.getSelectedItem().toString()));
        receitaDto.setVencimento(Integer.parseInt(this.txtCadRecVal.getText()));

        if (confirmar == true) {
            receitaDao.adicionarReceita(receitaDto);

        } else {
            receitaDao.atualizarReceita(receitaDto, Integer.parseInt(this.txtCadRecCodigo.getText()));
        }
    }

    private void limparCampos() {
        this.txtCadRecCodigo.setEnabled(true);
        this.txtCadRecCodigo.setText(null);
        this.txtCadRecDes.setText(null);
        this.txtCadRecPan.setText(null);
        this.txtCadRecVal.setText(null);
        this.txtCadRecComponentes.setText(null);
        this.txtCadRecConsumo.setValue(null);
        ((DefaultTableModel) this.tblCadRecComponentes.getModel()).setRowCount(0);
        this.cbCadReceitaTipo.setSelectedItem(null);

    }

    //evento para setar tblCadRecComponentes
    private void setarTabela() {

        String comp = this.txtCadRecComponentes.getText().trim();
        String cons = this.txtCadRecConsumo.getText().trim();

        DefaultTableModel val = (DefaultTableModel) this.tblCadRecComponentes.getModel();
        val.addRow(new String[]{comp, cons});
    }

    //evento para remover tblCadRecComponentes/ chama o metodo deletar componente
    private void removerTabela() {
//        deletarComponente();
        if (this.tblCadRecComponentes.getSelectedRow() != -1) {
            DefaultTableModel remov = (DefaultTableModel) this.tblCadRecComponentes.getModel();
            recInsDao.deletarComponente(Integer.parseInt(this.txtCadRecCodigo.getText()));
            remov.removeRow(this.tblCadRecComponentes.getSelectedRow());
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
        this.tblCadRecComponentes.setComponentPopupMenu(popupMenu);
    }

    private boolean verificaCampos() {
        if ((this.txtCadRecCodigo.getText().isEmpty()) || (this.txtCadRecDes.getText().isEmpty()) || (this.txtCadRecPan.getText().isEmpty()) || (this.txtCadRecVal.getText().isEmpty())) {
            return false;
        } else {
            return true;
        }
    }

    //mascara para o campo Consumo(foramato de moeda)
    private void mascaraConsu() {
        DecimalFormat dFormat = new DecimalFormat("###.00");
        NumberFormatter formatter = new NumberFormatter(dFormat);

        formatter.setFormat(dFormat);
        formatter.setAllowsInvalid(false);

        this.txtCadRecConsumo.setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

    private void confirmaAcao(boolean conf) {
        // chama o metodo adicionar / ou Atualizar       
        boolean total;
        boolean campos;
        boolean conf2 = false;
        ReceitaDao receitaDao = new ReceitaDao();
        // verifica os campos
        campos = verificaCampos();
        if (campos == false) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            conf2 = true;
        } else {
            //chama o metodo para confirmar se o codigo já existe
            total = receitaDao.confirmaCodigo(this.txtCadRecCodigo.getText());
            if (total == false) {
                confirmar(false);
            } else {
                total = receitaDao.confirmaDescricao(this.txtCadRecDes.getText());
                if (total == false) {
                    JOptionPane.showMessageDialog(null, "Descrição já existe.");
                } else {
                    confirmar(true);
                    limparCampos();
                }
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCadRecDes = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCadRecVal = new javax.swing.JTextField();
        btnCadRecAdicionar = new javax.swing.JButton();
        btnCadRecDeletar = new javax.swing.JButton();
        btnCadRecLimpar = new javax.swing.JButton();
        txtCadRecCodigo = new javax.swing.JTextField();
        txtCadRecPan = new javax.swing.JTextField();
        btnReceitaPesquisar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCadRecComponentes = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtCadRecComponentes = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnInsumoPesquisar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtCadRecConsumo = new javax.swing.JFormattedTextField();
        cbCadReceitaTipo = new javax.swing.JComboBox<>();
        tbnCadRecTipo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Receitas");
        setMaximumSize(new java.awt.Dimension(860, 560));
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

        jLabel1.setText("Código:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel2.setText("Pantone:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, -1, -1));
        getContentPane().add(txtCadRecDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 190, -1));

        jLabel3.setText("Tipo de pasta:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, -1, -1));

        jLabel4.setText("Descrição:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, -1));

        jLabel5.setText("Validade:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 50, -1, -1));
        getContentPane().add(txtCadRecVal, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, 120, -1));

        btnCadRecAdicionar.setText("Salvar");
        btnCadRecAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecAdicionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadRecAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 79, -1));

        btnCadRecDeletar.setText("Eliminar");
        btnCadRecDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecDeletarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadRecDeletar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 79, -1));

        btnCadRecLimpar.setText("Novo");
        btnCadRecLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecLimparActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadRecLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 79, -1));

        txtCadRecCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCadRecCodigoFocusLost(evt);
            }
        });
        txtCadRecCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCadRecCodigoKeyPressed(evt);
            }
        });
        getContentPane().add(txtCadRecCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 130, -1));
        getContentPane().add(txtCadRecPan, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 130, -1));

        btnReceitaPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnReceitaPesquisar.setToolTipText("Pesquisar");
        btnReceitaPesquisar.setContentAreaFilled(false);
        btnReceitaPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReceitaPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceitaPesquisarActionPerformed(evt);
            }
        });
        getContentPane().add(btnReceitaPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 28, 31));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Cadastro de Receita");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblCadRecComponentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Componentes", "Consumo %"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCadRecComponentes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblCadRecComponentesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCadRecComponentesKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblCadRecComponentes);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 660, 270));

        jLabel8.setText("Componentes:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));
        jPanel1.add(txtCadRecComponentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 260, -1));

        jLabel7.setText("Consumo por kg:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, -1, -1));

        btnInsumoPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnInsumoPesquisar.setToolTipText("Pesquisar");
        btnInsumoPesquisar.setContentAreaFilled(false);
        btnInsumoPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInsumoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsumoPesquisarActionPerformed(evt);
            }
        });
        jPanel1.add(btnInsumoPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 30, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/++++.png"))); // NOI18N
        jButton2.setToolTipText("Adicionar");
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 30, -1));
        jPanel1.add(txtCadRecConsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 110, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 830, 360));

        cbCadReceitaTipo.setEditable(true);
        getContentPane().add(cbCadReceitaTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 150, -1));

        tbnCadRecTipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/++++.png"))); // NOI18N
        tbnCadRecTipo.setToolTipText("Adicionar");
        tbnCadRecTipo.setContentAreaFilled(false);
        tbnCadRecTipo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbnCadRecTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnCadRecTipoActionPerformed(evt);
            }
        });
        getContentPane().add(tbnCadRecTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, 20, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/x.png"))); // NOI18N
        jButton1.setToolTipText("Apagar");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 20, -1));

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadRecAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecAdicionarActionPerformed
        //chama o metodo adicionar/salvar
        confirmaAcao(false);
    }//GEN-LAST:event_btnCadRecAdicionarActionPerformed

    private void btnCadRecLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecLimparActionPerformed
        // chama o metodo limpar campos;     
        limparCampos();
        //desabilitarCampos();
    }//GEN-LAST:event_btnCadRecLimparActionPerformed

    private void btnCadRecDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecDeletarActionPerformed
        // chama o metodo deletar
        if (this.txtCadRecCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione uma receita válida.");
        } else {
            int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Eliminar esta receita?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                ReceitaDao receitaDao = new ReceitaDao();
                ReceitaInsumoDao recInsDao = new ReceitaInsumoDao();
                recInsDao.deletarTodos(Integer.parseInt(this.txtCadRecCodigo.getText()));
                receitaDao.deletarReceita(Integer.parseInt(this.txtCadRecCodigo.getText()));
                limparCampos();
            }
        }
    }//GEN-LAST:event_btnCadRecDeletarActionPerformed

    private void btnReceitaPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceitaPesquisarActionPerformed
        // chama a TelaPesquisarReceita
        if (this.framePesReceita == null) {
            this.framePesReceita = new TelaPesquisarReceita();
        } else {
            this.framePesReceita.dispose();
            this.framePesReceita = new TelaPesquisarReceita();
        }
        this.rec.pesquisarReceita();
        this.frame.comandoInternal(this.framePesReceita);
        TelaPesquisarReceita.confirmarEscolha = true;


    }//GEN-LAST:event_btnReceitaPesquisarActionPerformed

    private void btnInsumoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsumoPesquisarActionPerformed
        // chama a TelaPesquisarInsumos

        if (this.framePesInsumo == null) {
            this.framePesInsumo = new TelaPesquisarInsumos();
        } else {
            this.framePesInsumo.dispose();
            this.framePesInsumo = new TelaPesquisarInsumos();
        }
        this.frame.comandoInternal(this.framePesInsumo);
        TelaPesquisarInsumos.confirmarEscolha = true;
        TelaPesquisarInsumos.confimaTela = false;

    }//GEN-LAST:event_btnInsumoPesquisarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // chama metodo para setar a tabela
        boolean total;
        if ((this.txtCadRecComponentes.getText().isEmpty()) || (this.txtCadRecConsumo.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {
            // chamaa o metodo para verificar se o insumo é válido
            total = recInsDao.confirmaInsumo(this.txtCadRecComponentes.getText());
            if (total == true) {
                JOptionPane.showMessageDialog(null, "O Componente descrito não existe.");
            } else {
                if (this.txtCadRecCodigo.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Código do cadastro inválido.");

                } else {
                    total = recInsDao.verificaInsumo(Integer.parseInt(this.txtCadRecCodigo.getText()), this.txtCadRecComponentes.getText());
                    if (total == false) {
                        JOptionPane.showMessageDialog(null, "Componente já existe.");
                    } else {
                        recInsDao.adicionarComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()), this.txtCadRecComponentes.getText(), this.txtCadRecConsumo.getText());
                        setarTabela();
                        this.txtCadRecComponentes.setText(null);
                        this.txtCadRecConsumo.setValue(null);
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tbnCadRecTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnCadRecTipoActionPerformed
        // chama o metodo adicionar tipo de pasta
        boolean conf = this.pasta.buscaCodTipoPasta(this.cbCadReceitaTipo.getSelectedItem().toString()) <= 0;
        if (this.cbCadReceitaTipo.getSelectedItem().equals("")) {
            JOptionPane.showMessageDialog(null, "Tipo de pasta inválido.");
        } else {
            if (conf == false) {
                JOptionPane.showMessageDialog(null, "Tipo de pasta já existe.");
            } else {
                String pastaAdicionada = this.cbCadReceitaTipo.getSelectedItem().toString();
                this.pasta.addComboBox(this.cbCadReceitaTipo.getSelectedItem().toString());
                this.cbCadReceitaTipo.removeAllItems();
                this.pasta.setarComboBox();
                this.cbCadReceitaTipo.setSelectedItem(pastaAdicionada);
            }
        }
    }//GEN-LAST:event_tbnCadRecTipoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Chama o metodo remover tipo de pasta
        int conf = this.pasta.buscaCodTipoPasta(this.cbCadReceitaTipo.getSelectedItem().toString());
        boolean confirmar = this.pasta.contTipoPasta(conf);
        if (confirmar == true) {
            JOptionPane.showMessageDialog(null, "Esse Tipo de pasta não pode ser removido.");
        } else {
            this.pasta.removeComboBox(this.cbCadReceitaTipo.getSelectedItem().toString());
            this.cbCadReceitaTipo.removeAllItems();
            this.pasta.setarComboBox();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblCadRecComponentesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCadRecComponentesKeyPressed
        // Chama o metodo atualizar atraves da tecla Enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.recInsDao.atualizarComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()));
        }
    }//GEN-LAST:event_tblCadRecComponentesKeyPressed

    private void txtCadRecCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCadRecCodigoFocusLost
        // Evento ao sair do campo de codigo, ele irar trazer todas as irformações referentes ao codigo
        if (!this.txtCadRecCodigo.getText().equals("")) {
            ReceitaDao pesq = new ReceitaDao();
            TelaPesquisarReceita pesqRec = new TelaPesquisarReceita();
            pesq.pesquisarReceita(Integer.parseInt(this.txtCadRecCodigo.getText()));
            pesqRec.setarTbComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()));
        }
    }//GEN-LAST:event_txtCadRecCodigoFocusLost

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        this.frame.bloquearMovimentacao(TelaPrincipal.frameReceita, 0, 0);
    }//GEN-LAST:event_formComponentMoved

    private void tblCadRecComponentesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCadRecComponentesKeyReleased
        // Chama o metodo atualizar enquanto digita
        this.recInsDao.atualizarComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()));
    }//GEN-LAST:event_tblCadRecComponentesKeyReleased

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // gerar mensagem de salvar antes de sair
        if ((this.txtCadRecCodigo.getText().isEmpty()) && (this.txtCadRecDes.getText().isEmpty()) && (this.txtCadRecPan.getText().isEmpty()) && (this.txtCadRecVal.getText().isEmpty()) && (this.txtCadRecComponentes.getText().isEmpty()) && (this.txtCadRecConsumo.getText().isEmpty())) {
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

    private void txtCadRecCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCadRecCodigoKeyPressed
        // chama o metodo atraves da tecla enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!this.txtCadRecCodigo.getText().equals("")) {
                ReceitaDao pesq = new ReceitaDao();
                TelaPesquisarReceita pesqRec = new TelaPesquisarReceita();
                pesq.pesquisarReceita(Integer.parseInt(this.txtCadRecCodigo.getText()));
                pesqRec.setarTbComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()));
            }
        }
       
    }//GEN-LAST:event_txtCadRecCodigoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadRecAdicionar;
    private javax.swing.JButton btnCadRecDeletar;
    private javax.swing.JButton btnCadRecLimpar;
    public static javax.swing.JButton btnInsumoPesquisar;
    private javax.swing.JButton btnReceitaPesquisar;
    public static javax.swing.JComboBox<String> cbCadReceitaTipo;
    private javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable tblCadRecComponentes;
    private javax.swing.JButton tbnCadRecTipo;
    public static javax.swing.JTextField txtCadRecCodigo;
    public static javax.swing.JTextField txtCadRecComponentes;
    public static javax.swing.JFormattedTextField txtCadRecConsumo;
    public static javax.swing.JTextField txtCadRecDes;
    public static javax.swing.JTextField txtCadRecPan;
    public static javax.swing.JTextField txtCadRecVal;
    // End of variables declaration//GEN-END:variables
}
