package com.webserver1.servlets;

import com.webserver1.http.HttpRequest;
import com.webserver1.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 修改密码业务
 */
public class UpdateServlet {
    public void service(HttpRequest reuqest, HttpResponse response){
        /**
         * 1:获取用户信息
         */
        String username=reuqest.getParameter("username");
        String password=reuqest.getParameter("password");
        String newpassword=reuqest.getParameter("newpassword");
        /**
         * 2:修改
         */
        try(RandomAccessFile raf=new RandomAccessFile("user.dat","rw");){
            //匹配用户
            boolean check=false;
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                //读取用户名
                byte[]data=new byte[32];
                raf.read(data);
                String name=new String(data,"UTF-8").trim();
                if(name.equals(username)){
                    check=true;
                    //找到此用户,匹配密码
                    raf.read(data);
                    String pwd=new String(data,"UTF-8").trim();
                    if(pwd.equals(password)){
                        //匹配上后修改密码
                        //1:先将指针移动到密码位置
                        raf.seek(i*100+32);
                        //2:将新密码重新输入
                        data=newpassword.getBytes("UTF-8");
                        data=Arrays.copyOf(data,32);
                        raf.write(data);
                        //3:响应修改完毕页面
                        response.setEntity(new File("webapps/myweb/update_success.html"));
                    }else{
                        //原密码输入有误
                        response.setEntity(new File("webapps/myweb/update_fail.html"));
                    }
                    break;
                }
            }
            if(!check){
                //没有此用户
                response.setEntity(new File("webapps/myweb/no_user.html"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
