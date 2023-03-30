package com.loginid.cryptodid.scanner;

import java.util.HashMap;

public class QrDecoder {

    private String rawData;
    private String url;
    private String otherData;
    private int port;

    public QrDecoder(String rawData) {

        this.rawData = rawData;
        String [] arr_url = rawData.split(":");
        this.url = arr_url[0];
        this.port = Integer.parseInt(arr_url[1]);
        this.otherData = arr_url.length > 2?arr_url[2]: "";
    }

    public String getUrl() {
        return url;
    }
    public String getOtherData() {
        return otherData;
    }
    public int getPort() {
        return port;
    }

    public void setRawData(String rawData) {

        this.rawData = rawData;


    }


    private String getData(String s){
        String [] arr = s.split(":");
        String [] subArr = arr[2].split("/");
        return "url : " + s + "\n \nPort : " + subArr[0];
    }

    private String getURL_PORT(String url){
        try{
            String [] arr_url = url.split(":");
            String ip = arr_url[0];
            String port = arr_url[1];
            return "url : " +  ip + "\n \nPort : " + port;
        }catch(Exception e){
            return "Maybe an issuer";
        }

    }
}
