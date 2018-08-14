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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.MascaraMoeda;
import util.Util;
import util.SoNumeros;
import util.UpperCaseDocument;

/**
 *
 * @author Leandro
 */
public class TelaMovimentacaoEstoque extends javax.swing.JInternalFrame {

    private Connection conexao = null;
    private String cbPesquisar = "ep.codigoReceita";
    private final MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();
    private final InsumoDao insumoDao = new InsumoDao();
    private final ReceitaDao receitaDao = new ReceitaDao();
    private final Util util = new Util();
    public static JInternalFrame framePesReceita;
    public static JInternalFrame framePesInsumo;
    public static JInternalFrame framePesEstoquePasta;
    private final TelaPesquisarReceita rec = new TelaPesquisarReceita();
    private final TelaPesquisarInsumos ins = new TelaPesquisarInsumos();

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
        this.txtEstData.setValue(null);
        limparCampos("Código:");
        this.txtEstUM.setText("kg");
        this.txtDescricao.setText(null);
        if (this.cbEstoque.getSelectedItem().equals("Insumo")) {
            this.txtEstUM.setText(null);
        }
    }

    private void movimentacao(boolean confirmaTipo, String confirmaEstoque) {
        MovimentacaoDto movDto = new MovimentacaoDto();
        MovimentacaoDao movDao = new MovimentacaoDao();
        int codigo = 0;
        if (confirmaEstoque.equals("Pasta")) {
            if (this.cbTipo.getSelectedItem().equals("Saída")) {
                codigo = Integer.parseInt(this.txtCodigo.getText());
            } else {
                codigo = movDao.buscaCodigoReceita(this.txtDescricao.getText());
            }
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
        movEstDto.setQuantidade(Double.parseDouble(this.txtEstQuantidade.getText().replace(".", "").replace(",", ".")));
        movEstDto.setData(inverterData(this.txtEstData.getText().replace("/", "-")));

        if (confirma == true) {
            movEstDto.setDataVencimento(movEstDao.dataVencimento(inverterData(this.txtEstData.getText().replace("/", "-")), this.receitaDao.buscaVencimento(Integer.parseInt(this.txtCodigo.getText()))));
            movEstDao.entradaPasta(movEstDto);
        } else {
            movEstDto.setDataVencimento(movEstDao.dataVencimento(inverterData(this.txtEstData.getText().replace("/", "-")), this.receitaDao.buscaVencimentoID(Integer.parseInt(this.txtCodigo.getText()))));
            movEstDao.saidaPasta(movEstDto.getQuantidade(), movEstDto.getCodigoReceita());
        }
    }

    //ativa a tabela de pastas
    private void ativarTblPasta() {
        this.lblRecIns.setText("Receita:");
        limparCampos("Código:");
        this.txtDescricao.setText(null);
        this.txtEstUM.setText("kg");
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
        this.txtEstQuantidade.setDocument(new MascaraMoeda());
        this.txtEstData.setValue(null);
        this.txtEstUM.setEnabled(false);
        this.txtCodigo.requestFocus();
        this.lblCustoProducao.setText("0,00");
        this.lblRecIns.setText("Insumo:");
    }

    // limpa os campos após entrada ou saida
    private void limparCampos(String codigoID) {
        this.txtCodigo.setEnabled(true);
        this.txtDescricao.setEnabled(false);
        this.txtEstQuantidade.setEnabled(false);
        this.txtEstData.setEnabled(false);
        this.txtCodigo.setText(null);
        this.txtDescricao.setText(null);
        this.txtEstQuantidade.setDocument(new MascaraMoeda());
        this.txtEstData.setValue(null);
        this.txtCodigo.requestFocus();
        this.lblCustoProducao.setText("0,00");
        this.lblCodigoID.setText(codigoID);
        ReceitaDao.custoPorKgReceita = 0;
    }

    private void alterarLbl() {
        if ((this.cbTipo.getSelectedItem().equals("Saída") && (this.cbEstoque.getSelectedItem().equals("Pasta")))) {
            this.lblCodigoID.setText("ID:");
        } else {
            this.lblCodigoID.setText("Código:");
        }
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
                    //verifica se a data é compatível
                    if (this.movEstDao.dataComparar(this.movEstDao.dataAtual(), inverterData(this.txtEstData.getText().replace("/", "-"))) == false) {
                        JOptionPane.showMessageDialog(null, "Não é possivel dar entrada com uma data futura.");
                    } else {
                        // verifica se a data é compatível  com o vencimento
                        if (this.movEstDao.dataComparar(this.movEstDao.dataAtual(), this.movEstDao.dataVencimento(inverterData(this.txtEstData.getText().replace("/", "-")), this.receitaDao.buscaVencimento(Integer.parseInt(this.txtCodigo.getText())))) == true) {
                            JOptionPane.showMessageDialog(null, "Data de vencimento da pasta é inferior a data atual.");
                        } else {
                            confirmaEstoquePasta(true);
                            movimentacao(true, "Pasta");
                            this.insumoDao.retirarInsumo(this.movEstDao.buscaCodigoReceita(this.txtDescricao.getText()));
                            limparCampos("Código:");
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
                        limparCampos("Código:");
                    }
                }
            } else {
                // confirma se é entrada ou saida
                if (this.cbTipo.getSelectedItem().equals("Entrada")) {

                    if (this.movEstDao.dataComparar(this.movEstDao.dataAtual(), inverterData(this.txtEstData.getText().replace("/", "-"))) == false) {
                        JOptionPane.showMessageDialog(null, "Não é possivel dar entrada com uma data futura.");
                    } else {

                        this.insumoDao.entradaInsumo(Double.parseDouble(this.txtEstQuantidade.getText().replace(".", "").replace(",", ".")), this.insumoDao.buscaCodigoInsumo(this.txtDescricao.getText()));
                        movimentacao(true, "Insumo");
                        limparCampos("Código:");
                    }
                } else {
                    //this.txtEstData.setText(inverterData(this.movEstDao.dataAtual()).replace("-", "/"));
                    if (this.insumoDao.saidaInsumo(Double.parseDouble(this.txtEstQuantidade.getText().replace(",", ".")), this.insumoDao.buscaCodigoInsumo(this.txtDescricao.getText())) == true) {
                        JOptionPane.showMessageDialog(null, "Saída de insumo realizada com sucesso.");
                        movimentacao(false, "Insumo");
                        limparCampos("Código:");
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao retirar os insumos.");
                    }
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
    
    //fecha as demais telas que estão abertas
    private void fecharTelas() {
        if (this.framePesReceita != null) {
            this.framePesReceita.dispose();
        }
        if (this.framePesEstoquePasta != null) {
            this.framePesEstoquePasta.dispose();
        }
        if (this.framePesInsumo != null) {
            this.framePesInsumo.dispose();
        }
    }
    
    private void fecharFramePesquizar() {
        if (this.framePesEstoquePasta != null) {
            this.framePesEstoquePasta.dispose();
        }
        if (this.framePesReceita != null) {
            this.framePesReceita.dispose();
        }
        if (this.framePesInsumo != null) {
            this.framePesInsumo.dispose();
        }
    }
    
     //chama o método para tornar as letras do campo Maiusculas
    private void upperCase() {
        this.txtDescricao.setDocument(new UpperCaseDocument());
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
        lblCodigoID = new javax.swing.JLabel();
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
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnConfirmar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnLimpar = new javax.swing.JButton();
        txtEstQuantidade = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel6 = new javax.swing.JLabel();
        lblCustoProducao = new javax.swing.JLabel();

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

        lblCodigoID.setForeground(new java.awt.Color(79, 79, 79));
        lblCodigoID.setText("Código:");
        jPanel2.add(lblCodigoID, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, -1));

        jLabel7.setForeground(new java.awt.Color(79, 79, 79));
        jLabel7.setText("Quantidade kg:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 130, -1, -1));

        lblRecIns.setForeground(new java.awt.Color(79, 79, 79));
        lblRecIns.setText("Receita:");
        jPanel2.add(lblRecIns, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, -1, -1));

        jLabel5.setForeground(new java.awt.Color(79, 79, 79));
        jLabel5.setText("Data:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 40, -1));

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
        jPanel2.add(txtEstData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 180, -1));

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

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 200, 100, 25));

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

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 200, 100, 25));

        txtEstQuantidade.setEnabled(false);
        txtEstQuantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEstQuantidadeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEstQuantidadeKeyReleased(evt);
            }
        });
        jPanel2.add(txtEstQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, 170, -1));

        jPanel5.setBackground(new java.awt.Color(236, 255, 209));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(155, 155, 155)));
        jPanel5.setPreferredSize(new java.awt.Dimension(860, 300));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDesktopPane1.setBackground(new java.awt.Color(236, 255, 209));

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 856, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 308, Short.MAX_VALUE)
        );

        jPanel5.add(jDesktopPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, -1, 308));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 859, 310));

        jLabel6.setText("Custo de produção: R$ ");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 204, -1, -1));

        lblCustoProducao.setForeground(new java.awt.Color(255, 0, 0));
        lblCustoProducao.setText("0,00");
        jPanel2.add(lblCustoProducao, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 204, -1, -1));

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
        }
        fecharTelas();
        alterarLbl();
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
        if (this.cbTipo.getSelectedItem().equals("Saída")) {
            this.lblCustoProducao.setText("0,00");
        }
        alterarLbl();
        this.txtCodigo.requestFocus();
        if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
            if (this.cbTipo.getSelectedItem().equals("Saída")) {
                limparCampos("ID:");
                fecharTelas();
            } else {
                fecharTelas();
                limparCampos("Código:");
            }
        }
    }//GEN-LAST:event_cbTipoActionPerformed

    private void btnMovEstPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovEstPesquisarActionPerformed
        // chama a tela de pesquisa receita ou insumo
        if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
            if (this.cbTipo.getSelectedItem().equals("Entrada")) {
                fecharTelas();
                if (this.framePesReceita == null) {
                    this.framePesReceita = new TelaPesquisarReceita();
                } else {
                    this.framePesReceita.dispose();
                    this.framePesReceita = new TelaPesquisarReceita();
                }
                this.framePesReceita.setVisible(true);
                this.jDesktopPane1.add(this.framePesReceita);
                this.rec.pesquisarReceita();
               // this.util.comandoInternal2(this.framePesReceita);
                TelaPesquisarReceita.confirmarEscolha = 2;
            } else {
                fecharTelas();
                if (this.framePesEstoquePasta == null) {
                    this.framePesEstoquePasta = new TelaPesquisarPastaEstoque();
                } else {
                    this.framePesEstoquePasta.dispose();
                    this.framePesEstoquePasta = new TelaPesquisarPastaEstoque();
                }
                this.framePesEstoquePasta.setVisible(true);
                this.jDesktopPane1.add(this.framePesEstoquePasta);
                this.rec.pesquisarReceita();
                //this.util.comandoInternal2(this.framePesEstoquePasta);
            }
        } else {
            fecharTelas();
            if (this.framePesInsumo == null) {
                this.framePesInsumo = new TelaPesquisarInsumos();
            } else {
                this.framePesInsumo.dispose();
                this.framePesInsumo = new TelaPesquisarInsumos();
            }
            this.framePesInsumo.setVisible(true);
            this.jDesktopPane1.add(this.framePesInsumo);
            this.ins.pesquisarInsumos();
            //this.util.comandoInternal2(this.framePesInsumo);
            TelaPesquisarInsumos.confirmarEscolha = false;
        }
    }//GEN-LAST:event_btnMovEstPesquisarActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // chama o metodo limpar
        //alteraCorPressionado(this.jPanel4, this.btnLimpar);
        limparCampos("Código:");
        this.txtDescricao.setText(null);
        if (this.cbEstoque.getSelectedItem().equals("Insumo")) {
            this.txtEstUM.setText(null);
        }
    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        // chama metodo ao pressionar a tecla Enter   
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
                if ((!this.txtCodigo.getText().equals("") && (this.cbTipo.getSelectedItem().equals("Saída")))) {
                    receitaDao.pesquisarReceitaMovi("select r.descricao, ri.custoPorKg from tbEstoquePasta as ep"
                            + " inner join tbReceitaInsumo as ri on ri.codigoReceita = ep.codigoReceita"
                            + " inner join tbreceita as r on r.codigorec = ri.codigoReceita"
                            + " where ep.ID ='" + Integer.parseInt(this.txtCodigo.getText()) + "'");
                    this.txtEstUM.setText("kg");
                } else {
                    receitaDao.pesquisarReceitaMovi("select r.descricao, ri.custoPorKg from tbReceitaInsumo as ri"
                            + " inner join tbreceita as r on r.codigorec = ri.codigoReceita"
                            + " where ri.codigoReceita ='" + Integer.parseInt(this.txtCodigo.getText()) + "'");
                    this.txtEstUM.setText("kg");
                }
            } else {
                if (!this.txtCodigo.getText().equals("")) {
                    this.insumoDao.pesquisarInsumos2(Integer.parseInt(this.txtCodigo.getText()));
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
        fecharFramePesquizar();
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
        fecharFramePesquizar();
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

    private void txtEstQuantidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstQuantidadeKeyReleased
        // calcula o preço de produção enquanto digita a quantidade que deseja produzir
        if ((this.cbEstoque.getSelectedItem().equals("Pasta")) && (this.cbTipo.getSelectedItem().equals("Entrada")) && (!this.txtEstQuantidade.getText().equals(""))) {
            this.lblCustoProducao.setText("" + this.util.formatadorQuant(this.receitaDao.custoPorKg(Double.parseDouble(this.txtEstQuantidade.getText().replace(".", "").replace(",", ".")))));
        }
    }//GEN-LAST:event_txtEstQuantidadeKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JButton btnMovEstPesquisar;
    private javax.swing.JComboBox<String> cbEstoque;
    public static javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JDesktopPane jDesktopPane1;
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
    private javax.swing.JLabel lblCodigoID;
    private javax.swing.JLabel lblCustoProducao;
    private javax.swing.JLabel lblRecIns;
    public static javax.swing.JTextField txtCodigo;
    public static javax.swing.JTextField txtDescricao;
    public static javax.swing.JFormattedTextField txtEstData;
    public static javax.swing.JFormattedTextField txtEstQuantidade;
    public static javax.swing.JTextField txtEstUM;
    // End of variables declaration//GEN-END:variables
}
