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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import static java.util.Objects.*;

public class ClientData {

    private final String typ;
    private final String challenge;
    private final String origin;

    private ClientData(String typ, String challenge, String origin) {
        this.typ = typ;
        this.challenge = challenge;
        this.origin = origin;
    }

    public String getTyp() {
        return typ;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getOrigin() {
        return origin;
    }

    public static ClientData of(String src) {
        requireNonNull(src, "src");

        String json
                = new String(
                        Base64.getUrlDecoder().decode(src),
                        StandardCharsets.UTF_8);

        String typ = null;
        String challenge = null;
        String origin = null;

        JsonFactory factory = new JsonFactory();
        
        try (JsonParser parser = factory.createParser(json)) {
            JsonToken token;
            while ((token = parser.nextToken()) != null) {
                if (token == JsonToken.FIELD_NAME) {
                    String fieldname = parser.getCurrentName();
                    String value = parser.nextTextValue();
                    switch (fieldname) {
                        case "typ":
                            typ = value;
                            break;
                        case "challenge":
                            challenge = value;
                            break;
                        case "origin":
                            origin = value;
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        if (nonNull(typ) && nonNull(challenge) && nonNull(origin)) {
            return new ClientData(typ, challenge, origin);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    

}
