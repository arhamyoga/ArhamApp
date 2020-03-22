package net.yoga.fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yoga.R;
import net.yoga.activities.MainActivity;
import net.yoga.adapter.ReminderAdapter;
import net.yoga.model.Reminder;
import net.yoga.receiver.AlarmHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderFragment extends Fragment {
    private static final String TAG = "ReminderFragment";
    SimpleDateFormat sdf;
    private AlarmHelper alarmHelper;
    private Gson gson;
    private ReminderAdapter mAdapter;
    private List<Reminder> mReCu;
    private RecyclerView mRecyclerView;
    private SharedPreferences mSharedPreferences;
    private TextView noreminders;
    private SharedPreferences.Editor prefsEditor;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.layout_reminderfragment, viewGroup, false);
        inflate.setTag(TAG);
        Toolbar toolbar = inflate.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().mutate().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        setHasOptionsMenu(true);
        this.alarmHelper = new AlarmHelper(getActivity());
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        this.mRecyclerView = inflate.findViewById(R.id.reminderlist);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.noreminders = inflate.findViewById(R.id.noreminder);
        this.gson = new Gson();
        this.mReCu = this.gson.fromJson(this.mSharedPreferences.getString("Reminder_customObjectList", null), new C15101(this).getType());
        if (this.mReCu == null || this.mReCu.size() <= 0) {
            this.mRecyclerView.setVisibility(View.GONE);
            this.noreminders.setVisibility(View.VISIBLE);
        } else {
            this.mRecyclerView.setVisibility(View.VISIBLE);
            this.mAdapter = new ReminderAdapter(getActivity(), this.mReCu, this.gson, this.mSharedPreferences, this.prefsEditor, this.alarmHelper);
            this.mRecyclerView.setAdapter(this.mAdapter);
            this.noreminders.setVisibility(View.GONE);
        }
        inflate.findViewById(R.id.addreminder).setOnClickListener(new C10432(this));
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

    class C10432 implements View.OnClickListener {

        final ReminderFragment f3352a;

        C10432(ReminderFragment reminderFragment) {
            this.f3352a = reminderFragment;
        }

        public void onClick(View view) {
            this.f3352a.showTimePickerDialog();
        }
    }

    class C10443 implements TimePickerDialog.OnTimeSetListener {

        final ReminderFragment f3353a;

        C10443(ReminderFragment reminderFragment) {
            this.f3353a = reminderFragment;
        }

        public void onTimeSet(TimePicker timePicker, int i, int i2) {
            if (timePicker.isShown()) {
                Calendar instance = Calendar.getInstance();
                instance.set(Calendar.HOUR_OF_DAY, i);
                instance.set(Calendar.MINUTE, i2);
                String str = ReminderFragment.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onTimeSet: ");
                stringBuilder.append(this.f3353a.startTimeFormat().format(instance.getTime()));
                Log.d(str, stringBuilder.toString());
                this.f3353a.showDialog(instance);
            }
        }
    }

    class C10476 implements DialogInterface.OnClickListener {

        final ReminderFragment f3359a;

        C10476(ReminderFragment reminderFragment) {
            this.f3359a = reminderFragment;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class C15101 extends TypeToken<List<Reminder>> {

        final ReminderFragment f5149d;

        C15101(ReminderFragment reminderFragment) {
            this.f5149d = reminderFragment;
        }
    }

    private void showTimePickerDialog() {
        Calendar instance = Calendar.getInstance();
        new TimePickerDialog(getActivity(), new C10443(this), instance.get(Calendar.HOUR_OF_DAY), instance.get(Calendar.MINUTE), false).show();
    }

    public SimpleDateFormat getHourFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh");
        this.sdf = simpleDateFormat;
        return simpleDateFormat;
    }

    public SimpleDateFormat getMinuteFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        this.sdf = simpleDateFormat;
        return simpleDateFormat;
    }
    public void showDialog(final Calendar calendar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Days");
        String arr[] ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        final ArrayList arrayList = new ArrayList();
        builder.setMultiChoiceItems(arr, null, (dialogInterface, i, z) -> {
            if (z) {
                arrayList.add(Integer.valueOf(i));
                return;
            }
            arrayList.remove(Integer.valueOf(i));
        });
        /* renamed from: c */
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            if (arrayList.size() > 0) {
                dialogInterface.dismiss();
                Reminder reminder_custom = new Reminder();
                reminder_custom.setTime(startTimeFormat().format(calendar.getTime()));
                for (i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).equals(Integer.valueOf(1))) {
                        reminder_custom.setMonday(true);
                    } else if (arrayList.get(i).equals(Integer.valueOf(2))) {
                        reminder_custom.setTuesday(true);
                    } else if (arrayList.get(i).equals(Integer.valueOf(3))) {
                        reminder_custom.setWednesday(true);
                    } else if (arrayList.get(i).equals(Integer.valueOf(4))) {
                        reminder_custom.setThurday(true);
                    } else if (arrayList.get(i).equals(Integer.valueOf(5))) {
                        reminder_custom.setFriday(true);
                    } else if (arrayList.get(i).equals(Integer.valueOf(6))) {
                        reminder_custom.setSaturday(true);
                    } else if (arrayList.get(i).equals(Integer.valueOf(0))) {
                        reminder_custom.setSunday(true);
                    }
                }
                m4641a(alarmHelper, calendar);
                reminder_custom.setOnTime(true);
                if (mReCu == null || mReCu.size() <= 0) {
                    mReCu = new ArrayList();
                }
                mReCu.add(reminder_custom);
                String toJson = gson.toJson(mReCu);
                prefsEditor = mSharedPreferences.edit();
                prefsEditor.putString("Reminder_customObjectList", toJson);
                prefsEditor.apply();
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter = new ReminderAdapter(getActivity(), mReCu, gson, mSharedPreferences, prefsEditor, alarmHelper);
                mRecyclerView.setAdapter(mAdapter);
                noreminders.setVisibility(View.GONE);
                return;
            }
            Toast.makeText(getActivity(), "Please select at-least one day", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", new C10476(this));
        builder.create().show();
    }

    public SimpleDateFormat startTimeFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        this.sdf = simpleDateFormat;
        return simpleDateFormat;
    }
    void m4641a(AlarmHelper paramAlarmHelper, Calendar paramCalendar)
    {
        int i=0;
        int j=0;
        int k=0;
        if (startTimeFormat().format(paramCalendar.getTime()).endsWith("AM"))
        {
            i = Integer.parseInt(getHourFormat().format(paramCalendar.getTime()));
            j = Integer.parseInt(getMinuteFormat().format(paramCalendar.getTime()));
            k=0;
        }
        else
        {
            i = Integer.parseInt(getHourFormat().format(paramCalendar.getTime()));
            j = Integer.parseInt(getMinuteFormat().format(paramCalendar.getTime()));
            k=1;
        }
        paramAlarmHelper.schedulePendingIntent(i, j, k);
    }
}
