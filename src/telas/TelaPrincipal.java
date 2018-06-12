/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import java.sql.Connection;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaPrincipal extends javax.swing.JFrame {

    JInternalFrame frameUsuario;
    JInternalFrame frameInsumo;
    JInternalFrame frameReceita;
    JInternalFrame frameMovimentacao;
    JInternalFrame frameEstoquePasta;
    Util frame = new Util();

    Connection conexao = null;

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();
        this.conexao = ModuloConexao.conector();
    }

//    public void comandoInternal(JInternalFrame frame) {
//
//        for (JInternalFrame internal : this.Desktop.getAllFrames()) {
//            if (internal.getClass().toString().equalsIgnoreCase(frame.getClass().toString())) {
//                frame.toFront();
//                return;
//            }
//        }
//        if (!frame.isVisible()) {
//            this.Desktop.add(frame);
//            frame.setVisible(true);
//        }
//        frame.toFront();
//    }
    private void gerarRelatorio(String relatorio) {
        // Gerando um relatório de clientes
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão deste relatório?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            //imprimindo relatório
            try {
                //Usando a classe JasperPrint para preparar a impressão de um relatório
                JasperPrint print = JasperFillManager.fillReport("C:\\Users\\Leandro\\Documents\\NetBeansProjects\\prjAproveitamentoPastas\\src\\Report\\"+relatorio+".jasper", null, conexao);
                // a linha abaix exibe o relatório através da classe JasperViewer
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
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

        Desktop = new javax.swing.JDesktopPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnUsuario = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnMovEstoque = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnReceita = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnInsumo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();
        tbnEstPasta = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
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
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Desktop.setBackground(new java.awt.Color(0, 153, 153));

        javax.swing.GroupLayout DesktopLayout = new javax.swing.GroupLayout(Desktop);
        Desktop.setLayout(DesktopLayout);
        DesktopLayout.setHorizontalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        DesktopLayout.setVerticalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        getContentPane().add(Desktop, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 860, 560));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(102, 102, 255));

        btnUsuario.setBackground(new java.awt.Color(153, 153, 255));
        btnUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnUsuario.setText("Usuário");
        btnUsuario.setBorder(null);
        btnUsuario.setContentAreaFilled(false);
        btnUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 160, 30));

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));

        btnMovEstoque.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnMovEstoque.setForeground(new java.awt.Color(255, 255, 255));
        btnMovEstoque.setText("Movimentação Estoque");
        btnMovEstoque.setContentAreaFilled(false);
        btnMovEstoque.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMovEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovEstoqueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(btnMovEstoque)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMovEstoque, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 160, 30));

        jPanel7.setBackground(new java.awt.Color(102, 102, 255));

        btnReceita.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReceita.setForeground(new java.awt.Color(255, 255, 255));
        btnReceita.setText("Receita");
        btnReceita.setContentAreaFilled(false);
        btnReceita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceitaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnReceita, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnReceita, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 160, 30));

        jPanel8.setBackground(new java.awt.Color(102, 102, 255));

        btnInsumo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnInsumo.setForeground(new java.awt.Color(255, 255, 255));
        btnInsumo.setText("Insumo");
        btnInsumo.setContentAreaFilled(false);
        btnInsumo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsumoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnInsumo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnInsumo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 160, 30));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Controle");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 60, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Cadastro");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));
        jPanel3.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 160, -1));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 160, 10));

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));

        tbnEstPasta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbnEstPasta.setForeground(new java.awt.Color(255, 255, 255));
        tbnEstPasta.setText("Estoque Pasta");
        tbnEstPasta.setContentAreaFilled(false);
        tbnEstPasta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbnEstPasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnEstPastaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbnEstPasta, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tbnEstPasta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, 160, 30));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 220, 620));

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, -10, 860, 30));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Logado como: ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        lblUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(153, 153, 255));
        lblUsuario.setText("Usuário");
        jPanel1.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 580, 860, 40));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 20, 620));

        menu.setBackground(new java.awt.Color(255, 255, 255));

        menCadastro.setText("Cadastro");
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

        menControle.setText("Controle");

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

        menRelatorio.setText("Relatório");

        menRelIns.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.SHIFT_MASK));
        menRelIns.setText("Insumo");
        menRelIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelInsActionPerformed(evt);
            }
        });
        menRelatorio.add(menRelIns);

        menRelRec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK));
        menRelRec.setText("Receita");
        menRelRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelRecActionPerformed(evt);
            }
        });
        menRelatorio.add(menRelRec);

        menRelUsu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK));
        menRelUsu.setText("Usuário");
        menRelUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelUsuActionPerformed(evt);
            }
        });
        menRelatorio.add(menRelUsu);

        menRelMovEst.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK));
        menRelMovEst.setText("Movimentação Estoque");
        menRelMovEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelMovEstActionPerformed(evt);
            }
        });
        menRelatorio.add(menRelMovEst);

        menu.add(menRelatorio);

        menOpc.setText("Opções");

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

        setSize(new java.awt.Dimension(1106, 671));
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
        //comandoInternal(new TelaCadUsuario());
        if (this.frameUsuario == null) {
            this.frameUsuario = new TelaCadUsuario();
        }
        this.frame.comandoInternal(this.frameUsuario);
    }//GEN-LAST:event_btnUsuarioActionPerformed

    private void menCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadastroActionPerformed

    }//GEN-LAST:event_menCadastroActionPerformed

    private void menCadUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadUsuActionPerformed
        // cahama a TelaCadUsu     
        if (this.frameUsuario == null) {
            this.frameUsuario = new TelaCadUsuario();
        }
        this.frame.comandoInternal(this.frameUsuario);
    }//GEN-LAST:event_menCadUsuActionPerformed

    private void btnInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsumoActionPerformed
        // chama a TelaCadInsumos
        if (this.frameInsumo == null) {
            this.frameInsumo = new TelaCadInsumo();
        }
        this.frame.comandoInternal(this.frameInsumo);

    }//GEN-LAST:event_btnInsumoActionPerformed

    private void menCadInsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadInsActionPerformed
        // chama a TelaCadInsumos       
        if (this.frameInsumo == null) {
            this.frameInsumo = new TelaCadInsumo();
        }
        this.frame.comandoInternal(this.frameInsumo);
    }//GEN-LAST:event_menCadInsActionPerformed

    private void btnReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceitaActionPerformed
        // chama a TelaCadReceita
        if (this.frameReceita == null) {
            this.frameReceita = new TelaCadReceita();
        }
        this.frame.comandoInternal(this.frameReceita);
    }//GEN-LAST:event_btnReceitaActionPerformed

    private void menCadRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadRecActionPerformed
        // chama a TelaCadReceita
        if (this.frameReceita == null) {
            this.frameReceita = new TelaCadReceita();
        }
        this.frame.comandoInternal(this.frameReceita);
    }//GEN-LAST:event_menCadRecActionPerformed

    private void menConMovEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menConMovEstActionPerformed
        // chama TelaMovimentacaoEstoque
        if (this.frameMovimentacao == null) {
            this.frameMovimentacao = new TelaMovimentacaoEstoque();
        }
        this.frame.comandoInternal(this.frameMovimentacao);
    }//GEN-LAST:event_menConMovEstActionPerformed

    private void tbnEstPastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnEstPastaActionPerformed
        // chama TelaEstoquePasta
        if (this.frameEstoquePasta == null) {
            this.frameEstoquePasta = new TelaEstoquePasta();
        }
        this.frame.comandoInternal(this.frameEstoquePasta);
    }//GEN-LAST:event_tbnEstPastaActionPerformed

    private void menConEstPasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menConEstPasActionPerformed
        // chama TelaEstoquePasta
        if (this.frameEstoquePasta == null) {
            this.frameEstoquePasta = new TelaEstoquePasta();
        }
        this.frame.comandoInternal(this.frameEstoquePasta);
    }//GEN-LAST:event_menConEstPasActionPerformed

    private void btnMovEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovEstoqueActionPerformed
        // chama TelaMovimentação Estoque
        if (this.frameMovimentacao == null) {
            this.frameMovimentacao = new TelaMovimentacaoEstoque();
        }
        this.frame.comandoInternal(this.frameMovimentacao);
    }//GEN-LAST:event_btnMovEstoqueActionPerformed

    private void menRelMovEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelMovEstActionPerformed
        // Gerando um relatório de clientes
        gerarRelatorio("Movimentacao");       
    }//GEN-LAST:event_menRelMovEstActionPerformed

    private void menRelUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelUsuActionPerformed
        // Gerando um relatório de clientes
        gerarRelatorio("Usuario");
    }//GEN-LAST:event_menRelUsuActionPerformed

    private void menRelRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelRecActionPerformed
        // Gerando um relatório de clientes
        gerarRelatorio("Receita");
    }//GEN-LAST:event_menRelRecActionPerformed

    private void menRelInsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelInsActionPerformed
        // Gerando um relatório de clientes
        gerarRelatorio("Insumo");
    }//GEN-LAST:event_menRelInsActionPerformed

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
    private javax.swing.JButton btnInsumo;
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
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
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
    private javax.swing.JMenuItem menRelUsu;
    private javax.swing.JMenu menRelatorio;
    private javax.swing.JMenuBar menu;
    private javax.swing.JButton tbnEstPasta;
    // End of variables declaration//GEN-END:variables
}
