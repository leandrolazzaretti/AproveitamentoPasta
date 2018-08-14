/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import dao.EstoquePastaFinalDao;
import dao.InsumoDao;
import dao.MovimentacaoDao;
import dao.MovimentacaoEstoqueDao;
import dao.ReceitaDao;
import dto.MovimentacaoDto;
import dto.MovimentacaoEstoqueDto;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import util.MascaraMoeda;
import util.Util;

/**
 *
 * @author Leandro
 */
public class TelaOpcaoPasta extends javax.swing.JInternalFrame {

    private final EstoquePastaFinalDao estPasFinal = new EstoquePastaFinalDao();
    private final InsumoDao insDao = new InsumoDao();
    private final Util util = new Util();
    private final MovimentacaoDao movDao = new MovimentacaoDao();

    /**
     * Creates new form TelaOpcaoPasta
     */
    public TelaOpcaoPasta() {
        initComponents();
    }

    private void insertPasta() {
        MovimentacaoEstoqueDto movEstDto = new MovimentacaoEstoqueDto();
        MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();
        ReceitaDao receitaDao = new ReceitaDao();

        movEstDto.setCodigoReceita(Integer.parseInt(TelaEstoquePasta.txtCodigo.getText()));
        movEstDto.setUM("kg");
        movEstDto.setQuantidade(Double.parseDouble(TelaEstoquePasta.txtQuantidade.getText().replace(".", "").replace(",", ".")));
        movEstDto.setData(movEstDao.dataAtual());
        movEstDto.setDataVencimento(movEstDao.dataVencimento(movEstDao.dataAtual(), receitaDao.buscaVencimento(Integer.parseInt(TelaEstoquePasta.txtCodigo.getText()))));

        movEstDao.entradaPasta(movEstDto);

        //a estrutura abaixo seta a tabela de movimentação de estoque
        MovimentacaoDto movDto = new MovimentacaoDto();
        movDto.setTipo("Pasta");
        movDto.setCodigoID(movEstDto.getCodigoReceita());
        movDto.setDescricao(this.movDao.buscaDescricaoReceita(movEstDto.getCodigoReceita()));
        movDto.setQuantidade("" + movEstDto.getQuantidade());
        movDto.setData(this.util.dataAtual());
        this.movDao.movimentacao(movDto);

        //dar saida nos insumos
        if (EstoquePastaFinalDao.insumosOp2.isEmpty()) {
        } else {
            for (int i = 0; i < EstoquePastaFinalDao.insumosOp2.size(); i++) {
                this.insDao.saidaInsumo2(EstoquePastaFinalDao.insumosOp2.get(i).getCodigo(), EstoquePastaFinalDao.pastaProduzirOp22.get(i).getConsumo());
                MovimentacaoDto movDto2 = new MovimentacaoDto();
                movDto2.setTipo("Insumo");
                movDto2.setCodigoID(EstoquePastaFinalDao.insumosOp2.get(i).getCodigo());
                movDto2.setDescricao(EstoquePastaFinalDao.insumosOp2.get(i).getDescricao());
                movDto2.setQuantidade("-" + EstoquePastaFinalDao.pastaProduzirOp22.get(i).getConsumo());
                movDto2.setData(this.util.dataAtual());
                this.movDao.movimentacao(movDto2);
            }
        }
    }

    private void limparCampos() {
        TelaEstoquePasta.txtCodigo.setText(null);
        TelaEstoquePasta.txtCodigo.setEnabled(true);
        TelaEstoquePasta.txtCodigo.requestFocus();
        TelaEstoquePasta.txtQuantidade.setDocument(new MascaraMoeda());
        TelaEstoquePasta.txtQuantidade.setEnabled(false);
        TelaEstoquePasta.lblCustoProducao.setText("0,00");
        TelaEstoquePasta.lblValorReaproveitadoOpc1.setText("0,00");
        TelaEstoquePasta.lblValorReaproveitadoOpc2.setText("0,00");
        TelaEstoquePasta.btnVisualizarOp1.setEnabled(false);
        TelaEstoquePasta.btnVisualizarOp2.setEnabled(false);
        EstoquePastaFinalDao.listFinalOp22.clear();
        EstoquePastaFinalDao.pastaProduzirOp22.clear();
        retornarCoresEnable();
        ((DefaultTableModel) TelaEstoquePasta.tblProducaoPastaOp1.getModel()).setRowCount(0);
        ((DefaultTableModel) TelaEstoquePasta.tblProducaoPastaOp2.getModel()).setRowCount(0);
    }

    private void retornarCoresEnable() {
        alterarCorEnable(TelaEstoquePasta.jPanel8, TelaEstoquePasta.btnVisualizarOp1);
        alterarCorEnable(TelaEstoquePasta.jPanel7, TelaEstoquePasta.btnVisualizarOp2);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tblOpPasta = new javax.swing.JScrollPane();
        tblOpcPasta = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnFechar = new javax.swing.JButton();
        lblOpDescricao = new javax.swing.JLabel();
        lblOpcao = new javax.swing.JLabel();
        lblQuantidade = new javax.swing.JLabel();
        lblReaproveitado = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblCustoProducao = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnProduzir = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblPasta = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(143, 165, 110));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(122, 122, 122)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblOpcPasta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblOpcPasta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Código", "Descrição", "Usar", "Equilave à (%)", "Vencimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOpPasta.setViewportView(tblOpcPasta);

        jPanel1.add(tblOpPasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 859, 240));

        jPanel3.setBackground(new java.awt.Color(143, 165, 110));
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

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(816, 2, 40, 20));

        lblOpDescricao.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblOpDescricao.setForeground(new java.awt.Color(79, 79, 79));
        lblOpDescricao.setText("Descrição da opção");
        jPanel1.add(lblOpDescricao, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        lblOpcao.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblOpcao.setForeground(new java.awt.Color(79, 79, 79));
        lblOpcao.setText("Opção de produção");
        jPanel1.add(lblOpcao, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        lblQuantidade.setForeground(new java.awt.Color(79, 79, 79));
        lblQuantidade.setText("Pasta ");
        jPanel1.add(lblQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, -1, -1));

        lblReaproveitado.setForeground(new java.awt.Color(2, 155, 0));
        lblReaproveitado.setText("0,00");
        jPanel1.add(lblReaproveitado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, -1, -1));

        jLabel8.setForeground(new java.awt.Color(79, 79, 79));
        jLabel8.setText("Custo em insumos: R$ ");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, -1, -1));

        lblCustoProducao.setForeground(new java.awt.Color(255, 0, 0));
        lblCustoProducao.setText("0,00");
        jPanel1.add(lblCustoProducao, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnProduzir.setForeground(new java.awt.Color(66, 66, 66));
        btnProduzir.setText("Produzir");
        btnProduzir.setToolTipText("Produzir");
        btnProduzir.setContentAreaFilled(false);
        btnProduzir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProduzir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProduzirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProduzirMouseExited(evt);
            }
        });
        btnProduzir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProduzirActionPerformed(evt);
            }
        });
        jPanel7.add(btnProduzir, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 25));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, -1, -1));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 201, 201)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCancelar.setForeground(new java.awt.Color(66, 66, 66));
        btnCancelar.setText("Cancelar");
        btnCancelar.setToolTipText("Produzir");
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelarMouseExited(evt);
            }
        });
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel8.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 25));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 120, -1, -1));

        jLabel10.setForeground(new java.awt.Color(79, 79, 79));
        jLabel10.setText("Reaproveitado: R$");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel11.setForeground(new java.awt.Color(79, 79, 79));
        jLabel11.setText("Quantidade produzir: ");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jLabel12.setForeground(new java.awt.Color(79, 79, 79));
        jLabel12.setText("Pasta: ");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        lblPasta.setForeground(new java.awt.Color(79, 79, 79));
        lblPasta.setText("Pasta ");
        jPanel1.add(lblPasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        setBounds(0, 100, 860, 417);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseEntered
        // quando o mouse está em cima
        this.jPanel3.setBackground(new Color(211, 57, 33));
        this.btnFechar.setForeground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnFecharMouseEntered

    private void btnFecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFecharMouseExited
        // quando o mouse sair de cima
        this.jPanel3.setBackground(new Color(143, 165, 110));
        this.btnFechar.setForeground(new Color(79, 79, 79));
    }//GEN-LAST:event_btnFecharMouseExited

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        //fecha a tela
        this.dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnProduzirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProduzirMouseEntered
        // quando o mouse estiver em cima
        if (this.btnProduzir.isEnabled()) {
            alteraCor(this.jPanel7, this.btnProduzir);
        }
    }//GEN-LAST:event_btnProduzirMouseEntered

    private void btnProduzirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProduzirMouseExited
        // quando o mouse sair de cima
        if (this.btnProduzir.isEnabled()) {
            retornaCor(this.jPanel7, this.btnProduzir);
        }
    }//GEN-LAST:event_btnProduzirMouseExited

    private void btnProduzirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProduzirActionPerformed
        // chama os metodos para fazer a produção da pasta, tendo como base a segunda opção de produção
        if (this.estPasFinal.produzirOpc1() == true) {
            insertPasta();
            limparCampos();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Erro na produção!");
        }
    }//GEN-LAST:event_btnProduzirActionPerformed

    private void btnCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseEntered
        // quando o mouse estiver em cima
        if (this.btnProduzir.isEnabled()) {
            alteraCor(this.jPanel8, this.btnCancelar);
        }
    }//GEN-LAST:event_btnCancelarMouseEntered

    private void btnCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseExited
        // quando o mouse sair de cima
        if (this.btnProduzir.isEnabled()) {
            retornaCor(this.jPanel8, this.btnCancelar);
        }
    }//GEN-LAST:event_btnCancelarMouseExited

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // cancela a produção e fecha a tela
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFechar;
    public static javax.swing.JButton btnProduzir;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    public static javax.swing.JLabel lblCustoProducao;
    public static javax.swing.JLabel lblOpDescricao;
    public static javax.swing.JLabel lblOpcao;
    public static javax.swing.JLabel lblPasta;
    public static javax.swing.JLabel lblQuantidade;
    public static javax.swing.JLabel lblReaproveitado;
    private javax.swing.JScrollPane tblOpPasta;
    public static javax.swing.JTable tblOpcPasta;
    // End of variables declaration//GEN-END:variables
}
