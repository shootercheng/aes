package com.scd.aes.test.randomkey;

import org.apache.commons.crypto.random.CryptoRandom;
import org.apache.commons.crypto.random.CryptoRandomFactory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @author James
 */
public class CryptoRandomKeyTest {

    @Test
    public void testOpenSslRandomKey() throws GeneralSecurityException {
        Properties properties = new Properties();
        properties.put(CryptoRandomFactory.CLASSES_KEY, CryptoRandomFactory.RandomProvider.OPENSSL.getClassName());
        final CryptoRandom random = CryptoRandomFactory.getCryptoRandom(properties);
        byte[] byteOne = new byte[1];
        random.nextBytes(byteOne);
    }

    @Test
    public void testOpenSslRandomKeyWithDebug() throws GeneralSecurityException {
        System.setProperty("commons.crypto.debug", "true");
        Properties properties = new Properties();
        properties.put(CryptoRandomFactory.CLASSES_KEY, CryptoRandomFactory.RandomProvider.OPENSSL.getClassName());
        CryptoRandom random = CryptoRandomFactory.getCryptoRandom(properties);
    }

    @Test
    public void testPosixAttribute() throws IOException {
        Path path = Paths.get("file/PDF_Software.pdf");
        final PosixFileAttributes attributes = Files.readAttributes(path, PosixFileAttributes.class);
    }
}
