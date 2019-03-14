package com.smladeoye.mqtt.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MqttTopicMessage {

    private String topic;

    private MqttMessage message;

    private MqttMessageType type;

    private String date;

    public MqttTopicMessage(String topic, MqttMessage message, MqttMessageType type)
    {
        this.setTopic(topic);
        this.setMessage(message);
        this.setType(type);
        this.setDate();
    }

    public String getDate() {
        return date;
    }

    private void setDate() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        this.date = dateFormat.format(Calendar.getInstance().getTime());
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
