/*

MIT License

Copyright (c) 2022 northern-64bit

*/

package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class postHTTP {

    public static void sendPost(String host, String statement)  {
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        httpRequestBuilder.uri(URI.create(host + "/?pretty=true"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json");
        httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofString("\""+ statement +"\""));

        HttpRequest request = httpRequestBuilder.build();
        HttpResponse<String> response;

        try {
            response = Client.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                System.out.printf("%nStatus code: "+ response.statusCode() + "%n");
            }

            System.out.printf("%n" + response.body().formatted().toString() + "%n");


        } catch (IOException | InterruptedException e) {
            System.out.printf("%n\u001B[31m Error \u001B[0m%n");
            e.printStackTrace();
        }

    }
}
