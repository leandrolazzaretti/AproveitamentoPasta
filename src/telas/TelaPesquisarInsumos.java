/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import dao.MovimentacaoEstoqueDao;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import util.CoresAlternadasTabela;
import util.SoNumeros;
import util.UpperCaseDocument;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaPesquisarInsumos extends javax.swing.JInternalFrame {

    private Connection conexao = null;

    private String cbPesquisar = "descricao";
    public static boolean confimaTela;
    public static boolean confirmarEscolha;
    private final Util util = new Util();
    private final CoresAlternadasTabela mudarCorLinha = new CoresAlternadasTabela();

    /**
     * Creates new form TelaPesquisarInsumos
     */
    public TelaPesquisarInsumos() {
        initComponents();

        this.conexao = ModuloConexao.conector();
        pesquisarInsumos();

        retirarBordas();
        this.tblCadInsumos.getTableHeader().setReorderingAllowed(false);
        upperCase();
    }

    // metodo para retirar as bordas do JinternalFrame
    private void retirarBordas() {
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null); //retirar o painel superior
        this.setBorder(null);//retirar bordas
    }

    public void pesquisarInsumos() {
        String sql = "select * from tbinsumos where " + this.cbPesquisar + " like ?";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) this.tblCadInsumos.getModel();
        modelo.setNumRows(0);

        this.tblCadInsumos.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblCadInsumos.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblCadInsumos.getColumnModel().getColumn(2).setPreferredWidth(40);
        this.tblCadInsumos.getColumnModel().getColumn(3).setPreferredWidth(40);
        this.tblCadInsumos.getColumnModel().getColumn(4).setPreferredWidth(40);

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1,"%"+ this.txtPesquisarInsumos.getText() + "%");
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    this.util.formatadorQuant(rs.getDouble(4)),
                    "R$ " + this.util.formatadorQuant(rs.getDouble(5))});
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        this.mudarCorLinha.CorNaLinhaQuantidade(tblCadInsumos);
    }

    //seta os campos do formulário com o coteúdo da tabela
    private void setarCampos() {
        int setar = this.tblCadInsumos.getSelectedRow();
        TelaCadInsumo.txtCadInsCodigo.setEnabled(false);
        TelaCadInsumo.txtCadInsCodigo.setText(this.tblCadInsumos.getModel().getValueAt(setar, 0).toString());
        TelaCadInsumo.txtCadInsDes.setText(this.tblCadInsumos.getModel().getValueAt(setar, 1).toString());
        TelaCadInsumo.cbCadInsUm.setSelectedItem(this.tblCadInsumos.getModel().getValueAt(setar, 2).toString());
        TelaCadInsumo.txtCadInsQuant.setText(this.tblCadInsumos.getModel().getValueAt(setar, 3).toString());
        TelaCadInsumo.txtCadInsPreco.setDocument(new SoNumeros());
        TelaCadInsumo.txtCadInsPreco.setText(this.tblCadInsumos.getModel().getValueAt(setar, 4).toString().replace("R$ ", ""));
    }

    //seta o campo da descrição na tela de movimentação de estoque
    private void setarCampos2() {
        int setar = this.tblCadInsumos.getSelectedRow();
        TelaMovimentacaoEstoque.txtCodigo.setText(this.tblCadInsumos.getModel().getValueAt(setar, 0).toString());
        TelaMovimentacaoEstoque.txtDescricao.setText(this.tblCadInsumos.getModel().getValueAt(setar, 1).toString());
        TelaMovimentacaoEstoque.txtEstUM.setText(this.tblCadInsumos.getModel().getValueAt(setar, 2).toString());
        TelaMovimentacaoEstoque.txtCodigo.setEnabled(false);
        TelaMovimentacaoEstoque.txtEstQuantidade.setEnabled(true);
        TelaMovimentacaoEstoque.txtEstData.setEnabled(true);
        if (TelaMovimentacaoEstoque.cbTipo.getSelectedItem().equals("Saída")) {
            MovimentacaoEstoqueDao movDao = new MovimentacaoEstoqueDao();
            TelaMovimentacaoEstoque.txtEstData.setText(movDao.inverterData(movDao.dataAtual()).replace("-", "/"));
        }
    }

    //seta os campos do formulário com o coteúdo da tabela
    private void setarCamposTbReceita() {
        int setar = this.tblCadInsumos.getSelectedRow();
        TelaCadReceita.txtCadRecComponentesCodigo.setText(this.tblCadInsumos.getModel().getValueAt(setar, 0).toString());
        TelaCadReceita.txtCadRecComponentesDesc.setText(this.tblCadInsumos.getModel().getValueAt(setar, 1).toString());
        TelaCadReceita.txtCadRecConsumo.requestFocus();
        
    }

     private void upperCase(){
        this.txtPesquisarInsumos.setDocument(new UpperCaseDocument());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        cbPesquisarIns = new javax.swing.JComboBox<>();
        txtPesquisarInsumos = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCadInsumos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(143, 165, 110));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(122, 122, 122)));
        setClosable(true);
        setIconifiable(true);
        setTitle("Pesquisar Insumos");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(858, 560));
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
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setForeground(new java.awt.Color(79, 79, 79));
        jLabel6.setText("Pesquisar por:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        cbPesquisarIns.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Descrição", "Código", "UM", "Quantidade", "Preço" }));
        cbPesquisarIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPesquisarInsActionPerformed(evt);
            }
        });
        getContentPane().add(cbPesquisarIns, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 153, -1));

        txtPesquisarInsumos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarInsumosKeyReleased(evt);
            }
        });
        getContentPane().add(txtPesquisarInsumos, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 216, -1));

        tblCadInsumos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblCadInsumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descrição", "UM", "Quantidade", "Preço"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCadInsumos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadInsumosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCadInsumos);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 840, 190));

        jPanel1.setBackground(new java.awt.Color(143, 165, 110));
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

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(816, 2, 40, 20));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Tabela de Insumos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 140, -1));

        setBounds(0, 0, 858, 312);
    }// </editor-fold>//GEN-END:initComponents

    private void tblCadInsumosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadInsumosMouseClicked
        // seta os campos do formulário através da tabela
        //TelaCadUsuario.
        if (evt.getClickCount() > 1) {
            if (this.confirmarEscolha == true) {

                if (this.confimaTela == true) {
                    setarCampos();
                    this.dispose();
                } else {
                    setarCamposTbReceita();
                    this.dispose();
                }
            } else {
                setarCampos2();
                this.dispose();
            }
        }

    }//GEN-LAST:event_tblCadInsumosMouseClicked

    private void txtPesquisarInsumosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarInsumosKeyReleased
        // chama o metodo pesquisar
        pesquisarInsumos();
    }//GEN-LAST:event_txtPesquisarInsumosKeyReleased

    private void cbPesquisarInsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPesquisarInsActionPerformed
        // adiciona um valor a variável cbPesquizar
        if (this.cbPesquisarIns.getSelectedItem().equals("Código")) {
            this.cbPesquisar = "codigo";
        } else {
            if (this.cbPesquisarIns.getSelectedItem().equals("Descrição")) {
                this.cbPesquisar = "descricao";
            } else {
                if (this.cbPesquisarIns.getSelectedItem().equals("UM")) {
                    this.cbPesquisar = "UM";
                } else {
                    if (this.cbPesquisarIns.getSelectedItem().equals("Quantidade")) {
                        this.cbPesquisar = "quantidade";
                    } else {
                        this.cbPesquisar = "preco";
                    }
                }
            }
        }
    }//GEN-LAST:event_cbPesquisarInsActionPerformed

    private void btnFecharMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseEntered
        // quando o mouse está em cima
        this.jPanel1.setBackground(new Color(211, 57, 33));
        this.btnFechar.setForeground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnFecharMouseEntered

    private void btnFecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseExited
        // quando o mouse sair de cima
        this.jPanel1.setBackground(new Color(143, 165, 110));
        this.btnFechar.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnFecharMouseExited

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // chama o metodo sair
        this.dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // passa o foco para o campo de texto 
        this.txtPesquisarInsumos.requestFocus();
    }//GEN-LAST:event_formInternalFrameActivated


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JComboBox<String> cbPesquisarIns;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable tblCadInsumos;
    private javax.swing.JTextField txtPesquisarInsumos;
    // End of variables declaration//GEN-END:variables
}
