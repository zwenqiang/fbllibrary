/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fullbloom.fbllibrary.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fullbloom.fbllibrary.FBLLibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 */

@SuppressLint("NewApi")
public class CommonUtils {

    /**
     * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";

    private static InputFilter emojiFilter = new InputFilter() {


        Pattern emoji = Pattern.compile(


                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",


                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);


        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart,


                                   int dend) {


            Matcher emojiMatcher = emoji.matcher(source);


            if (emojiMatcher.find()) {


                return "";


            }
            return null;


        }
    };

    /**
     * 是否有网络
     * @return
     */
    public static boolean isNetWorkConnecteds(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    public static boolean checkPhone(String phone) {
        String telRegex = "[1][345789]\\d{9}";
        Pattern pattern = Pattern.compile(telRegex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();

    }

    /**
     * //TODO 检测身份证
     * @author zwq
     */
    public static boolean checkShenFen(String phone) {
        String telRegex = "/^(\\d{16}|\\d{19})$/";
        Pattern pattern = Pattern.compile(telRegex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * //TODO 要求是密码大于6位，并仅允许字母及数字组合 且必须含一位以上大写字母
     * @return
     */
    public static boolean checkMiMa(String password) {
        String str = "^(?=.*?[A-Z])(?=.*?[0-9])[a-zA-Z0-9]{7,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isLetter(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        return pattern.matcher(str).matches();
    }

    /**
     * 作者：zwq
     * 日期: 2016/10/24 11:02
     * 描述:隐藏手机号中间4位
     */
    public static String hidePhone(String phone) {

        if (phone.length() < 11)
            throw new RuntimeException("胸弟，手机号不能小于11位");

        StringBuilder sb = new StringBuilder();
        return sb.append(phone.substring(0, 3)).append("****").append(phone.substring(7, phone.length())).toString();
    }

    /**
     * 作者：zwq
     * 日期：2016/11/24 10:53
     * 描述： 获取两位小数金额
     */
    public static String getMoney(double d) {
        StringBuffer builder = new StringBuffer();
        return builder.append(d).append("0").toString();
    }
    public static String getMoneyFormat(double d) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(d);
    }

    /**
     * 作者：zwq
     * 日期: 2016/10/24 11:02
     * 描述:中间字符为*
     * @param str 要替换字符串 				15165868558
     * @param  showLastNum 最后显示的位数 	3
     * @param starshowNum   开始显示的位数 	4
     *     例：   151****8558
     */
    public static String hideCenterNum(String str, int starshowNum, int showLastNum) {

        if (str.length() <= showLastNum + starshowNum)
            throw new RuntimeException("胸滴，字符串太短了");

        StringBuilder sb = new StringBuilder();
        StringBuilder rel = new StringBuilder();
        String center = str.substring(starshowNum, str.length() - showLastNum);
        char[] chars = center.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            String x = String.valueOf(aChar);
            rel.append(x.replace(x, "*"));
        }
        return sb.append(str.substring(0, starshowNum)).append(rel.toString()).append(str.substring(str.length() - showLastNum, str.length())).toString();
    }


    public static boolean checkChinese(String str) {
        boolean result = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            result = true;
        }
        return result;
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:16
     * //TODO 描述: 是否是数字
     */
    public static boolean checkNumber(String value) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public static boolean checkEmail(String value) {
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * //TODO 判断Sdcard是否存在
     *
     * @return
     */
    public static boolean checkSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }


    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:06
     * //TODO 描述: 格式化后时间
     */
    public static String getTime(long timeMillis) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        // System.out.println(now + " = " +
        // formatter.format(calendar.getTime()));

        return formatter.format(calendar.getTime());
    }

    public static String getTime(long timeMillis, String format) {

        DateFormat formatter = new SimpleDateFormat(format);
        // long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        // System.out.println(now + " = " +
        // formatter.format(calendar.getTime()));

        return formatter.format(calendar.getTime());
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:06
     * //TODO 描述: 格式化后时间
     */
    public static String getTime4(long timeMillis) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        // System.out.println(now + " = " +
        // formatter.format(calendar.getTime()));

        return formatter.format(calendar.getTime());
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:06
     * //TODO 描述: 格式化后时间
     */
    public static String getBirthday(long timeMillis) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        // System.out.println(now + " = " +
        // formatter.format(calendar.getTime()));

        return formatter.format(calendar.getTime());
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:05
     * //TODO 描述: 系统时间  long
     */
    public static long getTimeStamp(String data) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = null;
        try {
            date = format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return 1429763088984L;
        }

        return date.getTime();
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:05
     * //TODO 描述: 系统时间  long
     */
    public static String getFormatTime(long longs) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String date = null;
        try {
            date = format.format(new Date(longs));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return date.trim();
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:05
     * //TODO 描述: 系统时间  long
     */
    public static String getFormatTime2(long longs) {
        if (longs==0){
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = null;
        try {
            date = format.format(new Date(longs));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return date.trim();
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:05
     * //TODO 描述: 系统时间  long
     */
    public static String getFormatTime3(long longs) {

        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
        String date = null;
        try {
            date = format.format(new Date(longs));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return date.trim();
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:05
     * //TODO 描述: 月 日  时 分 miao
     */
    public static String getSYSDateStr1(long longs) {
        if (longs==0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        String date = sdf.format(new Date(longs));
        return date.trim();
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:05
     * //TODO 描述: 月 日  时 分
     */
    public static String getSYSDateStr(long longs) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
                Locale.getDefault());
        String date = sdf.format(new Date(longs));
        return date.trim();
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:04
     * 描述: 时分
     */
    public static String getSYSDateHourStr(long times) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        String date = sdf.format(new Date(times));
        return date.trim();
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:04
     * //TODO 描述: 获取系统年
     */
    public static int getSYSDateYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy",
                Locale.getDefault());
        String date = sdf.format(new Date(System.currentTimeMillis()));
        return Integer.valueOf(date.trim());
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:04
     * //TODO 描述: 打开文件
     */
    public static void openFile(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        context.startActivity(intent);
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:08
     * //TODO 描述: 获取文件类型
     */
    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean containsAny(String str, String searchChars) {
        if (str.contains(searchChars))
            return true;
        else
            return false;
    }


    /**
     * //TODO 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {

        try {
            String pkName = FBLLibrary.mContext.getPackageName();
            String versionName = FBLLibrary.mContext.getPackageManager().getPackageInfo(pkName, 0).versionName;

            int versionCode = FBLLibrary.mContext.getPackageManager().getPackageInfo(pkName, 0).versionCode;
            return versionCode;
        } catch (Exception e) {

        }
        return -1;
    }

    /**

     *  //TODO res --> Drawable
     * @return
     */
    @SuppressLint("NewApi")
    public static Drawable res2Drawable(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;


        InputStream is = context.getResources().openRawResource(resId);


        return new BitmapDrawable(BitmapFactory.decodeStream(is, null, opt));

    }

    /**
     * //TODO 短信分享
     *
     * smstext 短信分享内容
     * @return
     */
    public static Boolean sendSms(Context mContext, String smstext) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", smstext);
        mContext.startActivity(mIntent);
        return null;
    }

    /**
     * //TODO 邮件分享
     *
     * title 邮件的标题
     * text 邮件的内容
     * @return
     */
    public static void sendMail(Context mContext, String title, String text) {
        // 调用系统发邮件
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // 设置文本格式
        emailIntent.setType("text/plain");
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        mContext.startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));
    }

    /**
     * //TODO 隐藏软键盘
     */
    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                View currentFocus = activity.getCurrentFocus();
                if (currentFocus != null) {
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
            }
        }
    }

    /**
     * //TODO 显示软键盘
     */
    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!imm.isActive()) {
                imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /** 网络类型 **/
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    /**
     * //TODO 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 作者：zwq
     * 日期: 2016/8/12 11:15
     * //TODO 描述: 获取屏幕宽度
     */
    public static int getSreenwidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth();
        return w;
    }

    /**
     * //TODO 获取屏幕高度
     */
    public static int getScreenHeight(Context aty) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = aty.getResources().getDisplayMetrics();
        int h = dm.heightPixels;
        // int h = aty.getWindowManager().getDefaultDisplay().getHeight();
        return h;
    }


    /**
     * //TODO 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * //TODO 获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * //TODO 获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getSDCardPath() + File.separator + folderName
                + File.separator);
        file.mkdirs();
        return file;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * //TODO 把uri转为File对象
     */
    public static File uri2File(Activity aty, Uri uri) {
        if (getSDKVersion() < 11) {
            // 在API11以下可以使用：managedQuery
            String[] proj = {MediaStore.Images.Media.DATA};
            @SuppressWarnings("deprecation")
            Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null,
                    null);
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor
                    .getString(actual_image_column_index);
            return new File(img_path);
        } else {
            // 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(aty, uri, projection, null,
                    null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }

    /**
     * //TODO 快速复制文件（采用nio操作）
     *
     * @param is
     *            数据来源
     * @param os
     *            数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os)
            throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }

    /**
     * //TODO 获取手机IMEI码
     */
    public static String getPhoneIMEI(Context cxt) {


        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * //TODO 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * //TODO 获取系统版本
     *
     * @return 形如2.3.3
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * //TODO 调用系统发送短信
     */
    public static void sendSMS(Context cxt, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        cxt.startActivity(intent);
    }

    /**
     * //TODO 判断是否为wifi联网
     */
    public static boolean isWiFi(Context cxt) {
        ConnectivityManager cm = (ConnectivityManager) cxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // wifi的状态：ConnectivityManager.TYPE_WIFI
        // 3G的状态：ConnectivityManager.TYPE_MOBILE
        NetworkInfo.State state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        return NetworkInfo.State.CONNECTED == state;
    }

    /**
     * //TODO 判断当前应用程序是否后台运行
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 后台运行
                    return true;
                } else {
                    // 前台运行
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * //TODO 判断手机是否处理睡眠
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * //TODO 安装apk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * //TODO 获取当前应用程序的版本名
     */
    public static String getAppVersionName(Context context) {
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return version;
    }

    public static Integer getAppVersionCode(Context context) {
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return version;
    }

    /**
     * //TODO 回到home，后台运行
     */
    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * //TODO 获取应用签名
     *
     * @param context
     * @param pkgName
     */
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(
                    pkgName, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }

    /**
     * //TODO 将签名字符串转换成需要的32位签名
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * //TODO 获取设备的可用内存大小
     *
     * @param cxt
     *            应用上下文对象context
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory(Context cxt) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * //TODO 清理后台进程与服务
     *
     * @param cxt
     *            应用上下文对象context
     * @return 被清理的数量
     */
    public static int gc(Context cxt) {
        long i = getDeviceUsableMemory(cxt);
        int count = 0; // 清理掉的进程数
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(100);
        if (serviceList != null)
            for (ActivityManager.RunningServiceInfo service : serviceList) {
                if (service.pid == android.os.Process.myPid())
                    continue;
                try {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e) {
                    e.getStackTrace();
                    continue;
                }
            }

        // 获取正在运行的进程列表
        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null)
            for (ActivityManager.RunningAppProcessInfo process : processList) {
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (process.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    // pkgList 得到该进程下运行的包名
                    String[] pkgList = process.pkgList;
                    for (String pkgName : pkgList) {
                        try {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        } catch (Exception e) { // 防止意外发生
                            e.getStackTrace();
                            continue;
                        }
                    }
                }
            }
        return count;
    }

    /**
     * //TODO 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendlyTime(String sdate) {
        Date time = null;

        if (isInEasternEightZones()) {
            time = toDate(sdate);
        } else {
            time = transformTime(toDate(sdate), TimeZone.getTimeZone("GMT+08"),
                    TimeZone.getDefault());
        }

        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天 ";
        } else if (days > 2 && days < 31) {
            ftime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            ftime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            ftime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            ftime = "3个月前";
        } else {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }


    /**
     * 根据不同时区，转换时间 2014年7月31日
     */
    public static Date transformTime(Date date, TimeZone oldZone,
                                     TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }


    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    /**
     * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
            defaultVaule = true;
        else
            defaultVaule = false;
        return defaultVaule;
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     * @param sdate long
     * @return boolean
     */
    public static boolean isToday(Long sdate) {
        boolean b = false;
        Date time = new Date(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    public static boolean isTwoToday(Long sdate, Long edata) {
        boolean b = false;
        Date time = new Date(sdate);
        Date today = new Date(edata);
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime
     *            时间区间,半闭合,如[08/01-12/31)
     * @param curTime
     *            需要判断的时间 如 07/25
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }

    public static StringBuffer getSBuffer() {
        StringBuffer buffer = new StringBuffer();
        return buffer;
    }

    public static StringBuilder getSBuilder() {
        StringBuilder builder = new StringBuilder();
        return builder;
    }

    public static int changeTextSize(Context context, int size) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (size * dm.density);
    }

    //判断选择时间是否 在当前时间后
    public static boolean curDateBeforDate(String formatTime, String formatStr) {

        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date d1 = new Date(time);
        String formatData = format.format(d1);

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date d2;
        try {
            d2 = sdf.parse(formatTime);
            return d1.before(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断选择时间是否 在当前时间后
    public static boolean curDateBeforTwoDate(String startTime, String formatTime, String formatStr) {

        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date d1 = null;
        try {
            d1 = format.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date d2;
        try {
            d2 = sdf.parse(formatTime);
            return d1.before(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断选择时间是否 在当前时间钱
    public static boolean curDateAfterDate(String formatTime, String formatStr) {

        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date d1 = new Date(time);
        String formatData = format.format(d1);
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date d2;
        try {
            d2 = sdf.parse(formatTime);
            return d1.after(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Bitmap 转 Base64 -> String
     * @Description:Bitmap转Base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64
                        .encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * String -> Bitmap
     * @param picContent 图片内容 字符串
     * @return
     */
    public static Bitmap decodeImg(String picContent) {
        Bitmap bitmap = null;

        byte[] imgByte = null;
        InputStream input = null;
        try {
            imgByte = Base64.decode(picContent, Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            input = new ByteArrayInputStream(imgByte);
            SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
            bitmap = (Bitmap) softRef.get();
            ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imgByte != null) {
                imgByte = null;
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public static String formatDouble2Percent(double result) {
        NumberFormat percent = NumberFormat.getPercentInstance();  //建立百分比格式化引用
        percent.setMaximumFractionDigits(0); //百分比小数点最多3位
        BigDecimal bigdec = new BigDecimal(result);
        return percent.format(bigdec);

    }
    /** 对TextView设置不同状态时其文字颜色。 */
    public static ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /** 设置Selector。 */
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused,int idUnable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_focused }, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_window_focused }, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[] {}, normal);
        return bg;
    }


    /**
     * 压缩图片
     * @param image
     * @return
     */

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public static void setInputLength(EditText et,int strLength){

        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(strLength),emojiFilter});

    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @return
     */
    public static String listTOString(List list) {
       StringBuffer sb = new StringBuffer();
        if (list==null){
            return "";
        }

        for (int i = 0,count=list.size(); i <count; i++) {
            sb.append(list.get(i));
            if (i<count){
                sb.append(",");
            }

        }
        return sb.toString();
    }

    public static boolean SDKGT23(){
        return Build.VERSION.SDK_INT >= 23;
    }


    public static File getCacheFile(File parent, String child) {
        // 创建File对象，用于存储拍照后的图片
        File file = new File(parent, child);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
