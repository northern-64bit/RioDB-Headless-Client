/*

MIT License

Copyright (c) 2022 northern-64bit

*/

package client;

import java.io.*;
import java.util.*;
import java.net.*;
import java.net.http.*;


public class Client {

    static final String DEFAULT_HTTP_HOST = "http://localhost:2333";
    static final String DEFAULT_HTTPS_HOST = "https://localhost:2334";
    static final String DEFAULT_REQUEST = "HTTP";
    static final Boolean IF_WINDOWS = System.getProperty("os.name").contains("Windows");

    public static boolean testURL(String strUrl, String requestType) {

        try {
            URL url = new URL(strUrl);
            if (Objects.equals(requestType, "HTTP")) {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.connect();
            } else if (Objects.equals(requestType, "HTTPS")) {
                postHTTPS.testConnection(strUrl);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public final static HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();


    public static void main(String[] args) {
        System.out.printf("%nInitialising \u001B[33m RioDB Headless Client V.1 \u001B[0m");

        System.out.printf("%nThanks for using \u001B[33m RioDB Headless Client V.1 \u001B[0m !!! %nCopyright (c) 2022 northern-64bit %nMIT License %n%nSet up:%n");

        Scanner input = new Scanner(System.in);

        // Gets request type
        System.out.printf("Enter type of request (http or https)%n%n > ");
        String requestType = input.nextLine();
        requestType = requestType.toUpperCase(Locale.ROOT);
        String authorization = null;
        String defaultHost;
        if (Objects.equals(requestType, "HTTP")) {
            defaultHost = DEFAULT_HTTP_HOST;
        } else if (Objects.equals(requestType, "HTTPS")) {
            authorization = postHTTPS.getCredentials(input);
            defaultHost = DEFAULT_HTTPS_HOST;
        } else {
            System.out.printf("%nInvalid request type. Selecting default: [" + DEFAULT_REQUEST + "]%n");
            requestType = DEFAULT_REQUEST;
            if (Objects.equals(requestType, "HTTPS")) {
                defaultHost = DEFAULT_HTTPS_HOST;
            } else {
                defaultHost = DEFAULT_HTTP_HOST;
            }
        }

        System.out.printf("%nEnter a valid host [" + defaultHost + "]: %n%n > ");
        String strUrl = input.nextLine();
        if (Objects.equals(strUrl, "")) {
            strUrl = defaultHost;
        }

        // Testing connection
        while (!testURL(strUrl, requestType)) {
            System.err.println("\u001B[31m Error creating HTTP connection\u001B[0m");
            System.out.printf("Enter a valid host [" + defaultHost + "]: %n%n > ");
            strUrl = input.nextLine();
            if (Objects.equals(strUrl, "")) {
                strUrl = defaultHost;
            }
        }

        System.out.printf("%n\u001B[32m Connected to " + strUrl + " \u001B[0m%n");
        String host = strUrl;

        String statement = "";
        System.out.printf("%nEnter statement:%n%n > ");
        while (input.hasNext()) {
            String s = input.nextLine();
            if (s != null && s.equals("exit;")) {
                break;
            } else if (s != null && s.endsWith(";")) {
                statement = statement + s;  // add last line and wrap whole thing with double quotes.
                if (Objects.equals(requestType, "HTTP")) {
                    postHTTP.sendPost(host, statement); // make HTTP POST request
                } else if (Objects.equals(requestType, "HTTPS")) {
                    postHTTPS.sendPost(host, statement, authorization);
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
                statement = "";  // reset

            } else {
                statement = statement + "%n" + s;
            }
            System.out.print("\n > ");
        }

        input.close();
        System.out.printf("%nExiting the program. Good bye and have a splendid day!");
    }
}
