package com.gl.BlackList;

import com.gl.BlackList.Dao.BlacklistServiceDao;
import com.gl.BlackList.model.BlacklistTacDb;
import com.gl.rule_engine.RuleInfo;
import com.gl.utils.LogWriter;
import com.google.gson.Gson;
import java.net.URI;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Duration;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
 
public class EncriptonBlacklistService {

    static final Logger logger = LogManager.getLogger(EncriptonBlacklistService.class);
    static Cipher cipher;

    public static String startBlacklistApp(RuleInfo re) {
        LogWriter logWriter = new LogWriter();
        String status = null;
        BlacklistServiceDao blacklist = new BlacklistServiceDao();
        String rslt = blacklist.getBlacklistStatus(re.connection, re.imei);
        if (rslt == null) {
            String logpath = blacklist.getValueFromSysParam(re.connection, "GSMA_BLACKLIST_LogPath");
            logWriter.writeLogBlacklist(logpath, "Start with Imei " + re.imei);
            String message = verifyGSMA(re);
            logWriter.writeLogBlacklist(logpath, "End Result for  " + re.imei + " :: " + message);
            BlacklistTacDb blacklistTacDb = (new Gson().fromJson(message, BlacklistTacDb.class));
            if (blacklistTacDb.getResponsestatus().equalsIgnoreCase("success")) {
                status = blacklist.databaseMapper(blacklistTacDb, re.connection);
            } else {
                logger.warn("Not able to retrieve info for imei  =" + re.imei + ", Response: " + message);
            }
        } else {
            status = rslt;
        }
        logger.debug("Final status" + status);
        return status;
    }

    public static String verifyGSMA(RuleInfo re) {
        var blacklist = new BlacklistServiceDao();
        String url = blacklist.getValueFromSysParam(re.connection, "GSMA_BLACKLIST_URL");
        String APIKey = blacklist.getValueFromSysParam(re.connection, "GSMA_BLACKLIST_APIKey");
        String Password = blacklist.getValueFromSysParam(re.connection, "GSMA_BLACKLIST_Password");
        String Salt_String = blacklist.getValueFromSysParam(re.connection, "GSMA_BLACKLIST_Salt_String");
        String Secretkey = blacklist.getValueFromSysParam(re.connection, "GSMA_BLACKLIST_Secretkey");
        String partnerid = blacklist.getValueFromSysParam(re.connection, "GSMA_BLACKLIST_Partnerid");
        String Organization_Id = blacklist.getValueFromSysParam(re.connection, "GSMA_BLACKLIST_Organization_Id");

        String abc = getSHA(APIKey + Password + re.imei);
        String auth = encrypt(Salt_String + Organization_Id + "=" + abc, Secretkey);
        logger.debug("the auth key is =" + auth);

        URI uri = null;
        HttpHeaders headers = null;
        MultiValueMap<String, String> map = null;
        HttpEntity<MultiValueMap<String, String>> request = null;
        ResponseEntity<String> httpResponse = null;
        String respons = null;
        try {
            uri = new URI(url);
            final RestTemplate restTemplate = new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofMillis(10000))
                    .setReadTimeout(Duration.ofMillis(10000))
                    .build();
            headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorisation", auth);
            map = new LinkedMultiValueMap<>();
            map.add("deviceid", re.imei);
            map.add("partnerid", partnerid);

            request = new HttpEntity<>(map, headers);

            httpResponse = restTemplate.postForEntity(uri, request, String.class);
            logger.info("Response Body:  [" + httpResponse.getBody() + "]");
            respons = httpResponse.getBody();
        } catch (Exception ex) {
            logger.info(" Error :" + ex);
            respons = "NAN";
        }
        return respons;
    }

    public static String getSHA(String stringToHash) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(stringToHash.getBytes("UTF-8"), 0, stringToHash.length());

            byte byteData[] = md.digest();

            StringBuffer hashValue = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) {
                    hashValue.append('0');
                }
                hashValue.append(hex);
            }
            return hashValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String stringToEncrypt, String secretkey) {
        try {
            String algorithm = "AES";// AES encrption
            String algorithm_mode_padding = "AES/ECB/PKCS5Padding"; // algorithm_mode_padding
            String encryptedValue = encrypt(stringToEncrypt, secretkey, algorithm, algorithm_mode_padding);
            return encryptedValue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String stringToEncrypt, String secretkey, String algorithm,
            String algorithm_mode_padding) throws Exception {
        SecretKeySpec secret = new SecretKeySpec(secretkey.getBytes("UTF-8"), algorithm);
        if (cipher == null) {
            cipher = Cipher.getInstance(algorithm_mode_padding);// Algorithm/mode/padding
        }
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        String encryptedString = Base64.getEncoder().encodeToString(cipher.doFinal(stringToEncrypt.getBytes()));
        return encryptedString;
    }

}
