package com.fullbloom.fbllibrary.network;


/**
 * Restful传输的头信息
 */
public class HeaderEntity implements java.io.Serializable{

    //设备编号
    private String machine_code;

    //访问时间
    private long access_time;

    //访问路径
    private String access_url;


    //设备操作系统类型，如IOS , Android
    private String os_type;

    //设备操作系统版本号
    private String os_version;

    //APP版本号
    private String app_version;

    //本地语言
    private String locale_language;

    //访问Tocken, 和用户绑定
    private String access_token;

    //加密后的数据
    private String sign;


    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public long getAccess_time() {
        return access_time;
    }

    public void setAccess_time(long access_time) {
        this.access_time = access_time;
    }

    public String getAccess_url() {
        return access_url;
    }

    public void setAccess_url(String access_url) {
        this.access_url = access_url;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getOs_type() {
        return os_type;
    }

    public void setOs_type(String os_type) {
        this.os_type = os_type;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getLocale_language() {
        return locale_language;
    }

    public void setLocale_language(String locale_language) {
        this.locale_language = locale_language;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{")
                .append("machine_code:'").append(MacKit.encrypt(machine_code)).append("'")
                .append(", access_time:").append(access_time)
                .append(", access_url:'").append(access_url).append("'")
                .append(", os_type:'").append(os_type).append("'")
                .append(", os_version:'").append(os_version).append("'")
                .append(", app_version:'").append(app_version).append("'")
                .append(", locale_language:'").append(locale_language).append("'")
                .append(", access_token:'").append((access_token==null?"":access_token)).append('\'')
                .append("}").toString();
    }




    public static void main(String[] args) {
        System.out.print(new HeaderEntity());
    }
}
