package com.smladeoye.mqtt.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.smladeoye.mqtt.R;
import com.smladeoye.mqtt.adapter.MqttSubscribedTopicListAdapter;
import com.smladeoye.mqtt.adapter.MqttTopicMessageListAdapter;
import com.smladeoye.mqtt.listener.ListViewOnClickListenerInterface;
import com.smladeoye.mqtt.model.MqttTopic;
import com.smladeoye.mqtt.model.MqttTopicMessage;
import com.smladeoye.mqtt.presenter.BasePresenter;
import com.smladeoye.mqtt.presenter.MainPresenter;
import com.smladeoye.mqtt.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainView {

    MainPresenter presenter;

    EditText messageEdittext, messageTopicEdittext;
    Button sendMessageButton;
    RecyclerView topicMessageListRecyclerView;
    RecyclerView subscribedTopicListRecyclerView;
    MaterialDialog subscribedTopicsDialog;

    MqttTopicMessageListAdapter mqttTopicMessageListAdapter;
    MqttSubscribedTopicListAdapter mqttSubscribedTopicListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        messageTopicEdittext = findViewById(R.id.message_topic_edittext);
        messageEdittext = findViewById(R.id.message_edittext);
        sendMessageButton = findViewById(R.id.send_message_button);
        topicMessageListRecyclerView = findViewById(R.id.message_list_view);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.handleSendMessageAction(messageTopicEdittext.getText().toString(), messageEdittext.getText().toString());
            }
        });

        presenter.initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_add_subscription:
                presenter.handleSubscribeAction();
                break;
            case R.id.menu_action_subscribed_topics:
                presenter.handleShowSubscribedTopicsAction();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onSuccessConnection() {
        this.onSuccess(getString(R.string.mqtt_connection_success_message));
    }

    @Override
    public void onSuccessAddSubscriptionTopic() {
        this.onSuccess(getString(R.string.topic_subscription_success_message));
    }

    @Override
    public void onFailedConnection(String message) {
        this.onFailed(message);
    }

    @Override
    public void onFailedAddSubscriptionTopic(String message) {
        this.onFailed(message);
    }

    @Override
    public void showSubscribeDialog(final List<MqttTopic> mqttTopicList) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Subscribe to Topic")
                .customView(R.layout.add_topic_subscription, true)
                .positiveText(R.string.subscribe_label)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText topicEdittext;
                        if (dialog.getCustomView() != null) {
                            topicEdittext = dialog.getCustomView().findViewById(R.id.subscription_topic_edittext);
                            presenter.handleSubscribeToTopicAction(topicEdittext.getText().toString());
                        }
                    }
                }).build();
        dialog.show();
    }

    @Override
    public void onFailedSendTopicMessage(String message) {
        this.onFailed(message);
    }

    @Override
    public void onSuccessSendTopicMessage() {
        messageEdittext.setText("");
        //this.onSuccess(getString(R.string.send_message_success_message));
    }

    @Override
    public void updateSubscribedTopics(List<MqttTopic> mqttTopicList) {
        mqttSubscribedTopicListAdapter = new MqttSubscribedTopicListAdapter(mqttTopicList);
        mqttSubscribedTopicListAdapter.setDeleteItemClickListener(new ListViewOnClickListenerInterface() {
            @Override
            public void onClick(View view, int position) {
                presenter.handleUnsubscribeAction(position);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        subscribedTopicListRecyclerView.setHasFixedSize(true);
        subscribedTopicListRecyclerView.setLayoutManager(mLayoutManager);
        subscribedTopicListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        subscribedTopicListRecyclerView.setAdapter(mqttSubscribedTopicListAdapter);
    }

    @Override
    public void updateTopicMessages(List<MqttTopicMessage> mqttTopicMessageList) {
        mqttTopicMessageListAdapter = new MqttTopicMessageListAdapter(mqttTopicMessageList);
        mqttTopicMessageListAdapter.setDeleteItemClickListener(new ListViewOnClickListenerInterface() {
            @Override
            public void onClick(View view, int position) {
                presenter.handleDeleteTopicMessageAction(position);
            }
        });
        mqttTopicMessageListAdapter.setItemClickListener(new ListViewOnClickListenerInterface() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        topicMessageListRecyclerView.setHasFixedSize(true);
        topicMessageListRecyclerView.setLayoutManager(mLayoutManager);
        topicMessageListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topicMessageListRecyclerView.setAdapter(mqttTopicMessageListAdapter);
    }

    @Override
    public void onSuccessUnsubscribe() {
        this.onSuccess(getString(R.string.unsubscribe_success_message));
    }

    @Override
    public void onFailedUnsubscribe(String message) {
        this.onFailed(message);
    }

    @Override
    public void showSubscribedTopics(List<MqttTopic> mqttTopicList) {
        mqttSubscribedTopicListAdapter = new MqttSubscribedTopicListAdapter(mqttTopicList);
        mqttSubscribedTopicListAdapter.setDeleteItemClickListener(new ListViewOnClickListenerInterface() {
            @Override
            public void onClick(View view, int position) {
                subscribedTopicsDialog.dismiss();
                presenter.handleUnsubscribeAction(position);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        subscribedTopicsDialog = new MaterialDialog.Builder(this)
                .title(R.string.subscribed_topics_text)
                .negativeText(android.R.string.cancel)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                })
                .adapter(mqttSubscribedTopicListAdapter, mLayoutManager)
                .build();
        subscribedTopicsDialog.show();
    }
}
