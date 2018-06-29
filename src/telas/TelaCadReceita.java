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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import util.ComboKeyHandler;
import util.SoNumeros;
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
    Util util = new Util();

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
        this.pasta.setarComboBox(cbCadReceitaTipo);
        this.cbCadReceitaTipo.setSelectedItem(null);

        cbAtivar();
    }

    private void confirmar(boolean confirmar) {
        ReceitaDto receitaDto = new ReceitaDto();
        ReceitaDao receitaDao = new ReceitaDao();

        receitaDto.setCodigorec(Integer.parseInt(this.txtCadRecCodigo.getText()));
        receitaDto.setDescricao(this.txtCadRecDes.getText());
        receitaDto.setPantone(this.txtCadRecPan.getText());
        receitaDto.setCodigoTipoPasta(this.pasta.buscaCodTipoPasta(this.cbCadReceitaTipo.getSelectedItem().toString()));
        receitaDto.setDatavencimento(Integer.parseInt(this.txtCadRecVal.getText()));

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
        DecimalFormat dFormat = new DecimalFormat("##0.00");
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

    private void cbAtivar() {
        JTextField text = (JTextField) this.cbCadReceitaTipo.getEditor().getEditorComponent();
        text.addKeyListener(new ComboKeyHandler(this.cbCadReceitaTipo));
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCadRecDes = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCadRecVal = new javax.swing.JTextField();
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
        btnAddConsumo = new javax.swing.JButton();
        txtCadRecConsumo = new javax.swing.JFormattedTextField();
        cbCadReceitaTipo = new javax.swing.JComboBox<>();
        tbnCadRecTipo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnCadRecAdicionar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnCadRecDeletar = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnCadRecLimpar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Receitas");
        setMaximumSize(new java.awt.Dimension(860, 560));
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
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(219, 237, 194)));
        jPanel2.setMinimumSize(new java.awt.Dimension(860, 530));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Código:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel2.setForeground(new java.awt.Color(79, 79, 79));
        jLabel2.setText("Pantone:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, -1, -1));
        jPanel2.add(txtCadRecDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 190, -1));

        jLabel3.setForeground(new java.awt.Color(79, 79, 79));
        jLabel3.setText("Tipo de pasta:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, -1, -1));

        jLabel4.setForeground(new java.awt.Color(79, 79, 79));
        jLabel4.setText("Descrição:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, -1, -1));

        jLabel5.setForeground(new java.awt.Color(79, 79, 79));
        jLabel5.setText("Validade:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 80, -1, -1));
        jPanel2.add(txtCadRecVal, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 110, 120, -1));

        txtCadRecCodigo.setText("0");
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
        jPanel2.add(txtCadRecCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 130, -1));
        jPanel2.add(txtCadRecPan, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, 130, -1));

        btnReceitaPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnReceitaPesquisar.setToolTipText("Pesquisar");
        btnReceitaPesquisar.setContentAreaFilled(false);
        btnReceitaPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReceitaPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceitaPesquisarActionPerformed(evt);
            }
        });
        jPanel2.add(btnReceitaPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 28, 31));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Cadastro de Receita");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jPanel1.setBackground(new java.awt.Color(229, 247, 203));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(188, 188, 188)));
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

        jLabel8.setForeground(new java.awt.Color(79, 79, 79));
        jLabel8.setText("Componentes:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));
        jPanel1.add(txtCadRecComponentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 260, -1));

        jLabel7.setForeground(new java.awt.Color(79, 79, 79));
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
        jPanel1.add(btnInsumoPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 0, 30, 30));

        btnAddConsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/++++.png"))); // NOI18N
        btnAddConsumo.setToolTipText("Adicionar");
        btnAddConsumo.setContentAreaFilled(false);
        btnAddConsumo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddConsumoActionPerformed(evt);
            }
        });
        jPanel1.add(btnAddConsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 38, 30, -1));
        jPanel1.add(txtCadRecConsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 110, -1));

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 820, 360));

        cbCadReceitaTipo.setEditable(true);
        jPanel2.add(cbCadReceitaTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 110, 150, -1));

        tbnCadRecTipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/++++.png"))); // NOI18N
        tbnCadRecTipo.setToolTipText("Adicionar");
        tbnCadRecTipo.setContentAreaFilled(false);
        tbnCadRecTipo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbnCadRecTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnCadRecTipoActionPerformed(evt);
            }
        });
        jPanel2.add(tbnCadRecTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(658, 108, 20, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/x.png"))); // NOI18N
        jButton1.setToolTipText("Apagar");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(678, 108, 20, -1));

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

        jPanel3.setBackground(new java.awt.Color(229, 247, 203));
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

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(816, 2, 40, 20));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCadRecAdicionar.setForeground(new java.awt.Color(69, 69, 69));
        btnCadRecAdicionar.setText("Salvar");
        btnCadRecAdicionar.setContentAreaFilled(false);
        btnCadRecAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadRecAdicionarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadRecAdicionarMouseExited(evt);
            }
        });
        btnCadRecAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecAdicionarActionPerformed(evt);
            }
        });
        jPanel4.add(btnCadRecAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 150, 80, 25));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCadRecDeletar.setForeground(new java.awt.Color(69, 69, 69));
        btnCadRecDeletar.setText("Eliminar");
        btnCadRecDeletar.setContentAreaFilled(false);
        btnCadRecDeletar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadRecDeletarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadRecDeletarMouseExited(evt);
            }
        });
        btnCadRecDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecDeletarActionPerformed(evt);
            }
        });
        jPanel5.add(btnCadRecDeletar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 150, 80, 25));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCadRecLimpar.setForeground(new java.awt.Color(69, 69, 69));
        btnCadRecLimpar.setText("Limpar");
        btnCadRecLimpar.setContentAreaFilled(false);
        btnCadRecLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadRecLimparMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadRecLimparMouseExited(evt);
            }
        });
        btnCadRecLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecLimparActionPerformed(evt);
            }
        });
        jPanel7.add(btnCadRecLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 80, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadRecAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecAdicionarActionPerformed
        //chama o metodo adicionar/salvar
        alteraCorPressionado(this.jPanel4, this.btnCadRecAdicionar);
        confirmaAcao(false);
    }//GEN-LAST:event_btnCadRecAdicionarActionPerformed

    private void btnCadRecLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecLimparActionPerformed
        // chama o metodo limpar campos; 
        alteraCorPressionado(this.jPanel7, this.btnCadRecLimpar);
        limparCampos();
        //desabilitarCampos();
    }//GEN-LAST:event_btnCadRecLimparActionPerformed

    private void btnCadRecDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecDeletarActionPerformed
        // chama o metodo deletar
        alteraCorPressionado(this.jPanel5, this.btnCadRecDeletar);
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
        limparCampos();
        if (this.framePesReceita == null) {
            this.framePesReceita = new TelaPesquisarReceita();
        } else {
            this.framePesReceita.dispose();
            this.framePesReceita = new TelaPesquisarReceita();
        }
        this.rec.pesquisarReceita();
        this.util.comandoInternal(this.framePesReceita);
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
        this.util.comandoInternal(this.framePesInsumo);
        TelaPesquisarInsumos.confirmarEscolha = true;
        TelaPesquisarInsumos.confimaTela = false;

    }//GEN-LAST:event_btnInsumoPesquisarActionPerformed

    private void btnAddConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddConsumoActionPerformed
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
                        recInsDao.adicionarComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()), this.txtCadRecComponentes.getText(), this.txtCadRecConsumo.getText().replace(",", "."));
                        setarTabela();
                        this.txtCadRecComponentes.setText(null);
                        this.txtCadRecConsumo.setValue(null);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnAddConsumoActionPerformed

    private void tbnCadRecTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnCadRecTipoActionPerformed
        // chama o metodo adicionar tipo de pasta
        if (this.cbCadReceitaTipo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Tipo de pasta inválido.");
        } else {

            boolean conf = this.pasta.buscaCodTipoPasta(this.cbCadReceitaTipo.getSelectedItem().toString()) <= 0;
            if (conf == false) {
                JOptionPane.showMessageDialog(null, "Tipo de pasta já existe.");
            } else {
                String pastaAdicionada = this.cbCadReceitaTipo.getSelectedItem().toString();
                this.pasta.addComboBox(this.cbCadReceitaTipo.getSelectedItem().toString());
                this.cbCadReceitaTipo.removeAllItems();
                this.pasta.setarComboBox(cbCadReceitaTipo);
                this.cbCadReceitaTipo.setSelectedItem(pastaAdicionada);
                cbAtivar();
            }
        }
    }//GEN-LAST:event_tbnCadRecTipoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Chama o metodo remover tipo de pasta
        if (this.cbCadReceitaTipo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Tipo de pasta inválido.");
        } else {
            int conf = this.pasta.buscaCodTipoPasta(this.cbCadReceitaTipo.getSelectedItem().toString());
            boolean confirmar = this.pasta.contTipoPasta(conf);
            if (confirmar == true) {
                JOptionPane.showMessageDialog(null, "Esse Tipo de pasta não pode ser removido.");
            } else {
                this.pasta.removeComboBox(this.cbCadReceitaTipo.getSelectedItem().toString());
                this.cbCadReceitaTipo.removeAllItems();
                this.pasta.setarComboBox(cbCadReceitaTipo);
                cbAtivar();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblCadRecComponentesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCadRecComponentesKeyPressed
        // Chama o metodo atualizar atraves da tecla Enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.recInsDao.atualizarComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()));
            this.tblCadRecComponentes.removeAll();
            this.rec.setarTbComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()));
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

    private void tblCadRecComponentesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCadRecComponentesKeyReleased
        // Chama o metodo atualizar enquanto digita
        if (!this.txtCadRecCodigo.getText().equals("")) {
            this.recInsDao.atualizarComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()));
            this.tblCadRecComponentes.removeAll();
            this.rec.setarTbComponentes(Integer.parseInt(this.txtCadRecCodigo.getText()));
        }
    }//GEN-LAST:event_tblCadRecComponentesKeyReleased

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

    private void btnMinimiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimiMouseEntered
        // quando o mouse estiver em cima
        this.jPanel6.setBackground(new Color(192, 221, 147));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
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
        this.jPanel3.setBackground(new Color(211, 57, 33));
        this.btnFechar.setForeground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnFecharMouseEntered

    private void btnFecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseExited
        // quando o mouse sair de cima
        this.jPanel3.setBackground(new Color(229, 247, 203));
        this.btnFechar.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnFecharMouseExited

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // chama o metodo sair
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
                } 
            }
        }
       
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnCadRecLimparMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadRecLimparMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel7, this.btnCadRecLimpar);
    }//GEN-LAST:event_btnCadRecLimparMouseEntered

    private void btnCadRecLimparMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadRecLimparMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel7, this.btnCadRecLimpar);
    }//GEN-LAST:event_btnCadRecLimparMouseExited

    private void btnCadRecAdicionarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadRecAdicionarMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel4, this.btnCadRecAdicionar);
    }//GEN-LAST:event_btnCadRecAdicionarMouseEntered

    private void btnCadRecAdicionarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadRecAdicionarMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel4, this.btnCadRecAdicionar);
    }//GEN-LAST:event_btnCadRecAdicionarMouseExited

    private void btnCadRecDeletarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadRecDeletarMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel5, this.btnCadRecDeletar);
    }//GEN-LAST:event_btnCadRecDeletarMouseEntered

    private void btnCadRecDeletarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadRecDeletarMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel5, this.btnCadRecDeletar);
    }//GEN-LAST:event_btnCadRecDeletarMouseExited

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // transfere o foco para o campo de codigo
        this.txtCadRecCodigo.requestFocus();
    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // transfere o foco para o campo de codigo
        this.txtCadRecCodigo.requestFocus();
        // quando o mouse sair de cima
        this.jPanel6.setBackground(new Color(229, 247, 203));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameDeiconified


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnAddConsumo;
    private javax.swing.JButton btnCadRecAdicionar;
    private javax.swing.JButton btnCadRecDeletar;
    private javax.swing.JButton btnCadRecLimpar;
    private javax.swing.JButton btnFechar;
    public static javax.swing.JButton btnInsumoPesquisar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JButton btnReceitaPesquisar;
    public static javax.swing.JComboBox<String> cbCadReceitaTipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
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
