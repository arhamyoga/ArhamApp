package net.yoga.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.SystemClock;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.yoga.R;
import net.yoga.model.Reminder;
import net.yoga.receiver.AlarmHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ProductViewHolder> {

    SimpleDateFormat f5148a;
    private AlarmHelper alarmHelper;
    private Gson gson;
    private Context mCtx;
    private long mLastClickTime = 0;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor prefsEditor;
    private List<Reminder> productList;
    private Reminder reminderproduct;

    class C10427 implements DialogInterface.OnClickListener {

        final  ReminderAdapter f3351a;

        C10427(ReminderAdapter reminderAdapter) {
            this.f3351a = reminderAdapter;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView f5137a;
        TextView sunday;
        TextView monday;
        TextView tuesday;
        TextView wednesday;
        TextView thursday;
        TextView friday;
        TextView saturday;
        ImageView f5145i;
        Switch f5146j;
        final ReminderAdapter f5147k;

        public ProductViewHolder(ReminderAdapter reminderAdapter, View view) {
            super(view);
            this.f5147k = reminderAdapter;
            this.f5137a = view.findViewById(R.id.time);
            this.sunday = view.findViewById(R.id.day1);
            this.monday = view.findViewById(R.id.day2);
            this.tuesday = view.findViewById(R.id.day3);
            this.wednesday = view.findViewById(R.id.day4);
            this.thursday = view.findViewById(R.id.day5);
            this.friday = view.findViewById(R.id.day6);
            this.saturday = view.findViewById(R.id.day7);
            this.f5145i = view.findViewById(R.id.timedelete);
            this.f5146j = view.findViewById(R.id.timeswitch);
        }
    }
    public ReminderAdapter(Context context, List<Reminder> list, Gson gson, SharedPreferences sharedPreferences, SharedPreferences.Editor editor, AlarmHelper alarmHelper) {
        this.mCtx = context;
        this.productList = list;
        this.mSharedPreferences = sharedPreferences;
        this.prefsEditor = editor;
        this.gson = gson;
        this.alarmHelper = alarmHelper;
    }

    private void showTimePickerDialog(final Reminder reminder_custom, final int i) {
        Calendar instance = Calendar.getInstance();
        /* renamed from: c */
        new TimePickerDialog(this.mCtx, (timePicker, i1, i2) -> {
            if (timePicker.isShown()) {
                Calendar instance1 = Calendar.getInstance();
                instance1.set(Calendar.HOUR_OF_DAY, i1);
                instance1.set(Calendar.MINUTE, i2);
                showDialog(instance1, reminder_custom, i1);
            }
        }, instance.get(Calendar.HOUR_OF_DAY), instance.get(Calendar.MINUTE), false).show();
    }

    public SimpleDateFormat getHourFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh");
        this.f5148a = simpleDateFormat;
        return simpleDateFormat;
    }

    public int getItemCount() {
        return this.productList.size();
    }

    public SimpleDateFormat getMinuteFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        this.f5148a = simpleDateFormat;
        return simpleDateFormat;
    }

    @SuppressLint({"SetTextI18n"})
    public void onBindViewHolder(final ProductViewHolder productViewHolder, int i) {
        this.reminderproduct = this.productList.get(i);
        productViewHolder.f5137a.setText(this.reminderproduct.getTime());
        productViewHolder.f5137a.setOnClickListener(view -> {
            reminderproduct = productList.get(productViewHolder.getAdapterPosition());
            showTimePickerDialog(reminderproduct, productViewHolder.getAdapterPosition());
        });
        productViewHolder.sunday.setVisibility(View.VISIBLE);
        productViewHolder.monday.setVisibility(View.VISIBLE);
        productViewHolder.tuesday.setVisibility(View.VISIBLE);
        productViewHolder.wednesday.setVisibility(View.VISIBLE);
        productViewHolder.thursday.setVisibility(View.VISIBLE);
        productViewHolder.friday.setVisibility(View.VISIBLE);
        productViewHolder.saturday.setVisibility(View.VISIBLE);
        if (this.reminderproduct.getSunday()) {
            productViewHolder.sunday.setText("Sun");
        } else {
            productViewHolder.sunday.setVisibility(View.GONE);
        }
        if (this.reminderproduct.getMonday()) {
            productViewHolder.monday.setText("Mon");
        } else {
            productViewHolder.monday.setVisibility(View.GONE);
        }
        if (this.reminderproduct.getTuesday()) {
            productViewHolder.tuesday.setText("Tues");
        } else {
            productViewHolder.tuesday.setVisibility(View.GONE);
        }
        if (this.reminderproduct.getWednesday()) {
            productViewHolder.wednesday.setText("Wed");
        } else {
            productViewHolder.wednesday.setVisibility(View.GONE);
        }
        if (this.reminderproduct.getThurday()) {
            productViewHolder.thursday.setText("Thu");
        } else {
            productViewHolder.thursday.setVisibility(View.GONE);
        }
        if (this.reminderproduct.getFriday()) {
            productViewHolder.friday.setText("Fri");
        } else {
            productViewHolder.friday.setVisibility(View.GONE);
        }
        if (this.reminderproduct.getSaturday()) {
            productViewHolder.saturday.setText("Sat");
        } else {
            productViewHolder.saturday.setVisibility(View.GONE);
        }
        productViewHolder.f5146j.setChecked(this.reminderproduct.getOnTime());
        productViewHolder.f5146j.setOnCheckedChangeListener((compoundButton, z) -> {
            reminderproduct = productList.get(productViewHolder.getAdapterPosition());
            reminderproduct.setOnTime(z);
            String toJson = gson.toJson(productList);
            prefsEditor = mSharedPreferences.edit();
            prefsEditor.putString("Reminder_customObjectList", toJson);
            prefsEditor.apply();
        });
        productViewHolder.f5145i.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
                    mLastClickTime = SystemClock.elapsedRealtime();
                    removeAt(productViewHolder.getAdapterPosition());
                }
            }
        });

    }
    public SimpleDateFormat startTimeFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        this.f5148a = simpleDateFormat;
        return simpleDateFormat;
    }

    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ProductViewHolder(this, LayoutInflater.from(this.mCtx).inflate(R.layout.reminder_row, null));
    }

    public void removeAt(int i) {
        this.productList.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, this.productList.size());
        String toJson = this.gson.toJson(this.productList);
        this.prefsEditor = this.mSharedPreferences.edit();
        this.prefsEditor.putString("Reminder_customObjectList", toJson);
        this.prefsEditor.apply();
    }

    public void showDialog(Calendar calendar, Reminder reminder_custom, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mCtx);
        builder.setTitle("Days");
        final ArrayList arrayList = new ArrayList();
        String arr[] ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        /* renamed from: b */
        builder.setMultiChoiceItems(arr, null, (dialogInterface, i1, z) -> {
            if (z) {
                arrayList.add(Integer.valueOf(i1));
                return;
            }
            arrayList.remove(Integer.valueOf(i1));
        });
        final Reminder reminder_custom2 = reminder_custom;
        final Calendar calendar2 = calendar;
        final int i2 = i;
        builder.setPositiveButton("Yes", (dialogInterface, i12) -> {
            if (arrayList.size() > 0) {
                dialogInterface.dismiss();
                reminder_custom2.setTime(startTimeFormat().format(calendar2.getTime()));
                reminder_custom2.setMonday(false);
                reminder_custom2.setTuesday(false);
                reminder_custom2.setWednesday(false);
                reminder_custom2.setThurday(false);
                reminder_custom2.setFriday(false);
                reminder_custom2.setSaturday(false);
                reminder_custom2.setSunday(false);
                for (int i21 = 0; i21 < arrayList.size(); i21++) {
                    if (arrayList.get(i21).equals(Integer.valueOf(1))) {
                        reminder_custom2.setMonday(true);
                    } else if (arrayList.get(i21).equals(Integer.valueOf(2))) {
                        reminder_custom2.setTuesday(true);
                    } else if (arrayList.get(i21).equals(Integer.valueOf(3))) {
                        reminder_custom2.setWednesday(true);
                    } else if (arrayList.get(i21).equals(Integer.valueOf(4))) {
                        reminder_custom2.setThurday(true);
                    } else if (arrayList.get(i21).equals(Integer.valueOf(5))) {
                        reminder_custom2.setFriday(true);
                    } else if (arrayList.get(i21).equals(Integer.valueOf(6))) {
                        reminder_custom2.setSaturday(true);
                    } else if (arrayList.get(i21).equals(Integer.valueOf(0))) {
                        reminder_custom2.setSunday(true);
                    }
                }
                reminder_custom2.setOnTime(true);
                m4157a(alarmHelper, calendar2);
                String toJson = gson.toJson(productList);
                prefsEditor = mSharedPreferences.edit();
                prefsEditor.putString("Reminder_customObjectList", toJson);
                prefsEditor.apply();
                notifyItemChanged(i2);
                notifyItemRangeChanged(i2, productList.size());
                notifyDataSetChanged();
                return;
            }
            Toast.makeText(mCtx, "Please select at-least one day", 0).show();
        });
        builder.setNegativeButton("No", new C10427(this));
        builder.create().show();
    }

    void m4157a(AlarmHelper paramAlarmHelper, Calendar paramCalendar)
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
