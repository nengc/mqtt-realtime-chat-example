package com.smladeoye.mqtt.model;

public class MqttTopic {

    private String topic;

    public MqttTopic(String topic)
    {
        this.setTopic(topic);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
