package com.smladeoye.mqtt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.smladeoye.mqtt.R;
import com.smladeoye.mqtt.listener.ListViewOnClickListenerInterface;
import com.smladeoye.mqtt.model.MqttMessageType;
import com.smladeoye.mqtt.model.MqttTopic;
import com.smladeoye.mqtt.model.MqttTopicMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MqttSubscribedTopicListAdapter extends RecyclerView.Adapter<MqttSubscribedTopicListAdapter.MqttSubscribedTopicViewHolder>
{
    protected List<MqttTopic> mqttTopicList;
    public ListViewOnClickListenerInterface itemClickListener;
    public ListViewOnClickListenerInterface deleteItemClickListener;

    public void setItemClickListener(ListViewOnClickListenerInterface itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setDeleteItemClickListener(ListViewOnClickListenerInterface deleteItemClickListener)
    {
        this.deleteItemClickListener = deleteItemClickListener;
    }

    public class MqttSubscribedTopicViewHolder extends RecyclerView.ViewHolder
    {
        public View subscribedTopicView;

        public TextView topicText;
        public View topicDeleteButton;

        public MqttSubscribedTopicViewHolder(View itemView) {
            super(itemView);
            subscribedTopicView = (View) itemView.findViewById(R.id.subscribed_topic_view);
            topicText = (TextView) itemView.findViewById(R.id.subscribed_topic_text);
            topicDeleteButton = (View) itemView.findViewById(R.id.subscribed_topic_delete_action);
        }
    }

    public MqttSubscribedTopicListAdapter(List<MqttTopic> mqttTopicList)
    {
        this.setList(mqttTopicList);
    }

    public void setList(List<MqttTopic> mqttTopicList)
    {
        this.mqttTopicList = mqttTopicList;
    }

    public List<MqttTopic> getList() {
        return mqttTopicList;
    }

    @NonNull
    @Override
    public MqttSubscribedTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribed_topic_list_row,parent,false);
        final MqttSubscribedTopicViewHolder viewHolder = new MqttSubscribedTopicViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MqttSubscribedTopicViewHolder holder, final int position) {
        MqttTopic mqttTopic = this.getList().get(position);
        // do view
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK);

        if(deleteItemClickListener != null)
        {
            holder.topicDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItemClickListener.onClick(view,position);
                }
            });
        }
        holder.topicText.setText(mqttTopic.getTopic());
    }

    @Override
    public int getItemCount() {
         return this.getList().size();
    }

}
