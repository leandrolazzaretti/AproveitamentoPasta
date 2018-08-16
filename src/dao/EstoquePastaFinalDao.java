/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.ModuloConexao;
import dto.EstoquePastaDto;
import dto.InsumoDto;
import dto.MovimentacaoDto;
import dto.ReceitaInsumoDto;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import telas.TelaEstoquePasta;
import util.CoresAlternadasTabela;
import util.Util;

/**
 *
 * @author Leandro
 */
public class EstoquePastaFinalDao {

    Connection conexao = null;

    private final Util util = new Util();
    private final MovimentacaoEstoqueDao movEstDao = new MovimentacaoEstoqueDao();
    private final CoresAlternadasTabela mudarCorLinha = new CoresAlternadasTabela();
    private final InsumoDao insumoDao = new InsumoDao();
    private final MovimentacaoDto movDto = new MovimentacaoDto();
    private final MovimentacaoDao movDao = new MovimentacaoDao();

    private List<EstoquePastaDto> listTemp = new ArrayList<>();
    private List<EstoquePastaDto> listTempX = new ArrayList<>();
    public static List<EstoquePastaDto> listFinalOp1 = new ArrayList<>();
    public static List<EstoquePastaDto> listFinalOp22 = new ArrayList<>();
    public static List<EstoquePastaDto> listFinalOp23 = new ArrayList<>();
    private List<EstoquePastaDto> listFinalOp2 = new ArrayList<>();
    private List<ReceitaInsumoDto> pastaProduzirOp1 = new ArrayList<>();
    public static List<ReceitaInsumoDto> pastaProduzirOp2 = new ArrayList<>();
    public static List<ReceitaInsumoDto> pastaProduzirOp22 = new ArrayList<>();
    private List<ReceitaInsumoDto> pastaEstoque = new ArrayList<>();
    private List<Integer> verificaID = new ArrayList<>();
    public static List<InsumoDto> insumosOp2 = new ArrayList<>();

    private boolean cofirmaProduzirOp2 = true, confirmaUrl = true, confirmaListTempX = true;
    private String componenteDeletado = null, insumosDeletados, consultarMin = ") as tb1 order by tb1.vencimento";
    private double usadoDaPastaEstoque, pesoRestantePastaProduzir, valorReaproveitado;
    public static String valorReap, custoProd;
    private int proximaPastaEstoque = 0;

    public EstoquePastaFinalDao() {
        this.conexao = ModuloConexao.conector();
    }

    //Busca os insumos que compoem a pasta (pode ser do estoque ou que queremos produzir) true: Produzir  false: Estoque
    public void buscarInsumos(int codigo, double quantidade, boolean confirma) {
        String sql = "select i.codigo, i.descricao, i.quantidade, i.UM, ri.consumo from tbReceitaInsumo as ri"
                + " inner join tbinsumos as i on i.codigo = ri.codigoInsumo"
                + " inner join tbTipoInsumo as ti on ti.codigo = i.codigoTipoInsumo"
                + " where ri.codigoReceita = '" + codigo + "'"
                + " order by ti.ordenacao";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (confirma == true) {
                while (rs.next()) {
                    setaPastaProduzir(rs, quantidade);
                }
            } else {
                while (rs.next()) {
                    setaPastaEstoque(rs, quantidade);
                }
            }

            pst.close();
            //printInfo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }
    //seta o List pastaProduzir, com os dados do formulário

    private void setaPastaProduzir(ResultSet rs, double quantidade) throws SQLException {
        ReceitaInsumoDto recInsDto = new ReceitaInsumoDto();
        recInsDto.setCodigoInsumo(rs.getInt(1));
        recInsDto.setConsumo(this.util.formatador6(this.util.conversaoUMInsumos(rs.getString(4), rs.getDouble(5) * 100, quantidade)));
        recInsDto.setUm(rs.getString(4));
        this.pastaProduzirOp1.add(recInsDto);
    }

    //seta o List pastaEstoque, com os dados contidos no List listTemp
    private void setaPastaEstoque(ResultSet rs, double quantidade) throws SQLException {
        ReceitaInsumoDto recInsDto = new ReceitaInsumoDto();
        recInsDto.setCodigoInsumo(rs.getInt(1));
        recInsDto.setConsumo(this.util.formatador6(this.util.conversaoUMInsumos(rs.getString(4), rs.getDouble(5) * 100, quantidade)));
        recInsDto.setUm(rs.getString(4));
        this.pastaEstoque.add(recInsDto);
    }

    //verifica as pastas compatíveis com a receita desejada 
    public void pastaCompativel(String insumos) {
        boolean confirma;
        String sql = "select tb1.codigo, tb1.descricao, tb1.quantidade, tb1.vencimento, tb1.ID"
                + " from ("
                + " select distinct tb.receita as codigo, r.descricao as descricao, ep.quantidade as quantidade, ep.dataVencimento as vencimento, ep.ID as ID"
                + " from ("
                + "    SELECT codigoReceita as receita"
                + "    FROM tbReceitaInsumo"
                + " where codigoInsumo in(" + insumos + ")"
                + " ) as tb"
                + " inner join tbEstoquePasta as ep on ep.codigoReceita = tb.receita"
                + " inner join tbreceita as r on r.codigorec =  tb.receita"
                + " inner join tbReceitaInsumo as ri on ri.codigoReceita = tb.receita"
                + " where receita not in  ("
                + "    SELECT codigoReceita"
                + "    FROM tbReceitaInsumo"
                + "    where codigoReceita = tb.receita"
                + "    and codigoInsumo not in(" + insumos + ")"
                + " )"
                + " and quantidade > 0"
                + " " + this.consultarMin + ";";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                confirma = true;
                //esta estrutura verifica se o ID que está entrando já foi utilizado 
                for (int i = 0; i < this.verificaID.size(); i++) {
                    if (rs.getInt(5) == this.verificaID.get(i)) {
                        confirma = false;
                        break;
                    }
                }
                //se a resposta do ID for diferente de Falso e a quantidade em estoque da pasta for diferente de 0
                if ((confirma != false) && (rs.getInt(3) != 0) && (movEstDao.dataComparar(movEstDao.dataAtual(), rs.getString(4)) == false)) {
                    setaListTemp(rs);
                }
            }
            this.confirmaListTempX = false;
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setaListTemp(ResultSet rs) throws SQLException {
        EstoquePastaDto estPastDto = new EstoquePastaDto();
        estPastDto.setId(rs.getInt(5));
        estPastDto.setCodigo(rs.getInt(1));
        estPastDto.setDescricao(rs.getString(2));
        estPastDto.setEstoque(this.util.formatador6(rs.getDouble(3)));
        estPastDto.setVencimento(this.util.inverterData(rs.getString(4)).replace("-", "/"));

        this.listTemp.add(estPastDto);
        if (this.confirmaListTempX == true) {
            this.listTempX.add(estPastDto);
        }
    }

    //faz a subtração dos insumos das pastas, chamando os métodos necessarios para isso
    public void subtrairInsumos(int codigo) {
        //chama método para setar lista pastaEstoque com os insumos da pasta que esta na listaTemporária
        double quantDoInsumoEst = 0, porcentoTotal = 100, porcentoTemp, quantidadeConvertidaEst, quantidadeConvertidaPast;
        for (int i = 0; (!this.pastaProduzirOp1.isEmpty()) && (i < this.listTemp.size()); i++) {
            buscarInsumos(this.listTemp.get(i).getCodigo(), this.listTemp.get(i).getEstoque(), false);
            //entra dentro da lista pastaProduzir, para verificar compatibilidade dos insumos
            for (int in = 0; in < this.pastaProduzirOp1.size(); in++) {
                //entra dentro da pastaProduzir para varrer os insumos para verificar a compatibilidade
                for (int ind = 0; ind < this.pastaEstoque.size(); ind++) {
                    //verifica compatibilidade dos insumos através do código dos insumos
                    if (this.pastaProduzirOp1.get(in).getCodigoInsumo() == this.pastaEstoque.get(ind).getCodigoInsumo()) {
                        quantidadeConvertidaPast = this.util.conversaoUMparaKG(this.pastaProduzirOp1.get(in).getUm(), this.pastaProduzirOp1.get(in).getConsumo());
                        quantidadeConvertidaEst = this.util.conversaoUMparaKG(this.pastaEstoque.get(ind).getUm(), this.pastaEstoque.get(ind).getConsumo());
                        //verifica a % que pode ser usada em determinado insumo
                        porcentoTemp = this.util.formatador6(this.util.regraDeTres1(quantidadeConvertidaPast, quantidadeConvertidaEst));
                        //se a porcentagem temporária for maior que a Atual então é realizado a troca
                        if (porcentoTemp <= porcentoTotal) {
                            porcentoTotal = porcentoTemp;
                        }
                        break;
                    }
                }
            }
            //Recalcula o maximo a ser usada de todos os insumos da pasta estoque, com base na % total que this.util.formatador3(this.util.regraDeTres2(porcentoTotal, this.pastaEstoque.get(inde).getConsumo()))pode ser uasda
            for (int inde = 0; inde < this.pastaEstoque.size(); inde++) {
                this.pastaEstoque.get(inde).setConsumo(this.util.formatador6(this.util.regraDeTres2(porcentoTotal, this.pastaEstoque.get(inde).getConsumo())));
            }
            //printInfo();
            //chama o metodo que ira fazer a subtração
            subtrairPelaQuantidade();
            setaListFinal(usadoDaPastaEstoque, i);
            //chama o metodo para verificar se o insumo é menor ou igual a "0"
            if (verificarArray(pastaProduzirOp1) == true) {
                this.listTemp.clear();
                //se a lista for diferente de vazio, então chama o metodo para refazer a pesquisa com base nos insumos deletados
                if (!this.pastaProduzirOp1.isEmpty()) {
                    pastaCompativel(this.insumosDeletados);
                    //buscarInsumos(codigo, this.quantidade, true);
                    i = -1;
                }
            }
            porcentoTotal = 100;
        }
        if (this.cofirmaProduzirOp2 == true) {
            verificarArray(pastaProduzirOp1);
        }
    }

    // seta a listfinal com os dados da pasta do estoque que foi usada 
    private void setaListFinal(double usadoDaPastaEstoque, int iT) {
        this.verificaID.add(this.listTemp.get(iT).getId());
        this.listTemp.get(iT).setUsar(this.util.formatador6(usadoDaPastaEstoque));
        double porcent = this.util.formatador6(this.util.regraDeTres1(usadoDaPastaEstoque, Double.parseDouble(TelaEstoquePasta.txtQuantidade.getText().replace(".", "").replace(",", "."))));
        //verifica se a % ultrapassa os 100%, caso isso acontece o objeto recebe 100%
        if (porcent > 100) {
            this.listTemp.get(iT).setEquivalencia(100);
        } else {
            this.listTemp.get(iT).setEquivalencia(porcent);
        }

        this.listFinalOp1.add(this.listTemp.get(iT));
        this.pastaEstoque.clear();
        this.usadoDaPastaEstoque = 0;
    }

    //seta a tabela que vem por parâmetro
    public void setarTabelaOp1(JTable tabela) {
        double porcentoTotal = 0;

        for (int i = 0; i < this.listFinalOp1.size(); i++) {
            porcentoTotal += this.listFinalOp1.get(i).getEquivalencia();
        }
        if (porcentoTotal < 99.98) {
            if (this.proximaPastaEstoque <= this.listTempX.size()) {

                //aqui a baixo minha nova lógica
                limparVariaveis(false);
                for (int i = this.proximaPastaEstoque; i < this.listTempX.size(); i++) {
                    this.listTemp.add(this.listTempX.get(i));
                }
                buscarInsumos(Integer.parseInt(TelaEstoquePasta.txtCodigo.getText()), Double.parseDouble(TelaEstoquePasta.txtQuantidade.getText().replace(".", "").replace(",", ".")), true);
                subtrairInsumos(Integer.parseInt(TelaEstoquePasta.txtCodigo.getText()));
                this.proximaPastaEstoque++;
                setarTabelaOp1(TelaEstoquePasta.tblProducaoPastaOp1);
            } else {
                limparTblOp1();
            }
        } else {
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            modelo.setNumRows(0);

            for (int i = 0; i < this.listFinalOp1.size(); i++) {
                    this.listFinalOp23.add(this.listFinalOp1.get(i));
                modelo.addRow(new Object[]{
                    this.listFinalOp1.get(i).getId(),
                    this.listFinalOp1.get(i).getCodigo(),
                    this.listFinalOp1.get(i).getDescricao(),
                    this.util.formatadorQuant6(this.listFinalOp1.get(i).getEstoque()) + " kg",
                    this.util.formatadorQuant6(this.listFinalOp1.get(i).getUsar()) + " kg",
                    this.util.formatadorQuant(this.listFinalOp1.get(i).getEquivalencia()) + "%",
                    this.listFinalOp1.get(i).getVencimento()
                });
                valorReaproveitado(this.listFinalOp1.get(i).getCodigo(), this.listFinalOp1.get(i).getUsar());
            }
            TelaEstoquePasta.lblValorReaproveitadoOpc1.setText(this.util.formatadorQuant(this.valorReaproveitado));
            this.valorReap = this.util.formatadorQuant(this.valorReaproveitado);
            this.valorReaproveitado = 0;
            ativarTblOp1();
        }
    }

//Faz a produção da receita, dando update nas pasta da opc1
    public boolean produzirOpc1() {
        boolean retorno = true;
        for (int i = 0; i < this.listFinalOp1.size(); i++) {
            if (updatePastaEstoque(this.listFinalOp1.get(i).getId(), this.listFinalOp1.get(i).getUsar()) == true) {
            } else {
                retorno = false;
            }
        }
        this.confirmaListTempX = true;
        this.listTempX.clear();
        return retorno;
    }

    //metodo que seta movDto para atualizar a tabela de movimentação seta com os dados da pasta
    private void movimentarPastasOpc(List<EstoquePastaDto> estPasDto, String entradaSaida, int i) {
        this.movDto.setTipo("Pasta");
        this.movDto.setCodigoID(estPasDto.get(i).getCodigo());
        this.movDto.setDescricao(estPasDto.get(i).getDescricao());
        this.movDto.setQuantidade(entradaSaida + estPasDto.get(i).getUsar());
        this.movDto.setData(this.util.dataAtual());

        this.movDao.movimentacao(movDto);
    }

    //metodo que seta movDto para atualizar a tabela de movimentação, seta com os dados dos insumos
    private void movimentarInsumosOpc(List<ReceitaInsumoDto> recInsDto, String entradaSaida, int i) {
        this.movDto.setTipo("Insumo");
        this.movDto.setCodigoID(this.insumosOp2.get(i).getCodigo());
        this.movDto.setDescricao(this.insumosOp2.get(i).getDescricao());
        this.movDto.setQuantidade(entradaSaida + this.util.formatador6(recInsDto.get(i).getConsumo()));
        this.movDto.setData(this.util.dataAtual());

        this.movDao.movimentacao(movDto);
    }

    //Faz a produção da receita, dando update na pasta/ insumos da opc2
    public boolean produzirOpc2() {
        boolean retorno = true;
        if (this.listFinalOp2.size() > 0) {
            if (updatePastaEstoque(this.listFinalOp2.get(0).getId(), this.listFinalOp2.get(0).getUsar()) == true) {

            } else {
                retorno = false;
            }
        }
        for (int i = 0; i < this.insumosOp2.size(); i++) {
            if (this.insumoDao.saidaInsumo(this.pastaProduzirOp2.get(i).getConsumo(), this.insumosOp2.get(i).getCodigo()) == true) {
            } else {
                retorno = false;
            }
        }
        this.confirmaListTempX = true;
        this.listTempX.clear();
        return retorno;
    }

    //da update na tabela de PastaEstoque
    private boolean updatePastaEstoque(int ID, Double quantidade) {
        boolean confirma = true;
        String sql = "update tbEstoquePasta set quantidade = (quantidade - " + quantidade + ") where ID = " + ID + "";
        PreparedStatement pst;
        try {
            pst = conexao.prepareStatement(sql);
            ID = pst.executeUpdate();
            if (ID > 0) {
            } else {
                confirma = false;
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
        return confirma;
    }

    private void valorReaproveitado(int codigoReceita, double quantidade) {
        this.valorReaproveitado += quantidade * buscaCustoReceita(codigoReceita);
    }

    //busca o custo por kg de determinada receita através do código
    private double buscaCustoReceita(int codigoReceita) {
        String sql = "select custoPorKg from tbReceitaInsumo where codigoReceita = " + codigoReceita + "";
        PreparedStatement pst;
        double custo = 0;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                custo += rs.getDouble(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return custo;
    }

    private void limparTblOp1() {
        TelaEstoquePasta.tblProducaoPastaOp1.setVisible(false);
        TelaEstoquePasta.tblProducaoPastaOp1.setEnabled(false);
        TelaEstoquePasta.btnVisualizarOp1.setEnabled(false);
        TelaEstoquePasta.btnVisualizarOp1.setForeground(new Color(201, 201, 201));
    }

    private void ativarTblOp1() {
        TelaEstoquePasta.tblProducaoPastaOp1.setVisible(true);
        TelaEstoquePasta.tblProducaoPastaOp1.setEnabled(true);
        TelaEstoquePasta.btnVisualizarOp1.setEnabled(true);
        TelaEstoquePasta.btnVisualizarOp1.setForeground(new Color(66, 66, 66));
    }

    private void avitarTblOp2() {
        TelaEstoquePasta.tblProducaoPastaOp2.setEnabled(true);
        TelaEstoquePasta.tblProducaoPastaOp2.setVisible(true);
        TelaEstoquePasta.btnVisualizarOp2.setEnabled(true);
    }

    //seta a tabelaOp2
    public void setarTabelaOp2(JTable tabela) {
        double custoProducao = 0;
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setNumRows(0);

        // verifica se a lista nestá vazia, de pois seta a tabela com os dados da pasta com menor validade 
        if (!this.listFinalOp1.isEmpty()) {
            this.listFinalOp22.add(this.listFinalOp1.get(0));
            this.listFinalOp2.add(this.listFinalOp1.get(0));
            //tabela recebe o primeiro objeto da listFinalOp1
            modelo.addRow(new Object[]{
                this.listFinalOp1.get(0).getId(),
                this.listFinalOp1.get(0).getCodigo(),
                this.listFinalOp1.get(0).getDescricao(),
                this.util.formatadorQuant6(this.listFinalOp1.get(0).getEstoque()) + " kg",
                this.util.formatadorQuant6(this.listFinalOp1.get(0).getUsar()) + " kg",
                this.util.formatadorQuant(this.listFinalOp1.get(0).getEquivalencia()) + "%",
                this.listFinalOp1.get(0).getVencimento()
            });
            valorReaproveitado(this.listFinalOp1.get(0).getCodigo(), this.listFinalOp1.get(0).getUsar());
        }
        // seta a tabela com os dados dos insumos faltantes
        for (int i = 0; i < this.insumosOp2.size(); i++) {
            custoProducao += this.pastaProduzirOp2.get(i).getConsumo() * Double.parseDouble(this.insumosOp2.get(i).getPreco());
            modelo.addRow(new Object[]{
                "Insumo",
                this.insumosOp2.get(i).getCodigo(),
                this.insumosOp2.get(i).getDescricao(),
                this.util.formatadorQuant6(Double.parseDouble(this.insumosOp2.get(i).getQuantidade())) + " " + this.insumosOp2.get(i).getUM(),
                this.util.formatadorQuant6(this.pastaProduzirOp2.get(i).getConsumo()) + " " + this.insumosOp2.get(i).getUM(),
                this.util.formatadorQuant(this.util.formatador6(this.util.regraDeTres1(this.util.conversaoUMparaKG(this.insumosOp2.get(i).getUM(), this.pastaProduzirOp2.get(i).getConsumo()), Double.parseDouble(TelaEstoquePasta.txtQuantidade.getText().replace(".", "").replace(",", "."))))) + "%",
                ""
            });
        }
        avitarTblOp2();
        TelaEstoquePasta.lblValorReaproveitadoOpc2.setText(this.util.formatadorQuant(this.valorReaproveitado));
        this.valorReap = this.util.formatadorQuant(this.valorReaproveitado);
        this.valorReaproveitado = 0;
        TelaEstoquePasta.lblCustoProducao.setText(this.util.formatadorQuant(custoProducao));
        this.custoProd = this.util.formatadorQuant(custoProducao);
        mudarCorLinha.CorNaLinhaQuantidade(tabela);
    }

    //busca os insumos faltante pra compar a opção 2
    private void buscaInsumosFaltantes(String url) {
        String sql = "select i.codigo, i.descricao, i.UM, i.quantidade, i.preco from tbinsumos as i"
                + " inner join tbTipoInsumo as ti on ti.codigo = i.codigoTipoInsumo"
                + " where i.codigo in(" + url + ")"
                + " order by ti.ordenacao";
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                InsumoDto insuDto = new InsumoDto();
                insuDto.setCodigo(rs.getInt(1));
                insuDto.setDescricao(rs.getString(2));
                insuDto.setUM(rs.getString(3));
                insuDto.setQuantidade(rs.getString(4));
                insuDto.setPreco(rs.getString(5));

                this.insumosOp2.add(insuDto);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //subtrai os insumos baseados na porcentagem máxima permitida da determinada pasta do estoque
    private boolean subtrairPelaQuantidade() {
        double totalUsado = 0, totalTemp;
        boolean confirma = false;
        //Subtrai direto do array pastaProduzir
        for (int i = 0; i < this.pastaProduzirOp1.size(); i++) {
            for (int in = 0; in < this.pastaEstoque.size(); in++) {
                //verifica se os insumos são compatíveis
                if (this.pastaProduzirOp1.get(i).getCodigoInsumo() == this.pastaEstoque.get(in).getCodigoInsumo()) {
                    //faz a subtração do insumo que queremos produzir pelo insumo da pasta em estoque
                    double insuProd = this.util.conversaoUMparaKG(this.pastaProduzirOp1.get(i).getUm(), this.pastaProduzirOp1.get(i).getConsumo());
                    double insuEstoq = this.util.conversaoUMparaKG(this.pastaEstoque.get(in).getUm(), this.pastaEstoque.get(in).getConsumo());

                    totalTemp = insuProd - insuEstoq;
                    totalUsado = insuProd - totalTemp;
                    this.usadoDaPastaEstoque += totalUsado;
                    if (totalTemp < 0.001) {
                        totalTemp = 0;
                        confirma = true;
                    }
                    this.pastaProduzirOp1.get(i).setConsumo(this.util.conversaoKGparaUM(this.pastaProduzirOp1.get(i).getUm(), totalTemp));
                    break;
                }
            }
        }
        return confirma;
    }

    //se quantidade do insumo for menor  ou igual a "0", ele retira o insumo da lista
    private boolean verificarArray(List<ReceitaInsumoDto> produzir) {
        boolean retorno = false;
        this.pesoRestantePastaProduzir = 0;
        for (int i = 0; i < produzir.size(); i++) {
            //variavel recebe a soma do restante dos insumos, gerando assim o peso restante da pasta
            this.pesoRestantePastaProduzir += this.util.formatador6(this.util.conversaoUMparaKG(produzir.get(i).getUm(), produzir.get(i).getConsumo()));
            //verifica se é = "0", se for verdadeiro o insumo será eliminado da lista
            if (produzir.get(i).getConsumo() <= 0.001) {
                this.componenteDeletado += produzir.get(i).getCodigoInsumo() + ",";
                produzir.remove(i);
                retorno = true;
                i--;
            }
        }
        this.pastaProduzirOp1 = produzir;
        setarUrlInsu();
        setaPastaProduzirOp2();
        return retorno;
    }

    //seta o list da pastaproduzir2 através da pastaProduzir1 já alterada apos a primeira pasta do estoque 
    private void setaPastaProduzirOp2() {
        if (this.cofirmaProduzirOp2 == true) {
            for (int i = 0; i < this.pastaProduzirOp1.size(); i++) {
                ReceitaInsumoDto recInsDto = new ReceitaInsumoDto();
                recInsDto.setId(this.pastaProduzirOp1.get(i).getId());
                recInsDto.setCodigoReceita(this.pastaProduzirOp1.get(i).getCodigoReceita());
                recInsDto.setCodigoInsumo(this.pastaProduzirOp1.get(i).getCodigoInsumo());
                recInsDto.setUm(this.pastaProduzirOp1.get(i).getUm());
                recInsDto.setConsumo(this.pastaProduzirOp1.get(i).getConsumo());
                this.pastaProduzirOp2.add(recInsDto);
                this.pastaProduzirOp22.add(recInsDto);
                this.cofirmaProduzirOp2 = false;

            }
        }
    }

    //cria uma nova URL com com os códigos dos insumos que ainda não foram zerados
    private void setarUrlInsu() {
        String insu = null;
        for (int i = 0; (!this.pastaProduzirOp1.isEmpty()) && (i < this.pastaProduzirOp1.size()); i++) {
            insu += this.pastaProduzirOp1.get(i).getCodigoInsumo() + ",";
        }
        if (insu != null) {
            this.insumosDeletados = insu.substring(0, insu.length() - 1);
            this.insumosDeletados = this.insumosDeletados.replace("null", "");
        }
        if (this.confirmaUrl == true) {
            buscaInsumosFaltantes(this.insumosDeletados);
            this.confirmaUrl = false;
        }
    }

    //busca os códigos dos insumos contidos na Receita que deseja produzir, e retorna em forma de uma String(URL)
    public String buscaCodigoInsumos(int codigo) {
        String sql = "select ri.codigoInsumo from tbReceitaInsumo as ri"
                + " where ri.codigoReceita = '" + codigo + "'";

        PreparedStatement pst;
        //List<Integer> lista = new ArrayList<>();
        String insumos = null;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                insumos += rs.getString(1) + ",";
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        String modificada = insumos.substring(0, insumos.length() - 1);
        modificada = modificada.replace("null", "");
        return modificada;
    }

//    /**
//     * Busca o maior consumo entre os insumos da minha pasta em estoque e
//     * retorna o valor em kg do aproveitamento máximo da minha pasta em estoque.
//     * Atualiza a lista
//     */
//    private int maxInsumoEstoqueUsar(int codigo) {
//        String sql = "select ri.codigoInsumo, max(ri.consumo) as total  from tbReceitaInsumo as ri"
//                + " where ri.codigoReceita = " + codigo;
//        PreparedStatement pst;
//        int insumo = 0;
//        try {
//            pst = this.conexao.prepareStatement(sql);
//            ResultSet rs = pst.executeQuery();
//            if (rs.next()) {
//                insumo = rs.getInt(1);
//
//            }
//            pst.close();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//        return insumo;
//    }
    //busca o insumo e o consumo 
    //MOSTRA AS INFORMAÇÕES ATRAVÉS DO SYSTEM.OUT
//    public void printInfo() {
//
//        System.out.println("PASTA MAIS ANTIGA COMPATÍVEL: ");
//        System.out.println("ID: " + this.listTemp.get(0).getId());
//        System.out.println("CÓDIGO: " + this.listTemp.get(0).getCodigo());
//        System.out.println("DESCRIÇÃO: " + this.listTemp.get(0).getDescricao());
//        System.out.println("ESTOQUE: " + this.listTemp.get(0).getEstoque());
//        System.out.println("USAR: " + this.listTemp.get(0).getUsar());
//        System.out.println("EQUIVALÊNCIA %: " + this.listTemp.get(0).getEquivalencia());
//        System.out.println("VENCIMENTO: " + this.listTemp.get(0).getVencimento());
//
//        System.out.println("INSUMO DA PASTA MAIS ANTIGA: ");
//        for (int i = 0; i < this.pastaEstoque.size(); i++) {
//            System.out.print("ID: " + this.pastaEstoque.get(i).getId() + ", ");
//            System.out.print("INSUMO: " + this.pastaEstoque.get(i).getCodigoInsumo() + ", ");
//            System.out.print("UM: " + this.pastaEstoque.get(i).getUm() + ", ");
//            System.out.println("CONSUMO: " + this.pastaEstoque.get(i).getConsumo());
//        }
//
//        System.out.println("INSUMO DA PASTA QUE DESEJO PRODUZIR: ");
//        for (int i = 0; i < this.pastaProduzirOp1.size(); i++) {
//            System.out.print("ID: " + this.pastaProduzirOp1.get(i).getId() + ", ");
//            System.out.print("INSUMO: " + this.pastaProduzirOp1.get(i).getCodigoInsumo() + ", ");
//            System.out.print("UM: " + this.pastaProduzirOp1.get(i).getUm() + ", ");
//            System.out.println("CONSUMO: " + this.pastaProduzirOp1.get(i).getConsumo());
//        }
//    }
    //limpa as variaveis para iniciar uma nova busca 
    public void limparVariaveis(boolean confirma) {
        if (confirma == true) {
            this.listTempX.clear();
            this.confirmaListTempX = true;
            this.proximaPastaEstoque = 0;
        }
        this.listTemp.clear();
        this.listFinalOp1.clear();
        this.listFinalOp2.clear();
        this.pastaProduzirOp1.clear();
        this.pastaProduzirOp2.clear();
        this.pastaEstoque.clear();
        this.verificaID.clear();
        this.insumosOp2.clear();
        this.listFinalOp23.clear();

        this.confirmaUrl = true;
        this.cofirmaProduzirOp2 = true;
        this.insumosDeletados = null;
        this.componenteDeletado = null;
        this.consultarMin = ") as tb1 order by tb1.vencimento";
        this.usadoDaPastaEstoque = 0;
        this.pesoRestantePastaProduzir = 0;
        this.valorReaproveitado = 0;
    }
}
