/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import conexao.ModuloConexao;
import dto.EstoquePastaRelatorioDto;
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

    private Connection conexao;
    private List<UsuarioDto> listaUsuario = new ArrayList<>();
    private List<InsumoDto> listaInsumo = new ArrayList<>();
    private List<MovimentacaoDto> listaMovimentacao = new ArrayList<>();
    private List<RelatorioReceitaDto> listaReceita = new ArrayList<>();
    private List<EstoquePastaRelatorioDto> listaPastaEstoque = new ArrayList<>();
    private List lista;
    private String url;
    private Util util = new Util();

    public Relatorio() {
        this.conexao = ModuloConexao.conector();
    }

    public void gerarRelatorio() throws JRException {

//        Map parametros = new HashMap();
//        parametros.put("SUBREPORT_DIR", Conexao());
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
        boolean confirma = false;
        double totalQuantidade = 0;
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
                confirma = true;
                InsumoDto ins = new InsumoDto();
                ins.setCodigo(rs.getInt(1));
                ins.setDescricao(rs.getString(2));
                ins.setUM(rs.getString(3));
                ins.setQuantidade(this.util.formatadorQuant(rs.getDouble(4)));
                ins.setPreco(this.util.formatadorQuant(rs.getDouble(5)));
                if (rs.getDouble(4) > 0) {
                    totalQuantidade += rs.getDouble(4);
                }
                this.listaInsumo.add(ins);
            }
            if (confirma == true) {
                this.listaInsumo.get(this.listaInsumo.size() - 1).setTotalQuantidade(this.util.formatadorQuant(totalQuantidade));
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
        boolean confirma = false;
        double totQuantEntrada = 0, totQuantSaida = 0;
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
                confirma = true;
                MovimentacaoDto mov = new MovimentacaoDto();
                mov.setTipo(rs.getString(1));
                mov.setCodigoID(rs.getInt(2));
                mov.setDescricao(rs.getString(3));
                mov.setData(this.util.inverterData(rs.getString(4)).replace("-", "/"));
                mov.setQuantidade(this.util.formatadorQuant(rs.getDouble(5)));

                if (rs.getDouble(5) > 0) {
                    totQuantEntrada += rs.getDouble(5);
                } else {
                    totQuantSaida += rs.getDouble(5);
                }
                this.listaMovimentacao.add(mov);
            }
            if (confirma == true) {
                this.listaMovimentacao.get(this.listaMovimentacao.size() - 1).setTotQuantEntrada(this.util.formatadorQuant(totQuantEntrada));
                this.listaMovimentacao.get(this.listaMovimentacao.size() - 1).setTotQuantSaida(this.util.formatadorQuant(totQuantSaida).replace("-", ""));
            }
            this.lista = this.listaMovimentacao;
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void relatorioEstoquePastaSetar(String id, String codigo, String descricao, String dataDe, String dataAte, String dataValidade, boolean confirmaValidade) {
        String vazio = "";
        String validade = dataValidade;
        boolean confirma = false;
        double totalQuantidade = 0, totalValor = 0;
        if (confirmaValidade == true) {
            validade = dataValidade.replace("<", ">");
        }
        String sql = "select ep.ID, ep.codigoReceita, r.descricao, (sum(ri.custoPorKg) * ep.quantidade), ep.quantidade, ep.data, ep.dataVencimento"
                + " from tbEstoquePasta as ep"
                + " inner join tbreceita as r on r.codigorec = ep.codigoReceita"
                + " inner join tbReceitaInsumo as ri on ri.codigoReceita = ep.codigoReceita"
                + " where (ep.quantidade != 0) and (ep.ID ='" + id + "' or '" + id + "'='" + vazio + "')"
                + " and (ep.codigoReceita ='" + codigo + "' or '" + codigo + "'='" + vazio + "') and"
                + " (r.descricao = '" + descricao + "' or '" + descricao + "' = '" + vazio + "') and"
                + " (ep.data >= '" + dataDe + "' and ep.data <= '" + dataAte + "' or"
                + " '" + dataDe + "' = '" + vazio + "' and '" + dataAte + "' = '" + vazio + "') and"
                + " (ep.dataVencimento " + dataValidade + " or '" + vazio + "' " + validade + ")"
                + " group by ep.ID"
                + " order by ep.data";
        PreparedStatement pst;
        this.url = "/Report/EstoquePasta.jrxml";

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                confirma = true;
                EstoquePastaRelatorioDto estPasRelDto = new EstoquePastaRelatorioDto();
                estPasRelDto.setID(rs.getInt(1));
                estPasRelDto.setCodigoReceita(rs.getInt(2));
                estPasRelDto.setDescricao(rs.getString(3));
                estPasRelDto.setCustoPorKg(this.util.formatadorQuant(rs.getDouble(4)));
                estPasRelDto.setQuantidade(this.util.formatadorQuant(rs.getDouble(5)));
                estPasRelDto.setData(this.util.inverterData(rs.getString(6)).replace("-", "/"));
                estPasRelDto.setDataVencimento(this.util.inverterData(rs.getString(7)).replace("-", "/"));
                totalQuantidade += rs.getDouble(5);
                totalValor += rs.getDouble(4);
                this.listaPastaEstoque.add(estPasRelDto);
            }
            if (confirma == true) {
                this.listaPastaEstoque.get(this.listaPastaEstoque.size() - 1).setTotalQuantidade(this.util.formatadorQuant(totalQuantidade));
                this.listaPastaEstoque.get(this.listaPastaEstoque.size() - 1).setTotalValor(this.util.formatadorQuant(totalValor));
            }

            this.lista = this.listaPastaEstoque;
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void relatorioReceitaSetar(String codigo, String receita, String tipoPasta, String pantone, String vencimento, String insumo) {
        String vazio = "";
        int id = 0, idTemp = 0, cont = 0;
        double valor = 0;
        boolean confirma = false;
        String sql = "select ri.codigoReceita, r.descricao as descRec, tp.descricao as descTp, r.pantone, r.datavencimento, i.descricao as desc_i, ri.custoPorKg, ri.consumo   from tbReceitaInsumo as ri"
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

            //usado para gerar um espaçamento entre receitas
            RelatorioReceitaDto relRec2 = new RelatorioReceitaDto();
            relRec2.setCodigoReceita("");
            relRec2.setDescRec("");
            relRec2.setDescTP("");
            relRec2.setPantone("");
            relRec2.setDataVencimento("");
            relRec2.setDescIns("");
            relRec2.setValor("");
            relRec2.setConsumo("");
            while (rs.next()) {
                cont++;
                valor += rs.getDouble(7);
                confirma = true;
                RelatorioReceitaDto relRec = new RelatorioReceitaDto();
                // Se o codigo for igual ao anterior, os campos da receita recebem "" para ficarem vazios, somente insumo será setado
                if (id != rs.getInt(1)) {
                    if (cont != 1) {
                        valor = valor - rs.getDouble(7);
                    }
                    id = rs.getInt(1);

                    if (!this.listaReceita.isEmpty()) {
                        this.listaReceita.get(idTemp).setValor("R$ " + this.util.formatadorQuant(valor));
                        valor = rs.getDouble(7);
                    }
                    idTemp = cont;

                    this.listaReceita.add(relRec2);
                    cont++;
                    relRec.setCodigoReceita(rs.getString(1));
                    relRec.setDescRec(rs.getString(2));
                    relRec.setDescTP(rs.getString(3));
                    relRec.setPantone(rs.getString(4));
                    relRec.setDataVencimento(rs.getString(5));
                } else {
                    relRec.setCodigoReceita("");
                    relRec.setDescRec("");
                    relRec.setDescTP("");
                    relRec.setPantone("");
                    relRec.setDataVencimento("");
                }
                relRec.setDescIns(rs.getString(6));
                relRec.setValor("");
                relRec.setConsumo(this.util.formatadorQuant(rs.getDouble(8)));

                this.listaReceita.add(relRec);
            }
            if (confirma == true) {
                this.listaReceita.get(idTemp).setValor("R$ " + this.util.formatadorQuant(valor));
            }
            this.lista = this.listaReceita;
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

//    public void relatorioReceitaSetarTeste(String codigo, String receita, String tipoPasta, String pantone, String vencimento, String insumo) {
//        String vazio = "";
//        boolean confirma = false;
//        String sql = "select ri.codigoReceita, r.descricao as descRec, tp.descricao as descTp, r.pantone, r.datavencimento, ri.custoPorKg from tbReceitaInsumo as ri"
//                + " inner join tbreceita as r on r.codigorec = ri.codigoReceita"
//                + " inner join tbinsumos as i on i.codigo = ri.codigoInsumo"
//                + " inner join tbTipoPasta as tp on tp.codigo = r.codigoTipoPasta"
//                + " where (ri.codigoReceita ='" + codigo + "' or '" + codigo + "'='" + vazio + "')"
//                + " and (r.descricao ='" + receita + "' or '" + receita + "'='" + vazio + "') and"
//                + " (tp.descricao = '" + tipoPasta + "' or '" + tipoPasta + "' = '" + vazio + "') and"
//                + " (r.pantone = '" + pantone + "' or '" + pantone + "' = '" + vazio + "') and"
//                + " (r.datavencimento = '" + vencimento + "' or '" + vencimento + "' = '" + vazio + "') and"
//                + " (i.descricao = '" + insumo + "' or '" + insumo + "' = '" + vazio + "')"
//                + " order by ri.codigoReceita;";
//        PreparedStatement pst;
//        this.url = "/Report/Receita.jrxml";
//
//        try {
//            pst = this.conexao.prepareStatement(sql);
//            ResultSet rs = pst.executeQuery();
//            while (rs.next()) {
//                confirma = true;
//                RelatorioReceitaDto relRec = new RelatorioReceitaDto();
//                relRec.setCodigoReceita(rs.getInt(1));
//                relRec.setDescRec(rs.getString(2));
//                relRec.setDescTP(rs.getString(3));
//                relRec.setPantone(rs.getString(4));
//                relRec.setDataVencimento(rs.getInt(5));
//                relRec.setCustoPorKg(this.util.formatadorQuant(rs.getDouble(6)));
//
//                this.listaReceita.add(relRec);
//            }
//            if (confirma == true) {
//
//            }
//            this.lista = this.listaReceita;
//            pst.close();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//            System.out.println(e);
//        }
//    }
}
