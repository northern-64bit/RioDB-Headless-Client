/*

MIT License

Copyright (c) 2022 northern-64bit

*/

import java.io.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;
import java.net.*;
//import static org.junit.Assert.*;
import java.util.ArrayList;

public class Client {

    static final String DEFAULT_HOST = "http://localhost:2333";

    public static boolean testURL(String strUrl) {


        try {

            URL url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            return true;
        } catch (IOException  e) {
            e.printStackTrace();
            return false;
        }
    }

    private final static HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void sendPost(String statement, String host)  {
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        httpRequestBuilder.uri(URI.create(host + "/?pretty=true"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json");
        httpRequestBuilder.POST(BodyPublishers.ofString("\""+ statement +"\""));


        HttpRequest request = httpRequestBuilder.build();
        HttpResponse<String> response;


        try {
            response = httpClient.send(request, BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                System.out.println("Status code: "+ response.statusCode());
            }

            System.out.println(response.body().formatted().toString());


        } catch (IOException | InterruptedException e) {
            System.out.printf("\u001B[31m Error%n \u001B[0m");
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        System.out.println("Initialising \u001B[33m RioDB Headless Client V.1 \u001B[0m");

        System.out.printf("%nThanks for using \u001B[33m RioDB Headless Client V.1 \u001B[0m !!! %nCopyright (c) 2022 northern-64bit %nMIT License %n%nSet up:");

        Scanner input = new Scanner(System.in);

        System.out.printf("Enter a valid host ["+DEFAULT_HOST+"] %n> ");
        String strUrl = input.nextLine();
        if (Objects.equals(strUrl, "")){
            strUrl = DEFAULT_HOST;
        }

        // Testing connection
        while (!testURL(strUrl)){
            System.err.println("\u001B[31m Error creating HTTP connection\u001B[0m");
            System.out.print("Enter a valid host ["+DEFAULT_HOST+"]: ");
            strUrl = input.nextLine();
            if (Objects.equals(strUrl, "")){
                strUrl = DEFAULT_HOST;
            }
        }
        System.out.print("\u001B[32m Connected to "+ strUrl +" \u001B[0m");
        String host = strUrl;


        String statement = "";
        ArrayList<String> arrayList = new ArrayList<String>();
        System.out.printf("%n> ");
        while (input.hasNext()) {
            String s = input.nextLine();
            if (s != null && s.equals("exit;")){
                break;
            } else if (s != null && s.endsWith(";")){
                statement =  statement + s  ;  // add last line and wrap whole thing with double quotes.
                arrayList.add(statement);  // add to command history
                sendPost(statement, host); // make HTTP POST request
                statement = ""; // reset to start next statement
            } else if (s != null && s.equals("clear")) {
                System.out.print("\033[H\033[2J"); // Clears Screen
                System.out.flush(); // Reset cursor position
            } else if (s != null && s.equals(".")) {
                statement = "" ;  // reset
            } else {
                statement = statement + "%n" + s;
            }
            System.out.print("\n> ");

        }
        input.close();
        System.out.print("Good bye!");
    }
}
