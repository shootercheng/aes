package com.scd.aes;

import com.scd.constant.Constant;
import com.scd.exception.DecryptException;
import com.scd.exception.EncryptException;
import com.scd.exception.RandomKeyException;
import com.scd.model.CryptoKey;
import com.scd.util.Base64Util;
import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.random.CryptoRandom;
import org.apache.commons.crypto.random.CryptoRandomFactory;
import org.apache.commons.crypto.stream.CryptoInputStream;
import org.apache.commons.crypto.stream.CryptoOutputStream;
import org.apache.commons.crypto.utils.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Properties;

/**
 * @author James
 */
public class CryptoUtil {
    private static final String TRANSFORM = "AES/CBC/PKCS5Padding";

    private static final String ENCRYPT_TYPE = "AES";

    public static CryptoKey generateRandomKey() {
        byte[] key = new byte[32];
        byte[] iv = new byte[16];
        Properties properties = createProperties();

        // Gets the 'CryptoRandom' instance.
        try (CryptoRandom random = CryptoRandomFactory.getCryptoRandom(properties)) {
            // Generate random bytes and places them into the byte arrays.
            random.nextBytes(key);
            random.nextBytes(iv);
        } catch (GeneralSecurityException | IOException e) {
            throw new RandomKeyException("generate random key error ", e);
        }
        CryptoKey cryptoKey = new CryptoKey();
        cryptoKey.setSecretKeySpec(Base64Util.encryptBASE64(key));
        cryptoKey.setIvParameterSpec(Base64Util.encryptBASE64(iv));
        return cryptoKey;
    }

    private static Properties createProperties() {
        Properties properties = new Properties();
        properties.put(CryptoRandomFactory.CLASSES_KEY,
                CryptoRandomFactory.RandomProvider.OPENSSL.getClassName());
        return properties;
    }

    /**
     * Converts String to UTF8 bytes
     *
     * @param input the input string
     * @return UTF8 bytes
     */
    private static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Converts ByteBuffer to String
     *
     * @param buffer input byte buffer
     * @return the converted string
     */
    private static String asString(ByteBuffer buffer) {
        final ByteBuffer copy = buffer.duplicate();
        final byte[] bytes = new byte[copy.remaining()];
        copy.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String encrypt(SecretKeySpec key, IvParameterSpec iv, String text) {
        Properties properties = createProperties();
        //Creates a CryptoCipher instance with the transformation and properties.
        try (CryptoCipher encipher = Utils.getCipherInstance(TRANSFORM, properties)) {
            ByteBuffer inBuffer = ByteBuffer.allocateDirect(Constant.BUFFER_SIZE_4096);
            ByteBuffer outBuffer = ByteBuffer.allocateDirect(Constant.BUFFER_SIZE_4096);
            inBuffer.put(getUTF8Bytes(text));

            inBuffer.flip();
            // Show the data is there
            // Initializes the cipher with ENCRYPT_MODE,key and iv.
            encipher.init(Cipher.ENCRYPT_MODE, key, iv);

            // Continues a multiple-part encryption/decryption operation for byte buffer.
            int updateBytes = encipher.update(inBuffer, outBuffer);

            // We should call do final at the end of encryption/decryption.
            int finalBytes = encipher.doFinal(inBuffer, outBuffer);
            outBuffer.flip(); // ready for use as decrypt
            byte[] encoded = new byte[updateBytes + finalBytes];
            outBuffer.duplicate().get(encoded);
            return Base64Util.encryptBASE64(encoded);
        } catch (IOException | InvalidKeyException | ShortBufferException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new EncryptException("crypto encrypt error ", e);
        }
    }

    public static String decrypt(SecretKeySpec key, IvParameterSpec iv, String encryptStr) {
        Properties properties = createProperties();
        //Creates a CryptoCipher instance with the transformation and properties.
        try (CryptoCipher decipher = Utils.getCipherInstance(TRANSFORM, properties)) {
            decipher.init(Cipher.DECRYPT_MODE, key, iv);
            ByteBuffer inBuffer = ByteBuffer.allocateDirect(Constant.BUFFER_SIZE_4096);
            inBuffer.put(Base64Util.decryptBASE64(encryptStr));
            inBuffer.flip();
            ByteBuffer decoded = ByteBuffer.allocateDirect(Constant.BUFFER_SIZE_4096);
            decipher.update(inBuffer, decoded);
            decipher.doFinal(inBuffer, decoded);
            decoded.flip();
            return asString(decoded);
        } catch (InvalidKeyException | ShortBufferException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | IOException e) {
            throw new DecryptException("crypto decrypt error ", e);
        }
    }

    public static byte[] encrypt(SecretKeySpec key, IvParameterSpec iv, byte[] bytes) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CryptoOutputStream cos = new CryptoOutputStream(TRANSFORM, createProperties(), outputStream, key, iv)) {
            cos.write(bytes);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new EncryptException("encrypt byte error ", e);
        }
    }

    public static byte[] encrypt(String key, String iv, byte[] bytes) {
        SecretKeySpec secretKeySpec = createSecretKeySpec(key);
        IvParameterSpec ivParameterSpec = createIvParameterSpec(iv);
        return encrypt(secretKeySpec, ivParameterSpec, bytes);
    }

    public static byte[] decrypt(SecretKeySpec key, IvParameterSpec iv, byte[] encryptByte) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encryptByte);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CryptoInputStream cis = new CryptoInputStream(TRANSFORM, createProperties(), inputStream, key, iv)) {
            byte[] buffer = new byte[Constant.BUFFER_SIZE_4096];
            int readLen = - 1;
            while ((readLen = cis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLen);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new DecryptException("decrypt file error ", e);
        }
    }

    public static byte[] decrypt(String key, String iv, byte[] encryptByte) {
        SecretKeySpec secretKeySpec = createSecretKeySpec(key);
        IvParameterSpec ivParameterSpec = createIvParameterSpec(iv);
        return decrypt(secretKeySpec, ivParameterSpec, encryptByte);
    }

    public static void encrypt(SecretKeySpec key, IvParameterSpec iv, String filePath, String encryptFilPath) {
        try (InputStream inputStream = new FileInputStream(filePath);
             OutputStream outputStream = new FileOutputStream(encryptFilPath);
             CryptoOutputStream cos = new CryptoOutputStream(TRANSFORM, createProperties(), outputStream, key, iv)) {
            byte[] buffer = new byte[Constant.BUFFER_SIZE_4096];
            int readLen = -1;
            while ((readLen = inputStream.read(buffer)) != -1) {
                cos.write(buffer, 0, readLen);
            }
        } catch (IOException e) {
            throw new EncryptException("encrypt file error ", e);
        }
    }

    public static void encrypt(CryptoKey cryptoKey, String filePath, String encryptFilPath) {
        SecretKeySpec secretKeySpec = createSecretKeySpec(cryptoKey.getSecretKeySpec());
        IvParameterSpec ivParameterSpec = createIvParameterSpec(cryptoKey.getIvParameterSpec());
        encrypt(secretKeySpec, ivParameterSpec, filePath, encryptFilPath);
    }

    public static void decrypt(SecretKeySpec key, IvParameterSpec iv, String encryptFilePath, String decryptFilePath) {
        try (InputStream inputStream = new FileInputStream(encryptFilePath);
            OutputStream outputStream = new FileOutputStream(decryptFilePath);
            CryptoInputStream cis = new CryptoInputStream(TRANSFORM, createProperties(), inputStream, key, iv)) {
            byte[] buffer = new byte[Constant.BUFFER_SIZE_4096];
            int readLen = -1;
            while ((readLen = cis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLen);
            }
        } catch (IOException e) {
            throw new DecryptException("decrypt file error ", e);
        }
    }

    public static void decrypt(CryptoKey cryptoKey, String encryptFilePath, String decryptFilePath) {
        SecretKeySpec secretKeySpec = createSecretKeySpec(cryptoKey.getSecretKeySpec());
        IvParameterSpec ivParameterSpec = createIvParameterSpec(cryptoKey.getIvParameterSpec());
        decrypt(secretKeySpec, ivParameterSpec, encryptFilePath, decryptFilePath);
    }

    public static String encrypt(String key, String iv, String text) {
        SecretKeySpec secretKeySpec = createSecretKeySpec(key);
        IvParameterSpec ivParameterSpec = createIvParameterSpec(iv);
        return encrypt(secretKeySpec, ivParameterSpec, text);
    }

    public static String decrypt(String key, String iv, String encryptStr) {
        SecretKeySpec secretKeySpec = createSecretKeySpec(key);
        IvParameterSpec ivParameterSpec = createIvParameterSpec(iv);
        return decrypt(secretKeySpec, ivParameterSpec, encryptStr);
    }

    public static IvParameterSpec createIvParameterSpec(String iv) {
        byte[] byteIv = Base64Util.decryptBASE64(iv);
        return new IvParameterSpec(byteIv);
    }

    public static SecretKeySpec createSecretKeySpec(String key) {
        byte[] byteKey = Base64Util.decryptBASE64(key);
        return new SecretKeySpec(byteKey, ENCRYPT_TYPE);
    }
}
