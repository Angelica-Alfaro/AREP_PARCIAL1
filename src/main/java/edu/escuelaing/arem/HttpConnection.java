package edu.escuelaing.arem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {

    private static final String USER_AGENT = "Mozilla/5.0";
    
    public HttpConnection(){
    }

	public static String getClima() throws IOException {
    	String finalResponse = "None";
        URL obj = new URL("https://api.openweathermap.org/data/2.5/weather?q=London&appid=e6d589177c6d5fbf9467ccb98fab7dfb");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            finalResponse = response.toString();
            System.out.println(finalResponse);
        }
        
        else {
            System.out.println("GET request not worked");
        }
        
        System.out.println("GET DONE");
        return finalResponse;
    }
}
