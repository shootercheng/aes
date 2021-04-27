package com.scd.model;

/**
 * @author James
 */
public class CryptoKey {
    private String secretKeySpec;

    private String ivParameterSpec;

    public String getSecretKeySpec() {
        return secretKeySpec;
    }

    public void setSecretKeySpec(String secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

    public String getIvParameterSpec() {
        return ivParameterSpec;
    }

    public void setIvParameterSpec(String ivParameterSpec) {
        this.ivParameterSpec = ivParameterSpec;
    }
}
