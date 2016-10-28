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
package org.raupachz.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.SessionScoped;
import org.raupachz.u2f.RegisteredKey;

@SessionScoped
public class Storage implements Serializable {
    
    private final List<RegisteredKey> entries;
    
    public Storage() {
        this.entries = new ArrayList<>();
    }
    
    public void put(String version, byte[] keyHandle, byte[] publicKey) {
        RegisteredKey entry = new RegisteredKey(version, keyHandle, publicKey);
        entries.add(entry);
    }
    
    public Optional<RegisteredKey> get(byte[] keyHandle) {
        Optional<RegisteredKey> opt = Optional.empty();
        for (RegisteredKey key : entries) {
            if (Arrays.equals(key.getKeyHandle(), keyHandle)) {
                opt = Optional.of(key);
            }
        }
        return opt;
    }
    
    public Iterator<RegisteredKey> entries() {
        return entries.iterator();
    }
    
}
