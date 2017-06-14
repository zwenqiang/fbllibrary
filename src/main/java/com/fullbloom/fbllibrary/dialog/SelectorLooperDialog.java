package com.fullbloom.fbllibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.fullbloom.fbllibrary.R;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


/**select_looper_dialog.xml
 * 作者：zwq on 2016/9/20 11:17
 * 仿iOS 通用轮子
 *
 * T 为 必传
 * T 的实体 其中 要展示 字段 添加 @Looper 注解
 *
 */
public class SelectorLooperDialog<T> extends Dialog implements View.OnClickListener,OnItemSelectedListener<T> {
    private Context mContext;
    private TextView cancle,ok;
    private LoopView<T> left,center,right;
    private T selectleft,selectcenter,selectright;
    private OnSelect<T> onSelectTime;
    private TextView title;
    private boolean leftVisible,centerVisible,rightVisible = false;

    public SelectorLooperDialog(Context context) {
        this(context,0);
    }


    public SelectorLooperDialog(Context context, int theme) {
        super(context, R.style.DialogStyle_black);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.select_looper_dialog, null);
        setContentView(view);

        title = (TextView) view.findViewById(R.id.title);

        cancle = (TextView) view.findViewById(R.id.cancle);
        ok = (TextView) view.findViewById(R.id.ok);
        left = (LoopView<T>) view.findViewById(R.id.left);
        center = (LoopView<T>) view.findViewById(R.id.center);
        right = (LoopView<T>) view.findViewById(R.id.right);

        right.setOnItemSelectedListener(this);
        center.setOnItemSelectedListener(this);
        left.setOnItemSelectedListener(this);

        left.setNotLoop();
        center.setNotLoop();
        right.setNotLoop();

        cancle.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    public void setLeftData(List<T> data){
        if(data != null && data.size() >0){
            selectleft = data.get(0);
        }
        setVisible(leftVisible,centerVisible,rightVisible);
        left.setData(data);
    }public void setCenterData(List<T> data){
        if(data != null && data.size() >0){
            selectcenter = data.get(0);
        }
        setVisible(leftVisible,centerVisible,rightVisible);
        center.setData(data);

    }public void setRightData(List<T> data){
        if(data != null && data.size() >0){
            selectright = data.get(0);
        }
        setVisible(leftVisible,centerVisible,rightVisible);
        right.setData(data);
    }

    public void setLeftSelectPosition(int position){
        if(left != null){
            left.setInitPosition(position);
        }
    }
    public void setCenterSelectPosition(int position){
        if(center != null){
            center.setInitPosition(position);
        }
    }
    public void setRightSelectPosition(int position){
        if(right != null){
            right.setInitPosition(position);
        }
    }




    public void setVisible(boolean leftVisible,boolean centerVisible,boolean rightVisible){
        this.leftVisible = leftVisible;
        this.centerVisible = centerVisible;
        this.rightVisible = rightVisible;
        if(leftVisible){
            left.setVisibility(View.VISIBLE);
        }else {
            left.setVisibility(View.GONE);
        }
        if(centerVisible){
            center.setVisibility(View.VISIBLE);
        }else {
            center.setVisibility(View.GONE);
        }
        if(rightVisible){
            right.setVisibility(View.VISIBLE);
        }else {
            right.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.cancle) {
            dismiss();

        } else if (i == R.id.ok) {
            if (onSelectTime != null) {
                onSelectTime.onSelect(selectleft, selectcenter, selectright);
                dismiss();
            }

        }
    }
    @Override
    public void onItemSelected(LoopView loopView, int index, T values) {
        int i = loopView.getId();
        if (i == R.id.left) {
            selectleft = values;

        } else if (i == R.id.center) {
            selectcenter = values;

        } else if (i == R.id.right) {
            selectright = values;

        }
    }

    public interface OnSelect<T>{
       void onSelect(T left, T center, T right);
    }
    public void setOnSelect(OnSelect<T> onSelectTime){
        this.onSelectTime = onSelectTime;
    }

    public void setTitle(String val){
        title.setText(val);
    }

    private <T> String getStrForT(T t){

        if(t instanceof String){
            return (String) t;
        }
        List<Field> fields = Arrays.asList(t.getClass().getDeclaredFields());
        for (Field field:fields ) {
            if(field.getAnnotation(Looper.class) != null){
                field.setAccessible(true);
                try {
                    return  (String)field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
