package com.qiandai.fourfactors.common.utils;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SslContextUtils {
    private TrustManager trustAllManager;
    SSLContext sslcontext;
    HostnameVerifier allHostsValid;

    public SslContextUtils() {
        initContext();
    }

    private void initContext() {
        trustAllManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            }
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
            }
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{trustAllManager}, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        // Create all-trusting host name verifier
        allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    public void initHttpsConnect(HttpsURLConnection httpsConn) {
        httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());
        httpsConn.setHostnameVerifier(allHostsValid);
    }
}
