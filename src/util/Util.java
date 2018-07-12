/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import conexao.ModuloConexao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import static telas.TelaPrincipal.Desktop;

/**
 *
 * @author Leandro
 */
public class Util {

    Connection conexao = null;

    public Util() {
        this.conexao = ModuloConexao.conector();
    }

    // metodo para retirar todasd as bordas do JinternalFrame(gambiarra)
    public void retirarBordas(JInternalFrame frame) {
        ((BasicInternalFrameUI) frame.getUI()).setNorthPane(null); //retirar o painel superior
        frame.setBorder(null);//retirar bordas
    }

    //busca a menor data correspondente ao codigo da receita no banco
    public String buscarDataMenor(int codigoReceita) {
        String sql = "select min(ep.data) from tbEstoquePasta as ep where codigoReceita =?";
        String dataMenor = null;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, codigoReceita);
            ResultSet rs = pst.executeQuery();
            dataMenor = rs.getString(1);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return dataMenor;
    }

    //busca a menor data correspondente ao codigo da receita no banco
    public String buscarID(int codigoReceita) {
        String sql = "select min(ep.ID) from tbEstoquePasta as ep where codigoReceita =?";
        String idReceita = null;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, codigoReceita);
            ResultSet rs = pst.executeQuery();
            idReceita = rs.getString(1);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return idReceita;
    }

    public String buscarDescricaoReceita(String codigo) {
        String sql = "select descricao from tbreceita where codigorec =?";
        PreparedStatement pst;
        String descricao = null;
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, codigo);
            ResultSet rs = pst.executeQuery();
            descricao = rs.getString(1);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return descricao;
    }

    public void comandoInternal(JInternalFrame frame) {
        try {
            Desktop.add(frame);
            frame.setVisible(true);
            frame.toFront();
//            if (!frame.isVisible()) {
//                Desktop.remove(frame);
//                Desktop.add(frame);
//                frame.setVisible(true);
//            }
//            if (frame.isIcon()) {
//                frame.setIcon(false);
//            }
//            frame.toFront();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }

    }

    public void comandoInternal2(JInternalFrame frame) {
        try {
            Desktop.add(frame);
            frame.setLocation(0, 250);
            frame.setVisible(true);
            frame.toFront();
//         
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }

    }

//    public void bloquearMovimentacao(JInternalFrame frame, int x, int y) {
//        try {
//            frame.addComponentListener(new ComponentAdapter() {
//                @Override
//                public void componentMoved(ComponentEvent evento) {
//                    frame.setLocation(x, y);
//                }
//            });
//
//        } catch (Exception e) {
//        }
//    }
    public String formatador(double valor) {

        DecimalFormat formatador = new DecimalFormat("0.00");
        return formatador.format(valor).replace(",", ".");
    }

    public String formatadorQuant(Double quantidade) {

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(2);
        BigDecimal quant = new BigDecimal(this.formatador(quantidade));
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String quantFormatado = nf.format(quant);
        quantFormatado = quantFormatado.replace("R$ ", "");
        return quantFormatado;
    }

}
