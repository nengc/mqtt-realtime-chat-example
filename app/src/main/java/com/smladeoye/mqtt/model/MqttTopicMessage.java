package com.smladeoye.mqtt.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttTopicMessage {

    private String topic;

    private MqttMessage message;

    private MqttMessageType type;

    public MqttTopicMessage(String topic, MqttMessage message, MqttMessageType type)
    {
        this.setTopic(topic);
        this.setMessage(message);
        this.setType(type);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqttMessage getMessage() {
        return message;
    }

    public void setMessage(MqttMessage message) {
        this.message = message;
    }

    public MqttMessageType getType() {
        return type;
    }

    public void setType(MqttMessageType type) {
        this.type = type;
    }
}
