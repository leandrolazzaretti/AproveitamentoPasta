/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import dto.UsuarioDto;
import dao.UsuarioDao;
import java.awt.Color;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaCadUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;

    TelaPesquisarUsuario user = new TelaPesquisarUsuario();
    UsuarioDao lista = new UsuarioDao();
    private int index = -1;
    private int codigo;
    Util frame = new Util();
    public static JInternalFrame framePesUsuario;

    /**
     * Creates new form TelaCadUsuario
     */
    public TelaCadUsuario() {
        initComponents();

        this.conexao = ModuloConexao.conector();
        //this.txtCadUsuId.setDocument(new SoNumeros());
        setarCodigo();
    }

    private void confirmar(boolean confirmar) {
        UsuarioDto usuarioDto = new UsuarioDto();
        UsuarioDao usuarioDao = new UsuarioDao();

        usuarioDto.setCodigo(Integer.parseInt(this.txtCadUsuId.getText()));
        usuarioDto.setNome(this.txtCadUsuNome.getText());
        usuarioDto.setLogin(this.txtCadUsuLogin.getText());
        usuarioDto.setSenha(this.txtCadUsuSenha.getText());
        usuarioDto.setPerfil(this.cbCadUsuPerfil.getSelectedItem().toString());

        if (confirmar == true) {
            usuarioDao.adicionar(usuarioDto);

        } else {
            usuarioDao.atualizar(usuarioDto, Integer.parseInt(this.txtCadUsuId.getText()));
            this.txtCadUsuSenha.setText(null);
            this.txtCadUsuConfirmarSenha.setText(null);
        }
    }

    //verifica se existem campos em branco
    private boolean verificaCampos() {
        if ((this.txtCadUsuId.getText().isEmpty()) || (this.txtCadUsuNome.getText().isEmpty()) || (this.txtCadUsuLogin.getText().isEmpty()) || (this.txtCadUsuSenha.getText().isEmpty()) || (this.txtCadUsuConfirmarSenha.getText().isEmpty())) {
            return false;
        } else {
            return true;
        }
    }

    //confirma se o login já existe
    private boolean confirmaLogin(String login) {
        String sql = "select count (login) as total from tbusuarios where login ='" + login + "';";

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

    //confirma se o login já existe
    private boolean confirmaCodigo(String codigo) {
        String sql = "select count (codigo) as total from tbusuarios where codigo ='" + codigo + "';";

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
        return total <= 0;
    }

    //seta os campos através da lista
    private void setarCamposLista(UsuarioDto user) {
        this.txtCadUsuId.setEnabled(false);
        this.txtCadUsuId.setText(String.valueOf(user.getCodigo()));
        this.txtCadUsuNome.setText(user.getNome());
        this.txtCadUsuLogin.setText(user.getLogin());
        this.cbCadUsuPerfil.setSelectedItem(user.getPerfil());
    }

    //seta o código do usuário
    private void setarCodigo() {
        String sql = "select max(codigo)+1 from tbusuarios";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            this.txtCadUsuId.setText(rs.getString(1));

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        this.txtCadUsuId.setEnabled(false);
    }

    //limpa todos os campos da tela
    private void limparCampos() {
        this.txtCadUsuId.setEnabled(true);
        this.txtCadUsuLogin.setEnabled(true);
        this.txtCadUsuId.setText(null);
        this.txtCadUsuNome.setText(null);
        this.txtCadUsuLogin.setText(null);
        this.txtCadUsuSenha.setText(null);
        this.txtCadUsuConfirmarSenha.setText(null);
        this.cbCadUsuPerfil.setSelectedItem("Administrador");
        this.btnProximo.setEnabled(true);
        this.btnUltimo.setEnabled(true);
        this.btnPrimeiro.setEnabled(true);
        this.btnAnterior.setEnabled(true);
        this.index = -1;
        setarCodigo();
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
            total = confirmaCodigo(this.txtCadUsuId.getText());
            if (total == false) {
                if (this.txtCadUsuSenha.getText().equals(this.txtCadUsuConfirmarSenha.getText())) {
                    confirmar(false);
                } else {
                    JOptionPane.showMessageDialog(null, "As senhas não são correspondentes.");
                    conf2 = true;
                }
            } else {
                //chama o metodo para confirmar se o login já existe
                total = confirmaLogin(this.txtCadUsuLogin.getText());
                if (total == false) {
                    JOptionPane.showMessageDialog(null, "Login já existe.");
                    conf2 = true;

                } else {
                    //verifica a compatibilidade das senhas
                    if (this.txtCadUsuSenha.getText().equals(this.txtCadUsuConfirmarSenha.getText())) {
                        //adicionar();
                        confirmar(true);
                        limparCampos();
                    } else {
                        JOptionPane.showMessageDialog(null, "As senhas não são correspondentes.");
                        conf2 = true;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCadUsuNome = new javax.swing.JTextField();
        txtCadUsuLogin = new javax.swing.JTextField();
        cbCadUsuPerfil = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtCadUsuId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnCadUsePesquisar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtCadUsuSenha = new javax.swing.JPasswordField();
        txtCadUsuConfirmarSenha = new javax.swing.JPasswordField();
        jPanel6 = new javax.swing.JPanel();
        btnMinimi = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnExc = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnAdi = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnLimpar = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnProximo = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnPrimeiro = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnUltimo = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Usuários");
        setMaximumSize(new java.awt.Dimension(417, 409));
        setPreferredSize(new java.awt.Dimension(420, 406));
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

        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText(" Nome:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        jLabel2.setForeground(new java.awt.Color(79, 79, 79));
        jLabel2.setText(" Login:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        jLabel3.setForeground(new java.awt.Color(79, 79, 79));
        jLabel3.setText(" Senha:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        jLabel4.setForeground(new java.awt.Color(79, 79, 79));
        jLabel4.setText(" Perfil:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));
        jPanel1.add(txtCadUsuNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 213, -1));
        jPanel1.add(txtCadUsuLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 213, -1));

        cbCadUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Usuário" }));
        jPanel1.add(cbCadUsuPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 213, -1));

        jLabel5.setForeground(new java.awt.Color(79, 79, 79));
        jLabel5.setText(" Código:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));
        jPanel1.add(txtCadUsuId, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 150, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Cadastro de Usuários");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        btnCadUsePesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnCadUsePesquisar.setToolTipText("Pesquisar");
        btnCadUsePesquisar.setContentAreaFilled(false);
        btnCadUsePesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadUsePesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadUsePesquisarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCadUsePesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 30, 30));

        jLabel7.setForeground(new java.awt.Color(79, 79, 79));
        jLabel7.setText("Confirmar Senha:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 221, -1, -1));
        jPanel1.add(txtCadUsuSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 213, -1));
        jPanel1.add(txtCadUsuConfirmarSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 213, -1));

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

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 2, 40, 20));

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

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 2, 40, 20));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnExc.setForeground(new java.awt.Color(79, 79, 79));
        btnExc.setText("Eliminar");
        btnExc.setContentAreaFilled(false);
        btnExc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExcMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExcMouseExited(evt);
            }
        });
        btnExc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcActionPerformed(evt);
            }
        });
        jPanel2.add(btnExc, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 320, 80, 25));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAdi.setForeground(new java.awt.Color(79, 79, 79));
        btnAdi.setText("Salvar");
        btnAdi.setContentAreaFilled(false);
        btnAdi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdiMouseExited(evt);
            }
        });
        btnAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdiActionPerformed(evt);
            }
        });
        jPanel4.add(btnAdi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 320, 80, 25));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLimpar.setForeground(new java.awt.Color(79, 79, 79));
        btnLimpar.setText("Limpar");
        btnLimpar.setContentAreaFilled(false);
        btnLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
        jPanel5.add(btnLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 25));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 80, 25));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnProximo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnProximo.setForeground(new java.awt.Color(79, 79, 79));
        btnProximo.setText(">");
        btnProximo.setContentAreaFilled(false);
        btnProximo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProximo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProximoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProximoMouseExited(evt);
            }
        });
        btnProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProximoActionPerformed(evt);
            }
        });
        jPanel7.add(btnProximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 42, 21));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 350, 42, 21));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPrimeiro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrimeiro.setForeground(new java.awt.Color(79, 79, 79));
        btnPrimeiro.setText("<<");
        btnPrimeiro.setContentAreaFilled(false);
        btnPrimeiro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrimeiro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPrimeiroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPrimeiroMouseExited(evt);
            }
        });
        btnPrimeiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrimeiroActionPerformed(evt);
            }
        });
        jPanel8.add(btnPrimeiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 21));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 50, 21));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnUltimo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUltimo.setForeground(new java.awt.Color(79, 79, 79));
        btnUltimo.setText(">>");
        btnUltimo.setContentAreaFilled(false);
        btnUltimo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUltimo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUltimoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUltimoMouseExited(evt);
            }
        });
        btnUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUltimoActionPerformed(evt);
            }
        });
        jPanel9.add(btnUltimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 21));

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 350, 50, 21));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAnterior.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnterior.setForeground(new java.awt.Color(79, 79, 79));
        btnAnterior.setText("<");
        btnAnterior.setContentAreaFilled(false);
        btnAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnterior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAnteriorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAnteriorMouseExited(evt);
            }
        });
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        jPanel10.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 42, 21));

        jPanel1.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 350, 42, 21));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBounds(221, 77, 417, 409);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadUsePesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadUsePesquisarActionPerformed
        // chama a TelaPesquisarUsuario framePesUsuario
        if (this.framePesUsuario == null) {
            this.framePesUsuario = new TelaPesquisarUsuario();
        } else {
            this.framePesUsuario.dispose();
            this.framePesUsuario = new TelaPesquisarUsuario();
        }
        this.user.pesquisarUsuario();
        this.frame.comandoInternal(this.framePesUsuario);
    }//GEN-LAST:event_btnCadUsePesquisarActionPerformed

    private void btnUltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUltimoActionPerformed
        // ultimo registro
        this.index = this.lista.list().size() - 1;
        setarCamposLista(this.lista.list().get(this.index));
        this.btnProximo.setEnabled(false);
        this.btnUltimo.setEnabled(false);
        this.btnPrimeiro.setEnabled(true);
        this.btnAnterior.setEnabled(true);
    }//GEN-LAST:event_btnUltimoActionPerformed

    private void btnPrimeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrimeiroActionPerformed
        // primeiro registro
        this.index = 0;

        setarCamposLista(this.lista.list().get(this.index));
        this.btnProximo.setEnabled(true);
        this.btnUltimo.setEnabled(true);
        this.btnPrimeiro.setEnabled(false);
        this.btnAnterior.setEnabled(false);
    }//GEN-LAST:event_btnPrimeiroActionPerformed

    private void btnProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProximoActionPerformed
        // proximo registro
        this.index++;
        if (this.index < this.lista.list().size() - 1) {
            setarCamposLista(this.lista.list().get(this.index));
            this.btnAnterior.setEnabled(true);
            this.btnPrimeiro.setEnabled(true);
        } else {
            setarCamposLista(this.lista.list().get(this.index));
            this.btnProximo.setEnabled(false);
            this.btnUltimo.setEnabled(false);
        }
    }//GEN-LAST:event_btnProximoActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        // registro anterior
        this.index--;
        if (this.index > 0) {
            setarCamposLista(this.lista.list().get(this.index));
            this.btnProximo.setEnabled(true);
            this.btnUltimo.setEnabled(true);
        } else {
            this.index = 0;
            setarCamposLista(this.lista.list().get(this.index));
            this.btnAnterior.setEnabled(false);
            this.btnPrimeiro.setEnabled(false);
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // chama o metodo limpar
        alteraCorPressionado(this.jPanel5, this.btnLimpar);
        limparCampos();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnExcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcActionPerformed
        // chamad o metodo deletar
        alteraCorPressionado(this.jPanel2, this.btnExc);
        if ((this.txtCadUsuId.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Código inválido.");
        } else {
            UsuarioDao usuarioDao = new UsuarioDao();
            usuarioDao.deletar(Integer.parseInt(this.txtCadUsuId.getText()));
            limparCampos();
        }
    }//GEN-LAST:event_btnExcActionPerformed

    private void btnAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdiActionPerformed
        //chama o metodo adicionar / salvar
        alteraCorPressionado(this.jPanel4, this.btnAdi);
        confirmaAcao(false);
    }//GEN-LAST:event_btnAdiActionPerformed

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
       // gerar mensagem de salvar antes de sair
        if ((this.txtCadUsuLogin.getText().isEmpty()) && (this.txtCadUsuNome.getText().isEmpty()) && (this.txtCadUsuSenha.getText().isEmpty()) && (this.txtCadUsuConfirmarSenha.getText().isEmpty())) {
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
        alteraCor(this.jPanel5, this.btnLimpar);
    }//GEN-LAST:event_btnLimparMouseEntered

    private void btnLimparMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimparMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel5, this.btnLimpar);
    }//GEN-LAST:event_btnLimparMouseExited

    private void btnAdiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAdiMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel4, this.btnAdi);
    }//GEN-LAST:event_btnAdiMouseEntered

    private void btnAdiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAdiMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel4, this.btnAdi);
    }//GEN-LAST:event_btnAdiMouseExited

    private void btnExcMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel2, this.btnExc);
    }//GEN-LAST:event_btnExcMouseEntered

    private void btnExcMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel2, this.btnExc);
    }//GEN-LAST:event_btnExcMouseExited

    private void btnPrimeiroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrimeiroMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel8, this.btnPrimeiro);
    }//GEN-LAST:event_btnPrimeiroMouseEntered

    private void btnPrimeiroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrimeiroMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel8, this.btnPrimeiro);
    }//GEN-LAST:event_btnPrimeiroMouseExited

    private void btnAnteriorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnteriorMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel10, this.btnAnterior);
    }//GEN-LAST:event_btnAnteriorMouseEntered

    private void btnAnteriorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnteriorMouseExited
         // quando o mouse sair de cima     
        retornaCor(this.jPanel10, this.btnAnterior);
    }//GEN-LAST:event_btnAnteriorMouseExited

    private void btnProximoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProximoMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel7, this.btnProximo);
    }//GEN-LAST:event_btnProximoMouseEntered

    private void btnProximoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProximoMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel7, this.btnProximo);
    }//GEN-LAST:event_btnProximoMouseExited

    private void btnUltimoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUltimoMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel9, this.btnUltimo);
    }//GEN-LAST:event_btnUltimoMouseEntered

    private void btnUltimoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUltimoMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel9, this.btnUltimo);
    }//GEN-LAST:event_btnUltimoMouseExited

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // transfere o foco para campo de nome
        this.txtCadUsuNome.requestFocus();
    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        // transfere o foco para campo de nome
        this.txtCadUsuNome.requestFocus();
        // quando o mouse sair de cima
        this.jPanel6.setBackground(new Color(229, 247, 203));
        this.btnMinimi.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_formInternalFrameDeiconified


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdi;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnCadUsePesquisar;
    private javax.swing.JButton btnExc;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnMinimi;
    private javax.swing.JButton btnPrimeiro;
    private javax.swing.JButton btnProximo;
    private javax.swing.JButton btnUltimo;
    public static javax.swing.JComboBox<String> cbCadUsuPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField txtCadUsuConfirmarSenha;
    public static javax.swing.JTextField txtCadUsuId;
    public static javax.swing.JTextField txtCadUsuLogin;
    public static javax.swing.JTextField txtCadUsuNome;
    public static javax.swing.JPasswordField txtCadUsuSenha;
    // End of variables declaration//GEN-END:variables

}
