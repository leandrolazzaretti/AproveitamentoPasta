/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import conexao.ModuloConexao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import util.SoNumeros;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Leandro
 */
public class TelaCadReceita extends javax.swing.JInternalFrame {

    Connection conexao = null;
    public int codigoTipo;
    private String descIns;

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
        setarComboBox();
        AutoCompleteDecorator.decorate(cbCadReceitaTipo);
        this.cbCadReceitaTipo.setSelectedItem(null);
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

    //busca o código do tipo de pasta atraves do da descrição
    private int buscaCodTipoPasta() {
        int codigo = 0;
        String sql = "select codigo from tbTipoPasta where descricao=?";
        
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.cbCadReceitaTipo.getSelectedItem().toString());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                codigo = rs.getInt(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            //pst.close();
        }
        return codigo;
    }

    public void adicionarReceita() {
        String sql = "insert into tbreceita(codigorec,descricao,pantone,codigoTipoPasta,datavencimento) values(?,?,?,?,?)";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtCadRecCodigo.getText());
            pst.setString(2, this.txtCadRecDes.getText());
            pst.setString(3, this.txtCadRecPan.getText());
            pst.setInt(4, this.codigoTipo);
            pst.setString(5, this.txtCadRecVal.getText());
            //Atualiza a tabela receita
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Receita cadastrada com sucesso!");
                adicionarComponentes();
                limparCampos();
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    //Adiciona na tabela de relacionamento tbReceitaInsumo os itens adicionados na tabela de componentes
    public void adicionarComponentes() {
        String sql = "insert into tbReceitaInsumo(codigoReceita,codigoInsumo,consumo)values(?,?,?)";
        for (int x = 0; x < this.tblCadRecComponentes.getRowCount(); x++) {
            
            PreparedStatement pst;
            try {

                pst = this.conexao.prepareStatement(sql);

                descIns = (String) this.tblCadRecComponentes.getModel().getValueAt(x, 0);
                String cons = (String) this.tblCadRecComponentes.getModel().getValueAt(x, 1);
                int ins = codIns();
                String rec = this.txtCadRecCodigo.getText();
//                System.out.println(this.descIns);
//                System.out.println(ins);
//                System.out.println(rec);
//                System.out.println(cons);
                pst.setString(1, rec);
                pst.setInt(2, ins);
                pst.setString(3, cons);
//                pst.setInt(1, rec);
//                pst.setInt(2, ins);
//                pst.setDouble(3, cons);
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    System.out.println("Deu boa.");
                }
                pst.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                System.out.println(e);
            }
        }
    }

    private int codIns() {
        String sql = "select i.codigo from tbinsumos as i where descricao ='" + this.descIns + "'";
        int retorno = 0;
        
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            
            ResultSet rs =  pst.executeQuery();
            if (rs.next()) {
                retorno = rs.getInt(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return retorno;
    }

    public void atualizarReceita() {
        String sql = "update tbreceita set descricao=?, pantone=?, codigoTipoPasta=?, datavencimento=? where codigorec=?";

        PreparedStatement pst ;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.txtCadRecDes.getText());
            pst.setString(2, this.txtCadRecPan.getText());
            pst.setInt(3, this.codigoTipo);
            pst.setString(4, this.txtCadRecVal.getText());
            pst.setString(5, this.txtCadRecCodigo.getText());
            //Atualiza a tabela Receita
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Receita Atualizado com sucesso!");
                limparCampos();
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void deletarReceita() {
        if (this.txtCadRecCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione uma receita válida.");
        } else {
            int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar esta receita?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {

                String sql = "delete from tbreceita where codigorec=?";
                
                PreparedStatement pst = null;
                try {
                    pst = this.conexao.prepareStatement(sql);
                    pst.setString(1, this.txtCadRecCodigo.getText());
                    int deletado = pst.executeUpdate();

                    if (deletado > 0) {
                        JOptionPane.showMessageDialog(null, "Receita deletada com sucesso!");
                        limparCampos();
                    }
                    pst.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }

    //Seta o combobox tipo de pastas com os dados do banco
    private void setarComboBox() {
        String sql = "select descricao from tbTipoPasta";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                this.cbCadReceitaTipo.addItem(rs.getString(1));
            }
            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Adiciona um novo tipo de pasta no combobox
    private void addComboBox() {
        String sql = "insert into tbTipoPasta(descricao) values(?)";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.cbCadReceitaTipo.getSelectedItem().toString());
            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Adicionado com sucesso.");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Remove um tipo de pasta no combobox
    private void removeComboBox() {
        String sql = "delete from tbTipoPasta where descricao=?";

        PreparedStatement pst = null;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.cbCadReceitaTipo.getSelectedItem().toString());
            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Apagado com sucesso.");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //verifica se o tipo de pasta esta ligado a alguma receita
    private boolean verificaTipoPasta(int tipo) {
        String sql = "select codigoTipoPasta from tbreceita where codigoTipoPasta=?";
        int conf = 0;
        
        PreparedStatement pst ;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, tipo);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                conf = 1;
            } else {
                conf = 2;
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return conf == 1;
    }

    //evento para setar tblCadRecComponentes
    private void setarTabela() {

        String comp = this.txtCadRecComponentes.getText().trim();
        String cons = this.txtCadRecConsumo.getText().trim();

        DefaultTableModel val = (DefaultTableModel) this.tblCadRecComponentes.getModel();
        val.addRow(new String[]{comp, cons});

    }

    //evento para remover tblCadRecComponentes
    private void removerTabela() {
        if (this.tblCadRecComponentes.getSelectedRow() != -1) {
            DefaultTableModel remov = (DefaultTableModel) this.tblCadRecComponentes.getModel();
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

    //confirma se o codigo já existe
    private boolean confirmaCodigo(String codigo) {
        String sql = "select count (codigorec) as total from tbreceita where codigorec ='" + codigo + "';";
        int total = 0;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            
            ResultSet rs =  pst.executeQuery();
            total = Integer.parseInt(rs.getString(1));
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return total <= 0;

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
        DecimalFormat dFormat = new DecimalFormat("###.00");
        NumberFormatter formatter = new NumberFormatter(dFormat);
        formatter.setFormat(dFormat);
        formatter.setAllowsInvalid(false);

        this.txtCadRecConsumo.setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCadRecDes = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCadRecVal = new javax.swing.JTextField();
        btnCadRecAdicionar = new javax.swing.JButton();
        btnCadRecAtualizar = new javax.swing.JButton();
        btnCadRecDeletar = new javax.swing.JButton();
        btnCadRecLimpar = new javax.swing.JButton();
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
        jButton2 = new javax.swing.JButton();
        txtCadRecConsumo = new javax.swing.JFormattedTextField();
        cbCadReceitaTipo = new javax.swing.JComboBox<>();
        tbnCadRecTipo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Receitas");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Código:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel2.setText("Pantone:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, -1, -1));
        getContentPane().add(txtCadRecDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 190, -1));

        jLabel3.setText("Tipo de pasta:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, -1, -1));

        jLabel4.setText("Descrição:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, -1));

        jLabel5.setText("Validade:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 50, -1, -1));
        getContentPane().add(txtCadRecVal, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, 120, -1));

        btnCadRecAdicionar.setText("Salvar");
        btnCadRecAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecAdicionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadRecAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 79, -1));

        btnCadRecAtualizar.setText("Atualizar");
        btnCadRecAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecAtualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadRecAtualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 79, -1));

        btnCadRecDeletar.setText("Deletar");
        btnCadRecDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecDeletarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadRecDeletar, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, 79, -1));

        btnCadRecLimpar.setText("Novo");
        btnCadRecLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadRecLimparActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadRecLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 79, -1));
        getContentPane().add(txtCadRecCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 130, -1));
        getContentPane().add(txtCadRecPan, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 130, -1));

        btnReceitaPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/pesquisar.png"))); // NOI18N
        btnReceitaPesquisar.setToolTipText("Pesquisar");
        btnReceitaPesquisar.setContentAreaFilled(false);
        btnReceitaPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReceitaPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceitaPesquisarActionPerformed(evt);
            }
        });
        getContentPane().add(btnReceitaPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 28, 31));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Cadastro de Receita");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblCadRecComponentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Componentes", "Consumo"
            }
        ));
        tblCadRecComponentes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadRecComponentesMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblCadRecComponentesMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblCadRecComponentes);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 650, 270));

        jLabel8.setText("Componentes:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));
        jPanel1.add(txtCadRecComponentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 260, -1));

        jLabel7.setText("Consumo:");
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
        jPanel1.add(btnInsumoPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 30, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/++++.png"))); // NOI18N
        jButton2.setToolTipText("Adicionar");
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 30, -1));
        jPanel1.add(txtCadRecConsumo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 110, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 820, 360));

        cbCadReceitaTipo.setEditable(true);
        getContentPane().add(cbCadReceitaTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 150, -1));

        tbnCadRecTipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/++++.png"))); // NOI18N
        tbnCadRecTipo.setToolTipText("Adicionar");
        tbnCadRecTipo.setContentAreaFilled(false);
        tbnCadRecTipo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbnCadRecTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnCadRecTipoActionPerformed(evt);
            }
        });
        getContentPane().add(tbnCadRecTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, 20, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/x.png"))); // NOI18N
        jButton1.setToolTipText("Apagar");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 20, -1));

        setBounds(0, 0, 860, 560);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadRecAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecAdicionarActionPerformed
        // chama o metodo adicionar
        codigoTipo = buscaCodTipoPasta();
        boolean total;
        boolean campos;
        // verifica os campos
        campos = verificaCampos();
        if (campos == false) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
        } else {
            //chama o metodo para confirmar se o codigo já existe
            total = confirmaCodigo(this.txtCadRecCodigo.getText());
            if (total == false) {
                JOptionPane.showMessageDialog(null, "Código já existe.");
            } else {
                adicionarReceita();
            }

        }
    }//GEN-LAST:event_btnCadRecAdicionarActionPerformed

    private void btnCadRecLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecLimparActionPerformed
        // chama o metodo limpar campos;     
        limparCampos();
    }//GEN-LAST:event_btnCadRecLimparActionPerformed

    private void btnCadRecAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecAtualizarActionPerformed
        // cahama o metodo atualizar
        codigoTipo = buscaCodTipoPasta();
        boolean total;
        boolean campos;
        // verifica os campos
        campos = verificaCampos();
        if (campos == false) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
        } else {
            total = confirmaCodigo(this.txtCadRecCodigo.getText());
            if (total == true) {
                JOptionPane.showMessageDialog(null, "Código inválido.");
            } else {
                atualizarReceita();
                //adicionarComponentes();
//                adicionarComponentes();

            }
        }
    }//GEN-LAST:event_btnCadRecAtualizarActionPerformed

    private void btnCadRecDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadRecDeletarActionPerformed
        // chama o metodo deletar
        deletarReceita();
    }//GEN-LAST:event_btnCadRecDeletarActionPerformed

    private void btnReceitaPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceitaPesquisarActionPerformed
        // chama a TelaPesquisarReceita
        TelaPesquisarReceita receita = new TelaPesquisarReceita();
        TelaPrincipal.Desktop.add(receita);
        receita.setVisible(true);
    }//GEN-LAST:event_btnReceitaPesquisarActionPerformed

    private void tblCadRecComponentesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadRecComponentesMouseClicked

    }//GEN-LAST:event_tblCadRecComponentesMouseClicked

    private void btnInsumoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsumoPesquisarActionPerformed
        // chama a TelaPesquisarInsumos
        TelaPesquisarInsumos insumos = new TelaPesquisarInsumos();
        TelaPrincipal.Desktop.add(insumos);
        insumos.setVisible(true);
        insumos.confimaTela = false;
    }//GEN-LAST:event_btnInsumoPesquisarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // chama metodo para setar a tabela
        if ((this.txtCadRecComponentes.getText().isEmpty()) || (this.txtCadRecConsumo.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {
            setarTabela();
            this.txtCadRecComponentes.setText(null);
            this.txtCadRecConsumo.setValue(null);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblCadRecComponentesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadRecComponentesMouseReleased

    }//GEN-LAST:event_tblCadRecComponentesMouseReleased

    private void tbnCadRecTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnCadRecTipoActionPerformed
        // chama o metodo adicionar tipo de pasta
        boolean conf = verificaTipoPasta(buscaCodTipoPasta());
        if (this.cbCadReceitaTipo.getSelectedItem().equals("")) {
            JOptionPane.showMessageDialog(null, "Tipo de pasta inválido.");
        } else {
            if (conf == true) {
                JOptionPane.showMessageDialog(null, "Tipo de pasta já existe.");
            } else {
                addComboBox();
                this.cbCadReceitaTipo.removeAllItems();
                setarComboBox();
            }
        }
    }//GEN-LAST:event_tbnCadRecTipoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Chama o metodo remover tipo de pasta
        boolean conf = verificaTipoPasta(buscaCodTipoPasta());
        if (conf == true) {
            JOptionPane.showMessageDialog(null, "Esse Tipo de pasta não pode ser removido.");
        } else {
            removeComboBox();
            this.cbCadReceitaTipo.removeAllItems();
            setarComboBox();
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadRecAdicionar;
    private javax.swing.JButton btnCadRecAtualizar;
    private javax.swing.JButton btnCadRecDeletar;
    private javax.swing.JButton btnCadRecLimpar;
    private javax.swing.JButton btnInsumoPesquisar;
    private javax.swing.JButton btnReceitaPesquisar;
    public static javax.swing.JComboBox<String> cbCadReceitaTipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
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
