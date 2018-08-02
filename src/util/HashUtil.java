package util;


import java.math.BigInteger;
import java.security.MessageDigest;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Leandro
 */
public class HashUtil {
    
    public static String stringMD5(String string){
        String md5 = null;
        if (string == null) {
            return null;
        }
        
            //Criando objeto MessageDigest para o MD5
            MessageDigest digest; 
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(string.getBytes(), 0, string.length());
            //Convertendo para hexa
            md5 = new BigInteger(1, digest.digest()).toString(16);            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return md5;
    }
}
