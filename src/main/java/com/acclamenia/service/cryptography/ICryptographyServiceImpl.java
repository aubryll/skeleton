package com.acclamenia.service.cryptography;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.*;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.springframework.stereotype.Service;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;


@Service
public class ICryptographyServiceImpl<T> implements ICryptographyService<T> {

    @Override
    public byte[] sign(byte[] data, X509Certificate signingCertificate, PrivateKey signingKey)  {
        byte[] signedMessage = new byte[0];
        try {
            List<X509Certificate> certList = new ArrayList<>();
            CMSTypedData cmsData = new CMSProcessableByteArray(data);
            certList.add(signingCertificate);
            JcaCertStore certs = new JcaCertStore(certList);
            CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
            ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(signingKey);
            cmsGenerator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(contentSigner, signingCertificate));
            cmsGenerator.addCertificates(certs);
            CMSSignedData cms = cmsGenerator.generate(cmsData, true);
            signedMessage = cms.getEncoded();
        }catch (CMSException | IOException | OperatorCreationException | CertificateException e) {
            e.printStackTrace();
        }
        return signedMessage;

    }

    @Override
    public boolean verify(byte[] signedData) {
        try {
            ByteArrayInputStream bIn = new ByteArrayInputStream(signedData);
            ASN1InputStream aIn = new ASN1InputStream(bIn);
            CMSSignedData s = new CMSSignedData(ContentInfo.getInstance(aIn.readObject()));
            aIn.close();
            bIn.close();
            Store<X509CertificateHolder> certs = s.getCertificates();
            SignerInformationStore signers = s.getSignerInfos();
            Collection<SignerInformation> c = signers.getSigners();
            SignerInformation signer = c.iterator().next();
            Collection<X509CertificateHolder> certCollection = certs.getMatches(signer.getSID());
            Iterator<X509CertificateHolder> certIt = certCollection.iterator();
            X509CertificateHolder certHolder = certIt.next();
            return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certHolder));
        }catch (CMSException | IOException | OperatorCreationException | CertificateException e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public byte[] encrypt(byte[] data, X509Certificate encryptionCertificate) {
        byte[] encryptedData = new byte[0];
        try {
            if (data != null && encryptionCertificate != null) {
                CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator = new CMSEnvelopedDataGenerator();
                JceKeyTransRecipientInfoGenerator jceKey = new JceKeyTransRecipientInfoGenerator(encryptionCertificate);
                cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);
                CMSTypedData msg = new CMSProcessableByteArray(data);
                OutputEncryptor outputEncryptor = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC).setProvider("BC").build();
                CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator.generate(msg, outputEncryptor);
                encryptedData = cmsEnvelopedData.getEncoded();
            }
            }catch (CMSException | IOException  | CertificateException e){
                e.printStackTrace();
            }

        return encryptedData;
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, PrivateKey decryptionKey) {
        byte[] decryptedData = new byte[0];
        try {
            if (null != encryptedData && null != decryptionKey) {
                CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);
                Collection<RecipientInformation> recipients = envelopedData.getRecipientInfos().getRecipients();
                KeyTransRecipientInformation recipientInfo = (KeyTransRecipientInformation) recipients.iterator().next();
                JceKeyTransRecipient recipient = new JceKeyTransEnvelopedRecipient(decryptionKey);
                decryptedData = recipientInfo.getContent(recipient);
            }
        }catch (CMSException e){
            e.printStackTrace();
        }
        return decryptedData;
    }

    @Override
    public String toString(byte[] data) {
        return Base64.getUrlEncoder().encodeToString(data);
    }

    @Override
    public byte[] fromString(String data) {
        return Base64.getUrlDecoder().decode(data);
    }

    @Override
    public byte[] objectToByte(T t) {
        try {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutput out = new ObjectOutputStream(bos)) {
                out.writeObject(t);
                return bos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public T toObject(byte[] data) {
        try {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                 ObjectInput in = new ObjectInputStream(bis)) {
                Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                return tClass.cast(in.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
