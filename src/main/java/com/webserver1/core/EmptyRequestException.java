package com.webserver1.core;
/**
 * 空请求异常
 * @author ta
 *
 */
public class EmptyRequestException extends Exception{
    /**
     * serialVersionUID自动生成方法：
     * 1、打开File菜单，选择Settings选项，打开Settings设置对话框
     * 2、在Editor中打开Inspections
     * 3、在右边的搜索框中输入serialVersionUID关键字，出现以下选项，勾选"Serializable class without serialVersionUID"
     * 4、光标放在类名上，再次按Alt+Enter键，这个时候可以看到"Add serialVersionUID field"提示信息
     * 5、点击Add'serialVersionUID'field即可自动生成serialVersionUID
     */
    private static final long serialVersionUID =1L;

    public EmptyRequestException() {
        super();
    }

    public EmptyRequestException(String message) {
        super(message);
    }

    public EmptyRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyRequestException(Throwable cause) {
        super(cause);
    }

    protected EmptyRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
