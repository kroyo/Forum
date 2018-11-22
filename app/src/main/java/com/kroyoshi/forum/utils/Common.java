package com.kroyoshi.forum.utils;

/**
 * 通用的数据
 */
public class Common {

    //映射AVOS请求的错误代码
    public static String ormErrorCode(int code) {
        String errorStr = "";
        switch (code) {
            case 1:
                errorStr = "服务器内部错误或者参数错误";
                break;
            case 100:
                errorStr = "网络出现异常";
                break;
            case 124:
                errorStr = "请求超时";
                break;
            case 202:
                errorStr = "手机号码已经被注册";
                break;
            case 203:
                errorStr = "电子邮箱已经被占用";
                break;
            case 208:
                errorStr = "第三方帐号已经绑定到一个用户，不可绑定到其他用户";
                break;
            case 210:
                errorStr = "用户名和密码不匹配";
                break;
            case 211:
                errorStr = "找不到用户";
                break;
            case 214:
                errorStr = "手机号码已经被注册";
                break;
            case 215:
                errorStr = "手机号码未验证";
                break;
            case 216:
                errorStr = "邮箱未验证";
                break;
            case 301:
                errorStr = "新增对象失败";
                break;
            case 502:
                errorStr = "服务器维护中";
                break;
        }
        return errorStr;
    }

}
