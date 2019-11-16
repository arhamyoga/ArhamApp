package net.yoga.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import net.yoga.R;
import net.yoga.model.Notification;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private Context mCtx;
    private Gson gson;
    private Notification notification;
    private List<Notification> notificationList;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor prefsEditor;

    public NotificationsAdapter(Context mCtx, Gson gson, List<Notification> notificationList, SharedPreferences mSharedPreferences, SharedPreferences.Editor prefsEditor) {
        this.mCtx = mCtx;
        this.gson = gson;
        this.notificationList = notificationList;
        this.mSharedPreferences = mSharedPreferences;
        this.prefsEditor = prefsEditor;
    }

    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new NotificationViewHolder(this, LayoutInflater.from(this.mCtx).inflate(R.layout.card_notification,null));
    }

    @Override
    @SuppressLint({"SetTextI18n"})
    public void onBindViewHolder(final NotificationViewHolder notificationViewHolder, int i) {
        notification = notificationList.get(i);
        notificationViewHolder.notificationTitle.setText(notification.getNotificationTitle());
        notificationViewHolder.notificationText.setText(notification.getNotificationContent());
        notificationViewHolder.delNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = notificationViewHolder.getAdapterPosition();
                notificationList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos,notificationList.size());
                prefsEditor = mSharedPreferences.edit();
                String toJson = gson.toJson(notificationList);
                prefsEditor.putString("Notifications_customObjectList",toJson);
                prefsEditor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView notificationTitle;
        TextView notificationText;
        ImageButton delNotif;
        final NotificationsAdapter notificationsAdapter;

        public NotificationViewHolder(NotificationsAdapter notificationsAdapter, View view) {
            super(view);
            this.notificationTitle = view.findViewById(R.id.notif_title);
            this.notificationText = view.findViewById(R.id.text_notification);
            this.delNotif = view.findViewById(R.id.delete_notification);
            this.notificationsAdapter = notificationsAdapter;
        }
    }
}
