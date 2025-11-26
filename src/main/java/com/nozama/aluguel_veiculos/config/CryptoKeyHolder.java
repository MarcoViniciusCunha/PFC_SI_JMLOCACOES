package com.nozama.aluguel_veiculos.config;

import org.springframework.stereotype.Component;

@Component
public class CryptoKeyHolder {

    public static String encryptionKey;
    public static String hmacSecret;

    public CryptoKeyHolder(CryptoProperties properties) {
        encryptionKey = properties.getEncryptionKey();
        hmacSecret = properties.getHmacSecret();
        System.out.println("HMAC secret loaded: " + (hmacSecret==null ? "null" : "length=" + hmacSecret.length()));
    }
}
