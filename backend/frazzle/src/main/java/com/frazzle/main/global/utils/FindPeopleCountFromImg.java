package com.frazzle.main.global.utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class FindPeopleCountFromImg {

    @Value("${google.vision.api}")
    private static String apiKey;

    //사진 url 입력받는 메서드 
    public static int inputImgUrl(String imgUrl) {
        //사진 경로
        Path path = Path.of(imgUrl);
        
        //사람 최대 인원 수
        int maxResults = 10;

        try {
            String response = detectFaces(path, maxResults, apiKey);
            int faceCount = countFaces(response);
            
            return faceCount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return 0;
    }

    /** 주어진 경로에 있는 이미지에서 얼굴을 감지하고 응답을 문자열로 반환합니다. */
    public static String detectFaces(Path path, int maxResults, String apiKey) throws IOException {
        byte[] data = Files.readAllBytes(path);
        String base64Image = Base64.getEncoder().encodeToString(data);

        // JSON 요청 본문 구성
        JSONObject requestBody = new JSONObject();
        JSONArray requests = new JSONArray();
        JSONObject request = new JSONObject();
        JSONObject image = new JSONObject();
        image.put("content", base64Image);
        JSONObject feature = new JSONObject();
        feature.put("type", "FACE_DETECTION");
        feature.put("maxResults", maxResults);
        JSONArray features = new JSONArray();
        features.put(feature);
        request.put("image", image);
        request.put("features", features);
        requests.put(request);
        requestBody.put("requests", requests);

        // HTTP 요청 보내기
        URL url = new URL("https://vision.googleapis.com/v1/images:annotate?key=" + apiKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // 응답 읽기
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    /** 응답에서 감지된 얼굴 수를 계산합니다 */
    public static int countFaces(String jsonResponse) {
        JSONObject responseJson = new JSONObject(jsonResponse);
        JSONArray responses = responseJson.getJSONArray("responses");
        if (responses.length() > 0) {
            JSONObject response = responses.getJSONObject(0);
            if (response.has("faceAnnotations")) {
                JSONArray faceAnnotations = response.getJSONArray("faceAnnotations");
                return faceAnnotations.length();
            }
        }
        return 0;
    }
}