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
package org.raupachz.view;

import org.raupachz.data.Storage;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Messages;
import org.raupachz.u2f.U2F;
import org.raupachz.u2f.ClientData;
import org.raupachz.u2f.RegistrationData;

@Named
@ViewScoped
public class Register implements Serializable {

    @Inject
    private Storage storage;

    @Inject
    private U2F u2f;

    // We send these values to the token
    private String challenge;
    private String registeredKeys;

    // We receive these values from the token on postback
    private int errorCode;
    private String clientData;
    private String registrationData;

    @PostConstruct
    public void initialize() {
        challenge = u2f.challenge();
        registeredKeys = u2f.stringify(storage);
    }

    public String submit() {
        if (errorCode != 0) {
            String message = null;
            switch (errorCode) {
                case 1: message = "OTHER ERROR";
                        break;
                case 2: message = "BAD REQUEST";
                        break;
                case 3: message = "CONFIGURATION UNSUPPORTED";
                        break;
                case 4: message = "DEVICE_INELIGIBLE";
                        break;
                case 5: message = "TIMEOUT";
                        break;
            }
            Messages.addFlashGlobalError("U2F token was *not* registered: " + message);
        } else {
            ClientData _clientData = ClientData.of(clientData);
            RegistrationData _registrationData = RegistrationData.of(registrationData);

            boolean verify = _registrationData.verify(
                    new String(u2f.decode(clientData),StandardCharsets.UTF_8),
                    getAppId());

            if (verify) {
                storage.put(getVersion(), _registrationData.getKeyHandle(), _registrationData.getUserPublicKey());
                Messages.addFlashGlobalInfo("U2F token was registered.");
            } else {
                Messages.addFlashGlobalError("U2F token was *not* registered: signature mismatch.");
            }
        }

        return "index";
    }

    public String getAppId() {
        return "https://localhost:8443";
    }

    // readonly
    public String getVersion() {
        return "U2F_V2";
    }

    // readonly
    public String getChallenge() {
        return challenge;
    }

    // readonly
    public String getRegisteredKeys() {
        return registeredKeys;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getClientData() {
        return clientData;
    }

    public void setClientData(String clientData) {
        this.clientData = clientData;
    }

    public String getRegistrationData() {
        return registrationData;
    }

    public void setRegistrationData(String registrationData) {
        this.registrationData = registrationData;
    }

}
