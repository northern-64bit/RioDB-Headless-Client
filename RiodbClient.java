/*
MIT License
Copyright (c) 2022 northern-64bit
*/

package client;

import java.io.*;
import java.util.*;
import java.net.*;
import java.net.http.*;

public class RiodbClient {

	static final String DEFAULT_HOST = "http://localhost:2333";
	static final Boolean IF_WINDOWS = System.getProperty("os.name").contains("Windows");

	public static void main(String[] args) {

		// flag if user provided HTTP or HTTPS server address. HTTP by default.
		String requestType = "HTTP";

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
			if (Objects.equals(strUrl, "")) {
				strUrl = DEFAULT_HOST;
			} else if (strUrl != null && (strUrl.contains("https://") || strUrl.contains("HTTPS://"))) {
				requestType = "HTTPS";
				authorization = PostHTTPS.getCredentials(input);
			} else if (strUrl != null && !strUrl.startsWith("https://") && !strUrl.startsWith("http://")) {
				strUrl = "http://" + strUrl;
			}

			// test the connection to the provided server
			if (requestType.equals("HTTP") && PostHTTP.testURL(strUrl)) {
				connectionSuccess = true;
			} else if (requestType.equals("HTTPS") && PostHTTPS.testURL(strUrl, authorization)) {
				connectionSuccess = true;
			} else {
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
}