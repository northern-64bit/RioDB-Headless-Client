/*
MIT License
Copyright (c) 2022 northern-64bit
*/

package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class PostHTTP {
	
    public static boolean testURL(String strUrl) {
    
    	String r = sendPost(strUrl, "");
        return r != null && r.contains("200");

    }
	
	public static String sendPost(String host, String statement)  {
		
		String responseStr = "";
		
		HttpClient httpClient = HttpClient.newBuilder()
		            .version(HttpClient.Version.HTTP_2)
		            .build();

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
		httpRequestBuilder.uri(URI.create(host
				+ "/?pretty=true"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "text/plain");
        httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofString(statement));

        HttpRequest request = httpRequestBuilder.build();
        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
               responseStr = "Status code: "+ response.statusCode();
            } else {

            	responseStr = response.body().formatted().toString();
           
           }

        } catch (IOException | InterruptedException e) {
            responseStr = "%n\u001B[31m Error \u001B[0m%n" + e.getMessage();
        }
        
        return responseStr;

    }
}