package com.smladeoye.mqtt.view;

public interface BaseView {

    public void showProgress();

    public void hideProgress();

    public void onSuccess(String message);

    public void onFailed(String message);
}
