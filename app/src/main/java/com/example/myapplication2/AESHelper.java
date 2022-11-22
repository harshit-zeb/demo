package com.example.myapplication2;

import android.util.Base64;

import java.net.URLEncoder;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by mehul on 4/14/17.
 */

public class AESHelper {

    private static final String CIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";


    private AESHelper() {
        throw new IllegalStateException("Utility class");
    }

    /****
     *This method is used to encrypt the pin using api secret PREF_AES
     * @return
     */
    public static String encrypt(String plain, String secreatKey) {
        try {
            //App current version is greater >= fire base version setting android pin reset
            // version code

            // Edited by gitika to make  secret key 16 char long as per server logic.
            int len = 0;
            if (secreatKey != null)
                len = secreatKey.length();
            if (len < 16) {
                StringBuilder zeros = new StringBuilder();
                for (int i = 1; i <= 16 - len; i++) {
                    zeros.append("0");
                }
                secreatKey += zeros.toString();
            }
                secreatKey = secreatKey.replaceAll("[^=a-zA-Z0-9]", "").replace(" ", "");
                Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
                byte[] key = Arrays.copyOf(secreatKey.getBytes(CHARSET), 16);
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
                byte[] enc = cipher.doFinal(plain.getBytes(CHARSET));
                return URLEncoder.encode(Base64.encodeToString(enc, Base64.DEFAULT).trim(),
                        "utf-8");

        } catch (Exception e) {
            return plain;
        }
    }


    /****
     *This method is used to encrypt the pin without URL encoder
     * @return
     */
    public static String encryptPinForNewApiServer(String plain, String secreatKey) {
        try {
            secreatKey = secreatKey.replaceAll("[^=a-zA-Z0-9]", "").replace(" ", "");
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            byte[] key = Arrays.copyOf(secreatKey.getBytes(CHARSET), 16);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
            byte[] enc = cipher.doFinal(plain.getBytes(CHARSET));
            return Base64.encodeToString(enc, Base64.DEFAULT).trim();
        } catch (Exception e) {
            return plain;
        }
    }


    /****
     *This method is used to decrypt the decrypt the encoded text message
     * which is come while zebpay.Application.ui.user request for forgot pin & decrypted code
     * will call verifysecurecode api
     */
    public static String decrypt(String encoded, String secreatKey) {
        try {

            // Edited by gitika to make  secret key 16 char long as per server logic.
            int len = 0;
            if (secreatKey != null)
                len = secreatKey.length();
            if (len < 16) {
                StringBuilder zeros = new StringBuilder();
                for (int i = 1; i <= 16 - len; i++) {
                    zeros.append("0");
                }

                secreatKey += zeros.toString();
            }

            encoded = encoded.replace(" ", "+");
            //App current version is greater >= fire base version setting android pin reset
            // version code

                secreatKey = secreatKey.replaceAll("[^=a-zA-Z0-9]", "").replace(" ", "");
                byte[] key = Arrays.copyOf(secreatKey.getBytes(CHARSET), 16);
                Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
                byte[] dnc = cipher.doFinal(Base64.decode(encoded, Base64.DEFAULT));
                String dncryText = new String(dnc);
                return URLEncoder.encode(dncryText, "utf-8");

        } catch (Exception e) {
            return encoded;
        }
    }
}
