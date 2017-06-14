package com.fullbloom.fbllibrary.dialog.time;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zwq on 2017/3/31.
 */

public class TimeDialog {

    private TimeDialog(){}
    private static TimeDialog T = null;
    private Activity mActivity = null;
    public static TimeDialog getC(Activity activity){
        if(T == null){
            synchronized (TimeDialog.class){
                if(T == null){
                    T = new TimeDialog();
                }
            }
        }
        T.mActivity = activity;
        return T;
    }

    private TimePickerView pvTime;
    private int startTime = 2017;
    private int endTime = 2027;
    public void show() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();

        Calendar startDate = Calendar.getInstance();
        startDate.set(startTime,0,31);

        Calendar endDate = Calendar.getInstance();
        endDate.set(endTime,11,30);
        //时间选择器
        pvTime = new TimePickerView.Builder(mActivity, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                if(onSelectTime != null){
                    onSelectTime.onSelectTime(getTime(date));
                }
            }
        })
                .setType(new boolean[]{true,true,true,false,false,false})
                .setLabel("年", "月", "日", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate,endDate)
                .isCenterLabel(false)
                .build();
        pvTime.show();
    }

    public void setStartAndEndTime(int startTime,int endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }



    public interface OnSelectTime{
        void onSelectTime(String time);


    }
    private OnSelectTime onSelectTime;

    public void setOnSelectTime(OnSelectTime onSelectTime) {
        this.onSelectTime = onSelectTime;
    }



}
