package com.nozama.aluguel_veiculos.domain.converter;

import com.nozama.aluguel_veiculos.config.CryptoKeyHolder;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Converter(autoApply = false)
public class CryptoConverter implements AttributeConverter<String, String> {

    private static final String AES = "AES";
    private static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_BIT_LENGTH = 128;

    private SecretKeySpec getKeySpec() {
        String base64Key = CryptoKeyHolder.encryptionKey;

        if (base64Key == null) {
            throw new IllegalStateException("crypto.encryption-key não foi carregada.");
        }

        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

        if (keyBytes.length != 32) {
            throw new IllegalStateException("A chave AES deve ter 256 bits (32 bytes).");
        }

        return new SecretKeySpec(keyBytes, AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;

        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
            cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(), new GCMParameterSpec(TAG_BIT_LENGTH, iv)); // ✔ CORRIGIDO

            byte[] cipherText = cipher.doFinal(attribute.getBytes("UTF-8"));

            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null;
            }

            // Não é Base64 → está em texto puro
            if (!isBase64(dbData)) {
                return dbData;
            }

            byte[] decoded = Base64.getDecoder().decode(dbData);

            // Formato inválido → provavelmente texto puro
            if (decoded.length < IV_LENGTH) {
                return dbData;
            }

            byte[] iv = Arrays.copyOfRange(decoded, 0, IV_LENGTH);
            byte[] cipherText = Arrays.copyOfRange(decoded, IV_LENGTH, decoded.length);

            Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    getKeySpec(),
                    new GCMParameterSpec(TAG_BIT_LENGTH, iv)
            );

            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain, "UTF-8");

        } catch (Exception e) {
            // Se falhar → assume que já está descriptografado
            return dbData;
        }
    }

    private boolean isBase64(String value) {
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
