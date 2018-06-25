/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import conexao.ModuloConexao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Leandro
 */
public class Jasper {
    Connection conexao = null;
    
   public Jasper(){
       this.conexao = ModuloConexao.conector();
   }
   
   public void relatorioUsuario(){
       try {
           //correga jrxml file usando stream
           InputStream inputStream = new FileInputStream((new File("C:\\Users\\Leandro\\Documents\\NetBeansProjects\\prjAproveitamentoPastas\\src\\Report\\Usuario.jrxml")));
           // compila jrxml file
           JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
           JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
           
           // cria jasper print objetct
           Map<String,String> map = new HashMap<>();
           map.put("jasper report","Testee Report");
           
           //preencher o relatório
           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, this.conexao);
           
           //exporta o report
           OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\Leandro\\Documents\\NetBeansProjects\\prjAproveitamentoPastas\\src\\Report\\Testeeee.pdf"));
           
           
           JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
           
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
       }
   }
}
