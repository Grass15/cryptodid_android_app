package com.loginid.cryptodid.presentation.issuer.voting;

import android.util.Log;

import java.util.List;
import java.util.Vector;

public class PrivilegeDataSender {
    public Vector<Integer> vector = new Vector<>();
    private String url;

    public PrivilegeDataSender(List<Integer> numbers, String url) {
        this.url = url;
        this.vector.addAll(numbers);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PrivilegeDataSender(List<Integer> numbers) {
        this.vector.addAll(numbers);
    }

    public PrivilegeDataSender(String url) {
        this.url = url;
    }

    public Vector<Integer> ToCubeValue(){
        this.vector.replaceAll(num -> (int) Math.pow(num,3));
        return this.vector;
    }
    public void SendSocketData(){
        Log.d("DataToSend",this.ToCubeValue().get(0).toString());
    }
}
