/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.ModuloConexao;
import dto.UsuarioDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Leandro
 */
public class UsuarioDao {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public UsuarioDao(){
        this.conexao = ModuloConexao.conector();
    }
    
    public List<UsuarioDto> list() {

        String sql = "select * from tbusuarios ";

        List<UsuarioDto> lista = new ArrayList<>();

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.rs = this.pst.executeQuery();
            
            while(this.rs.next()){
                
                UsuarioDto usuario = new UsuarioDto();
                
                usuario.setIduser(this.rs.getInt("iduser"));
                usuario.setNome(this.rs.getString("nome"));
                usuario.setLogin(this.rs.getString("login"));
                usuario.setSenha(this.rs.getString("senha"));
                usuario.setPerfil(this.rs.getString("perfil"));
                
                lista.add(usuario);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return lista;
    }
}
