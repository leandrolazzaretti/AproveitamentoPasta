/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import conexao.ModuloConexao;
import dto.InsumoDto;
import dto.MovimentacaoDto;
import dto.RelatorioReceitaDto;
import dto.UsuarioDto;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Leandro
 */
public class Relatorio {

    Connection conexao;
    List<UsuarioDto> listaUsuario = new ArrayList<>();
    List<InsumoDto> listaInsumo = new ArrayList<>();
    List<MovimentacaoDto> listaMovimentacao = new ArrayList<>();
    List<RelatorioReceitaDto> listaReceita = new ArrayList<>();
    List lista;
    String url;
    Util util = new Util();

    public Relatorio() {
        this.conexao = ModuloConexao.conector();
    }

    public void gerarRelatorio() throws JRException {

        InputStream arq = Relatorio.class.getResourceAsStream(this.url);
        JasperReport report = JasperCompileManager.compileReport(arq);
        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(this.lista));
        JasperViewer.viewReport(print, false);
    }

    //Seta a lista de usuarios com os dados do banco
    public void relatorioUsuarioSetar(String codigo, String nome, String login, String perfil) {
        String vazio = "";
        String sql = "select codigo, nome, login, perfil from tbusuarios where"
                + " (codigo ='" + codigo + "' or '" + codigo + "'='" + vazio + "')"
                + " and (nome='" + nome + "' or '" + nome + "'='" + vazio + "') and"
                + " (login = '" + login + "' or '" + login + "' = '" + vazio + "') and"
                + " (perfil = '" + perfil + "' or '" + perfil + "' = '" + vazio + "')";
        PreparedStatement pst;
        this.url = "/Report/Usuarios.jrxml";

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                UsuarioDto user = new UsuarioDto();
                user.setCodigo(rs.getInt(1));
                user.setNome(rs.getString(2));
                user.setLogin(rs.getString(3));
                user.setPerfil(rs.getString(4));

                this.listaUsuario.add(user);
            }
            this.lista = this.listaUsuario;
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    //Seta a lista de insumos com os dados do banco
    public void relatorioInsumoSetar(String codigo, String insumo, String um, String quant) {
        String vazio = "";
        String sql = "select * from tbinsumos where (codigo ='" + codigo + "' or '" + codigo + "'='" + vazio + "')"
                + " and (descricao ='" + insumo + "' or '" + insumo + "'='" + vazio + "') and"
                + " (UM = '" + um + "' or '" + um + "' = '" + vazio + "') and"
                + " (quantidade = '" + quant + "' or '" + quant + "' = '" + vazio + "')";

        PreparedStatement pst;
        this.url = "/Report/Insumos.jrxml";

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                InsumoDto ins = new InsumoDto();
                ins.setCodigo(rs.getInt(1));
                ins.setDescricao(rs.getString(2));
                ins.setUM(rs.getString(3));
                ins.setQuantidade(this.util.formatadorQuant(rs.getDouble(4)));
                ins.setPreco(this.util.formatadorQuant(rs.getDouble(5)));

                this.listaInsumo.add(ins);
            }
            this.lista = this.listaInsumo;
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void relatorioMovimentacaoSetar(String tipo, String estoque, String codigo, String descricao, String dataDe, String dataAte) {
        String vazio = "";
        String sql = "select * from tbMovimentacao where (quantidade " + tipo + "'0') and"
                + " (tipo ='" + estoque + "' or '" + estoque + "'='" + vazio + "')"
                + " and (codigoID ='" + codigo + "' or '" + codigo + "'='" + vazio + "') and"
                + " (descricao = '" + descricao + "' or '" + descricao + "' = '" + vazio + "') and"
                + " (data >= '" + dataDe + "' and data <= '" + dataAte + "' or"
                + " '" + dataDe + "' = '" + vazio + "' and '" + dataAte + "' = '" + vazio + "')"
                + " order by data";
        PreparedStatement pst;
        this.url = "/Report/Movimentacao.jrxml";

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                MovimentacaoDto mov = new MovimentacaoDto();
                mov.setTipo(rs.getString(1));
                mov.setCodigoID(rs.getInt(2));
                mov.setDescricao(rs.getString(3));
                mov.setData(rs.getString(4));
                mov.setQuantidade(this.util.formatadorQuant(rs.getDouble(5)));

                this.listaMovimentacao.add(mov);
            }
            this.lista = this.listaMovimentacao;
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void relatorioReceitaSetar(String codigo, String receita, String tipoPasta, String pantone, String vencimento, String insumo) {
        String vazio = "";
        String sql = "select ri.codigoReceita, r.descricao as desc_r, tp.descricao as desc_tp, r.pantone, r.datavencimento, i.descricao as desc_i, i.UM, ri.consumo   from tbReceitaInsumo as ri"
                + " inner join tbreceita as r on r.codigorec = ri.codigoReceita"
                + " inner join tbinsumos as i on i.codigo = ri.codigoInsumo"
                + " inner join tbTipoPasta as tp on tp.codigo = r.codigoTipoPasta"
                + " where (ri.codigoReceita ='" + codigo + "' or '" + codigo + "'='" + vazio + "')"
                + " and (r.descricao ='" + receita + "' or '" + receita + "'='" + vazio + "') and"
                + " (tp.descricao = '" + tipoPasta + "' or '" + tipoPasta + "' = '" + vazio + "') and"
                + " (r.pantone = '" + pantone + "' or '" + pantone + "' = '" + vazio + "') and"
                + " (r.datavencimento = '" + vencimento + "' or '" + vencimento + "' = '" + vazio + "') and"
                + " (i.descricao = '" + insumo + "' or '" + insumo + "' = '" + vazio + "')"
                + " order by ri.codigoReceita;";
        PreparedStatement pst;
        this.url = "/Report/Receitas.jrxml";

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                RelatorioReceitaDto relRec = new RelatorioReceitaDto();

                relRec.setCodigoReceita(rs.getInt(1));
                relRec.setDescRec(rs.getString(2));
                relRec.setDescTP(rs.getString(3));
                relRec.setPantone(rs.getString(4));
                relRec.setDataVencimento(rs.getInt(5));
                relRec.setDescIns(rs.getString(6));
                relRec.setUM(rs.getString(7));
                relRec.setConsumo(this.util.formatadorQuant(rs.getDouble(8)));

                this.listaReceita.add(relRec);
            }
            this.lista = this.listaReceita;
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

}
