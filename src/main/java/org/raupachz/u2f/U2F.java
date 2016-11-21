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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import org.raupachz.data.Storage;
import org.raupachz.u2f.RegisteredKey;

@ApplicationScoped
public class U2F implements Serializable {
    
    public static final int CHALLENGE_BYTES = 128;
    
    private SecureRandom random;
    private JsonFactory factory;
    
    @PostConstruct
    public void initialize() {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.factory = new JsonFactory();
    }
    
    public String challenge() {
        byte[] challengeBytes = new byte[CHALLENGE_BYTES];
        random.nextBytes(challengeBytes);
        return encode(challengeBytes);
    }
    
    public String encode(byte[] bytes) {
        return java.util.Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
    
    public byte[] decode(String base64) {
        return java.util.Base64.getUrlDecoder()
                .decode(base64);
    }
    
    public String stringify(Storage storage) {
        StringWriter writer = new StringWriter();
        
        try {
            JsonGenerator g = factory.createGenerator(writer);
            g.writeStartArray();
            
            for (Iterator<RegisteredKey> it = storage.entries(); it.hasNext(); ) {
                RegisteredKey registeredKey = it.next();
                g.writeStartObject();
                g.writeStringField("version", registeredKey.getVersion());
                g.writeStringField("keyHandle", encode(registeredKey.getKeyHandle()));
                g.writeEndObject();
            }
            
            g.writeEndArray();
            g.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return writer.toString();
    }
}
