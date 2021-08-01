package com.stetis.utils;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by SnrMngrFinance on 18/12/2017.
 */

public class Crypto {

    private int L = 2;
    private int i;
    private BigInteger n, e, d;
    static String T;
    StringBuilder time;
    static int MAX_V_SIZE = 26;
    static int MAX_KEY_SIZE = 11;
    static int MAX_HOUR_SIZE = 10;
    static int MAX_T_SIZE = 36;

    public Crypto(String token) {
        this.T = token;
        //n = new BigInteger
        n = new BigInteger("253");
        d = new BigInteger("17");
        e = new BigInteger("13");
    }



    public static String getToken(String I, String K) {
        //  System.out.println("I>>>>>>>>>>> > >>>>>>" + I);
        //System.out.println("K>>>>>>>>>>> > >>>>>>" + K);
        int i;
        int j;
        String H;
        int len1;
        int len2;
        int len;
        String Token = "";
        char V[] = new char[MAX_V_SIZE + 1];
        char T[] = new char[MAX_T_SIZE + 1];
        for (int k = 0; k < I.length(); k++) {
            V[k] = I.charAt(k);
        }
        for (int k = 15; k < MAX_V_SIZE; k++) {
            V[k] = K.charAt(k - 15);
        }
        len1 = V.length;
        H = String.valueOf(System.currentTimeMillis() / 1000);
        len2 = H.length();
        if (len1 > MAX_V_SIZE) {
            len1 = MAX_V_SIZE;
        }
        if (len2 > MAX_HOUR_SIZE) {
            len2 = MAX_HOUR_SIZE;
        }
        len = len1 + len2;
        i = 0;
        for (j = 0; j < len1; j++) {
            if (j < len2) {
                T[i] = V[j];
                T[i + 1] = H.charAt(j);
                i = i + 2;
            } else {
                T[i] = V[j];
                i = i + 1;
            }
        }
        for (int k = 0; k < MAX_T_SIZE; k++) {
            Token = Token + T[k];
        }

        return Token;
    }

    /**
     * Method to extract device information from token
     *
     * @return
     */
    public Map<String, String> getDeviceinfo() {
        Map<String, String> deviceMap = new TreeMap<>();
        try {

            String I;
            String K;
            StringBuilder V = new StringBuilder("");
            time = new StringBuilder("");

            for (int j = 0; j < T.length(); j++) {
                if (time.length() < 10) {
                    if ((j % 2) == 0) {
                        V.append(T.charAt(j));
                    } else {
                        time.append(T.charAt(j));
                    }
                } else {
                    V.append(T.charAt(j));
                }
            }
            // System.out.printf("V : %s   time : %s", V.toString(), time.toString());

            String dd = V.toString();
            I = dd.substring(0, 15);
            deviceMap.put("IMEI", I);
            K = dd.substring(15, MAX_V_SIZE);
            deviceMap.put("KEY", K);
            // System.out.println("Imei : " + I + "    Key : " + K);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return deviceMap;
    }

    public boolean isTokenExpired() {
        boolean isExpired = true;
        long currentTime = System.currentTimeMillis() / 1000;
        long sessionStarts = Long.valueOf(time.toString());
        long sessionEnd = getTokenSession(sessionStarts);

        if (currentTime >= sessionStarts && currentTime <= sessionEnd) {
            isExpired = false;
        } else {
            isExpired = true;
        }
        return isExpired;
    }

    private long getTokenSession(long time) {

        return (time + (60 * L));
    }

    /**
     * Methpd to encrypt a single character
     *
     * @param x
     * @param y
     * @param n
     * @return
     */
    private BigInteger mult(BigInteger x, BigInteger y, BigInteger n) {
        return x.modPow(y, n);
    }

    /**
     * Method to Encrypt a plain text
     *
     * @param msg
     * @return cypher text
     */
    public String encrypt(String msg) {
        StringBuilder inttext = new StringBuilder();
        BigInteger ct[] = new BigInteger[msg.length()];
        for (i = 0; i < msg.length(); i++) {
            BigInteger x = new BigInteger(String.valueOf(Integer.valueOf(msg.charAt(i))));
            ct[i] = mult(x, e, n);
            inttext.append(String.valueOf(ct[i]));
            if (i != msg.length() - 1) {
                inttext.append(",");
            }
        }
        return inttext.toString();
    }

    /**
     * Method to decrypt a cypher text
     *
     * @param msg
     * @return Plain text
     */
    public String decrypt(String msg) {
        StringBuilder plaintext = new StringBuilder();
        String[] rf = msg.split(",");
        BigInteger pt[] = new BigInteger[rf.length];
        for (i = 0; i < rf.length; i++) {
            BigInteger x = new BigInteger(rf[i]);
            pt[i] = mult(x, d, n);
            char c = (char) pt[i].intValue();
            plaintext.append(c);
        }
        return plaintext.toString();
    }

    /**
     * Method to generate a unique activation key
     *
     * @return
     */
    public String generateKey() {
        int bits = 16;
        BigInteger b = new BigInteger(bits, 15, new Random());
        return b.toString();
    }


}
