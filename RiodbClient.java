/*
MIT License
Copyright (c) 2022 northern-64bit
*/

package client;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RiodbClient {

	static final String DEFAULT_HOST = "https://localhost:2333";
	static final Boolean IF_WINDOWS = System.getProperty("os.name").contains("Windows");

	public static void main(String[] args) {

		// flag if user provided HTTP or HTTPS server address. HTTP by default.
		String requestType = "";

		// RioDB server url
		String strUrl = "";

		// auth string
		String authorization = null;

		// scanner object
		Scanner input = new Scanner(System.in);

		System.out.printf("%nInitialising \u001B[33m RioDB Headless Client V.1 \u001B[0m"
				+ "%nThanks for using \u001B[33m RioDB Headless Client V.1 \u001B[0m !!! %n"
				+ "Copyright (c) 2022 northern-64bit %nMIT License %n%nSet up:%n");

		// Testing connection
		boolean connectionSuccess = false;

		while (!connectionSuccess) {

			// Get RioDB server address, and determine if HTTPS
			System.out.printf("%nEnter a valid host [" + DEFAULT_HOST + "]: %n%n> ");
			strUrl = input.nextLine();

			// If user didn't provide a host, we go with default. 
			if (strUrl == null || Objects.equals(strUrl, "")) {
				strUrl = DEFAULT_HOST;
			} 
			// if user didn't specify http:// or https://, we go with https://
			else if (!strUrl.startsWith("http")) {
				strUrl = "https://" + strUrl;
			}

			if (!isValidUrl(strUrl)) {
				System.err.printf("%n\u001B[31m Invalid URL: " + strUrl + " \u001B[0m");
			} else if (strUrl.contains("https://") || strUrl.contains("HTTPS://")) {
				requestType = "HTTPS";
				authorization = PostHTTPS.getCredentials(input);
				if (PostHTTPS.testURL(strUrl, authorization)) {
					connectionSuccess = true;
				}
			} else if (strUrl.startsWith("http://") || strUrl.startsWith("http://")) {
				requestType = "HTTP";
				if (PostHTTP.testURL(strUrl)) {
					connectionSuccess = true;
				}
			} else {
				System.err.printf("%n\u001B[31m Invalid URL: " + strUrl + " \u001B[0m");
			}

			if (!connectionSuccess) {
				System.err.printf("%n\u001B[31m Unable to connect to " + strUrl + " \u001B[0m");
			}

		}
		System.out.printf("%n\u001B[32m Connected to " + strUrl + " \u001B[0m%n");

		// run statements...
		String statement = "";
		System.out.printf("%nEnter statement:%n%n> ");
		while (input.hasNext()) {
			String s = input.nextLine();
			if (s != null && s.equals("exit;")) {
				break;
			} else if (s != null && s.endsWith(";")) {
				statement = statement + s; // add last line and wrap whole thing with double quotes.
				if (Objects.equals(requestType, "HTTP")) {
					System.out.printf(PostHTTP.sendPost(strUrl, statement) + "%n%n> "); // make HTTP POST request
				} else if (Objects.equals(requestType, "HTTPS")) {
					System.out.printf(PostHTTPS.sendPost(strUrl, statement, authorization) + "%n%n> ");
				}

				statement = ""; // reset to start next statement

			} else if (s != null && s.equals("clear")) {
				try {
					if (IF_WINDOWS) {
						new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
					} else {
						System.out.print("\033[H\033[2J"); // Clears Screen
						System.out.flush(); // Reset cursor position
					}
				} catch (IOException | InterruptedException ex) {
				}

			} else if (s != null && s.equals(".")) {
				statement = ""; // reset

			} else {
				statement = statement + "\n" + s;
			}
			// System.out.print("\n > ");
		}

		input.close();
		System.out.printf("%nExiting the program. Good bye and have a splendid day!");
	}

	private static boolean isValidUrl(String s) {
		String pattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		try {
			Pattern patt = Pattern.compile(pattern);
			Matcher matcher = patt.matcher(s);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}
}