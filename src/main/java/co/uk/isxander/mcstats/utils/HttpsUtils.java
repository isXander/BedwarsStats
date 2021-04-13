package co.uk.isxander.mcstats.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class HttpsUtils {

    public static HttpsResponse getString(String url) throws IOException {
        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestMethod("GET");
        httpClient.setUseCaches(false);
        httpClient.setRequestProperty("User-Agent", "McStats/1.0");
        httpClient.setReadTimeout(15000);
        httpClient.setConnectTimeout(15000);
        httpClient.setDoOutput(true);

        BufferedReader in;
        try {
            in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()));
        } catch (IOException e) {
            throw e;
        }


        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        return new HttpsResponse(response.toString(), httpClient.getResponseCode());
    }

    public static class HttpsResponse {
        private final String data;
        private final int responseCode;

        public HttpsResponse(String data, int response) {
            this.data = data;
            this.responseCode = response;
        }

        public String getData() {
            return data;
        }

        public int getResponseCode() {
            return responseCode;
        }
    }

}
