package com.stetis.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpUtils {
    public static final String TERMINAL = "terminal";
    public static final String LPOS = "lpos";
    public static final String RETRIEVELPOS = "lpos?storeid=%s&fromdate=%s&todate=%s";
    public static final String RETRIEVEREQUEUSTS = "stores/items/requests?storeid=%s&fromdate=%s&todate=%s";
    public static final String ACCREDITATION = "accreditation";
    public static final String LOGIN = "auth";
    public static final String STORES = "stores";
    public static final String ITEMSUPDATE = "stores/itemsupdate";
    public static final String STOREITEMS = "stores/items?storeid=%s";
    public static final String CREATEITEMREQUEST = "stores/items/requests";
    public static final String SUPPLIERS = "suppliers";
    public static final String FINGERUPLOAD = "fingers";
    public static final String FINGER = "fingers/auth";
    public static final String RECIPIENTIDS = "accreditation/recipientids?clientid=%s&maxrecord=%s";
    public static final String ENROLMENT = "enrolment";
    public static final String TERMINAL_VALIDATION = "terminal/validation";
    public static final String TERMINAL_VERIFICATION = "terminal/verification";
    public static final String RETRIEVEFINGER = "finger/%s";
    public static final Object PASSWORDRESET = "auth/pinreset";


    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }

    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }



    public static String baseUrlV2(){

        //return "http://172.32.1.10:8180/sams-web/api/v1/%s";
        //return "http://41.73.7.115:8180/sams-web/api/v1/%s";
        //return "http://sams.com.ng:8181/sams-web/api/v1/%s";
        //return "http://ws.sams.com.ng/sams-web/api/v1/%s";
        //return "http://69.64.87.150/sams-web/api/v2/%s";
        //return "http://192.168.43.81:5000/mcdream/api/v1/%s";
        //return "http://10.0.2.2:3000/mcdream/api/v1/%s";
        return "http://192.168.88.138:5000/mcdream/api/v1/%s";

    }

    public static String localServer(){

        //return "http://172.32.1.10:8180/sams-web/api/v1/%s";
        //return "http://41.73.7.115:8180/sams-web/api/v1/%s";

        //return "http://sams.com.ng:8181/sams-web/api/v1/%s";
        //return "http://ws.sams.com.ng/sams-web/api/v1/%s";
        //return "http://69.64.87.150/sams-web/api/v2/%s";
        
        return "http://192.168.88.42:8080/Payara/api/finger";
    }
}
