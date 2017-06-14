package com.fullbloom.fbllibrary.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理类
 * @author zwq
 *
 */
public class CatchHandler implements UncaughtExceptionHandler {
	
	public static final String TAG = "CatchHandler";
	private static CatchHandler instance = null;
	//系统默认的UncaughtException处理类 
	private UncaughtExceptionHandler mHandler = null;
	private Context mContext = null;
	private Map<String,String> infos = new HashMap<String,String>();
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	
	/*保证只有一个CatchHandler实例*/
	private CatchHandler(){}
	/*获取CatchHandler的实例，单例模式*/
	public static CatchHandler getInstance() {
		if(instance == null){
			instance = new CatchHandler();
		}
		return instance ;
	}

	public void init(Context applicationContext) {
		this.mContext = applicationContext ;
		//获取系统默认的UncaughtException处理器 
		this.mHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CatchHandler为程序的默认处理器 
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	/**   
     * 当UncaughtException发生时会转入该函数来处理   
     */  
	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		if (!handleException(exception) && mHandler != null) {      
            //如果用户没有处理则让系统默认的异常处理器来处理      
            mHandler.uncaughtException(thread, exception);      
        } else {      
            try {      
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();      
            }      
            //退出程序      
            android.os.Process.killProcess(android.os.Process.myPid());      
            System.exit(1);
        }
	}
	/**   
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.   
     *    
     * @param ex   
     * @return true:如果处理了该异常信息;否则返回false.   
     */      
    private boolean handleException(Throwable exception) {
        if (exception == null) {      
            return false;      
        }      
        //收集设备参数信息       
        
        collectDeviceInfo(mContext);      
          
        //使用Toast来显示异常信息      
        new Thread() {
            @Override
            public void run() {      
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }      
        }.start();      
        //保存日志文件       
        saveCatchInfo2File(exception);    
        return true;      
    } 
    /**   
     * 收集设备参数信息   
     * @param ctx   
     */      
    public void collectDeviceInfo(Context ctx) {
        try {      
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {      
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);      
                infos.put("versionCode", versionCode);      
            }      
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }      
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {      
                field.setAccessible(true);      
                infos.put(field.getName(), field.get(null).toString());      
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }      
        }      
    } 
    /**   
     * 保存错误信息到文件中   
     *    
     * @param ex   
     * @return  返回文件名称   
     */      
    private String saveCatchInfo2File(Throwable ex) {
              
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");      
        }      
              
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);      
        Throwable cause = ex.getCause();
        while (cause != null) {      
            cause.printStackTrace(printWriter);      
            cause = cause.getCause();      
        }      
        printWriter.close();      
        String result = writer.toString();
        sb.append(result);      
        try {      
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = time +".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/mnt/sdcard/woodmall/";
                File dir = new File(path);
                if (!dir.exists()) {      
                    dir.mkdirs();      
                }
                File file = new File(dir,fileName);
                if(!file.exists()){
                	try {
                		file.createNewFile();
                	} catch (IOException e) {
                		e.printStackTrace();
                	}
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());    
                //发送给开发人员  
                //sendCrashLog2PM(path+fileName);  
                fos.close();      
            }      
            return fileName;      
        } catch (Exception e) {
            Log.e("LogError", "an error occured while writing file...", e);
        }      
        return null;      
    }
    
    private void sendCrashLog2PM(String fileName){
        if(!new File(fileName).exists()){
            Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return;  
        }  
        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {  
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
            while(true){  
                s = reader.readLine();  
                if(s == null) break;  
                //由于目前尚未确定以何种方式发送，所以先打出log日志。  
                Log.i("info", s.toString());
            }  
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }finally{   // 关闭流  
            try {  
                reader.close();  
                fis.close();  
            } catch (IOException e) {
                e.printStackTrace();  
            }  
        }  
    }
}
