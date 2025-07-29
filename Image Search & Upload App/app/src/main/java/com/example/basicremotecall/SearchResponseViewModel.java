package com.example.basicremotecall;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchResponseViewModel extends ViewModel {

    public MutableLiveData<String> response;
    public SearchResponseViewModel(){
        response = new MutableLiveData<String>();
    }  // Initialize the LiveData for the API response

    public String getResponse(){
        return response.getValue();
    }  // Method to get the API response value as a string
    public void setResponse(String value){
        response.postValue(value);
    }  // Method to set the API response value using LiveData
}
