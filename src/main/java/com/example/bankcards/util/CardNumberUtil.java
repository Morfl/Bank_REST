package com.example.bankcards.util;

import com.example.bankcards.exception.DecryptingCardException;
import io.github.cdimascio.dotenv.Dotenv;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class CardNumberUtil {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = Dotenv.load().get("SECRET_KEY");

    public static String generateCardNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }

    public static String encryptCardNumber(String cardNumber) throws Exception {
        SecretKey key = getKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(cardNumber.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decryptCardNumber(String encryptedCardNumber) {
        try {
            SecretKey key = getKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(encryptedCardNumber);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e){
            throw new DecryptingCardException("Ошибка расшифровки карты", e);
        }
    }

    public static String maskCardNumber(String cardNumber) {
        String decryptCardNumber = decryptCardNumber(cardNumber);
        System.out.println(decryptCardNumber);
        return "**** **** **** " + decryptCardNumber.substring(12);
    }

    public static String decryptPlusMaskNumber(String cardNumber) {
        try {
            return maskCardNumber(decryptCardNumber(cardNumber));
        } catch (Exception e) {
            throw new DecryptingCardException("Ошибка расшифровки карты", e);
        }
    }

    private static SecretKey getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
    }
}