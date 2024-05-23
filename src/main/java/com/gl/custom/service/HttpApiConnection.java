package com.gl.custom.service;

import com.gl.custom.model.AuthApiResponse;
import com.gl.custom.model.CustomApiResponse;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.time.Duration;
import java.util.List;

import static com.gl.custom.dao.CustomQuery.getValueFromSysParam;

public class HttpApiConnection {
    private static final Logger logger = LogManager.getLogger(HttpApiConnection.class);

    public static void main(String[] args) {

        var BASE_URL = "http://staging.gateway.customs.gov.kh:82/ecustoms/dmc-interface";
        var VERSION = "v1";
        var CLIENT_ID = "2";
        var SECRET_KEY = "QNzhRKixNfStVkomv5S1XsQdgb3ufgAiktF2wPMz";
        var url = "http://staging.gateway.customs.gov.kh:82/ecustoms/dmc-interface/api/v1/oauth/token";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", CLIENT_ID);
        map.add("secret_key", SECRET_KEY);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpConnectionForApp(url, headers, map);
    }

    public static CustomApiResponse getDataFromApi(Connection conn, String imei) {
        String token = authenticationApi(conn);
        return taxCheckApi(conn, token, imei);
    }

    private static CustomApiResponse taxCheckApi(Connection conn, String token, String imei) {
        var url = "http://staging.gateway.customs.gov.kh:82/ecustoms/dmc-interface/api/v1/imei-tax-check";
        //   String header =  'Authorization: Bearer 12345
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("imei", imei);
        String res = httpConnectionForApp(url, headers, map);
        CustomApiResponse body = new Gson().fromJson(res, CustomApiResponse.class);
        return body;
    }

    private static String authenticationApi(Connection conn) {
        var url = getValueFromSysParam(conn, "CustomAuthApiUriPath");
        var clientId = getValueFromSysParam(conn, "CustomAuthApiClientId");
        var secretKey = getValueFromSysParam(conn, "CustomAuthApiSecretKey");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("secret_key", secretKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        try {
            var response = httpConnectionForApp(url, headers, map);
            JSONObject json = (JSONObject) new JSONParser().parse(response);
            logger.info("json string {}", json);
            AuthApiResponse body = new Gson().fromJson(response, AuthApiResponse.class);
            //      CustomApiResponse body = new Gson().fromJson(response, CustomApiResponse.class);
            return body.getAccess_token();
        } catch (Exception e) {
            logger.error("Not able get token " + e + " :: " + e.getCause());
            return null;
        }
    }


    static String httpConnectionForApp(String url, HttpHeaders headers, MultiValueMap<String, String> map) {
        try {
            logger.info("Http Connection Body Created ");
            HttpEntity<MultiValueMap<String, String>> request = null;
            ResponseEntity<String> httpResponse = null;
            String respons = null;
            URI uri = new URI(url);
            final RestTemplate restTemplate = new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofMillis(10000))
                    .setReadTimeout(Duration.ofMillis(10000))
                    .build();

            logger.info("Http Connection Header Created ");
            request = new HttpEntity<>(map, headers);
            httpResponse = restTemplate.postForEntity(uri, request, String.class);
            respons = httpResponse.getBody();
            logger.info("Request:" + url + " Body:" + map.toString() + " Response :" + respons);
            return respons;
        } catch (Exception e) {
            logger.error("Not able to http Api  " + e + " :: " + e.getCause());
        }
        return null;
    }

    public void sendDataViaPostApi(String url, String body) {
        StringBuffer response = new StringBuffer();
        logger.info("POST  Start Url-> " + url + " ;Body->" + body);
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");// For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
            os.close();// For POST only - END
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                logger.info(" Response:" + response.toString());

            } else {
                logger.warn("POST request not worked");
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public static void postRequesFormData() {
        String url = "https://example.com/api/endpoint";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("key1", "value1");
        formData.add("key2", "value2");

        // Wrap the form data in an HttpEntity object
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.println("Response: " + response.getBody());
    }


}

//  String body = "{ \"alertId\": \"" + alertId + "\", \"alertMessage\": \"" + alertmsg + "\", \"alertProcess\": \"FileCopier\"}";
