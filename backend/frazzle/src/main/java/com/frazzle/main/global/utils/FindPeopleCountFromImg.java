package com.frazzle.main.global.utils;

import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Slf4j
@Component
public class FindPeopleCountFromImg {

    @Value("${google.vision.api}")
    private static String apiKey;

    // MultipartFile을 입력받는 메서드
    public static int analyzeImageFile(MultipartFile image) {
        log.info("analyzeImageFile");
        // 사람 최대 인원 수
        int maxResults = 10;

        try {
            String response = detectFaces(image, maxResults, apiKey);
            int faceCount = countFaces(response);

            log.info("Face count: " + faceCount);
            return faceCount;
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FAILED_CONVERT_FILE.getMessage());
        }
    }

    /** 주어진 MultipartFile에서 얼굴을 감지하고 응답을 문자열로 반환합니다. */
    private static String detectFaces(MultipartFile file, int maxResults, String apiKey) throws IOException {
        log.info("detectFaces");
        byte[] data = file.getBytes();
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

    /** 응답에서 감지된 얼굴 수를 계산합니다. */
    private static int countFaces(String jsonResponse) {
        log.info("countFaces");
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
