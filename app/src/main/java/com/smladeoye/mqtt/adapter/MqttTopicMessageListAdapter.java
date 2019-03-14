package com.smladeoye.mqtt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smladeoye.mqtt.R;
import com.smladeoye.mqtt.listener.ListViewOnClickListenerInterface;
import com.smladeoye.mqtt.model.MqttMessageType;
import com.smladeoye.mqtt.model.MqttTopicMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MqttTopicMessageListAdapter extends RecyclerView.Adapter<MqttTopicMessageListAdapter.MqttTopicMessageViewHolder>
{
    private List<MqttTopicMessage> mqttTopicMessageList;
    private ListViewOnClickListenerInterface itemClickListener;
    public ListViewOnClickListenerInterface editItemClickListener;
    private ListViewOnClickListenerInterface deleteItemClickListener;

    public void setItemClickListener(ListViewOnClickListenerInterface itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setDeleteItemClickListener(ListViewOnClickListenerInterface deleteItemClickListener)
    {
        this.deleteItemClickListener = deleteItemClickListener;
    }

    class MqttTopicMessageViewHolder extends RecyclerView.ViewHolder{

        View topicMessageView;
        View messageDeleteButton;
        TextView messageTopic, messageStatus, messageDate, messageText;

        MqttTopicMessageViewHolder(View itemView) {
            super(itemView);
            topicMessageView = itemView.findViewById(R.id.topic_message_view);
            messageTopic = itemView.findViewById(R.id.topic_text);
            messageStatus = itemView.findViewById(R.id.status_text);
            messageDate = itemView.findViewById(R.id.date_text);
            messageText = itemView.findViewById(R.id.message_text);
            messageDeleteButton = itemView.findViewById(R.id.message_delete_action);
        }
    }

    public MqttTopicMessageListAdapter(List<MqttTopicMessage> mqttTopicMessageList)
    {
        this.setList(mqttTopicMessageList);
    }

    private void setList(List<MqttTopicMessage> mqttTopicMessageList)
    {
        this.mqttTopicMessageList = mqttTopicMessageList;
    }

    private List<MqttTopicMessage> getList() {
        return mqttTopicMessageList;
    }

    @NonNull
    @Override
    public MqttTopicMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_message_list_row,parent,false);
        return new MqttTopicMessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MqttTopicMessageViewHolder holder, final int position) {
        MqttTopicMessage mqttTopicMessage = this.getList().get(position);
        if(itemClickListener != null)
        {
            holder.topicMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onClick(view,position);
                }
            });
        }

        if(deleteItemClickListener != null)
        {
            holder.messageDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItemClickListener.onClick(view,position);
                }
            });
        }

        holder.messageDeleteButton.setVisibility(View.GONE);
        if(mqttTopicMessage.getType().equals(MqttMessageType.RECEIVED))
        {
            holder.topicMessageView.setBackgroundResource(R.drawable.received_message_background);
        }
        else if(mqttTopicMessage.getType().equals(MqttMessageType.SENT))
        {
            holder.topicMessageView.setBackgroundResource(R.drawable.sent_message_background);
        }
        else if(mqttTopicMessage.getType().equals(MqttMessageType.WAITING))
        {
            holder.topicMessageView.setBackgroundResource(R.drawable.waiting_message_background);
            holder.messageDeleteButton.setVisibility(View.VISIBLE);
        }
        else if(mqttTopicMessage.getType().equals(MqttMessageType.DELETED))
        {
            holder.topicMessageView.setBackgroundResource(R.drawable.deleted_message_background);
        }
        holder.messageTopic.setText(mqttTopicMessage.getTopic());
        holder.messageStatus.setText(mqttTopicMessage.getType().toString());
        holder.messageDate.setText(mqttTopicMessage.getDate());
        holder.messageText.setText(new String(mqttTopicMessage.getMessage().getPayload()));
    }

    @Override
    public int getItemCount() {
         return this.getList().size();
    }

}
