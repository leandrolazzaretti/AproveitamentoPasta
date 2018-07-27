/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Leandro
 */
public class MascaraMoeda3 extends PlainDocument {

    public static final int NUMERO_DIGITOS_MAXIMO = 16;

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        String texto = getText(0, getLength());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c)) {
                return;
            }
        }

        if (texto.length() < this.NUMERO_DIGITOS_MAXIMO) {
            super.remove(0, getLength());
            texto = texto.replace(".", "").replace(",", "");
            StringBuffer s = new StringBuffer(texto + str);

            if (s.length() > 0 && s.charAt(0) == '0') {
                s.deleteCharAt(0);
            }

            if (s.length() < 4) {
                if (s.length() < 1) {
                    s.insert(0, "0000");
                } else if (s.length() < 2) {
                    s.insert(0, "000");
                } else {
                    if (s.length() < 3) {
                        s.insert(0, "00");
                    } else {
                        s.insert(0, "0");
                    }
                }
            }

            s.insert(s.length() - 3, ",");

            if (s.length() > 7) {
                s.insert(s.length() - 7, ".");
            }

            if (s.length() > 11) {
                s.insert(s.length() - 11, ".");
            }

            if (s.length() > 15) {
                s.insert(s.length() - 15, ".");
            }
            super.insertString(0, s.toString(), a);
        }
    }

    public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);
        String texto = getText(0, getLength());
        texto = texto.replace(",", "");
        texto = texto.replace(".", "");
        super.remove(0, getLength());
        insertString(0, texto, null);
    }

}
