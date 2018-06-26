/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import Report.Relatorio;
import conexao.ModuloConexao;
import dto.UsuarioDto;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.Util;
import java.util.HashMap;

/**
 *
 * @author Leandro
 */
public class TelaPrincipal extends javax.swing.JFrame {

    public static JInternalFrame frameUsuario;
    public static JInternalFrame frameInsumo;
    public static JInternalFrame frameReceita;
    public static JInternalFrame frameMovimentacao;
    public static JInternalFrame frameEstoquePasta;

    private List<UsuarioDto> lista = new ArrayList<UsuarioDto>();

    Util util = new Util();
    int xMouse;
    int yMouse;

    Connection conexao = null;

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();
        this.conexao = ModuloConexao.conector();
    }

    private void gerarRelatorio(String relatorio) {
        // Gerando um relatório de clientes
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão deste relatório?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            //imprimindo relatório
            try {
                //Usando a classe JasperPrint para preparar a impressão de um relatório
                JasperPrint print = JasperFillManager.fillReport("C:/Users/Leandro/Documents/NetBeansProjects/prjAproveitamentoPastas/src/Report/" + relatorio + ".jasper", null, conexao);
                // a linha abaix exibe o relatório através da classe JasperViewer
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            //Lista com os parametros para o relátorio
            HashMap params = new HashMap<>();

            //Passândo parâmetros e convertendo o dados pra ser compativel
            params.put("datanasci",
                    new SimpleDateFormat("yyyy-MM-dd").
                            parse(jTextFieldData.getText()));

            //Invocando a geração do relatório 
            String file = new Relatorio().gerarRelatorio(params,
                    "cliente-data-parametro", "pdf");

            //Exibindo o relatório na tela para o usuário
            this.desktop.getDesktop().open(new File(file));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

//    private void relatorioUsuarioSetar(){
//        String sql = "select * from tbusuarios";
//        PreparedStatement pst;
//        UsuarioDto usuario = new UsuarioDto();
//        try {
//            pst = this.conexao.prepareStatement(sql);
//            ResultSet rs = pst.executeQuery();
//            while(rs.next()){
//                usuario.setIduser(rs.getInt(1));
//                usuario.setNome(rs.getString(2));
//                usuario.setLogin(rs.getString(3));
//                usuario.setPerfil(rs.getString(5));
//             
//                
//                this.lista.add(usuario);
//            }
//            
//            pst.close();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//    }
//    private void gerarRelatorioUser(){
//        Relatorio relatorio = new Relatorio();
//        
//        try {
//            relatorio.gerarRelatorio(this.lista);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//            System.out.println(e);
//        }
//        
//    }
//    // metodo para retirar todasd as bordas do JinternalFrame(gambiarra)
//    private void retirarBordas(JInternalFrame frame) {
//        ((BasicInternalFrameUI) frame.getUI()).setNorthPane(null); //retirar o painel superior
//        frame.setBorder(null);//retirar bordas
//    }
    // quando o mouse estiver em cima
    private void alteraCor(JPanel painel, JSeparator separador, JButton botao) {
        painel.setBackground(new Color(229, 247, 203));
        separador.setBackground(new Color(229, 247, 203));
        separador.setForeground(new Color(229, 247, 203));
        botao.setForeground(new Color(175, 175, 175));
    }

    // retorna a cor original do botão
    public void retornaCor(JPanel painel, JSeparator separador, JButton botao) {
        painel.setBackground(new Color(255, 255, 255));
        separador.setBackground(new Color(255, 255, 255));
        separador.setForeground(new Color(219, 219, 219));
        botao.setForeground(new Color(66, 66, 66));

    }

    // quando o botão for pressionado
    private void alteraCorPressionado(JPanel painel, JSeparator separador, JButton botao) {
        painel.setBackground(new Color(198, 226, 163));
        separador.setBackground(new Color(198, 226, 163));
        separador.setForeground(new Color(198, 226, 163));
        botao.setForeground(new Color(255, 255, 255));
    }

    private void confirmaBotaoTela() {

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
        jLabel1 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnUsuario = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        btnMovEstoque = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        btnReceita = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        btnInsumo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        tbnEstPasta = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        Desktop = new javax.swing.JDesktopPane();
        menu = new javax.swing.JMenuBar();
        menCadastro = new javax.swing.JMenu();
        menCadIns = new javax.swing.JMenuItem();
        menCadRec = new javax.swing.JMenuItem();
        menCadUsu = new javax.swing.JMenuItem();
        menControle = new javax.swing.JMenu();
        menConMovEst = new javax.swing.JMenuItem();
        menConEstPas = new javax.swing.JMenuItem();
        menRelatorio = new javax.swing.JMenu();
        menRelIns = new javax.swing.JMenuItem();
        menRelRec = new javax.swing.JMenuItem();
        menRelUsu = new javax.swing.JMenuItem();
        menRelMovEst = new javax.swing.JMenuItem();
        menOpc = new javax.swing.JMenu();
        menOpcSair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1100, 620));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(210, 226, 186));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Logado como: ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        lblUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(36, 160, 27));
        lblUsuario.setText("Usuário");
        jPanel1.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, -1, -1));

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 580, 859, 36));

        jPanel4.setBackground(new java.awt.Color(210, 226, 186));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 859, 20));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnUsuario.setBackground(new java.awt.Color(153, 153, 255));
        btnUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUsuario.setForeground(new java.awt.Color(66, 66, 66));
        btnUsuario.setText("Usuário");
        btnUsuario.setBorder(null);
        btnUsuario.setContentAreaFilled(false);
        btnUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUsuarioMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUsuarioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUsuarioMouseExited(evt);
            }
        });
        btnUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuarioActionPerformed(evt);
            }
        });
        jPanel5.add(btnUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 40));

        jSeparator4.setForeground(new java.awt.Color(219, 219, 219));
        jPanel5.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 10));

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 200, 238, 40));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnMovEstoque.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnMovEstoque.setForeground(new java.awt.Color(66, 66, 66));
        btnMovEstoque.setText("Movimentação Estoque");
        btnMovEstoque.setContentAreaFilled(false);
        btnMovEstoque.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMovEstoque.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMovEstoqueMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMovEstoqueMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMovEstoqueMouseExited(evt);
            }
        });
        btnMovEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovEstoqueActionPerformed(evt);
            }
        });
        jPanel6.add(btnMovEstoque, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 40));

        jSeparator2.setForeground(new java.awt.Color(219, 219, 219));
        jPanel6.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 10));

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 370, 238, 40));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnReceita.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReceita.setForeground(new java.awt.Color(66, 66, 66));
        btnReceita.setText("Receita");
        btnReceita.setContentAreaFilled(false);
        btnReceita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReceita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReceitaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReceitaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReceitaMouseExited(evt);
            }
        });
        btnReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceitaActionPerformed(evt);
            }
        });
        jPanel7.add(btnReceita, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 40));

        jSeparator3.setForeground(new java.awt.Color(219, 219, 219));
        jPanel7.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 10));

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 160, 238, 40));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnInsumo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnInsumo.setForeground(new java.awt.Color(66, 66, 66));
        btnInsumo.setText("Insumo");
        btnInsumo.setContentAreaFilled(false);
        btnInsumo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInsumo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInsumoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnInsumoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnInsumoMouseExited(evt);
            }
        });
        btnInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsumoActionPerformed(evt);
            }
        });
        jPanel8.add(btnInsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 40));

        jSeparator1.setForeground(new java.awt.Color(219, 219, 219));
        jPanel8.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 10));

        jPanel3.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 120, 238, 40));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(36, 160, 27));
        jLabel2.setText("Controle");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 60, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(36, 160, 27));
        jLabel3.setText("Cadastro");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbnEstPasta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbnEstPasta.setForeground(new java.awt.Color(66, 66, 66));
        tbnEstPasta.setText("Estoque Pasta");
        tbnEstPasta.setContentAreaFilled(false);
        tbnEstPasta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbnEstPasta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbnEstPastaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tbnEstPastaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tbnEstPastaMouseExited(evt);
            }
        });
        tbnEstPasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnEstPastaActionPerformed(evt);
            }
        });
        jPanel9.add(tbnEstPasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 40));

        jSeparator6.setForeground(new java.awt.Color(219, 219, 219));
        jPanel9.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 10));

        jPanel3.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 410, 238, 40));

        jSeparator5.setForeground(new java.awt.Color(219, 219, 219));
        jPanel3.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 220, 10));

        jSeparator7.setForeground(new java.awt.Color(219, 219, 219));
        jPanel3.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 220, 10));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 616));

        Desktop.setBackground(new java.awt.Color(244, 249, 245));
        Desktop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 242, 242)));
        Desktop.setForeground(new java.awt.Color(255, 255, 255));
        Desktop.setMaximumSize(new java.awt.Dimension(860, 560));
        Desktop.setName(""); // NOI18N

        javax.swing.GroupLayout DesktopLayout = new javax.swing.GroupLayout(Desktop);
        Desktop.setLayout(DesktopLayout);
        DesktopLayout.setHorizontalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 856, Short.MAX_VALUE)
        );
        DesktopLayout.setVerticalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 558, Short.MAX_VALUE)
        );

        jPanel2.add(Desktop, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 858, 560));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 616));

        menu.setBackground(new java.awt.Color(255, 255, 255));
        menu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 170, 170)));
        menu.setForeground(new java.awt.Color(255, 255, 255));
        menu.setBorderPainted(false);
        menu.setPreferredSize(new java.awt.Dimension(214, 30));
        menu.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                menuMouseDragged(evt);
            }
        });
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuMousePressed(evt);
            }
        });

        menCadastro.setText("   Cadastro  ");
        menCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadastroActionPerformed(evt);
            }
        });

        menCadIns.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
        menCadIns.setText("Insumo");
        menCadIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadInsActionPerformed(evt);
            }
        });
        menCadastro.add(menCadIns);

        menCadRec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        menCadRec.setText("Receita");
        menCadRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadRecActionPerformed(evt);
            }
        });
        menCadastro.add(menCadRec);

        menCadUsu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK));
        menCadUsu.setText("Usuário");
        menCadUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadUsuActionPerformed(evt);
            }
        });
        menCadastro.add(menCadUsu);

        menu.add(menCadastro);

        menControle.setText("  Controle  ");

        menConMovEst.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        menConMovEst.setText("Movimentação Estoque");
        menConMovEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menConMovEstActionPerformed(evt);
            }
        });
        menControle.add(menConMovEst);

        menConEstPas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        menConEstPas.setText("Estoque Pasta");
        menConEstPas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menConEstPasActionPerformed(evt);
            }
        });
        menControle.add(menConEstPas);

        menu.add(menControle);

        menRelatorio.setText("   Relatório  ");

        menRelIns.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menRelIns.setText("Insumo");
        menRelIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelInsActionPerformed(evt);
            }
        });
        menRelatorio.add(menRelIns);

        menRelRec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        menRelRec.setText("Receita");
        menRelRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelRecActionPerformed(evt);
            }
        });
        menRelatorio.add(menRelRec);

        menRelUsu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        menRelUsu.setText("Usuário");
        menRelUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelUsuActionPerformed(evt);
            }
        });
        menRelatorio.add(menRelUsu);

        menRelMovEst.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        menRelMovEst.setText("Movimentação");
        menRelMovEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelMovEstActionPerformed(evt);
            }
        });
        menRelatorio.add(menRelMovEst);

        menu.add(menRelatorio);

        menOpc.setText("  Opções  ");

        menOpcSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        menOpcSair.setText("Sair");
        menOpcSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOpcSairActionPerformed(evt);
            }
        });
        menOpc.add(menOpcSair);

        menu.add(menOpc);

        setJMenuBar(menu);

        setSize(new java.awt.Dimension(1105, 674));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menOpcSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOpcSairActionPerformed
        // evento sair
        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (sair == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_menOpcSairActionPerformed

    private void btnUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuarioActionPerformed
        // Chama a tela usuario
        if (this.frameUsuario == null) {
            this.frameUsuario = new TelaCadUsuario();
            this.util.retirarBordas(this.frameUsuario);

        } else {
            this.frameUsuario.dispose();
            this.frameUsuario = new TelaCadUsuario();
            this.util.retirarBordas(this.frameUsuario);
        }
        this.util.comandoInternal(this.frameUsuario);
    }//GEN-LAST:event_btnUsuarioActionPerformed

    private void menCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadastroActionPerformed

    }//GEN-LAST:event_menCadastroActionPerformed

    private void menCadUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadUsuActionPerformed
        // cahama a TelaCadUsu     
        if (this.frameUsuario == null) {
            this.frameUsuario = new TelaCadUsuario();
            this.util.retirarBordas(this.frameUsuario);
        } else {
            this.frameUsuario.dispose();
            this.frameUsuario = new TelaCadUsuario();
            this.util.retirarBordas(this.frameUsuario);
        }
        this.util.comandoInternal(this.frameUsuario);
    }//GEN-LAST:event_menCadUsuActionPerformed

    private void btnInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsumoActionPerformed
        // chama a TelaCadInsumos  
        if (this.frameInsumo == null) {
            this.frameInsumo = new TelaCadInsumo();
            this.util.retirarBordas(this.frameInsumo);
        } else {
            this.frameInsumo.dispose();
            this.frameInsumo = new TelaCadInsumo();
            this.util.retirarBordas(this.frameInsumo);
        }
        this.util.comandoInternal(this.frameInsumo);
//        }
    }//GEN-LAST:event_btnInsumoActionPerformed

    private void menCadInsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadInsActionPerformed
        // chama a TelaCadInsumos     
        if (this.frameInsumo == null) {
            this.frameInsumo = new TelaCadInsumo();
            this.util.retirarBordas(this.frameInsumo);
        } else {
            this.frameInsumo.dispose();
            this.frameInsumo = new TelaCadInsumo();
            this.util.retirarBordas(this.frameInsumo);
        }
        this.util.comandoInternal(this.frameInsumo);

    }//GEN-LAST:event_menCadInsActionPerformed

    private void btnReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceitaActionPerformed
        // chama a TelaCadReceita
        if (this.frameReceita == null) {
            this.frameReceita = new TelaCadReceita();
            this.util.retirarBordas(this.frameReceita);
        } else {
            this.frameReceita.dispose();
            this.frameReceita = new TelaCadReceita();
            this.util.retirarBordas(this.frameReceita);
        }
        this.util.comandoInternal(this.frameReceita);
    }//GEN-LAST:event_btnReceitaActionPerformed

    private void menCadRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadRecActionPerformed
        // chama a TelaCadReceita
        if (this.frameReceita == null) {
            this.frameReceita = new TelaCadReceita();
            this.util.retirarBordas(this.frameReceita);
        } else {
            this.frameReceita.dispose();
            this.frameReceita = new TelaCadReceita();
            this.util.retirarBordas(this.frameReceita);
        }
        this.util.comandoInternal(this.frameReceita);
    }//GEN-LAST:event_menCadRecActionPerformed

    private void menConMovEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menConMovEstActionPerformed
        // chama TelaMovimentacaoEstoque
        if (this.frameMovimentacao == null) {
            this.frameMovimentacao = new TelaMovimentacaoEstoque();
            this.util.retirarBordas(this.frameMovimentacao);
        } else {
            this.frameMovimentacao.dispose();
            this.frameMovimentacao = new TelaMovimentacaoEstoque();
            this.util.retirarBordas(this.frameMovimentacao);
        }
        this.util.comandoInternal(this.frameMovimentacao);
    }//GEN-LAST:event_menConMovEstActionPerformed

    private void tbnEstPastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnEstPastaActionPerformed
        // chama TelaEstoquePasta
        if (this.frameEstoquePasta == null) {
            this.frameEstoquePasta = new TelaEstoquePasta();
            //this.util.retirarBordas(this.frameEstoquePasta);
        } else {
            this.frameEstoquePasta.dispose();
            this.frameEstoquePasta = new TelaEstoquePasta();
            //this.util.retirarBordas(this.frameEstoquePasta);
        }
        this.util.comandoInternal(this.frameEstoquePasta);
    }//GEN-LAST:event_tbnEstPastaActionPerformed

    private void menConEstPasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menConEstPasActionPerformed
        // chama TelaEstoquePasta
        if (this.frameEstoquePasta == null) {
            this.frameEstoquePasta = new TelaEstoquePasta();
            //this.util.retirarBordas(this.frameEstoquePasta);
        } else {
            this.frameEstoquePasta.dispose();
            this.frameEstoquePasta = new TelaEstoquePasta();
            //this.util.retirarBordas(this.frameEstoquePasta);

        }
        this.util.comandoInternal(this.frameEstoquePasta);
    }//GEN-LAST:event_menConEstPasActionPerformed

    private void btnMovEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovEstoqueActionPerformed
        // chama TelaMovimentação Estoque
        if (this.frameMovimentacao == null) {
            this.frameMovimentacao = new TelaMovimentacaoEstoque();
            this.util.retirarBordas(this.frameMovimentacao);
        } else {
            this.frameMovimentacao.dispose();
            this.frameMovimentacao = new TelaMovimentacaoEstoque();
            this.util.retirarBordas(this.frameMovimentacao);
        }

        this.util.comandoInternal(this.frameMovimentacao);
    }//GEN-LAST:event_btnMovEstoqueActionPerformed

    private void menRelMovEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelMovEstActionPerformed
        // Gerando um relatório de clientes
        gerarRelatorio("Movimentacao");
    }//GEN-LAST:event_menRelMovEstActionPerformed

    private void menRelUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelUsuActionPerformed
        // Gerando um relatório de clientes
        gerarRelatorio("Usuario");
//        
//        relatorioUsuarioSetar();
//        gerarRelatorioUser();
    }//GEN-LAST:event_menRelUsuActionPerformed

    private void menRelRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelRecActionPerformed
        // Gerando um relatório de clientes
        gerarRelatorio("Receita");
    }//GEN-LAST:event_menRelRecActionPerformed

    private void menRelInsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelInsActionPerformed
        // Gerando um relatório de clientes
        gerarRelatorio("Insumo");
    }//GEN-LAST:event_menRelInsActionPerformed

    private void btnInsumoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInsumoMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel8, this.jSeparator1, this.btnInsumo);
    }//GEN-LAST:event_btnInsumoMouseEntered

    private void btnInsumoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInsumoMouseExited
        // quando o mouse sair de cima     
        retornaCor(this.jPanel8, this.jSeparator1, this.btnInsumo);
    }//GEN-LAST:event_btnInsumoMouseExited

    private void btnReceitaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReceitaMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel7, this.jSeparator3, this.btnReceita);
    }//GEN-LAST:event_btnReceitaMouseEntered

    private void btnReceitaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReceitaMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel7, this.jSeparator3, this.btnReceita);
    }//GEN-LAST:event_btnReceitaMouseExited

    private void btnUsuarioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsuarioMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel5, this.jSeparator4, this.btnUsuario);
    }//GEN-LAST:event_btnUsuarioMouseEntered

    private void btnUsuarioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsuarioMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel5, this.jSeparator4, this.btnUsuario);
    }//GEN-LAST:event_btnUsuarioMouseExited

    private void btnMovEstoqueMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMovEstoqueMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel6, this.jSeparator2, this.btnMovEstoque);
    }//GEN-LAST:event_btnMovEstoqueMouseEntered

    private void btnMovEstoqueMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMovEstoqueMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel6, this.jSeparator2, this.btnMovEstoque);
    }//GEN-LAST:event_btnMovEstoqueMouseExited

    private void tbnEstPastaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbnEstPastaMouseEntered
        // quando o mouse estiver em cima
        alteraCor(this.jPanel9, this.jSeparator6, this.tbnEstPasta);
    }//GEN-LAST:event_tbnEstPastaMouseEntered

    private void tbnEstPastaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbnEstPastaMouseExited
        // quando o mouse sair de cima
        retornaCor(this.jPanel9, this.jSeparator6, this.tbnEstPasta);
    }//GEN-LAST:event_tbnEstPastaMouseExited

    private void menuMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuMouseDragged
        // evento para mover o frame sem a barra superior
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();

        this.setLocation(x - this.xMouse, y - this.yMouse);
    }//GEN-LAST:event_menuMouseDragged

    private void menuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuMousePressed
        // evento para mover o frame sem a barra superior
        this.xMouse = evt.getX();
        this.yMouse = evt.getY();
    }//GEN-LAST:event_menuMousePressed

    private void btnInsumoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInsumoMouseClicked
        // quando pressionado altera a cor
        alteraCorPressionado(this.jPanel8, this.jSeparator1, this.btnInsumo);
    }//GEN-LAST:event_btnInsumoMouseClicked

    private void btnReceitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReceitaMouseClicked
        // quando pressionado altera a cor
        alteraCorPressionado(this.jPanel7, this.jSeparator3, this.btnReceita);
    }//GEN-LAST:event_btnReceitaMouseClicked

    private void btnUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsuarioMouseClicked
        // quando pressionado altera a cor
        alteraCorPressionado(this.jPanel5, this.jSeparator4, this.btnUsuario);
    }//GEN-LAST:event_btnUsuarioMouseClicked

    private void btnMovEstoqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMovEstoqueMouseClicked
        // quando pressionado altera a cor
        alteraCorPressionado(this.jPanel6, this.jSeparator2, this.btnMovEstoque);
    }//GEN-LAST:event_btnMovEstoqueMouseClicked

    private void tbnEstPastaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbnEstPastaMouseClicked
        // quando pressionado altera a cor
        alteraCorPressionado(this.jPanel9, this.jSeparator6, this.tbnEstPasta);
    }//GEN-LAST:event_tbnEstPastaMouseClicked

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDesktopPane Desktop;
    public static javax.swing.JButton btnInsumo;
    private javax.swing.JButton btnMovEstoque;
    private javax.swing.JButton btnReceita;
    public static javax.swing.JButton btnUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    public static javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    public static javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    public static javax.swing.JLabel lblUsuario;
    private javax.swing.JMenuItem menCadIns;
    private javax.swing.JMenuItem menCadRec;
    public static javax.swing.JMenuItem menCadUsu;
    private javax.swing.JMenu menCadastro;
    private javax.swing.JMenuItem menConEstPas;
    private javax.swing.JMenuItem menConMovEst;
    private javax.swing.JMenu menControle;
    private javax.swing.JMenu menOpc;
    private javax.swing.JMenuItem menOpcSair;
    private javax.swing.JMenuItem menRelIns;
    private javax.swing.JMenuItem menRelMovEst;
    private javax.swing.JMenuItem menRelRec;
    public static javax.swing.JMenuItem menRelUsu;
    private javax.swing.JMenu menRelatorio;
    private javax.swing.JMenuBar menu;
    private javax.swing.JButton tbnEstPasta;
    // End of variables declaration//GEN-END:variables
}
