package com.loginid.cryptodid.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClaimViewModel extends ViewModel {

    private MutableLiveData<Claim> _claim =  new MutableLiveData<Claim>();
    LiveData<Claim> claim = _claim;

    public void stickNewValue(Claim claim){
         this._claim.setValue(claim);
    }

    public LiveData<Claim> getData(){

        return this.claim;
    }
    public void reset(){
        this._claim.setValue(null);
    }

}
