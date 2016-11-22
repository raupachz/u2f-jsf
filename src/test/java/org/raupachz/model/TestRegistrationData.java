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

import java.security.Security;
import javax.security.cert.CertificateException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.raupachz.u2f.RegistrationData;
import org.junit.Test;

public class TestRegistrationData {
    
    @BeforeClass
    public static void bootstrap() {
    }
    
    @Ignore
    @Test(expected = NullPointerException.class)
    public void of_null() {
        RegistrationData.of(null);
    }
    
    @Ignore
    @Test
    public void of() throws CertificateException {
        String src = "BQQ-eNWZQ_kGK_Hk8btPVBdpUIHiYmjvF54eeEEV3l9ERhLx612q60_r8qWppy2vSdR8hqJpfDS2B_7zxJEB-WXnQGntqy696dmXhB1sd57_R83QawmxSnoNrWnBVApMtbJSDmjLnMC9EBbuEbwZpm9SugKTZyE9bDYZxAjSFgNVQS8wggItMIIBF6ADAgECAgQFtgV5MAsGCSqGSIb3DQEBCzAuMSwwKgYDVQQDEyNZdWJpY28gVTJGIFJvb3QgQ0EgU2VyaWFsIDQ1NzIwMDYzMTAgFw0xNDA4MDEwMDAwMDBaGA8yMDUwMDkwNDAwMDAwMFowKDEmMCQGA1UEAwwdWXViaWNvIFUyRiBFRSBTZXJpYWwgOTU4MTUwMzMwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAT9uN6zoe1w62NsBm62AGmWpflw_LXbiPw7MF1B5ZZvDBtUuFL-8KCQftF_O__CnU0yG5z4qEos6qA4yr011ZjeoyYwJDAiBgkrBgEEAYLECgIEFTEuMy42LjEuNC4xLjQxNDgyLjEuMTALBgkqhkiG9w0BAQsDggEBAH7T-2zMJSAT-C8hjCo32mAx0g5_MIHa_K6xKPx_myM5FL-2TWE18XziIfp2T0U-8Sc6jOlllWRCuy8eR0g_c33LyYtYU3f-9QsnDgKJ-IQ28a3PSbJiHuXjAt9VW5q3QnLgafkYFJs97E8SIosQwPiN42r1inS7RCuFrgBTZL2mcCBY_B8th5tTARHqYOhsY_F_pZRMyD8KommEiz7jiKbAnmsFlT_LuPR-g6J-AHKmPDKtZIZOkm1xEvoZl_eDllb7syvo94idDwFFUZonr92ORrBMpCkNhUC2NLiGFh51iMhimdzdZDXRZ4o6bwp0gpxN0_cMNSTR3fFteK3SG2QwRAIgXyI_yrUZk7GaEN2Oj206fgI3PD56-TE_Y0o17yJ4pV4CICB0Ask-028HUXdZY_mYSrq7YOFtAAA-4iQ-JArH4XEp";
        RegistrationData registrationData = RegistrationData.of(src);
    }
    
    @Ignore
    @Test
    public void verify() throws Exception {
            String src = "BQQ-eNWZQ_kGK_Hk8btPVBdpUIHiYmjvF54eeEEV3l9ERhLx612q60_r8qWppy2vSdR8hqJpfDS2B_7zxJEB-WXnQGntqy696dmXhB1sd57_R83QawmxSnoNrWnBVApMtbJSDmjLnMC9EBbuEbwZpm9SugKTZyE9bDYZxAjSFgNVQS8wggItMIIBF6ADAgECAgQFtgV5MAsGCSqGSIb3DQEBCzAuMSwwKgYDVQQDEyNZdWJpY28gVTJGIFJvb3QgQ0EgU2VyaWFsIDQ1NzIwMDYzMTAgFw0xNDA4MDEwMDAwMDBaGA8yMDUwMDkwNDAwMDAwMFowKDEmMCQGA1UEAwwdWXViaWNvIFUyRiBFRSBTZXJpYWwgOTU4MTUwMzMwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAT9uN6zoe1w62NsBm62AGmWpflw_LXbiPw7MF1B5ZZvDBtUuFL-8KCQftF_O__CnU0yG5z4qEos6qA4yr011ZjeoyYwJDAiBgkrBgEEAYLECgIEFTEuMy42LjEuNC4xLjQxNDgyLjEuMTALBgkqhkiG9w0BAQsDggEBAH7T-2zMJSAT-C8hjCo32mAx0g5_MIHa_K6xKPx_myM5FL-2TWE18XziIfp2T0U-8Sc6jOlllWRCuy8eR0g_c33LyYtYU3f-9QsnDgKJ-IQ28a3PSbJiHuXjAt9VW5q3QnLgafkYFJs97E8SIosQwPiN42r1inS7RCuFrgBTZL2mcCBY_B8th5tTARHqYOhsY_F_pZRMyD8KommEiz7jiKbAnmsFlT_LuPR-g6J-AHKmPDKtZIZOkm1xEvoZl_eDllb7syvo94idDwFFUZonr92ORrBMpCkNhUC2NLiGFh51iMhimdzdZDXRZ4o6bwp0gpxN0_cMNSTR3fFteK3SG2QwRAIgXyI_yrUZk7GaEN2Oj206fgI3PD56-TE_Y0o17yJ4pV4CICB0Ask-028HUXdZY_mYSrq7YOFtAAA-4iQ-JArH4XEp";
            RegistrationData registrationData = RegistrationData.of(src);
            
            String clientData = "eyJ0eXAiOiJuYXZpZ2F0b3IuaWQuZmluaXNoRW5yb2xsbWVudCIsImNoYWxsZW5nZSI6ImxGVk5Oc2pLQ0R4Ri1LQ1lQZzFJQ0FKLWF0YlYxLTUxaWpYc0UwamNEOTh1UE5lT3Bib2F2NTQyTnVvU3J1eEdfTDlZRG9aaC1CZm04d2dVUEpabVUwMWRNeUxTTmVEVWZfdFZYdUJjUS1fTzdJeW5qNW80UUExMU9XbVo2cGN5RmFXSm1KSURLcWlqQ0dLVkJ4ZWRSYVJrSHNZSmg0MGxwQk9vQTBjcjdxTSIsIm9yaWdpbiI6Imh0dHBzOi8vbG9jYWxob3N0Ojg0NDMiLCJjaWRfcHVia2V5IjoidW51c2VkIn0";
            String appId = "https://localhost:8443";
            
            boolean result = registrationData.verify(clientData, appId);
            
    }
    
}
