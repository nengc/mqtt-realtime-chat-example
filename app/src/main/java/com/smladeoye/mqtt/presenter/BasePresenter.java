package com.smladeoye.mqtt.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.smladeoye.mqtt.library.ApiClient;
import com.smladeoye.mqtt.view.BaseView;

public class BasePresenter<E extends BaseView>
{

    public E view;

    public BasePresenter(E view){
        this.attachView(view);
    }

    public void attachView(E view) { this.view = view; }

    public E getView() { return this.view; }

    /*public void handleErrorCode(int statusCode) {
        String message = "";
        Log.e("failed","Response Status Code: "+ statusCode);
        switch (statusCode)
        {
            case 500:
                message = "Something went wrong at the server end";
                getView().onError(message);
                break;
            default:
                message = "Connection failed. Please check your internet connection";
                getView().onError(message);
        }
    }*/

    public void dettachView(){this.view = null;}
}
