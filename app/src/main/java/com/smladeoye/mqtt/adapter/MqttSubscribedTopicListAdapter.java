package com.smladeoye.mqtt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smladeoye.mqtt.R;
import com.smladeoye.mqtt.listener.ListViewOnClickListenerInterface;
import com.smladeoye.mqtt.model.MqttTopic;
import java.util.List;

public class MqttSubscribedTopicListAdapter extends RecyclerView.Adapter<MqttSubscribedTopicListAdapter.MqttSubscribedTopicViewHolder>
{
    private List<MqttTopic> mqttTopicList;
    private ListViewOnClickListenerInterface itemClickListener;
    private ListViewOnClickListenerInterface deleteItemClickListener;

    public void setItemClickListener(ListViewOnClickListenerInterface itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setDeleteItemClickListener(ListViewOnClickListenerInterface deleteItemClickListener)
    {
        this.deleteItemClickListener = deleteItemClickListener;
    }

    class MqttSubscribedTopicViewHolder extends RecyclerView.ViewHolder
    {
        private View subscribedTopicView;

        TextView topicText;
        View topicDeleteButton;

        MqttSubscribedTopicViewHolder(View itemView) {
            super(itemView);
            subscribedTopicView = itemView.findViewById(R.id.subscribed_topic_view);
            topicText = itemView.findViewById(R.id.subscribed_topic_text);
            topicDeleteButton = itemView.findViewById(R.id.subscribed_topic_delete_action);
        }
    }

    public MqttSubscribedTopicListAdapter(List<MqttTopic> mqttTopicList)
    {
        this.setList(mqttTopicList);
    }

    private void setList(List<MqttTopic> mqttTopicList)
    {
        this.mqttTopicList = mqttTopicList;
    }

    private List<MqttTopic> getList() {
        return mqttTopicList;
    }

    @NonNull
    @Override
    public MqttSubscribedTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribed_topic_list_row,parent,false);
        return new MqttSubscribedTopicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MqttSubscribedTopicViewHolder holder, final int position) {
        MqttTopic mqttTopic = this.getList().get(position);
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
