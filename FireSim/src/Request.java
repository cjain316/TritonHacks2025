import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpClient;
import java.util.Scanner;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {
    public static HttpClient client = HttpClient.newHttpClient();

    private String baseUrl;
    private String apiKey;

    public Request() {
        baseUrl = "https://maps.googleapis.com/maps/api/elevation/";
        getAPIkey("api.key");
    }

    private void getAPIkey(String dir){
        try {
            File myObj = new File(dir);
            Scanner myReader = new Scanner(myObj);
            apiKey = myReader.nextLine();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public String get(String endpoint) throws IOException {
        URL url = new URL(baseUrl + endpoint + "&key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream()
        ));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        conn.disconnect();
        return response.toString();
    }
}
