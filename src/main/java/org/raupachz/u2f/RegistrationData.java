/*
 * The MIT License
 *
 * Copyright 2016 Björn Raupach <raupach@me.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.raupachz.u2f;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Signature;
import java.util.Arrays;
import java.util.Base64;
import static java.util.Objects.requireNonNull;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Registration Response Message: Success
 *
 * see FIDO U2F Raw Message Formats for reference
 */
public class RegistrationData {

    private final byte reserved;
    private final byte[] publicKey;
    private final byte keyHandleLength;
    private final byte[] keyHandle;
    private final byte[] attestationCertificate;
    private final byte[] signature;

    private RegistrationData(byte reserved, byte[] publicKey, byte keyHandleLength, byte[] keyHandle, byte[] attestationCertificate, byte[] signature) {
        this.reserved = reserved;
        this.publicKey = publicKey;
        this.keyHandleLength = keyHandleLength;
        this.keyHandle = keyHandle;
        this.attestationCertificate = attestationCertificate;
        this.signature = signature;
    }

    public byte getReserved() {
        return reserved;
    }

    public byte[] getUserPublicKey() {
        return Arrays.copyOf(publicKey, publicKey.length);
    }

    public byte getKeyHandleLength() {
        return keyHandleLength;
    }

    public byte[] getKeyHandle() {
        return Arrays.copyOf(keyHandle, keyHandle.length);
    }

    public byte[] getAttestationCertificate() {
        return Arrays.copyOf(attestationCertificate, attestationCertificate.length);
    }

    public byte[] getSignature() {
        return Arrays.copyOf(signature, signature.length);
    }

    public X509Certificate getCertificate() throws CertificateException {
        return X509Certificate.getInstance(getAttestationCertificate());
    }

    public static RegistrationData of(String src) {
        requireNonNull(src, "src");

        byte reserved;
        byte[] publicKey;
        byte keyHandleLength;
        byte[] keyHandle;
        byte[] certificate;
        byte[] signature;

        // decode Base64 into bytes
        byte[] registrationData = Base64.getUrlDecoder().decode(src);

        // reserved byte (value: 0x05)
        reserved = registrationData[0];

        // user public key: 65 bytes
        publicKey = new byte[65];
        System.arraycopy(registrationData, 1, publicKey, 0, publicKey.length);

        // key handle length
        keyHandleLength = registrationData[66];

        // key handle
        keyHandle = new byte[keyHandleLength];
        System.arraycopy(registrationData, 67, keyHandle, 0, keyHandleLength);

        // certificate (ASN.1 DER)
        int skipBytes = 1;
        int offset = 67 + keyHandleLength + skipBytes;
        byte lengthValue = registrationData[offset];
        int certLength = lengthValue & 0x0f;
        if ((lengthValue & 0x80) != 0) {
            if (certLength <= 4) {
                int tmp = 0;
                for (int i = 0; i < certLength; i++) {
                    tmp = tmp << 8;
                    tmp = (tmp | registrationData[offset + skipBytes + i]);
                }
                skipBytes = skipBytes + certLength;
                certLength = tmp;
            } else {
                throw new IllegalArgumentException("certificate length exceeds length limit");
            }
        }
        skipBytes++;
        certificate = new byte[certLength + skipBytes];
        System.arraycopy(registrationData, 67 + keyHandleLength, certificate, 0, certificate.length);

        // signature (ASN.1 DER)
        offset = 1 + 65 + 1 + keyHandleLength + certificate.length;
        int signatureLength = registrationData.length - (1 + 65 + 1 + keyHandleLength + certificate.length);
        signature = new byte[signatureLength];
        System.arraycopy(registrationData, offset, signature, 0, signature.length);

        return new RegistrationData(reserved, publicKey, keyHandleLength, keyHandle, certificate, signature);
    }

    public boolean verify(String clientData, String appId) {
        boolean verify = false;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(clientData.getBytes(StandardCharsets.UTF_8));
            byte[] challengeParameter = md.digest();

            md.reset();
            
            md.update(appId.getBytes(StandardCharsets.UTF_8));
            byte[] applicationParameter = md.digest();

            // verify this
            byte[] raw = new byte[1 + 32 + 32 + keyHandleLength + 65];
            raw[0] = 0;
            System.arraycopy(applicationParameter, 0, raw, 1, applicationParameter.length);
            System.arraycopy(challengeParameter, 0, raw, 33, challengeParameter.length);
            System.arraycopy(keyHandle, 0, raw, 65, keyHandle.length);
            System.arraycopy(publicKey, 0, raw, 65 + keyHandle.length, publicKey.length);

            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());
            ecdsaVerify.initVerify(getCertificate().getPublicKey());
            ecdsaVerify.update(raw);

            verify = ecdsaVerify.verify(signature);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return verify;
    }

}
