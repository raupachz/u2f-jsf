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
package org.raupachz.model;

import org.raupachz.u2f.ClientData;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestClientData {
    
    @Test(expected = NullPointerException.class)
    public void of_null() {
        ClientData.of(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void of_empty() {
        ClientData.of("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void of_invalid() {
        ClientData.of("ABCDEFGH");
    }
    
    @Test
    public void of() {
        String src = "eyJ0eXAiOiJuYXZpZ2F0b3IuaWQuZmluaXNoRW5yb2xsbWVudCIsImNoYWxsZW5nZSI6ImxGVk5Oc2pLQ0R4Ri1LQ1lQZzFJQ0FKLWF0YlYxLTUxaWpYc0UwamNEOTh1UE5lT3Bib2F2NTQyTnVvU3J1eEdfTDlZRG9aaC1CZm04d2dVUEpabVUwMWRNeUxTTmVEVWZfdFZYdUJjUS1fTzdJeW5qNW80UUExMU9XbVo2cGN5RmFXSm1KSURLcWlqQ0dLVkJ4ZWRSYVJrSHNZSmg0MGxwQk9vQTBjcjdxTSIsIm9yaWdpbiI6Imh0dHBzOi8vbG9jYWxob3N0Ojg0NDMiLCJjaWRfcHVia2V5IjoidW51c2VkIn0";
        ClientData data = ClientData.of(src);
        assertEquals("navigator.id.finishEnrollment", data.getTyp());
        assertEquals("https://localhost:8443", data.getOrigin());
        assertEquals("lFVNNsjKCDxF-KCYPg1ICAJ-atbV1-51ijXsE0jcD98uPNeOpboav542NuoSruxG_L9YDoZh-Bfm8wgUPJZmU01dMyLSNeDUf_tVXuBcQ-_O7Iynj5o4QA11OWmZ6pcyFaWJmJIDKqijCGKVBxedRaRkHsYJh40lpBOoA0cr7qM", data.getChallenge());
    }
    
}
