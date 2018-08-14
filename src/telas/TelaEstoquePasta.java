/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.sun.glass.events.KeyEvent;
import dao.EstoquePastaDao;
import dao.EstoquePastaFinalDao;
import dao.MovimentacaoDao;
import dao.MovimentacaoEstoqueDao;
import dao.ReceitaDao;
import dto.MovimentacaoDto;
import dto.MovimentacaoEstoqueDto;
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

    public static JInternalFrame frameOpPasta;
    public static JInternalFrame framePesReceita;
    private final TelaPesquisarReceita rec = new TelaPesquisarReceita();
    private final Util util = new Util();
    private final EstoquePastaDao estPas = new EstoquePastaDao();
    private final EstoquePastaFinalDao estPasFinal = new EstoquePastaFinalDao();
    private final MovimentacaoDao movDao = new MovimentacaoDao();
    public static boolean jDialogSobre;
    public static String descricao;

    /**
     * Creates new form TelaEstoquePasta
     */
    public TelaEstoquePasta() {
        initComponents();
        this.txtCodigo.setDocument(new SoNumeros());
        this.txtQuantidade.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        this.txtQuantidade.setDocument(new MascaraMoeda());
        retornarCoresEnable();
        this.tblProducaoPastaOp1.getTableHeader().setReorderingAllowed(false);
        this.tblProducaoPastaOp2.getTableHeader().setReorderingAllowed(false);
    }

    private void limparCampos() {
        this.txtCodigo.setText(null);
        this.txtCodigo.setEnabled(true);
        this.txtCodigo.requestFocus();
        this.txtQuantidade.setDocument(new MascaraMoeda());
        this.txtQuantidade.setEnabled(false);
        this.lblCustoProducao.setText("0,00");
        this.lblValorReaproveitadoOpc1.setText("0,00");
        this.lblValorReaproveitadoOpc2.setText("0,00");
        this.btnVisualizarOp1.setEnabled(false);
        this.btnVisualizarOp2.setEnabled(false);
        retornarCoresEnable();
        ((DefaultTableModel) this.tblProducaoPastaOp1.getModel()).setRowCount(0);
        ((DefaultTableModel) this.tblProducaoPastaOp2.getModel()).setRowCount(0);
    }

    private void insertPasta() {
        MovimentacaoEstoqueDto movEstDto = new MovimentacaoEstoqueDto();
        MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();
        ReceitaDao receitaDao = new ReceitaDao();

        movEstDto.setCodigoReceita(Integer.parseInt(this.txtCodigo.getText()));
        movEstDto.setUM("kg");
        movEstDto.setQuantidade(Double.parseDouble(this.txtQuantidade.getText().replace(".", "").replace(",", ".")));
        movEstDto.setData(movEstDao.dataAtual());
        movEstDto.setDataVencimento(movEstDao.dataVencimento(movEstDao.dataAtual(), receitaDao.buscaVencimento(Integer.parseInt(this.txtCodigo.getText()))));

        movEstDao.entradaPasta(movEstDto);

        //a estrutura abaixo seta a tabela de movimentação de estoque
        MovimentacaoDto movDto = new MovimentacaoDto();
        movDto.setTipo("Pasta");
        movDto.setCodigoID(movEstDto.getCodigoReceita());
        movDto.setDescricao(this.movDao.buscaDescricaoReceita(movEstDto.getCodigoReceita()));
        movDto.setQuantidade("" + movEstDto.getQuantidade());
        movDto.setData(this.util.dataAtual());
        this.movDao.movimentacao(movDto);
    }

    private void setaTabelaOpcPasta1() {
        DefaultTableModel modelo = (DefaultTableModel) TelaOpcaoPasta.tblOpcPasta.getModel();
        modelo.setNumRows(0);

        for (int i = 0; i < EstoquePastaFinalDao.listFinalOp1.size(); i++) {
            modelo.addRow(new Object[]{
                EstoquePastaFinalDao.listFinalOp1.get(i).getId(),
                EstoquePastaFinalDao.listFinalOp1.get(i).getCodigo(),
                EstoquePastaFinalDao.listFinalOp1.get(i).getDescricao(),
                this.util.formatadorQuant6(EstoquePastaFinalDao.listFinalOp1.get(i).getUsar()) + " kg",
                this.util.formatadorQuant(EstoquePastaFinalDao.listFinalOp1.get(i).getEquivalencia()) + "%",
                EstoquePastaFinalDao.listFinalOp1.get(i).getVencimento()
            });
        }
    }

    private void setaTabelaOpcPasta2() {
        DefaultTableModel modelo = (DefaultTableModel) TelaOpcaoPasta.tblOpcPasta.getModel();
        modelo.setNumRows(0);

        if (EstoquePastaFinalDao.listFinalOp22.isEmpty()) {

        } else {
            modelo.addRow(new Object[]{
                EstoquePastaFinalDao.listFinalOp22.get(0).getId(),
                EstoquePastaFinalDao.listFinalOp22.get(0).getCodigo(),
                EstoquePastaFinalDao.listFinalOp22.get(0).getDescricao(),
                this.util.formatadorQuant6(EstoquePastaFinalDao.listFinalOp22.get(0).getUsar()) + " kg",
                this.util.formatadorQuant(EstoquePastaFinalDao.listFinalOp22.get(0).getEquivalencia()) + "%",
                EstoquePastaFinalDao.listFinalOp22.get(0).getVencimento()
            });
        }

        for (int i = 0; i < EstoquePastaFinalDao.insumosOp2.size(); i++) {
            modelo.addRow(new Object[]{
                "Insumo",
                EstoquePastaFinalDao.insumosOp2.get(i).getCodigo(),
                EstoquePastaFinalDao.insumosOp2.get(i).getDescricao(),
                this.util.formatadorQuant6(EstoquePastaFinalDao.pastaProduzirOp22.get(i).getConsumo()) + " " + EstoquePastaFinalDao.insumosOp2.get(i).getUM(),
                this.util.formatadorQuant(this.util.formatador6(this.util.regraDeTres1(this.util.conversaoUMparaKG(EstoquePastaFinalDao.insumosOp2.get(i).getUM(), EstoquePastaFinalDao.pastaProduzirOp22.get(i).getConsumo()), Double.parseDouble(TelaEstoquePasta.txtQuantidade.getText().replace(".", "").replace(",", "."))))) + "%",
                ""
            });
        }
    }

    private void fecharFram() {
        if (this.framePesReceita != null) {
            this.framePesReceita.dispose();
        }
        if (this.frameOpPasta != null) {
            this.frameOpPasta.dispose();
        }
    }

    private void retornarCoresBtn() {
        retornaCor(this.jPanel8, this.btnVisualizarOp1);
        retornaCor(this.jPanel7, this.btnVisualizarOp2);
    }

    private void retornarCoresEnable() {
        alterarCorEnable(this.jPanel8, this.btnVisualizarOp1);
        alterarCorEnable(this.jPanel7, this.btnVisualizarOp2);
    }

    //retorna a cor para padrão enabled
    private void alterarCorEnable(JPanel painel, JButton botao) {
        painel.setBackground(new Color(255, 255, 255));
        botao.setForeground(new Color(201, 201, 201));
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
        btnProcurar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProducaoPastaOp2 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProducaoPastaOp1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        lblCustoProducao = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnVisualizarOp1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnVisualizarOp2 = new javax.swing.JButton();
        lbltext = new javax.swing.JLabel();
        lblValorReaproveitadoOpc1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblValorReaproveitadoOpc2 = new javax.swing.JLabel();
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

        btnProcurar.setForeground(new java.awt.Color(79, 79, 79));
        btnProcurar.setText("Procurar");
        btnProcurar.setContentAreaFilled(false);
        btnProcurar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProcurarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProcurarMouseExited(evt);
            }
        });
        btnProcurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarActionPerformed(evt);
            }
        });
        jPanel3.add(btnProcurar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 24));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 100, 25));

        jLabel4.setForeground(new java.awt.Color(79, 79, 79));
        jLabel4.setText("Código:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jPanel5.setBackground(new java.awt.Color(236, 255, 209));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(155, 155, 155)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("Primeira opção - Utilizando apenas Pastas do estoque:");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 12, -1, 25));

        jLabel7.setText("Segunda opção - Utilizando Pastas mais Insumos:");
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 12, -1, 25));

        tblProducaoPastaOp2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblProducaoPastaOp2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Código", "Descrição", "Estoque", "Usar", "Equivale à (%)", "Vencimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblProducaoPastaOp2);

        jPanel5.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 63, 420, 272));

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
        jScrollPane2.setViewportView(tblProducaoPastaOp1);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 63, 430, 272));

        jLabel8.setText("Custo em insumos: R$ ");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(607, 43, -1, -1));

        lblCustoProducao.setForeground(new java.awt.Color(255, 0, 0));
        lblCustoProducao.setText("0,00");
        jPanel5.add(lblCustoProducao, new org.netbeans.lib.awtextra.AbsoluteConstraints(722, 43, -1, -1));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnVisualizarOp1.setText("Visualizar");
        btnVisualizarOp1.setToolTipText("Produzir");
        btnVisualizarOp1.setContentAreaFilled(false);
        btnVisualizarOp1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVisualizarOp1.setEnabled(false);
        btnVisualizarOp1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVisualizarOp1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVisualizarOp1MouseExited(evt);
            }
        });
        btnVisualizarOp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualizarOp1ActionPerformed(evt);
            }
        });
        jPanel8.add(btnVisualizarOp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 25));

        jPanel5.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 12, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnVisualizarOp2.setText("Visualizar");
        btnVisualizarOp2.setToolTipText("Produzir");
        btnVisualizarOp2.setContentAreaFilled(false);
        btnVisualizarOp2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVisualizarOp2.setEnabled(false);
        btnVisualizarOp2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVisualizarOp2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVisualizarOp2MouseExited(evt);
            }
        });
        btnVisualizarOp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualizarOp2ActionPerformed(evt);
            }
        });
        jPanel7.add(btnVisualizarOp2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 25));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(757, 12, -1, -1));

        lbltext.setText("Reaproveitado: R$");
        jPanel5.add(lbltext, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 43, -1, -1));

        lblValorReaproveitadoOpc1.setForeground(new java.awt.Color(0, 153, 0));
        lblValorReaproveitadoOpc1.setText("0,00");
        jPanel5.add(lblValorReaproveitadoOpc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 43, -1, -1));

        jLabel9.setText("Reaproveitado: R$");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 43, -1, -1));

        lblValorReaproveitadoOpc2.setForeground(new java.awt.Color(0, 153, 0));
        lblValorReaproveitadoOpc2.setText("0,00");
        jPanel5.add(lblValorReaproveitadoOpc2, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 43, -1, -1));

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
        fecharFram();
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
        // gerar mensagem de salvar antes de sair
        this.dispose();
        fecharFram();
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
        this.util.comandoInternal2(this.framePesReceita);
        limparCampos();
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

    private void btnProcurarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProcurarMouseEntered
        /// quando o mouse estiver em cima
        alteraCor(this.jPanel3, this.btnProcurar);
    }//GEN-LAST:event_btnProcurarMouseEntered

    private void btnProcurarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProcurarMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel3, this.btnProcurar);
    }//GEN-LAST:event_btnProcurarMouseExited

    private void btnProcurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarActionPerformed
        //chama o metodo para dar entrado ou saida em insumo ou pasta
        //alteraCorPressionado(this.jPanel3, this.btnConfirmar);
        if (!this.txtQuantidade.getText().equals("") && (!this.txtCodigo.getText().equals(""))) {
            if ((!this.txtQuantidade.getText().equals("0,00"))) {

                int codigo = Integer.parseInt(this.txtCodigo.getText());
                double quantidade = Double.parseDouble(this.txtQuantidade.getText().replace(".", "").replace(",", "."));

                this.tblProducaoPastaOp1.removeAll();
                this.tblProducaoPastaOp2.removeAll();
                EstoquePastaFinalDao.listFinalOp22.clear();
                EstoquePastaFinalDao.pastaProduzirOp22.clear();
                this.lblValorReaproveitadoOpc1.setText("0,00");
                this.estPasFinal.limparVariaveis(true);
                this.estPasFinal.pastaCompativel(this.estPasFinal.buscaCodigoInsumos(codigo));
                this.estPasFinal.buscarInsumos(codigo, quantidade, true);
                this.estPasFinal.subtrairInsumos(codigo);
                retornarCoresBtn();
                this.estPasFinal.setarTabelaOp2(tblProducaoPastaOp2);
                this.estPasFinal.setarTabelaOp1(tblProducaoPastaOp1);

            } else {
                this.tblProducaoPastaOp1.removeAll();
                this.tblProducaoPastaOp2.removeAll();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }

    }//GEN-LAST:event_btnProcurarActionPerformed

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
                if ((!this.txtQuantidade.getText().equals("0,00"))) {

                    int codigo = Integer.parseInt(this.txtCodigo.getText());
                    double quantidade = Double.parseDouble(this.txtQuantidade.getText().replace(".", "").replace(",", "."));

                    this.tblProducaoPastaOp1.removeAll();
                    this.tblProducaoPastaOp2.removeAll();
                    EstoquePastaFinalDao.listFinalOp22.clear();
                    EstoquePastaFinalDao.pastaProduzirOp22.clear();
                    this.lblValorReaproveitadoOpc1.setText("0,00");
                    this.estPasFinal.limparVariaveis(true);
                    this.estPasFinal.pastaCompativel(this.estPasFinal.buscaCodigoInsumos(codigo));
                    this.estPasFinal.buscarInsumos(codigo, quantidade, true);
                    this.estPasFinal.subtrairInsumos(codigo);
                    retornarCoresBtn();
                    this.estPasFinal.setarTabelaOp2(tblProducaoPastaOp2);
                    this.estPasFinal.setarTabelaOp1(tblProducaoPastaOp1);
                } else {
                    this.tblProducaoPastaOp1.removeAll();
                    this.tblProducaoPastaOp2.removeAll();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            }
        }
    }//GEN-LAST:event_txtQuantidadeKeyPressed

    private void btnVisualizarOp1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVisualizarOp1MouseEntered
        // quando o mouse estiver em cima
        if (this.btnVisualizarOp1.isEnabled()) {
            alteraCor(this.jPanel8, this.btnVisualizarOp1);
        }
    }//GEN-LAST:event_btnVisualizarOp1MouseEntered

    private void btnVisualizarOp1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVisualizarOp1MouseExited
        // quando o mouse sair de cima
        if (this.btnVisualizarOp1.isEnabled()) {
            retornaCor(this.jPanel8, this.btnVisualizarOp1);
        }
    }//GEN-LAST:event_btnVisualizarOp1MouseExited

    private void btnVisualizarOp2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVisualizarOp2MouseEntered
        // quando o mouse estiver em cima
        if (this.btnVisualizarOp2.isEnabled()) {
            alteraCor(this.jPanel7, this.btnVisualizarOp2);
        }
    }//GEN-LAST:event_btnVisualizarOp2MouseEntered

    private void btnVisualizarOp2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVisualizarOp2MouseExited
        // quando o mouse sair de cima
        if (this.btnVisualizarOp2.isEnabled()) {
            retornaCor(this.jPanel7, this.btnVisualizarOp2);
        }
    }//GEN-LAST:event_btnVisualizarOp2MouseExited

    private void btnVisualizarOp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisualizarOp1ActionPerformed
        //chama a tela para confirmar a produção de pasta
        if (this.frameOpPasta == null) {
            this.frameOpPasta = new TelaOpcaoPasta();
            this.util.retirarBordas(this.frameOpPasta);
        } else {
            this.frameOpPasta.dispose();
            this.frameOpPasta = new TelaOpcaoPasta();
            this.util.retirarBordas(this.frameOpPasta);
        }
        this.util.comandoInternal(this.frameOpPasta);
        TelaOpcaoPasta.lblPasta.setText(this.descricao);
        TelaOpcaoPasta.lblQuantidade.setText(this.txtQuantidade.getText() + " Kg");
        TelaOpcaoPasta.lblOpcao.setText("Primeira Opção -");
        TelaOpcaoPasta.lblOpDescricao.setText("Utilizando apenas Pastas do estoque");
        TelaOpcaoPasta.lblReaproveitado.setText(EstoquePastaFinalDao.valorReap);
        setaTabelaOpcPasta1();
    }//GEN-LAST:event_btnVisualizarOp1ActionPerformed

    private void btnVisualizarOp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisualizarOp2ActionPerformed
        //chama a tela para confirmar a produção de pasta
        if (this.frameOpPasta == null) {
            this.frameOpPasta = new TelaOpcaoPasta();
            this.util.retirarBordas(this.frameOpPasta);
        } else {
            this.frameOpPasta.dispose();
            this.frameOpPasta = new TelaOpcaoPasta();
            this.util.retirarBordas(this.frameOpPasta);
        }
        this.util.comandoInternal(this.frameOpPasta);
        TelaOpcaoPasta.lblPasta.setText(this.descricao);
        TelaOpcaoPasta.lblQuantidade.setText(this.txtQuantidade.getText() + " Kg");
        TelaOpcaoPasta.lblOpcao.setText("Segunda Opção -");
        TelaOpcaoPasta.lblOpDescricao.setText("Utilizando Pastas mais Insumos");
        TelaOpcaoPasta.lblReaproveitado.setText(EstoquePastaFinalDao.valorReap);
        TelaOpcaoPasta.lblCustoProducao.setText(EstoquePastaFinalDao.custoProd);
        setaTabelaOpcPasta2();
    }//GEN-LAST:event_btnVisualizarOp2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnProcurar;
    public static javax.swing.JButton btnVisualizarOp1;
    public static javax.swing.JButton btnVisualizarOp2;
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
    public static javax.swing.JPanel jPanel7;
    public static javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JLabel lblCustoProducao;
    public static javax.swing.JLabel lblValorReaproveitadoOpc1;
    public static javax.swing.JLabel lblValorReaproveitadoOpc2;
    private javax.swing.JLabel lbltext;
    public static javax.swing.JTable tblProducaoPastaOp1;
    public static javax.swing.JTable tblProducaoPastaOp2;
    public static javax.swing.JTextField txtCodigo;
    public static javax.swing.JFormattedTextField txtQuantidade;
    // End of variables declaration//GEN-END:variables
}
