package com.smladeoye.mqtt.view;

import com.smladeoye.mqtt.model.MqttTopic;
import com.smladeoye.mqtt.model.MqttTopicMessage;

import java.util.List;

public interface MainView extends BaseView {

    void onSuccessConnection();

    void onSuccessAddSubscriptionTopic();

    void onFailedConnection(String message);

    void onFailedAddSubscriptionTopic(String message);

    void showSubscribeDialog(List<MqttTopic> mqttTopicList);

    void onFailedSendTopicMessage(String message);

    void onSuccessSendTopicMessage();

    void updateSubscribedTopics(List<MqttTopic> mqttTopicList);

    void updateTopicMessages(List<MqttTopicMessage> mqttTopicMessageList);

    void onSuccessUnsubscribe();

    void onFailedUnsubscribe(String message);

    void showSubscribedTopics(List<MqttTopic> mqttTopicList);

    void showTopicMessageDeleteDialog(int position);
}
