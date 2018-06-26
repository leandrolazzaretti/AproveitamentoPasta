/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import conexao.ModuloConexao;
import java.sql.Connection;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Leandro
 */
public class Relatorio implements Serializable{
    Connection conexao; 
    private static final long serialVersionUID = 1L;
    
    private static final String FOLDER_RELATORIOS = "/Report";
    private String caminhoArquivoRelatorio = null;
    private JRExporter exporter = null;
    private File arquivoGerado = null;
    
    public Relatorio(){
        this.conexao = ModuloConexao.conector();
    }
    
    public String gerarRelatorio(HashMap parametrosRelatorio,
            String nomeRelatorioJasper, String tipoExportar) throws Exception {
 
        String caminhoRelatorio = this.getClass()
                .getResource(FOLDER_RELATORIOS).getPath();
 
        /* caminho completo até o relatório compilado indicado */
        String caminhoArquivosJasper = caminhoRelatorio + File.separator
                + nomeRelatorioJasper + ".jasper";
 
        /* Faz o carregamento do relatório */
        JasperReport relatorioJasper = (JasperReport) JRLoader
                .loadObjectFromFile(caminhoArquivosJasper);
 
        /* Carrega o arquivo */
        JasperPrint impressoraJasper = JasperFillManager.fillReport(
                relatorioJasper, parametrosRelatorio, this.conexao);
 
        if (tipoExportar.equalsIgnoreCase("pdf")) {
            exporter = new JRPdfExporter();
        } else if (tipoExportar.equalsIgnoreCase("xls")) {
            exporter = new JRXlsExporter();
        }
 
        /* Caminho relatorio exportado */
        caminhoArquivoRelatorio = caminhoRelatorio + File.separator
                + nomeRelatorioJasper + "." + tipoExportar;
 
        /* Criar novo arquivos exportado */
 
        arquivoGerado = new File(caminhoArquivoRelatorio);
 
        /* Prepara a impressão */
        exporter.setParameter(JRExporterParameter.JASPER_PRINT,
                impressoraJasper);
 
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
 
        /* Excuta a exportação */
        exporter.exportReport();
 
        return caminhoArquivoRelatorio;
 
    }
    
//    public void gerarRelatorio(List<UsuarioDto> lista) throws JRException{
//    
//        InputStream fonte = this.getClass().getResourceAsStream("/Report/Usuarios.jrxml");
//        
//        //String fonte = "C:\\Users\\Leandro\\Documents\\NetBeansProjects\\prjAproveitamentoPastas\\src\\Report\\Usuarios.jrxml";
//        
//        JasperReport report = JasperCompileManager.compileReport(fonte);
//        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(lista));
//        JasperViewer.viewReport(print, false);
//    }
}
