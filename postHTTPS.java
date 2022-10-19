/*
MIT License
Copyright (c) 2022 northern-64bit
*/

package client;

import java.io.*;
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

public final class PostHTTPS {

	public static String getCredentials(Scanner input) {

		// console object
		Console cnsl = System.console();

		// Read line
		String userStr = cnsl.readLine("%nEnter username:%n%n> ");

		// Read password
		// into character array
		char[] userPwd = cnsl.readPassword("%nEnter password:%n%n> ");

		return Base64.getEncoder().encodeToString((userStr + ":" + new String(userPwd)).getBytes());
	}

	public static boolean testURL(String strUrl, String authorization) {

		String r = sendPost(strUrl, "", authorization);
		if (r != null && (r.contains("200") || r.contains("401"))) {
			return true;
		}
		System.out.println(r);
		return false;

	}

	/**
	 * A simple implementation to pretty-print JSON file.
	 *
	 * @param unformattedJsonString
	 * @return
	 */
	public static String prettyPrintJSON(String unformattedJsonString) {
		StringBuilder prettyJSONBuilder = new StringBuilder();
		int indentLevel = 0;
		boolean inQuote = false;
		for (char charFromUnformattedJson : unformattedJsonString.toCharArray()) {
			switch (charFromUnformattedJson) {
			case '"':
				// switch the quoting status
				inQuote = !inQuote;
				prettyJSONBuilder.append(charFromUnformattedJson);
				break;
			case ' ':
				// For space: ignore the space if it is not being quoted.
				if (inQuote) {
					prettyJSONBuilder.append(charFromUnformattedJson);
				}
				break;
			case '{':
			case '[':
				// Starting a new block: increase the indent level
				prettyJSONBuilder.append(charFromUnformattedJson);
				indentLevel++;
				appendIndentedNewLine(indentLevel, prettyJSONBuilder);
				break;
			case '}':
			case ']':
				// Ending a new block; decrese the indent level
				indentLevel--;
				appendIndentedNewLine(indentLevel, prettyJSONBuilder);
				prettyJSONBuilder.append(charFromUnformattedJson);
				break;
			case ',':
				// Ending a json item; create a new line after
				prettyJSONBuilder.append(charFromUnformattedJson);
				if (!inQuote) {
					appendIndentedNewLine(indentLevel, prettyJSONBuilder);
				}
				break;
			default:
				prettyJSONBuilder.append(charFromUnformattedJson);
			}
		}
		return prettyJSONBuilder.toString();
	}

	/**
	 * Print a new line with indention at the beginning of the new line.
	 * 
	 * @param indentLevel
	 * @param stringBuilder
	 */
	private static void appendIndentedNewLine(int indentLevel, StringBuilder stringBuilder) {
		stringBuilder.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			// Assuming indention using 2 spaces
			stringBuilder.append("  ");
		}
	}

	public static String sendPost(String urlStr, String payload, String authorization) {

		String responseStr = "";
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

			URL urlObj = new URL(urlStr);

			HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
			URL object = new URL(urlStr);

			// connection timeout 15 seconds
			con.setConnectTimeout(15000);
			con.setReadTimeout(15000);
			con = (HttpsURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "text/plain");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("pretty", "true");
			con.setRequestMethod("POST");

			// Setting authorization
			con.setRequestProperty("Authorization", "Basic " + authorization);

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
				responseStr = prettyPrintJSON(sb.toString());

			} else {
				responseStr = "Status code: " + responseCode;
			}

		} catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
			responseStr = "error: " + e.getMessage();
		}

		return responseStr;

	}

}