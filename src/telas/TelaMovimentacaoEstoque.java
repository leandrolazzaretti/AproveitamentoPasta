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
import javax.swing.table.DefaultTableModel;
import util.Buscar;
import util.SoNumeros;

/**
 *
 * @author Leandro
 */
public class TelaMovimentacaoEstoque extends javax.swing.JInternalFrame {

    Connection conexao = null;
    private String[] dataSeparada;
    private String cbPesquisar = "ID";

    /**
     * Creates new form TelaEstoque
     */
    //Construtor da classe
    public TelaMovimentacaoEstoque() {
        initComponents();
        this.conexao = ModuloConexao.conector();
        this.txtEstData.setDocument(new SoNumeros());
        this.txtEstQuantidade.setDocument(new SoNumeros());
        ativarTblPasta();     
    }

    //da entrada no estoque de pasta
    private void entradaPasta() {
        String sql = "insert into tbEstoquePasta(codigoReceita,UM,quantidade,data)"
                + " values(?,?,?,?)";
        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, buscaCodigoReceita());
            pst.setString(2, this.txtEstUM.getText());
            pst.setString(3, this.txtEstQuantidade.getText());
            pst.setString(4, this.dataSeparada[2] + "/" + this.dataSeparada[1] + "/" + this.dataSeparada[0]);
            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Entrada de pasta efetuada com sucesso.");
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    // da saída na pasta desejada 
    private void saidaPasta() {
        String sql = "update tbEstoquePasta set quantidade = quantidade - ? where ID =? and codigoReceita =? and data =?";
        PreparedStatement pst;
        Buscar buscar = new Buscar();
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtEstQuantidade.getText());
            pst.setString(2, buscar.buscarID(buscaCodigoReceita()));
            pst.setInt(3, buscaCodigoReceita());
            pst.setString(4, buscar.buscarDataMenor(buscaCodigoReceita()));
            
            int confirmar = pst.executeUpdate();
            if (confirmar > 0) {
                JOptionPane.showMessageDialog(null, "Saída de pasta realizada com sucesso.");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //faz um update na tabela de insumos aumentando a quantidade
    private void entradaInsumo() {
        String sql = "update tbinsumos set quantidade = quantidade + ? where codigo = ?";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtEstQuantidade.getText());
            pst.setInt(2, buscaCodigoInsumo());
            int confirmar = pst.executeUpdate();

            if (confirmar > 0) {
                JOptionPane.showMessageDialog(null, "Entrada de insumo realizada com sucesso.");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //faz um update na tabela de insumos diminuindo a quantidade
    private void saidaInsumo() {
        String sql = "update tbinsumos set quantidade = quantidade - ? where codigo = ?";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtEstQuantidade.getText());
            pst.setInt(2, buscaCodigoInsumo());
            int confirmar = pst.executeUpdate();

            if (confirmar > 0) {
                JOptionPane.showMessageDialog(null, "Saida de insumo realizada com sucesso.");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Seta a tabela de estoque de pastas
    public void setarTabelaPasta() {
        String sql = "select * from tbEstoquePasta where " + this.cbPesquisar + " like ?";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) this.tblEstPasta.getModel();
        modelo.setNumRows(0);

        this.tblEstPasta.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(1).setPreferredWidth(40);
        this.tblEstPasta.getColumnModel().getColumn(2).setPreferredWidth(40);
        this.tblEstPasta.getColumnModel().getColumn(3).setPreferredWidth(40);

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtMovEst.getText() + "%");
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(4),
                    rs.getString(5)});
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    //Da entrada na tabela de movimentação 
    private void movimentacao(int codigoID, String quantidade) {
        String sql = "insert into tbMovimentacao(tipo,codigoID,data,quantidade) values(?,?,?,?)";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.cbEstoque.getSelectedItem().toString());
            pst.setInt(2, codigoID);
            pst.setString(3, this.dataSeparada[2] + "/" + this.dataSeparada[1] + "/" + this.dataSeparada[0]);
            pst.setString(4, quantidade);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // busca o codigo da tabela de receita através da descrição
    private int buscaCodigoReceita() {
        String sql = "select codigorec from tbreceita where descricao =?";
        int codigo = 0;
        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtDescricao.getText());
            ResultSet rs = pst.executeQuery();

            codigo = Integer.parseInt(rs.getString(1));
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return codigo;
    }

    // busca o codigo da tabela de insumos através da descrição
    private int buscaCodigoInsumo() {
        String sql = "select codigo from tbinsumos where descricao =?";
        int codigo = 0;
        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtDescricao.getText());
            ResultSet rs = pst.executeQuery();

            codigo = Integer.parseInt(rs.getString(1));
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return codigo;
    }

    //ativa a tabela de insumos
    private void ativarTblInsumo() {
        this.lblRecIns.setText("Insumo:");
        this.paiGuiEstoque.setEnabledAt(0, false);
        this.paiGuiEstoque.setEnabledAt(1, true);
        this.paiGuiEstoque.setSelectedIndex(1);
        limparCampos();
        this.txtDescricao.setText(null);
        this.txtEstUM.setEnabled(true);
        this.txtEstUM.setText(null);
    }

    //ativa a tabela de pastas
    private void ativarTblPasta() {
        this.lblRecIns.setText("Receita:");
        this.paiGuiEstoque.setEnabledAt(0, true);
        this.paiGuiEstoque.setEnabledAt(1, false);
        this.paiGuiEstoque.setSelectedIndex(0);
        limparCampos();
        this.txtDescricao.setText(null);
        this.txtEstUM.setEnabled(false);
        this.txtEstUM.setText("kg");
        setarTabelaPasta();
    }

    // limpa os campos após entrada ou saida
    private void limparCampos() {
        this.txtEstQuantidade.setText(null);
        this.txtEstData.setText(null);
    }

    // verifica se todos os campos estão setados
    private boolean verificaCampos() {
        int confirma = 0;
        if ((this.txtDescricao.getText().isEmpty()) || (this.txtEstQuantidade.getText().isEmpty()) || (this.txtEstUM.getText().isEmpty()) || (this.txtEstData.getText().isEmpty())) {
            confirma = 1;
        }
        return confirma > 0;
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbTipo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtEstQuantidade = new javax.swing.JFormattedTextField();
        txtDescricao = new javax.swing.JTextField();
        lblRecIns = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbEstoque = new javax.swing.JComboBox<>();
        btnConfirmar = new javax.swing.JButton();
        btnMovEstPesquisar = new javax.swing.JButton();
        txtEstData = new javax.swing.JFormattedTextField();
        txtEstUM = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        paiGuiEstoque = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEstPasta = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEstInsumo = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        cbPesMovEst = new javax.swing.JComboBox<>();
        txtMovEst = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Movimentação Estoque");
        setToolTipText("");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setToolTipText("");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Tipo:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel3.setText("Unidade de Medida:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, -1, -1));

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entrada", "Saída" }));
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
            }
        });
        jPanel1.add(cbTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 100, -1));

        jLabel7.setText("Quantidade:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, -1, -1));
        jPanel1.add(txtEstQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 170, -1));
        jPanel1.add(txtDescricao, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 240, -1));

        lblRecIns.setText("Receita:");
        jPanel1.add(lblRecIns, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel5.setText("Data:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 100, -1, -1));

        jLabel1.setText("Estoque");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, -1));

        cbEstoque.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pasta", "Insumo" }));
        cbEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstoqueActionPerformed(evt);
            }
        });
        jPanel1.add(cbEstoque, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 84, -1));

        btnConfirmar.setText("Confirmar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });
        jPanel1.add(btnConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 100, -1));

        btnMovEstPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnMovEstPesquisar.setToolTipText("Pesquisar");
        btnMovEstPesquisar.setContentAreaFilled(false);
        btnMovEstPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMovEstPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovEstPesquisarActionPerformed(evt);
            }
        });
        jPanel1.add(btnMovEstPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, 30, 30));

        try {
            txtEstData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtEstData.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtEstData, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, 130, -1));
        jPanel1.add(txtEstUM, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, 130, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Movimentação de Estoque");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setToolTipText("");

        paiGuiEstoque.setToolTipText("");

        tblEstPasta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Pasta", "Quantidade ", "Data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblEstPasta);

        paiGuiEstoque.addTab("Pasta", jScrollPane2);

        tblEstInsumo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Insumo", "Insumo", "UM", "Quantidade", "Data"
            }
        ));
        jScrollPane1.setViewportView(tblEstInsumo);

        paiGuiEstoque.addTab("Insumo", jScrollPane1);

        jLabel6.setText("Pesquisar por:");

        cbPesMovEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Pasta", "Quantidade", "Data" }));
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paiGuiEstoque, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(cbPesMovEst, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMovEst, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbPesMovEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(txtMovEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(paiGuiEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

    private void cbEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstoqueActionPerformed
        // Altera o nome da lblRecIns conforme o item selecionado do combobox
        if (this.cbEstoque.getSelectedItem().equals("Insumo")) {
            ativarTblInsumo();
        } else {
            ativarTblPasta();
        }
    }//GEN-LAST:event_cbEstoqueActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        // confirma a ação de entrada ou saida    
        //Verifica se existe algum campo vazio 
        
        
        if (verificaCampos() == true) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {
            //confirma se é pasta ou insumo
            if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
                //confirma se é entrada ou saida
                int receita = buscaCodigoReceita();
                if (this.cbTipo.getSelectedItem().equals("Entrada")) {
                    this.dataSeparada = this.txtEstData.getText().split("/");
                    entradaPasta();
                    movimentacao(receita, this.txtEstQuantidade.getText());
                } else {
                    this.dataSeparada = this.txtEstData.getText().split("/");
                    saidaPasta();
                    movimentacao(receita, "-" + this.txtEstQuantidade.getText());
                }
            } else {
                int insumo = buscaCodigoInsumo();
                if (this.cbTipo.getSelectedItem().equals("Entrada")) {
                    this.dataSeparada = this.txtEstData.getText().split("/");
                    entradaInsumo();
                    movimentacao(insumo, this.txtEstQuantidade.getText());
                } else {
                    this.dataSeparada = this.txtEstData.getText().split("/");
                    saidaInsumo();
                    movimentacao(insumo, "-" + this.txtEstQuantidade.getText());
                }
            }
            limparCampos();
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        // Altera entre saida ou entrada
    }//GEN-LAST:event_cbTipoActionPerformed

    private void btnMovEstPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovEstPesquisarActionPerformed
        // chama a tela de pesquisa receita ou insumo
        if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
            TelaPesquisarReceita receita = new TelaPesquisarReceita();
            TelaPrincipal.Desktop.add(receita);
            receita.setVisible(true);
            receita.confirmarEscolha = false;
        } else {
            TelaPesquisarInsumos insumo = new TelaPesquisarInsumos();
            TelaPrincipal.Desktop.add(insumo);
            insumo.setVisible(true);
            insumo.confirmarEscolha = false;
        }
    }//GEN-LAST:event_btnMovEstPesquisarActionPerformed

    private void cbPesMovEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPesMovEstActionPerformed
        //  // adiciona um valor a variável cbPesquizar
        if (this.cbPesMovEst.getSelectedItem().equals("ID")) {
            this.cbPesquisar = "ID";
        } else {
            if (this.cbPesMovEst.getSelectedItem().equals("Pasta")) {
                this.cbPesquisar = "codigoReceita";
            } else {
                if (this.cbPesMovEst.getSelectedItem().equals("Quantidade")) {
                    this.cbPesquisar = "quantidade";
                } else {                 
                        this.cbPesquisar = "data";      
                }
            }
        }
    }//GEN-LAST:event_cbPesMovEstActionPerformed

    private void txtMovEstKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMovEstKeyReleased
        // chama metodo pesquisar enquanto digita
        setarTabelaPasta();
    }//GEN-LAST:event_txtMovEstKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnMovEstPesquisar;
    private javax.swing.JComboBox<String> cbEstoque;
    private javax.swing.JComboBox<String> cbPesMovEst;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblRecIns;
    private javax.swing.JTabbedPane paiGuiEstoque;
    private javax.swing.JTable tblEstInsumo;
    private javax.swing.JTable tblEstPasta;
    public static javax.swing.JTextField txtDescricao;
    public static javax.swing.JFormattedTextField txtEstData;
    private javax.swing.JFormattedTextField txtEstQuantidade;
    public static javax.swing.JTextField txtEstUM;
    private javax.swing.JTextField txtMovEst;
    // End of variables declaration//GEN-END:variables
}
