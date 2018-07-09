/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.sun.glass.events.KeyEvent;
import conexao.ModuloConexao;
import dao.InsumoDao;
import dao.MovimentacaoDao;
import dao.MovimentacaoEstoqueDao;
import dao.ReceitaDao;
import dto.MovimentacaoDto;
import dto.MovimentacaoEstoqueDto;
import java.awt.Color;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import util.MascaraMoeda;
import util.Util;
import util.SoNumeros;

/**
 *
 * @author Leandro
 */
public class TelaMovimentacaoEstoque extends javax.swing.JInternalFrame {

    Connection conexao = null;
    private String cbPesquisar = "ep.codigoReceita";
    MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();
    InsumoDao insumoDao = new InsumoDao();
    ReceitaDao receitaDao = new ReceitaDao();
    Util util = new Util();
    public static JInternalFrame framePesReceita;
    public static JInternalFrame framePesInsumo;
    TelaPesquisarReceita rec = new TelaPesquisarReceita();
    TelaPesquisarInsumos ins = new TelaPesquisarInsumos();

    /**
     * Creates new form TelaEstoque
     */
    //Construtor da classe
    public TelaMovimentacaoEstoque() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        this.txtCodigo.setDocument(new SoNumeros());
        this.txtEstData.setDocument(new SoNumeros());
        this.txtEstQuantidade.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        this.txtEstQuantidade.setDocument(new MascaraMoeda());
        ativarTblPasta();
    }

    private void movimentacao(boolean confirmaTipo, String confirmaEstoque) {
        MovimentacaoDto movDto = new MovimentacaoDto();
        MovimentacaoDao movDao = new MovimentacaoDao();
        int codigo = 0;
        if (confirmaEstoque.equals("Pasta")) {
            codigo = movDao.buscaCodigoReceita(this.txtDescricao.getText());
        } else {
            codigo = movDao.buscaCodigoInsumo(this.txtDescricao.getText());
        }
        if (confirmaTipo == true) {
            movDto.setTipo(this.cbEstoque.getSelectedItem().toString());
            movDto.setCodigoID(codigo);
            movDto.setDescricao(this.txtDescricao.getText());
            movDto.setData(inverterData(this.txtEstData.getText().replace("/", "-")));
            movDto.setQuantidade(this.txtEstQuantidade.getText().replace(",", "."));

        } else {
            movDto.setTipo(this.cbEstoque.getSelectedItem().toString());
            movDto.setCodigoID(codigo);
            movDto.setDescricao(this.txtDescricao.getText());
            movDto.setData(inverterData(this.txtEstData.getText().replace("/", "-")));
            movDto.setQuantidade("-" + this.txtEstQuantidade.getText().replace(",", "."));
        }
        movDao.movimentacao(movDto);
    }

    private void confirmaEstoquePasta(boolean confirma) {
        MovimentacaoEstoqueDto movEstDto = new MovimentacaoEstoqueDto();
        MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();

        movEstDto.setCodigoReceita(movEstDao.buscaCodigoReceita(this.txtDescricao.getText()));
        movEstDto.setUM(this.txtEstUM.getText());
        movEstDto.setQuantidade(Double.parseDouble(this.txtEstQuantidade.getText().replace(",", ".")));
        movEstDto.setData(inverterData(this.txtEstData.getText().replace("/", "-")));
        movEstDto.setDataVencimento(movEstDao.dataVencimento(inverterData(this.txtEstData.getText().replace("/", "-")), this.receitaDao.buscaVencimento(Integer.parseInt(this.txtCodigo.getText()))));

        if (confirma == true) {
            movEstDao.entradaPasta(movEstDto);
        } else {
            movEstDao.saidaPasta(movEstDto.getQuantidade(), movEstDto.getCodigoReceita());
        }
    }

    //Seta a tabela de estoque de pastas
    public void setarTabelaPasta() {
        String sql = "select ep.codigoReceita, r.descricao, ep.quantidade, r.datavencimento, ep.data, ep.dataVencimento from tbEstoquePasta as ep"
                + " inner join tbreceita as r on ep.codigoReceita = r.codigorec"
                + " where " + this.cbPesquisar + " like ?";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) this.tblEstPasta.getModel();
        modelo.setNumRows(0);

        this.tblEstPasta.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(1).setPreferredWidth(30);
        this.tblEstPasta.getColumnModel().getColumn(2).setPreferredWidth(30);
        this.tblEstPasta.getColumnModel().getColumn(3).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(4).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(5).setPreferredWidth(20);

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtMovEst.getText() + "%");
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    this.util.formatadorQuant(rs.getString(3)),
                    rs.getString(4),
                    inverterData(rs.getString(5)).replace("-", "/"),
                    inverterData(rs.getString(6)).replace("-", "/")
                });
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }

    }

    //ativa a tabela de pastas
    private void ativarTblPasta() {
        this.lblRecIns.setText("Receita:");
        this.tblEstPasta.setVisible(true);
        this.cbPesMovEst.setEnabled(true);
        this.txtMovEst.setEnabled(true);
        limparCampos();
        this.txtDescricao.setText(null);
        this.txtEstUM.setText("kg");
        setarTabelaPasta();
        this.lblRecIns.setText("Receita:");
    }

    private void ativarInsumo() {
        this.btnMovEstPesquisar.setEnabled(true);
        this.btnMovEstPesquisar.setVisible(true);
        this.txtEstQuantidade.setEnabled(false);
        this.txtEstData.setEnabled(false);
        this.txtCodigo.setText(null);
        this.txtCodigo.setEnabled(true);
        this.txtDescricao.setText(null);
        this.txtEstUM.setText(null);
        this.txtEstQuantidade.setValue(null);
        this.txtEstData.setValue(null);
        this.txtEstUM.setEnabled(false);
        this.tblEstPasta.setVisible(false);
        this.cbPesMovEst.setEnabled(false);
        this.txtMovEst.setEnabled(false);
        this.lblRecIns.setText("Insumo:");
    }

    // limpa os campos após entrada ou saida
    private void limparCampos() {
        this.txtCodigo.setEnabled(true);
        this.txtDescricao.setEnabled(false);
        this.txtEstQuantidade.setEnabled(false);
        this.txtEstData.setEnabled(false);
        this.txtCodigo.setText(null);
        this.txtDescricao.setText(null);
        this.txtEstQuantidade.setValue(null);
        this.txtEstData.setValue(null);
    }

    // verifica se todos os campos estão setados
    private boolean verificaCampos() {
        int confirma = 0;
        if ((this.txtDescricao.getText().isEmpty()) || (this.txtEstQuantidade.getText().isEmpty()) || (this.txtEstUM.getText().isEmpty()) || (this.txtEstData.getText().equals("  /  /    "))) {
            confirma = 1;
        }
        return confirma > 0;
    }

    private String inverterData(String data) {
        String[] dataInvertida = data.split("-");
        return dataInvertida[2] + "-" + dataInvertida[1] + "-" + dataInvertida[0];
    }

    private void confirmaAcao(boolean conf) {
        // confirma a ação de entrada ou saida    
        //Verifica se existe algum campo vazio 
        boolean conf2 = false;
        if (verificaCampos() == true) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            conf2 = true;
        } else {
            //confirma se é pasta ou insumo
            if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
                //confirma se é entrada ou saida                
                if (this.cbTipo.getSelectedItem().equals("Entrada")) {
                    if (this.movEstDao.dataComparar(this.movEstDao.dataAtual(), inverterData(this.txtEstData.getText().replace("/", "-"))) == false) {
                        JOptionPane.showMessageDialog(null, "Não é possivel dar entrada com uma data futura.");
                    } else {
                        if (this.movEstDao.dataComparar(this.movEstDao.dataAtual(), this.movEstDao.dataVencimento(inverterData(this.txtEstData.getText().replace("/", "-")), this.receitaDao.buscaVencimento(Integer.parseInt(this.txtCodigo.getText())))) == true) {
                            JOptionPane.showMessageDialog(null, "Data de vencimento da pasta é inferior a data atual.");
                        } else {
                            confirmaEstoquePasta(true);
                            movimentacao(true, "Pasta");
                            this.insumoDao.retirarInsumo(this.movEstDao.buscaCodigoReceita(this.txtDescricao.getText()));
                            setarTabelaPasta();
                            limparCampos();
                        }
                    }

                } else {
                    //this.txtEstData.setText(inverterData(this.movEstDao.dataAtual()).replace("-", "/"));
                    double quantidade = Double.parseDouble(this.txtEstQuantidade.getText().replace(",", "."));
                    int soma = this.movEstDao.somaPastas(this.txtDescricao.getText());
                    if (soma < quantidade) {
                        JOptionPane.showMessageDialog(null, "Quantidade em estoque " + soma + "kg\nNão atende a sua necessidade.");
                        conf2 = true;

                    } else {
                        confirmaEstoquePasta(false);
                        movimentacao(false, "Pasta");
                        setarTabelaPasta();
                        limparCampos();
                    }
                }
            } else {
                // confirma se é entrada ou saida
                if (this.cbTipo.getSelectedItem().equals("Entrada")) {

                    if (this.movEstDao.dataComparar(this.movEstDao.dataAtual(), inverterData(this.txtEstData.getText().replace("/", "-"))) == false) {
                        JOptionPane.showMessageDialog(null, "Não é possivel dar entrada com uma data futura.");
                    } else {

                        this.insumoDao.entradaInsumo(Double.parseDouble(this.txtEstQuantidade.getText().replace(",", ".")), this.insumoDao.buscaCodigoInsumo(this.txtDescricao.getText()));
                        movimentacao(true, "Insumo");
                        limparCampos();
                    }
                } else {
                    //this.txtEstData.setText(inverterData(this.movEstDao.dataAtual()).replace("-", "/"));

                    this.insumoDao.saidaInsumo(Double.parseDouble(this.txtEstQuantidade.getText().replace(",", ".")), this.insumoDao.buscaCodigoInsumo(this.txtDescricao.getText()));
                    movimentacao(false, "Insumo");
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblRecIns = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbTipo = new javax.swing.JComboBox<>();
        cbEstoque = new javax.swing.JComboBox<>();
        txtCodigo = new javax.swing.JTextField();
        btnMovEstPesquisar = new javax.swing.JButton();
        txtDescricao = new javax.swing.JTextField();
        txtEstUM = new javax.swing.JTextField();
        txtEstData = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cbPesMovEst = new javax.swing.JComboBox<>();
        txtMovEst = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEstPasta = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnConfirmar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnLimpar = new javax.swing.JButton();
        txtEstQuantidade = new javax.swing.JFormattedTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Movimentação Estoque");
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

        jPanel2.setBackground(new java.awt.Color(229, 247, 203));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setForeground(new java.awt.Color(79, 79, 79));
        jLabel2.setText("Tipo:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel3.setForeground(new java.awt.Color(79, 79, 79));
        jLabel3.setText("Unidade de Medida:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, -1, -1));

        jLabel9.setForeground(new java.awt.Color(79, 79, 79));
        jLabel9.setText("Código:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, -1));

        jLabel7.setForeground(new java.awt.Color(79, 79, 79));
        jLabel7.setText("Quantidade:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 130, -1, -1));

        lblRecIns.setForeground(new java.awt.Color(79, 79, 79));
        lblRecIns.setText("Receita:");
        jPanel2.add(lblRecIns, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, -1, -1));

        jLabel5.setForeground(new java.awt.Color(79, 79, 79));
        jLabel5.setText("Data:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 180, -1, -1));

        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Estoque");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(79, 79, 79));
        jLabel4.setText("Movimentação de Estoque");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entrada", "Saída" }));
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
            }
        });
        jPanel2.add(cbTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 100, -1));

        cbEstoque.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pasta", "Insumo" }));
        cbEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstoqueActionPerformed(evt);
            }
        });
        jPanel2.add(cbEstoque, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 84, -1));

        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });
        jPanel2.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 180, -1));

        btnMovEstPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnMovEstPesquisar.setToolTipText("Pesquisar");
        btnMovEstPesquisar.setContentAreaFilled(false);
        btnMovEstPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMovEstPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovEstPesquisarActionPerformed(evt);
            }
        });
        jPanel2.add(btnMovEstPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 30, 30));

        txtDescricao.setEnabled(false);
        jPanel2.add(txtDescricao, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 240, -1));

        txtEstUM.setEnabled(false);
        jPanel2.add(txtEstUM, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, 130, -1));

        try {
            txtEstData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtEstData.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEstData.setEnabled(false);
        txtEstData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEstDataKeyPressed(evt);
            }
        });
        jPanel2.add(txtEstData, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 200, 170, -1));

        jPanel5.setBackground(new java.awt.Color(229, 247, 203));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(188, 188, 188)));
        jPanel5.setToolTipText("");

        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Pesquisar por:");

        cbPesMovEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Pasta", "Quantidade", "Validade" }));
        cbPesMovEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPesMovEstActionPerformed(evt);
            }
        });

        txtMovEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMovEstKeyReleased(evt);
            }
        });

        tblEstPasta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Pasta", "Quantidade ", "Validade", "Data", "Data Vencimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEstPasta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEstPastaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEstPasta);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(79, 79, 79));
        jLabel8.setText("Tabela Estoque de Pastas");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(18, 18, 18)
                            .addComponent(cbPesMovEst, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(txtMovEst, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8))
                .addContainerGap(584, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 16, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(cbPesMovEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMovEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 824, 300));

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

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(776, 2, 40, 20));

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

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(816, 2, 40, 20));

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

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 100, 25));

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

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 210, 100, 25));

        txtEstQuantidade.setEnabled(false);
        txtEstQuantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEstQuantidadeKeyPressed(evt);
            }
        });
        jPanel2.add(txtEstQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, 170, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 859, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

    private void cbEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstoqueActionPerformed
        // Altera o nome da lblRecIns conforme o item selecionado do combobox
        if (this.cbEstoque.getSelectedItem().equals("Insumo")) {
            ativarInsumo();
        } else {
            ativarTblPasta();
            if (this.cbTipo.getSelectedItem().equals("Saída")) {
                this.btnMovEstPesquisar.setEnabled(false);
                this.btnMovEstPesquisar.setVisible(false);
            }
        }
    }//GEN-LAST:event_cbEstoqueActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        //chama o metodo para dar entrado ou saida em insumo ou pasta
        //alteraCorPressionado(this.jPanel3, this.btnConfirmar);
        confirmaAcao(false);
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        // Altera entre saida ou entrada
        if ((!this.txtCodigo.getText().equals("")) && (this.txtEstData.getText().equals("  /  /    "))) {
            this.txtEstData.setText(this.movEstDao.inverterData(this.movEstDao.dataAtual()).replace("-", "/"));
        }

        if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
            if (this.cbTipo.getSelectedItem().equals("Saída")) {
                this.btnMovEstPesquisar.setEnabled(false);
                this.btnMovEstPesquisar.setVisible(false);

            } else {
                this.btnMovEstPesquisar.setEnabled(true);
                this.btnMovEstPesquisar.setVisible(true);
            }
        } else {
            this.btnMovEstPesquisar.setEnabled(true);
            this.btnMovEstPesquisar.setVisible(true);
        }
    }//GEN-LAST:event_cbTipoActionPerformed

    private void btnMovEstPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovEstPesquisarActionPerformed
        // chama a tela de pesquisa receita ou insumo

        if (this.cbEstoque.getSelectedItem().equals("Pasta")) {

            if (this.framePesReceita == null) {
                this.framePesReceita = new TelaPesquisarReceita();
            } else {
                this.framePesReceita.dispose();
                this.framePesReceita = new TelaPesquisarReceita();
            }
            this.rec.pesquisarReceita();
            this.util.comandoInternal(this.framePesReceita);
            TelaPesquisarReceita.confirmarEscolha = 2;

        } else {
            if (this.framePesInsumo == null) {
                this.framePesInsumo = new TelaPesquisarInsumos();
            } else {
                this.framePesInsumo.dispose();
                this.framePesInsumo = new TelaPesquisarInsumos();
            }
            this.ins.pesquisarInsumos();
            this.util.comandoInternal(this.framePesInsumo);
            TelaPesquisarInsumos.confirmarEscolha = false;
        }
    }//GEN-LAST:event_btnMovEstPesquisarActionPerformed

    private void cbPesMovEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPesMovEstActionPerformed
        //  // adiciona um valor a variável cbPesquizar
        if (this.cbPesMovEst.getSelectedItem().equals("Código")) {
            this.cbPesquisar = "ep.codigoReceita";
        } else {
            if (this.cbPesMovEst.getSelectedItem().equals("Pasta")) {
                this.cbPesquisar = "r.descricao";
            } else {
                if (this.cbPesMovEst.getSelectedItem().equals("Quantidade")) {
                    this.cbPesquisar = "ep.quantidade";
                } else {
                    this.cbPesquisar = "r.datavencimento";
                }
            }
        }
    }//GEN-LAST:event_cbPesMovEstActionPerformed

    private void txtMovEstKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMovEstKeyReleased
        // chama metodo pesquisar enquanto digita
        setarTabelaPasta();
    }//GEN-LAST:event_txtMovEstKeyReleased

    private void tblEstPastaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEstPastaMouseClicked
        // seta a descrição da pasta nno campo Receita, através de duplo click com o mouse

        if (evt.getClickCount() > 1) {
            ReceitaDao receitaDao = new ReceitaDao();
            int setar = this.tblEstPasta.getSelectedRow();
            this.txtDescricao.setText((String) this.tblEstPasta.getModel().getValueAt(setar, 1));
            this.txtCodigo.setText(receitaDao.buscaCodigo((String) this.tblEstPasta.getModel().getValueAt(setar, 1)));
            this.txtCodigo.setEnabled(false);
            this.txtEstQuantidade.setEnabled(true);
            this.txtEstData.setEnabled(true);
            if (this.cbTipo.getSelectedItem().equals("Saída")) {
                this.txtEstData.setText(this.movEstDao.inverterData(this.movEstDao.dataAtual()).replace("-", "/"));
            }
        }
    }//GEN-LAST:event_tblEstPastaMouseClicked

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // chama o metodo limpar
        //alteraCorPressionado(this.jPanel4, this.btnLimpar);
        limparCampos();
        this.txtDescricao.setText(null);
        if (this.cbEstoque.getSelectedItem().equals("Insumo")) {
            this.txtEstUM.setText(null);
        }
    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        // chama metodo ao pressionar a tecla Enter   

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
                if (!this.txtCodigo.getText().equals("")) {
                    ReceitaDao pesq = new ReceitaDao();
                    pesq.pesquisarReceitaMovi(Integer.parseInt(this.txtCodigo.getText()));
                }
            } else {
                if (!this.txtCodigo.getText().equals("")) {
                    insumoDao.pesquisarInsumos2(Integer.parseInt(this.txtCodigo.getText()));
                }

            }
        }

    }//GEN-LAST:event_txtCodigoKeyPressed

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
        if (this.txtCodigo.getText().isEmpty()) {
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
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnLimparMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel4, this.btnLimpar);
    }//GEN-LAST:event_btnLimparMouseEntered

    private void btnLimparMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel4, this.btnLimpar);
    }//GEN-LAST:event_btnLimparMouseExited

    private void btnConfirmarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmarMouseEntered
        /// quando o mouse estiver em cima
        alteraCor(this.jPanel3, this.btnConfirmar);
    }//GEN-LAST:event_btnConfirmarMouseEntered

    private void btnConfirmarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmarMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel3, this.btnConfirmar);
    }//GEN-LAST:event_btnConfirmarMouseExited

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // passa o foco para o campo de texto 
        this.txtCodigo.requestFocus();
    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // passa o foco para o campo de texto 
        this.txtCodigo.requestFocus();
        this.jPanel6.setBackground(new Color(229, 247, 203));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameDeiconified

    private void txtEstQuantidadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstQuantidadeKeyPressed
        // quando ENTER é pressionado
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.txtEstData.requestFocus();
        }
    }//GEN-LAST:event_txtEstQuantidadeKeyPressed

    private void txtEstDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstDataKeyPressed
        // quando ENTER é pressionado
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            confirmaAcao(false);
        }
    }//GEN-LAST:event_txtEstDataKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JButton btnMovEstPesquisar;
    private javax.swing.JComboBox<String> cbEstoque;
    private javax.swing.JComboBox<String> cbPesMovEst;
    public static javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblRecIns;
    private javax.swing.JTable tblEstPasta;
    public static javax.swing.JTextField txtCodigo;
    public static javax.swing.JTextField txtDescricao;
    public static javax.swing.JFormattedTextField txtEstData;
    public static javax.swing.JFormattedTextField txtEstQuantidade;
    public static javax.swing.JTextField txtEstUM;
    private javax.swing.JTextField txtMovEst;
    // End of variables declaration//GEN-END:variables
}
