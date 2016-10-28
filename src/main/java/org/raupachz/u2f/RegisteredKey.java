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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class RegisteredKey implements Serializable {

    private final String version;
    private final byte[] keyHandle;
    private final byte[] publicKey;

    public RegisteredKey(String version, byte[] keyHandle, byte[] publicKey) {
        Objects.requireNonNull(keyHandle, "keyHandle");
        Objects.requireNonNull(publicKey, "publicKey");
        
        this.version = Objects.requireNonNull(version, "version");
        this.keyHandle = Arrays.copyOf(keyHandle, keyHandle.length);
        this.publicKey = Arrays.copyOf(publicKey, publicKey.length);
    }
    
    public String getVersion() {
        return version;
    }

    public byte[] getKeyHandle() {
        return Arrays.copyOf(keyHandle, keyHandle.length);
    }

    public byte[] getPublicKey() {
        return Arrays.copyOf(publicKey, publicKey.length);
    }

    @Override
    public int hashCode() {
        int hash = 41;
        hash = 37 * hash + Objects.hashCode(this.version);
        hash = 37 * hash + Arrays.hashCode(this.keyHandle);
        hash = 37 * hash + Arrays.hashCode(this.publicKey);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegisteredKey other = (RegisteredKey) obj;
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Arrays.equals(this.keyHandle, other.keyHandle)) {
            return false;
        }
        if (!Arrays.equals(this.publicKey, other.publicKey)) {
            return false;
        }
        return true;
    }

}
