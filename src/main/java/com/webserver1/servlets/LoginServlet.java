package com.webserver1.servlets;

import com.webserver1.http.HttpRequest;
import com.webserver1.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * 登录业务
 */
public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){
        //1获取用户登录信息
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        //2读user.dat文件,比对用户信息
        try (RandomAccessFile raf=new RandomAccessFile("user.dat","r");){
            //开关,默认登录失败
            boolean check=false;
            for(int i=0;i<raf.length()/100;i++){
                //移动指针到当前记录的开始位置
                raf.seek(i*100);
                //读用户名
                byte []data=new byte[32];
                raf.read(data);
                String name=new String(data,"UTF-8").trim();
                if(name.equals(username)){
                    //读密码
                    raf.read(data);
                    String pwd=new String(data,"UTF-8").trim();
                    if(pwd.equals(password)){
                        //登陆成功
                        check=true;
                    }
                    break;
                }
            }
            if(check){
                //登陆成功
                response.setEntity(new File("webapps/myweb/login_success.html"));
            }else{
                //登陆失败
                response.setEntity(new File("webapps/myweb/login_fail.html"));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
