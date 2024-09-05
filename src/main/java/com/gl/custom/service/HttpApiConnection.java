package com.gl.custom.service;

import com.gl.custom.dao.CustomQuery;
import com.gl.custom.model.AuthApiResponse;
import com.gl.custom.model.CustomApiResponse;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
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

import static com.gl.custom.dao.CustomQuery.*;

public class HttpApiConnection {
    private static final Logger logger = LogManager.getLogger(HttpApiConnection.class);

    public static CustomApiResponse getDataFromApi(Connection conn, String imei) {
        String token = getTokenDetails(conn);
        if (token != null) {
            return getCustomApiResponse(conn, imei, token);
        } else {
            var a = authenticationApi(conn);
            if (a != null && a.getStatusCodeValue() == 200) {
                AuthApiResponse b = new Gson().fromJson(a.getBody(), AuthApiResponse.class);
                insertIntoCustomAuthToken(b, conn);
                return getCustomApiResponse(conn, imei, b.getAccess_token());
            } else {
                return new CustomApiResponse("Error", "Client authentication failed");
            }
        }

    }

    private static void insertIntoCustomAuthToken(AuthApiResponse b, Connection conn) {
        saveInGdceauthtoken(conn, b.getAccess_token(), b.getExpires_in());
    }

    private static String getTokenDetails(Connection conn) {
        return getTokenFromGdceAuthToken(conn);
    }

    private static CustomApiResponse getCustomApiResponse(Connection conn, String imei, String token) {
        var c = taxCheckApi(conn, token, imei);
        if (c != null && c.getStatusCodeValue() == 200) {
            CustomApiResponse body = new Gson().fromJson(c.getBody(), CustomApiResponse.class);
            return body;
        } else {
            return new CustomApiResponse("Error", "Imei Check Api Response Fail");
        }
    }
//

    private static ResponseEntity<String> taxCheckApi(Connection conn, String token, String imei) {
        var url = getValueFromSysParam(conn, "CustomCheckImeiApiUrlPath");
        var camDxHeaderName = getValueFromSysParam(conn, "CamDxLayerHeaderName");
        var camDxHeaderValue = getValueFromSysParam(conn, "CamDxLayerHeaderValue");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(token);
        headers.set(camDxHeaderName, camDxHeaderValue);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("imei", imei);
        return httpConnectionForApp(url, headers, map ,conn);
    }

    private static ResponseEntity<String> authenticationApi(Connection conn) {
        var url = getValueFromSysParam(conn, "CustomAuthApiUrlPath");
        var clientId = getValueFromSysParam(conn, "CustomAuthApiClientId");
        var secretKey = getValueFromSysParam(conn, "CustomAuthApiSecretKey");
        var camDxHeaderName = getValueFromSysParam(conn, "CamDxLayerHeaderName");
        var camDxHeaderValue = getValueFromSysParam(conn, "CamDxLayerHeaderValue");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set(camDxHeaderName, camDxHeaderValue);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("secret_key", secretKey);
        try {
            ResponseEntity<String> response = httpConnectionForApp(url, headers, map,conn);
            logger.info(" Auth Response Code {} with  {}", response.getStatusCodeValue(), response.getBody());
            return response;
        } catch (Exception e) {
            logger.error("Not able get token " + e + " :: " + e.getCause());
            return null;
        }
    }



    static ResponseEntity<String> httpConnectionForApp(String url, HttpHeaders headers, MultiValueMap<String, String> map,Connection conn) {
        try {
            logger.info("POST  Start Url-> " + url + " ;Body->" + map.toString() + " ::: Headers:" + headers);
            HttpEntity<MultiValueMap<String, String>> request = null;
            ResponseEntity<String> httpResponse = null;
            URI uri = new URI(url);
            final RestTemplate restTemplate = new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofSeconds(120))
                    .setReadTimeout(Duration.ofSeconds(120))
                    .build();
            request = new HttpEntity<>(map, headers);
            httpResponse = restTemplate.postForEntity(uri, request, String.class);
            logger.info("Request:" + url + " Body:" + map.toString() + " Response :" + httpResponse.getBody());
            return httpResponse;
        } catch (HttpClientErrorException he) {
            logger.error("HttpClientErrorException  " + he + " :: " + he.getResponseBodyAsString());
            CustomQuery.deleteFromGdceAuthToken(conn);  // refactor
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

// insert into sys_param (tag,feature_name) values ('CustomAuthApiUrlPath','https://stage-gateway.customs.gov.kh/dmc-interface/api/v1/oauth/token','CustomCheckImei');
//  insert into sys_param (tag ,value,feature_name) values ('CustomCheckImeiApiUrlPath','https://stage-gateway.customs.gov.kh/dmc-interface/api/v1/oauth/token','CustomCheckImei');
// insert into sys_param (tag ,value,feature_name) values ('CustomAuthApiClientId','2','CustomCheckImei');
// insert into sys_param (tag ,value,feature_name) values ('CustomAuthApiSecretKey','QNzhRKixNfStVkomv5S1XsQdgb3ufgAiktF2wPMz','CustomCheckImei');

// f (i.getStatusCodeValue() == 200) {
//            CustomApiResponse body = new Gson().fromJson(i.getBody(), CustomApiResponse.class);
//            return body;
//        } else {
//            return null;
//        }
//
//        var response =
//        if (response.equalsIgnoreCase("ERROR")) {
//            return null;
//        } else {
//            var a = taxCheckApi(conn, response, imei);
//            if (a.getStatusCodeValue() == 200) {
//                CustomApiResponse body = new Gson().fromJson(a.getBody(), CustomApiResponse.class);
//                return body;
//            } else {
//                return null;
//            }
//            //  CustomApiResponse body = new Gson().fromJson(res, CustomApiResponse.class);
//            //   return body;
//        }
//    }