package net.yoga.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.yoga.R;
import net.yoga.activities.MainActivity;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";
    private SharedPreferences mSharedPreferences;
    private TextView noNotifications;
    private SharedPreferences.Editor prefsEditor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.layout_notificationsfragment,container,false);
        inflate.setTag(TAG);
        Log.d(TAG,TAG);
        Toolbar toolbar = inflate.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().mutate().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        setHasOptionsMenu(true);
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
}
