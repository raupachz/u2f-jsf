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

import org.junit.Test;
import static org.junit.Assert.*;

public class TestBytes {
    
    @Test(expected = NullPointerException.class)
    public void test_null() {
        Bytes.concat(null);
    }
    
    @Test
    public void test_oneArg() {
        byte[] expected = new byte[] { 0x0a };
        byte[] actual = Bytes.concat(expected);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void test_twoArgs() {
        byte[] a = new byte[] { 0x0a };
        byte[] b = new byte[] { 0x0b, 0x0c };
        
        byte[] actual = Bytes.concat(a, b);
        byte[] expected = new byte[] { 0x0a, 0x0b, 0x0c };
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void test_threeArgs() {
        byte[] a = new byte[] { 0x0a };
        byte[] b = new byte[] { 0x0b, 0x0c };
        byte[] c = new byte[] { 0x0d };
        
        byte[] actual = Bytes.concat(a, b, c);
        byte[] expected = new byte[] { 0x0a, 0x0b, 0x0c, 0x0d };
        assertArrayEquals(expected, actual);
    }
    
}
