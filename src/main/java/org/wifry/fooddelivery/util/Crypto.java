package org.wifry.fooddelivery.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Ejemplo de encriptado y desencriptado con algoritmo AES.
 * Se apoya en RSAAsymetricCrypto.java para salvar en fichero
 * o recuperar la clave de encriptacion. AES/ECB/PKCS5Padding
 *
 * @author Alfredo
 */
public final class Crypto {

    private static String keyStr = "ABCB28BCEE5947E8AFCE3386871EC0DA";

    public static String encrypt(String texto) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            Key key = new SecretKeySpec(keyStr.getBytes(), 0, 16, "AES");
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes.init(Cipher.ENCRYPT_MODE, key);
            byte[] encriptado = new byte[0];
            encriptado = aes.doFinal(texto.getBytes());
            return javax.xml.bind.DatatypeConverter.printHexBinary(encriptado);
        } catch (IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String textoe) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            Key key = new SecretKeySpec(keyStr.getBytes(), 0, 16, "AES");
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] b = javax.xml.bind.DatatypeConverter.parseHexBinary(textoe);
            aes.init(Cipher.DECRYPT_MODE, key);
            byte[] desencriptado = aes.doFinal(b);
            return new String(desencriptado);
        } catch (IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

}
