package Main;

import Entity.SnakeGameUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Asnit Bakhati
 */
public class APIRequest {

    public static final String baseURL = "https://ecommercedeploy-35yl.onrender.com/";

    private static String getDigitalSignature(String username, int score) {
        try {
            String key = "QxyEWs0GCS7YAb0XklnEteVnVLe1ZrQoQ2L2vyXeR3k=";
            String toHash = "username : " + username + " , score : " + score;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(toHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder ans = new StringBuilder();
            for (byte b : bytes) {
                ans.append(String.format("%02x", b));
            }
            return ans.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void sendPutRequest(String username, int score) {
        HttpURLConnection conn = null;
        try {
            String signature = getDigitalSignature(username, score);

            String encodedUser = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
            String urlString = baseURL + "api/snakeGame/update/" + encodedUser + "/" + score + "?sign=" + signature;

            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Score updated successfully!");
            } else if (responseCode == 401) {
                System.out.println("Security Error: Invalid Signature (Unauthorized).");
            } else {
                System.out.println("Failed to update. Server returned: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Critical Error during PUT request: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<SnakeGameUser> fetchHighScores() {
        try {
            URL url = new URL(APIRequest.baseURL+"api/snakeGame/get/top10");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Server returned error code: " + responseCode);
                return new ArrayList<>();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            List<SnakeGameUser> scores = objectMapper.readValue(response.toString(), new TypeReference<List<SnakeGameUser>>() {});
            return scores != null ? scores : new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException("Cannot load HighScores.");
        }
    }

    public static boolean isServerAvailable() {
        try {
            URL url = new URL(baseURL + "api/snakeGame/connect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
}
