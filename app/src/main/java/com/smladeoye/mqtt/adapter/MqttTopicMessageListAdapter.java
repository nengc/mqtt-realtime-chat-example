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
    protected List<MqttTopicMessage> mqttTopicMessageList;
    public ListViewOnClickListenerInterface itemClickListener;
    public ListViewOnClickListenerInterface editItemClickListener;
    public ListViewOnClickListenerInterface deleteItemClickListener;

    public void setItemClickListener(ListViewOnClickListenerInterface itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setDeleteItemClickListener(ListViewOnClickListenerInterface deleteItemClickListener)
    {
        this.deleteItemClickListener = deleteItemClickListener;
    }

    public class MqttTopicMessageViewHolder extends RecyclerView.ViewHolder{

        public View topicMessageView;
        public View messageDeleteButton;
        public TextView messageTopic, messageStatus, messageDate, messageText;

        public MqttTopicMessageViewHolder(View itemView) {
            super(itemView);
            topicMessageView = (View) itemView.findViewById(R.id.topic_message_view);
            messageTopic = (TextView) itemView.findViewById(R.id.topic_text);
            messageStatus = (TextView) itemView.findViewById(R.id.status_text);
            messageDate = (TextView) itemView.findViewById(R.id.date_text);
            messageText = (TextView) itemView.findViewById(R.id.message_text);
            messageDeleteButton = (View) itemView.findViewById(R.id.message_delete_action);
        }
    }

    public MqttTopicMessageListAdapter(List<MqttTopicMessage> mqttTopicMessageList)
    {
        this.setList(mqttTopicMessageList);
    }

    public void setList(List<MqttTopicMessage> mqttTopicMessageList)
    {
        this.mqttTopicMessageList = mqttTopicMessageList;
    }

    public List<MqttTopicMessage> getList() {
        return mqttTopicMessageList;
    }

    @NonNull
    @Override
    public MqttTopicMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_message_list_row,parent,false);
        final MqttTopicMessageViewHolder viewHolder = new MqttTopicMessageViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MqttTopicMessageViewHolder holder, final int position) {
        MqttTopicMessage mqttTopicMessage = this.getList().get(position);
        // do view
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        if(deleteItemClickListener != null)
        {
            holder.messageDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItemClickListener.onClick(view,position);
                }
            });
        }
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
            holder.messageDeleteButton.setVisibility(View.VISIBLE);
        }else if(mqttTopicMessage.getType().equals(MqttMessageType.DELETED))
        {
            holder.messageDeleteButton.setVisibility(View.GONE);
            holder.topicMessageView.setBackgroundResource(R.drawable.deleted_message_background);
        }
        holder.messageTopic.setText(mqttTopicMessage.getTopic());
        holder.messageStatus.setText(mqttTopicMessage.getType().toString());
        holder.messageDate.setText(dateFormat.format(Calendar.getInstance().getTime()));
        holder.messageText.setText(new String(mqttTopicMessage.getMessage().getPayload()));
    }

    @Override
    public int getItemCount() {
         return this.getList().size();
    }

}
