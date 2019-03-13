package com.smladeoye.mqtt.presenter;

import android.content.Context;
import android.util.Log;

import com.smladeoye.mqtt.model.MqttMessageType;
import com.smladeoye.mqtt.model.MqttTopic;
import com.smladeoye.mqtt.model.MqttTopicMessage;
import com.smladeoye.mqtt.view.BaseView;
import com.smladeoye.mqtt.view.MainView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainPresenter extends BasePresenter<MainView> {

    private String clientId = MqttClient.generateClientId();

    private MqttAndroidClient client;

    private List<MqttTopicMessage> mqttTopicMessageList = new ArrayList<>();
    private List<MqttTopicMessage> mqttTopicMessageWaitingList = new ArrayList<>();
    private List<MqttTopic> mqttTopicList = new ArrayList<>();

    public MainPresenter(MainView view) {
        super(view);
    }

    private MqttAndroidClient getClient() {
        return client;
    }

    private void setClient(MqttAndroidClient client) {
        this.client = client;
    }

    public void initialize() {
        if (this.getClient() == null) {
            this.connect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MainPresenter.this.setClientCallback(client);
                    view.hideProgress();
                    view.onSuccessConnection();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    view.hideProgress();
                    view.onFailedConnection(exception.getMessage());
                }
            });
        }
    }

    private void connect(IMqttActionListener listener) {
        view.showProgress();
        client = new MqttAndroidClient((Context) view, "tcp://broker.hivemq.com:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(listener);
        } catch (MqttException e) {
            view.hideProgress();
            view.onFailedConnection(e.getMessage());
        }
    }

    private void setClientCallback(MqttAndroidClient client) {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                MainPresenter.this.setClient(null);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                mqttTopicMessageList.add(new MqttTopicMessage(topic, message, MqttMessageType.RECEIVED));
                view.updateTopicMessages(mqttTopicMessageList);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void handleSubscribeAction() {
        Log.e("topic","Total topic subscription: "+mqttTopicList.size());
        view.showSubscribeDialog(mqttTopicList);
    }

    private boolean validateSubscriptionTopic(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            view.hideProgress();
            view.onFailedAddSubscriptionTopic("Subscription Topic is empty");
            return false;
        }
        return true;
    }

    public void handleSubscribeToTopicAction(final String topic) {
        if (this.validateSubscriptionTopic(topic)) {
            if (this.getClient() != null && this.getClient().isConnected())
            {
                this.subscribeToTopic(topic);
            }
            else
            {
                this.connect(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        subscribeToTopic(topic);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        view.hideProgress();
                        view.onFailedConnection(exception.getMessage());
                    }
                });
            }
        }
    }

    private void subscribeToTopic(final String topic) {
        view.showProgress();
        int qos = 1;
        try
        {
            IMqttToken subToken = getClient().subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttTopicList.add(new MqttTopic(topic));
                    Log.e("topic","New topic subscription: "+topic);
                    Log.e("topic","Total topic subscription: "+mqttTopicList.size());
                    view.hideProgress();
                    //view.updateSubscribedTopics(mqttTopicList);
                    view.onSuccessAddSubscriptionTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    view.hideProgress();
                    view.onFailedAddSubscriptionTopic(exception.getMessage());
                }
            });
        }
        catch (MqttException e)
        {
            view.hideProgress();
            view.onFailedAddSubscriptionTopic(e.getMessage());
        }
    }

    private boolean validateTopicMesage(String topic, String message) {
        if (topic == null || topic.trim().isEmpty()) {
            view.hideProgress();
            view.onFailedSendTopicMessage("Topic is empty");
            return false;
        } else if (message == null || message.trim().isEmpty()) {
            view.hideProgress();
            view.onFailedSendTopicMessage("Message is empty");
            return false;
        }
        return true;
    }

    public void handleSendMessageAction(final String topic, final String message)
    {
        if (this.validateTopicMesage(topic, message))
        {
            if (this.getClient() != null && this.getClient().isConnected()) {
                this.sendTopicMessage(topic, message);
            } else {
                this.connect(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        sendTopicMessage(topic, message);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        view.hideProgress();
                        view.onFailedConnection(exception.getMessage());
                    }
                });
            }
        }
    }

    private void sendTopicMessage(final String topic, final String topicMessage) {
        byte[] encodedMessage;
        try {
            encodedMessage = topicMessage.getBytes("UTF-8");
            final MqttMessage message = new MqttMessage(encodedMessage);

            MqttTopicMessage waitingMessage = new MqttTopicMessage(topic, message, MqttMessageType.WAITING);
            mqttTopicMessageList.add(waitingMessage);
            mqttTopicMessageWaitingList.add(waitingMessage);
            view.updateTopicMessages(mqttTopicMessageList);
            view.onSuccessSendTopicMessage();

            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            MqttTopicMessage pendingTopicMessage = mqttTopicMessageWaitingList.get(0);
                            if(pendingTopicMessage.getType().equals(MqttMessageType.WAITING))
                            {
                                try {
                                    IMqttToken messageToken = client.publish(topic, message);
                                    messageToken.setActionCallback(new IMqttActionListener() {
                                        @Override
                                        public void onSuccess(IMqttToken asyncActionToken) {
                                            mqttTopicMessageWaitingList.get(0).setType(MqttMessageType.SENT);
                                            mqttTopicMessageWaitingList.remove(0);
                                            view.hideProgress();
                                            view.updateTopicMessages(mqttTopicMessageList);
                                        }

                                        @Override
                                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                            view.hideProgress();
                                            view.onFailedSendTopicMessage(exception.getMessage());
                                        }
                                    });
                                }
                                catch (MqttException e)
                                {
                                    view.hideProgress();
                                    view.onFailedAddSubscriptionTopic(e.getMessage());
                                }
                            }
                            else
                            {
                                mqttTopicMessageWaitingList.remove(0);
                            }
                        }
                    }, 10000);
        } catch (Exception e) {
            view.hideProgress();
            view.onFailedAddSubscriptionTopic(e.getMessage());
        }
    }

    public void handleUnsubscribeAction(final int position) {
        if (this.getClient() != null && this.getClient().isConnected()) {
            this.unsubscribe(position);
        } else {
            this.connect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    unsubscribe(position);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    view.hideProgress();
                    view.onFailedConnection(exception.getMessage());
                }
            });
        }
    }

    private void unsubscribe(final int position) {
        String topic = mqttTopicList.get(position).getTopic();
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttTopicList.remove(position);
                    view.showSubscribedTopics(mqttTopicList);
                    view.onSuccessUnsubscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    view.onFailedUnsubscribe(exception.getMessage());
                }
            });
        } catch (MqttException e) {
            view.onFailedUnsubscribe(e.getMessage());
        }
    }

    public void handleShowSubscribedTopicsAction() {
        view.showSubscribedTopics(mqttTopicList);
    }

    public void handleDeleteTopicMessageAction(int position) {
        mqttTopicMessageList.get(position).setType(MqttMessageType.DELETED);
        view.updateTopicMessages(mqttTopicMessageList);
    }
}
