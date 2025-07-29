package com.example.basicremotecall;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ErrorViewModel extends ViewModel {
    public MutableLiveData<Integer> errorCode;  // A LiveData object to hold an error code
    public ErrorViewModel(){
        errorCode = new MutableLiveData<Integer>();   // Initialize the LiveData object
        errorCode.setValue(0);
    }

    public Integer getErrorCode(){
        return errorCode.getValue();  // Retrieve the current error code value
    }
    public void setErrorCode(Integer value){
        errorCode.postValue(value);  // Set a new error code value and notify observers
    }
}
