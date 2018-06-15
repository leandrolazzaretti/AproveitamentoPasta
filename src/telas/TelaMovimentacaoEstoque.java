/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import dao.InsumoDao;
import dao.MovimentacaoDao;
import dao.MovimentacaoEstoqueDao;
import dto.MovimentacaoDto;
import dto.MovimentacaoEstoqueDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.Util;
import util.SoNumeros;

/**
 *
 * @author Leandro
 */
public class TelaMovimentacaoEstoque extends javax.swing.JInternalFrame {

    Connection conexao = null;
    private String cbPesquisar = "ep.ID";
    MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();
    InsumoDao insumoDao = new InsumoDao();
    Util frame = new Util();
    JInternalFrame framePesReceita;
    JInternalFrame framePesInsumo;
    TelaPesquisarReceita rec = new TelaPesquisarReceita();
    TelaPesquisarInsumos ins = new TelaPesquisarInsumos();
   
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
            movDto.setCodigo(codigo);
            movDto.setDescricao(this.txtDescricao.getText());
            movDto.setData(inverterData(this.txtEstData.getText()));
            movDto.setQuantidade(this.txtEstQuantidade.getText().replace(",","."));

        } else {
            movDto.setTipo(this.cbEstoque.getSelectedItem().toString());
            movDto.setCodigo(codigo);
            movDto.setDescricao(this.txtDescricao.getText());
            movDto.setData(inverterData(this.txtEstData.getText()));
            movDto.setQuantidade("-" + this.txtEstQuantidade.getText().replace(",","."));
        }
        movDao.movimentacao(movDto);
    }

    private void confirmaEstoquePasta(boolean confirma) {
        MovimentacaoEstoqueDto movEstDto = new MovimentacaoEstoqueDto();
        MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();

        movEstDto.setCodigoReceita(movEstDao.buscaCodigoReceita(this.txtDescricao.getText()));
        movEstDto.setUM(this.txtEstUM.getText());
        movEstDto.setQuantidade(Double.parseDouble(this.txtEstQuantidade.getText().replace(",",".")));
        movEstDto.setData(inverterData(this.txtEstData.getText()));

        if (confirma == true) {
            movEstDao.entradaPasta(movEstDto);
        } else {
            movEstDao.saidaPasta(movEstDto.getQuantidade(), movEstDto.getCodigoReceita());
        }

    }

    //Seta a tabela de estoque de pastas
    public void setarTabelaPasta() {
        String sql = "select ep.ID, r.descricao, ep.quantidade, r.datavencimento, ep.data from tbEstoquePasta as ep"
                + " inner join tbreceita as r on ep.codigoReceita = r.codigorec"
                + " where " + this.cbPesquisar + " like ?";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) this.tblEstPasta.getModel();
        modelo.setNumRows(0);
        Util buscarDes = new Util();

        this.tblEstPasta.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(1).setPreferredWidth(40);
        this.tblEstPasta.getColumnModel().getColumn(2).setPreferredWidth(40);
        this.tblEstPasta.getColumnModel().getColumn(3).setPreferredWidth(20);
        this.tblEstPasta.getColumnModel().getColumn(4).setPreferredWidth(20);

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtMovEst.getText() + "%");
            ResultSet rs = pst.executeQuery();
            //Preencher a tabela usando a bibliotéca rs2xml.jar
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3).replace(".", ","),
                    rs.getString(4),
                    inverterData(rs.getString(5))
                });
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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
        this.txtEstUM.setEnabled(false);
        this.txtEstUM.setText("kg");
        setarTabelaPasta();
        this.lblRecIns.setText("Receita:");
    }

    private void ativarInsumo() {
        this.btnMovEstPesquisar.setEnabled(true);
        this.txtDescricao.setText(null);
        this.txtEstUM.setText(null);
        this.txtEstQuantidade.setText(null);
        this.txtEstData.setText(null);
        this.txtEstUM.setEnabled(true);
        this.tblEstPasta.setVisible(false);
        this.cbPesMovEst.setEnabled(false);
        this.txtMovEst.setEnabled(false);
        this.lblRecIns.setText("Insumo:");
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

    private String inverterData(String data) {
        String[] dataInvertida = data.split("/");
        return dataInvertida[2] + "/" + dataInvertida[1] + "/" + dataInvertida[0];
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
        jLabel6 = new javax.swing.JLabel();
        cbPesMovEst = new javax.swing.JComboBox<>();
        txtMovEst = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEstPasta = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Movimentação Estoque");
        setToolTipText("");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 824, 210));

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setToolTipText("");

        jLabel6.setText("Pesquisar por:");

        cbPesMovEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Pasta", "Quantidade", "Validade", "Data" }));
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
                "ID", "Pasta", "Quantidade ", "Validade", "Data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
        jLabel8.setText("Tabela Estoque de Pastas");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
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
                .addContainerGap(579, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
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

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 223, 824, 300));

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

    private void cbEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstoqueActionPerformed
        // Altera o nome da lblRecIns conforme o item selecionado do combobox
        if (this.cbEstoque.getSelectedItem().equals("Insumo")) {
            ativarInsumo();
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
                if (this.cbTipo.getSelectedItem().equals("Entrada")) {
                    confirmaEstoquePasta(true);
                    movimentacao(true, "Pasta");
                    this.insumoDao.retirarInsumo(this.movEstDao.buscaCodigoReceita(this.txtDescricao.getText()));
                    setarTabelaPasta();
                } else {
                    double quantidade = Double.parseDouble(this.txtEstQuantidade.getText().replace(",","."));
                    int soma = this.movEstDao.somaPastas(this.txtDescricao.getText());
                    if (soma < quantidade) {
                        JOptionPane.showMessageDialog(null, "Quantidade em estoque " + soma + "kg\nNão atende a sua necessidade.");

                    } else {
                        confirmaEstoquePasta(false);
                        movimentacao(false, "Pasta");
                        setarTabelaPasta();
                    }
                }
            } else {
                // confirma se é entrada ou saida
                if (this.cbTipo.getSelectedItem().equals("Entrada")) {
                    this.insumoDao.entradaInsumo(Double.parseDouble(this.txtEstQuantidade.getText().replace(",",".")), this.insumoDao.buscaCodigoInsumo(this.txtDescricao.getText()));
                    movimentacao(true, "Insumo");
                } else {
                    this.insumoDao.saidaInsumo(Double.parseDouble(this.txtEstQuantidade.getText().replace(",",".")), this.insumoDao.buscaCodigoInsumo(this.txtDescricao.getText()));
                    movimentacao(false, "Insumo");
                }
            }
            limparCampos();
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        // Altera entre saida ou entrada
        if (this.cbEstoque.getSelectedItem().equals("Pasta")) {
            if (this.cbTipo.getSelectedItem().equals("Saída")) {
                this.btnMovEstPesquisar.setEnabled(false);
            } else {
                this.btnMovEstPesquisar.setEnabled(true);
            }
        } else {
            this.btnMovEstPesquisar.setEnabled(true);
        }
    }//GEN-LAST:event_cbTipoActionPerformed

    private void btnMovEstPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovEstPesquisarActionPerformed
        // chama a tela de pesquisa receita ou insumo
        if (this.cbEstoque.getSelectedItem().equals("Pasta")) {

            if (this.framePesReceita == null) {
                this.framePesReceita = new TelaPesquisarReceita();
            }
            this.rec.pesquisarReceita();
            this.frame.comandoInternal(this.framePesReceita);
            TelaPesquisarReceita.confirmarEscolha = false;

        } else {
            if (this.framePesInsumo == null) {
                this.framePesInsumo = new TelaPesquisarInsumos();
            }
            this.ins.pesquisarInsumos();
            this.frame.comandoInternal(this.framePesInsumo);
            TelaPesquisarInsumos.confirmarEscolha = false;
        }
    }//GEN-LAST:event_btnMovEstPesquisarActionPerformed

    private void cbPesMovEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPesMovEstActionPerformed
        //  // adiciona um valor a variável cbPesquizar
        if (this.cbPesMovEst.getSelectedItem().equals("ID")) {
            this.cbPesquisar = "ep.ID";
        } else {
            if (this.cbPesMovEst.getSelectedItem().equals("Pasta")) {
                this.cbPesquisar = "r.descricao";
            } else {
                if (this.cbPesMovEst.getSelectedItem().equals("Quantidade")) {
                    this.cbPesquisar = "ep.quantidade";
                } else {
                    if (this.cbPesMovEst.getSelectedItem().equals("Validade")) {
                        this.cbPesquisar = "r.datavencimento";
                    } else {
                        this.cbPesquisar = "ep.data";
                    }
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
            int setar = this.tblEstPasta.getSelectedRow();
            this.txtDescricao.setText((String) this.tblEstPasta.getModel().getValueAt(setar, 1));
        }
    }//GEN-LAST:event_tblEstPastaMouseClicked

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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblRecIns;
    private javax.swing.JTable tblEstPasta;
    public static javax.swing.JTextField txtDescricao;
    public static javax.swing.JFormattedTextField txtEstData;
    public static javax.swing.JFormattedTextField txtEstQuantidade;
    public static javax.swing.JTextField txtEstUM;
    private javax.swing.JTextField txtMovEst;
    // End of variables declaration//GEN-END:variables
}
