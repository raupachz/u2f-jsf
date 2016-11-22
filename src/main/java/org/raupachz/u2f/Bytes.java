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

import java.util.Objects;

class Bytes {
    
    public static byte[] concat(byte[]... args) {
        Objects.requireNonNull(args, "args");
        byte[] c = args[0];
        for (int i = 1; i < args.length; i++) {
            c = concat(c, args[i]);
        }
        return c;
    }
    
    public static byte[] itob(int i) {
        byte b[] = new byte[4];
        b[0] = (byte)(i >>> 24);
        b[1] = (byte)(i >>> 16);
        b[2] = (byte)(i >>> 8);
        b[3] = (byte)(i);
        return b;
    }
    
    private static byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
    
    
}
