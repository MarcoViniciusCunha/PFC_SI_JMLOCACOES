package com.nozama.aluguel_veiculos.utils;

import com.nozama.aluguel_veiculos.config.CryptoKeyHolder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class HashUtils {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static volatile byte[] KEY; // lazy init
    private HashUtils() {}

    private static synchronized void ensureKeyInitialized() {
        if (KEY != null) return;

        String base64 = CryptoKeyHolder.hmacSecret;
        if (base64 == null || base64.isBlank()) {
            throw new IllegalStateException(
                    "crypto.hmac-secret não foi carregada. Verifique application.properties (crypto.hmac-secret)."
            );
        }

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Valor de crypto.hmac-secret não é um Base64 válido.", ex);
        }

        // recomendação: pelo menos 16/32 bytes; HMAC aceita qualquer tamanho, mas chaves longas são melhores
        if (decoded.length < 16) {
            throw new IllegalStateException("HMAC key muito curta. Recomenda-se ao menos 128 bits (16 bytes).");
        }

        KEY = decoded;
    }

    public static String hmacSha256Base64(String value) {
        if (value == null) return null;
        if (KEY == null) ensureKeyInitialized();

        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(KEY, HMAC_SHA256));
            byte[] raw = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(raw);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash HMAC", e);
        }
    }
}
