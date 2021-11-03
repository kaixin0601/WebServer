package com.webserver1.core;


import com.webserver1.http.HttpRequest;
import com.webserver1.http.HttpResponse;
import com.webserver1.servlets.LoginServlet;
import com.webserver1.servlets.RegServlet;
import com.webserver1.servlets.UpdateServlet;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
/**
 * 处理客户端请求
 * @author ta
 *
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler (Socket socket){
        try{
            this.socket=socket;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try{
            /**
             * 主流程:
             * 1:解析请求
             * 2:处理请求
             * 3:发送响应
             */

            //1准备工作
            //1.1解析请求,创建请求对象
            HttpRequest request=new HttpRequest(socket);
            //1.2创建响应对象
            HttpResponse response=new HttpResponse(socket);
/*          String da=request.getHeaders("Accept");
            System.out.println("da:"+da);*/
            System.out.println(request.getHeaders("Accept"));
            //2处理请求
            //2.1:获取请求的资源路径
            String url=request.getRequestURI();
            //判断该请求是否为请求业务
            if("/myweb/reg".equals(url)){
                RegServlet servlet=new RegServlet();
                servlet.service(request,response);
            }else if("/myweb/login".equals(url)){
                LoginServlet servlet=new LoginServlet();
                servlet.service(request,response);
            }else if("/myweb/update".equals(url)){
                UpdateServlet servlet=new UpdateServlet();
                servlet.service(request,response);
            }else{
                //2.2:根据资源路径去webapps目录中寻找该资源
                File file=new File("webapps"+url);
                if(file.exists()){
                    System.out.println("找到该资源!");
                    //向响应对象中设置要响应的资源内容
                    response.setEntity(file);
                }else{
                    System.out.println("该资源不存在!");
                    //设置状态代码404
                    response.setStatusCode(404);
                    //设置404页面
                    response.setEntity(new File("webapps/root/404.html"));
                }
            }
            //3响应客户端
            //http://localhost:8888/myweb/index.html
            response.flush();
        }catch(EmptyRequestException e){
            /**
             * 实例化HttpRequest时若发现是空请求时
             * 该构造方法会将该异常抛出，这里不做任
             * 何处理，直接在finally中与客户端断开
             * 即可
             */
            System.out.println("空请求!");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                //与客户端断开连接
                socket.close();
                System.out.println("关闭连接...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
