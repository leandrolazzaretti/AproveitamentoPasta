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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import util.HashUtil;

/**
 *
 * @author Leandro
 */
public class UsuarioDao {

    Connection conexao = null;

    public UsuarioDao() {
        this.conexao = ModuloConexao.conector();
    }

    public void adicionar(UsuarioDto usuario) {
        String sql = "insert into tbusuarios(codigo,nome,login,senha,perfil) values(?,?,?,?,?)";
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, usuario.getCodigo());
            pst.setString(2, usuario.getNome());
            pst.setString(3, usuario.getLogin());
            pst.setString(4, HashUtil.stringMD5(usuario.getSenha()));
            pst.setString(5, usuario.getPerfil());

            //Atualiza a tabela usuarios
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
            }
            pst.close();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, sqle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void atualizar(UsuarioDto usuario, int codigo) {
        String sql = "update tbusuarios set nome=?, senha=?, perfil=? where codigo=?";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, usuario.getNome());
            pst.setString(2, HashUtil.stringMD5(usuario.getSenha()));
            pst.setString(3, usuario.getPerfil());
            pst.setInt(4, codigo);

            //Atualiza a tabela usuarios
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Usuário Atualizado com sucesso!");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void deletar(int codigo) {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            String sql = "delete from tbusuarios where codigo=?";

            PreparedStatement pst;

            try {
                pst = this.conexao.prepareStatement(sql);
                pst.setInt(1, codigo);
                int deleta = pst.executeUpdate();
                //confirma o deleta
                if (deleta > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
                }
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }

    public List<UsuarioDto> list() {

        String sql = "select * from tbusuarios ";

        List<UsuarioDto> lista = new ArrayList<>();
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                UsuarioDto usuario = new UsuarioDto();

                usuario.setCodigo(rs.getInt("codigo"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));

                lista.add(usuario);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return lista;
    }
}
