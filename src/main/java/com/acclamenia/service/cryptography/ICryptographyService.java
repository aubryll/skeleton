package com.acclamenia.service.cryptography;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface ICryptographyService<T> {

     byte[] sign(byte[] data, final X509Certificate signingCertificate, final PrivateKey signingKey);

    boolean verify(final byte[] signedData);

    byte[] encrypt(final byte[] data, X509Certificate encryptionCertificate);

    byte[] decrypt(final byte[] encryptedData, final PrivateKey decryptionKey);

    String toString(final byte[] data);

    byte[] fromString(final String data);

    byte[] objectToByte(final T t);

    T toObject(final byte[] data);

}
