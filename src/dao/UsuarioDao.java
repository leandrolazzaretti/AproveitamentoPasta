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

    public UsuarioDao() {
        this.conexao = ModuloConexao.conector();
    }

    public List<UsuarioDto> list() {

        String sql = "select * from tbusuarios ";

        List<UsuarioDto> lista = new ArrayList<>();

        try {
            this.pst = this.conexao.prepareStatement(sql);
            this.rs = this.pst.executeQuery();

            while (this.rs.next()) {

                UsuarioDto usuario = new UsuarioDto();

                usuario.setIduser(this.rs.getInt("codigo"));
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

//    public void adicionar(UsuarioDto usuario) {
//        String sql = "insert into tbusuarios(codigo,nome,login,senha,perfil) values(?,?,?,?,?)";
//
//        try {
//            this.pst = this.conexao.prepareStatement(sql);
//            this.pst.setInt(1, usuario.getIduser());
//            this.pst.setString(2, usuario.getNome());
//            this.pst.setString(3, usuario.getLogin());
//            this.pst.setString(4, usuario.getSenha());
//            this.pst.setString(5, usuario.getPerfil());
//            this.pst.executeUpdate();
//
//            //Validação dos campos obirgatórios
//            if ((this.txtCadUsuId.getText().isEmpty()) || (this.txtCadUsuNome.getText().isEmpty()) || (this.txtCadUsuLogin.getText().isEmpty()) || (this.txtCadUsuSenha.getText().isEmpty())) {
//                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
//            } else {
//                //Atualiza a tabela usuarios
//                int adicionado = this.pst.executeUpdate();
//                //Linha abaixo serve de apoio
//                //System.out.println(adicionado);
//                //confirma se realmente foi atualizada
//                if (adicionado > 0) {
//                    JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
//                    limparCampos();
//                }
//            }
//        } catch (SQLException sqle) {
//            JOptionPane.showMessageDialog(null, sqle);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//            System.out.println(e);
//        }
//    }
//}
