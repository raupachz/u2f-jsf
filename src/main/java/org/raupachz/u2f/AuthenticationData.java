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
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.util.Arrays;
import java.util.Base64;
import static java.util.Objects.requireNonNull;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;

/**
 * Authentication Response Message: Success
 *
 * see FIDO U2F Raw Message Formats for reference
 */
public class AuthenticationData {

    private final byte userPresence;
    private final int counter;
    private final byte[] signature;

    private AuthenticationData(byte userPresence, int counter, byte[] signature) {
        this.userPresence = userPresence;
        this.counter = counter;
        this.signature = signature;
    }

    public byte getUserPresence() {
        return userPresence;
    }

    public int getCounter() {
        return counter;
    }

    public byte[] getSignature() {
        return Arrays.copyOf(signature, signature.length);
    }

    public static AuthenticationData of(String src) {
        requireNonNull(src, "src");

        byte userPresence;
        int counter;
        byte[] signature;

        // decode Base64 into bytes
        byte[] authenticationData = Base64.getUrlDecoder().decode(src);

        // user presence
        userPresence = authenticationData[0];

        // 4 bytes, big endian counter
        counter = 0;
        for (int i = 0; i < 4; i++) {
            counter = counter << 8;
            counter = (counter | authenticationData[1 + i]);
        }
        
        // signature
        signature = new byte[authenticationData.length - 5];
        System.arraycopy(authenticationData, 5, signature, 0, signature.length);
        
        return new AuthenticationData(userPresence, counter, signature);
    }
    
    public boolean verify(String clientData, String appId, byte[] publicKey) {
        boolean verify = false;

        try {
            ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("P-256");
            ECCurve curve = spec.getCurve();
            
            EllipticCurve ellipticCurve = EC5Util.convertCurve(curve, spec.getSeed());
            ECPoint ecPoint = ECPointUtil.decodePoint(ellipticCurve, publicKey);
            ECParameterSpec ecParameterSpec = EC5Util.convertSpec(ellipticCurve, spec);
            
            
            ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, ecParameterSpec);
            KeyFactory kf = KeyFactory.getInstance("EC");
            PublicKey _publicKey = kf.generatePublic(publicKeySpec);
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(clientData.getBytes(StandardCharsets.UTF_8));
            byte[] challengeParameter = md.digest();

            md.reset();
            
            md.update(appId.getBytes(StandardCharsets.UTF_8));
            byte[] applicationParameter = md.digest();

            // verify this
            byte[] raw = new byte[32 + 1 + 4 + 32];
            System.arraycopy(applicationParameter, 0, raw, 0, applicationParameter.length);
            raw[32] = userPresence;
            // 4 bytes counter
            raw[33] = (byte)(counter >>> 24);
            raw[34] = (byte)(counter >>> 16);
            raw[35] = (byte)(counter >>> 8);
            raw[36] = (byte)(counter);
            
            // challenge data
            System.arraycopy(challengeParameter, 0, raw, 37, challengeParameter.length);
            
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());
            ecdsaVerify.initVerify(_publicKey);
            ecdsaVerify.update(raw);

            verify = ecdsaVerify.verify(signature);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return verify;
    }

}
