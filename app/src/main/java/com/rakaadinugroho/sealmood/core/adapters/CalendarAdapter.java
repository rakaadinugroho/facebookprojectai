package com.rakaadinugroho.sealmood.core.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.models.Mood;
import com.rakaadinugroho.sealmood.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.internal.Util;

import static android.content.ContentValues.TAG;

/**
 * Created by Raka Adi Nugroho on 4/5/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private GregorianCalendar mCalendar;
    private Calendar mCalendarToday;
    private Context mContext;
    private List<String> mItems;
    private int mMonth;
    private int mYear;
    private int mDaysShown;
    private int mDaysLastMonth;
    private int mDaysNextMonth;
    private final String[] mDays    = {"S", "S", "R", "K", "J", "S", "M"};
    private final int[] mDaysInMonth    = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private ArrayList<Mood> moods;

    public CalendarAdapter(Context context, int mMonth, int mYear, DisplayMetrics metrics, ArrayList<Mood> moods){
        this.mContext   = context;
        this.mMonth     = mMonth;
        this.mYear      = mYear;
        this.mCalendar  = new GregorianCalendar(mYear, mMonth, 1);
        this.mCalendarToday = Calendar.getInstance();
        this.moods  = moods;
        populateMonth();
    }

    private void populateMonth() {
        mItems  = new ArrayList<String>();
        for (String day: mDays){
            mItems.add(day);
            mDaysShown++;
        }
        
        int firstDay    = getDay(mCalendar.get(Calendar.DAY_OF_WEEK));
        int prevDay;

        if (mMonth == 0)
            prevDay = daysInMonth(11) - firstDay + 1;
        else
            prevDay = daysInMonth(mMonth-1) - firstDay + 1;

        for (int i = 0; i < firstDay; i++){
            mItems.add(String.valueOf(prevDay+1));
            mDaysLastMonth++;
            mDaysShown++;
        }

        int daysInMonth = daysInMonth(mMonth);
        for (int i=1; i <= daysInMonth; i++){
            mItems.add(String.valueOf(i));
            mDaysShown++;
        }

        mDaysNextMonth  = 1;
        while (mDaysShown % 7 != 0){
            mItems.add(String.valueOf(mDaysNextMonth));
            mDaysShown++;
            mDaysNextMonth++;
        }

    }

    private int daysInMonth(int month) {
        int daysInMonth = mDaysInMonth[month];
        if (month == 1 && mCalendar.isLeapYear(mYear))
            daysInMonth++;
        return daysInMonth;
    }

    private int getDay(int day) {
        switch (day){
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;

            default:
                return 0;
        }
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CalendarViewHolder(inflater.inflate(R.layout.item_calendar, parent, false));
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        holder.dayCalendar.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        holder.dayCalendar.setText(mItems.get(position));
        holder.dayCalendar.setTextColor(Color.BLACK);

        int[] date  = getDate(position);
        if (date != null){
            // This Day is Part of This Month ?
            holder.dayCalendar.setTextColor(CalendarUtil.resolveDate(date[1], mMonth)? Color.BLACK: Color.GRAY);
            for (Mood mood: moods) {
                if (date[0] == mood.day){
                    holder.cardCalendar.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                }
            }
            if (isToday(date[0], date[1], date[2])){
                holder.dayCalendar.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                holder.cardCalendar.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private boolean isToday(int day, int month, int year) {
        return mCalendarToday.get(Calendar.MONTH) == month && mCalendarToday.get(Calendar.YEAR) == year && mCalendarToday.get(Calendar.DAY_OF_MONTH) == day;
    }

    private int[] getDate(int position) {
        int date[] = new int[3];
        if (position <= 6){
            return null;
        }else if (position <= mDaysLastMonth + 6){
            // Previous Month
            date[0] = Integer.parseInt(mItems.get(position));
            if (mMonth == 0){
                date[1] = 11;
                date[2] = mYear - 1;
            }else{
                date[1] = mMonth-1;
                date[2] = mYear;
            }
        }else if (position <= mDaysShown - mDaysNextMonth){
            // Current Month
            date[0] = position - (mDaysLastMonth + 6);
            date[1] = mMonth;
            date[2] = mYear;
        }else {
            // Next Month
            date[0] = Integer.parseInt(mItems.get(position));
            if (mMonth == 11){
                // Last Year
                date[1] = 0;
                date[2] = mYear + 1;
            }else {
                // Desember
                date[1] = mMonth + 1;
                date[2] = mYear;
            }
        }
        return date;
    }

    @Override
    public int getItemCount() {
        return 40;
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayCalendar;
        CardView cardCalendar;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            dayCalendar = (TextView) itemView.findViewById(R.id.dayCalendar);
            cardCalendar    = (CardView) itemView.findViewById(R.id.cardCalendar);
        }
    }
}
