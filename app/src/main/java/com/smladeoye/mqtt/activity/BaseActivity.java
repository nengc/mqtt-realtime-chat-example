package com.smladeoye.mqtt.activity;

import android.support.v7.app.AppCompatActivity;

import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.smladeoye.mqtt.R;
import com.smladeoye.mqtt.presenter.BasePresenter;
import com.smladeoye.mqtt.view.BaseView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected SweetAlertDialog alertDialog;

    protected abstract BasePresenter getPresenter();

    @Override
    protected void onDestroy() {
        if(getPresenter() != null)
        {
            getPresenter().dettachView();
        }
        hideProgress();
        super.onDestroy();
    }

    @Override
    public void showProgress()
    {
        alertDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(getString(R.string.alert_loading_title))
                .setContentText(getString(R.string.alert_loading_description));
        //alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void hideProgress() {
        if ((alertDialog != null) && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onSuccess(String message) {

        alertDialog = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.alert_success_title))
                .setContentText(message);
        //alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onFailed(String message) {

        alertDialog = new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.alert_error_title))
                .setContentText(message);
        //alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
