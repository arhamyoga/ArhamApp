package net.yoga.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
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
        TextView f5138b;
        TextView f5139c;
        TextView f5140d;
        TextView f5141e;
        TextView f5142f;
        TextView f5143g;
        TextView f5144h;
        ImageView f5145i;
        Switch f5146j;
        final ReminderAdapter f5147k;

        public ProductViewHolder(ReminderAdapter reminderAdapter, View view) {
            super(view);
            this.f5147k = reminderAdapter;
            this.f5137a = view.findViewById(R.id.time);
            this.f5138b = view.findViewById(R.id.day1);
            this.f5139c = view.findViewById(R.id.day2);
            this.f5140d = view.findViewById(R.id.day3);
            this.f5141e = view.findViewById(R.id.day4);
            this.f5142f = view.findViewById(R.id.day5);
            this.f5143g = view.findViewById(R.id.day6);
            this.f5144h = view.findViewById(R.id.day7);
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
        new TimePickerDialog(this.mCtx, new TimePickerDialog.OnTimeSetListener() {
            /* renamed from: c */

            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                if (timePicker.isShown()) {
                    Calendar instance = Calendar.getInstance();
                    instance.set(Calendar.HOUR_OF_DAY, i);
                    instance.set(Calendar.MINUTE, i2);
                    showDialog(instance, reminder_custom, i);
                }
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
        productViewHolder.f5137a.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                reminderproduct = productList.get(productViewHolder.getAdapterPosition());
                showTimePickerDialog(reminderproduct, productViewHolder.getAdapterPosition());
            }
        });
        productViewHolder.f5138b.setVisibility(0);
        productViewHolder.f5139c.setVisibility(0);
        productViewHolder.f5140d.setVisibility(0);
        productViewHolder.f5141e.setVisibility(0);
        productViewHolder.f5142f.setVisibility(0);
        productViewHolder.f5143g.setVisibility(0);
        productViewHolder.f5144h.setVisibility(0);
        Log.d("Reminder adapter",this.reminderproduct.getMonday()+"");
        if (this.reminderproduct.getMonday()) {
            productViewHolder.f5138b.setText("M");
        } else {
            productViewHolder.f5138b.setVisibility(8);
        }
        if (this.reminderproduct.getTuesday()) {
            productViewHolder.f5139c.setText("T");
        } else {
            productViewHolder.f5139c.setVisibility(8);
        }
        if (this.reminderproduct.getWednesday()) {
            productViewHolder.f5140d.setText("W");
        } else {
            productViewHolder.f5140d.setVisibility(8);
        }
        if (this.reminderproduct.getThurday()) {
            productViewHolder.f5141e.setText("T");
        } else {
            productViewHolder.f5141e.setVisibility(8);
        }
        if (this.reminderproduct.getFriday()) {
            productViewHolder.f5142f.setText("F");
        } else {
            productViewHolder.f5142f.setVisibility(8);
        }
        if (this.reminderproduct.getSaturday()) {
            productViewHolder.f5143g.setText("S");
        } else {
            productViewHolder.f5143g.setVisibility(8);
        }
        if (this.reminderproduct.getSunday()) {
            productViewHolder.f5144h.setText("S");
        } else {
            productViewHolder.f5144h.setVisibility(8);
        }
        productViewHolder.f5146j.setChecked(this.reminderproduct.getOnTime());
        productViewHolder.f5146j.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                reminderproduct = productList.get(productViewHolder.getAdapterPosition());
                reminderproduct.setOnTime(z);
                String toJson = gson.toJson(productList);
                prefsEditor = mSharedPreferences.edit();
                prefsEditor.putString("Reminder_customObjectList", toJson);
                prefsEditor.apply();
            }
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
        builder.setMultiChoiceItems(arr, null, new DialogInterface.OnMultiChoiceClickListener() {
            /* renamed from: b */

            public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                if (z) {
                    arrayList.add(Integer.valueOf(i));
                    return;
                }
                arrayList.remove(Integer.valueOf(i));
            }
        });
        final Reminder reminder_custom2 = reminder_custom;
        final Calendar calendar2 = calendar;
        final int i2 = i;
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
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
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        if (arrayList.get(i2).equals(Integer.valueOf(0))) {
                            reminder_custom2.setMonday(true);
                        } else if (arrayList.get(i2).equals(Integer.valueOf(1))) {
                            reminder_custom2.setTuesday(true);
                        } else if (arrayList.get(i2).equals(Integer.valueOf(2))) {
                            reminder_custom2.setWednesday(true);
                        } else if (arrayList.get(i2).equals(Integer.valueOf(3))) {
                            reminder_custom2.setThurday(true);
                        } else if (arrayList.get(i2).equals(Integer.valueOf(4))) {
                            reminder_custom2.setFriday(true);
                        } else if (arrayList.get(i2).equals(Integer.valueOf(5))) {
                            reminder_custom2.setSaturday(true);
                        } else if (arrayList.get(i2).equals(Integer.valueOf(6))) {
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
            }
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
//        paramAlarmHelper.schedulePendingIntent(i, j, k);
    }

}
