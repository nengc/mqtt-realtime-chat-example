package com.smladeoye.mqtt.presenter;

import com.smladeoye.mqtt.view.BaseView;

public class BasePresenter<E extends BaseView>
{
    public E view;

    BasePresenter(E view){
        this.attachView(view);
    }

    private void attachView(E view) { this.view = view; }

    public E getView() { return this.view; }

    public void dettachView(){this.view = null;}
}
