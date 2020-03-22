package net.yoga.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yoga.R;
import net.yoga.activities.MainActivity;
import net.yoga.adapter.NotificationsAdapter;
import net.yoga.model.Notification;

import java.util.List;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";
    private SharedPreferences mSharedPreferences;
    private TextView noNotifications;
    private SharedPreferences.Editor prefsEditor;
    private Gson gson;
    private NotificationsAdapter notificationsAdapter;
    private List<Notification> notificationList;
    private RecyclerView mRecyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.layout_notificationsfragment,container,false);
        inflate.setTag(TAG);
        Log.d(TAG,TAG);
        Toolbar toolbar = inflate.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().mutate().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        setHasOptionsMenu(true);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mRecyclerView = inflate.findViewById(R.id.notificationslist);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        noNotifications = inflate.findViewById(R.id.noNotifications);
        gson = new Gson();
        notificationList = gson.fromJson(mSharedPreferences.getString("Notifications_customObjectList",null),new CustomType(this).getType());
        if(notificationList == null|| notificationList.size()<=0){
            mRecyclerView.setVisibility(View.GONE);
            noNotifications.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noNotifications.setVisibility(View.GONE);
            notificationsAdapter = new NotificationsAdapter(getActivity(),gson,notificationList,mSharedPreferences,prefsEditor);
            mRecyclerView.setAdapter(notificationsAdapter);
        }
        return inflate;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    class CustomType extends TypeToken<List<Notification>>{
        final NotificationsFragment notificationsFragment;

        public CustomType(NotificationsFragment notificationsFragment) {
            this.notificationsFragment = notificationsFragment;
        }
    }


}
