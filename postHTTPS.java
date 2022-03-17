/*

MIT License

Copyright (c) 2022 northern-64bit

*/

package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Console;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class postHTTPS {

    public static String getCredentials(Scanner input) {
        Console console = System.console();
        System.out.printf("%nEnter username:%n%n > ");
        String userStr = input.nextLine();
        char[] passwordArray = console.readPassword("Enter your password: ");
        String pwdStr = new String(passwordArray);

        return Base64.getEncoder().encodeToString((userStr + ":" + pwdStr).getBytes());
    }

    public static void testConnection(String urlStr) {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLContext.setDefault(sc);

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL urlObj = new URL(urlStr);

            HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
            URL object = new URL(urlStr);

        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {}
    }

    public static void sendPost(String urlStr, String payload, String authorization) {

        try {

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLContext.setDefault(sc);

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL urlObj = new URL(urlStr + "/?pretty=true");

            HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
            URL object = new URL(urlStr);

            // connection timeout 5 seconds
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con = (HttpsURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            // Setting authorization
            con.setRequestProperty("Authorization", authorization);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            if (payload != null) {
                wr.write(payload);
                wr.flush();
            }

            StringBuilder sb = new StringBuilder();
            int responseCode = con.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                String response = sb.toString();

                System.out.printf("%n" + response + "%n");

            } else {
                System.out.printf("%nStatus code: " + responseCode + "%n");
            }

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            System.out.println(e.getMessage());
        }


    }

}
