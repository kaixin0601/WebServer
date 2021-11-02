package com.webserver1.http;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Http协议相关内容定义
 * @author ta
 *
 */
public class HttpContext {
    /**
     * 回车符CR
     */
    public static final int CR = 13;
    /**
     * 换行符LF
     */
    public static final int LF = 10;
    /**
     * 状态代码与对应状态描述
     * key:状态代码
     * value:状态描述
     */
    private static Map<Integer,String> statusCode_Descriprion_Mapping=new HashMap<Integer,String>();
    /**
     *	介质类型映射
     *  key:资源后缀名
     *  value:介质类型(Content-Type对应的值)
     */
    private static Map<String,String> mime_Mapping=new HashMap<String,String>();
    static{
        //初始化静态成员
        initStatus_Mapping();
        initMimeMapping();
    }
    /**
     * 初始化状态代码与对应描述
     */
    private static void initStatus_Mapping() {
        statusCode_Descriprion_Mapping.put(200,"OK");
        statusCode_Descriprion_Mapping.put(201,"Created");
        statusCode_Descriprion_Mapping.put(202,"Accepted");
        statusCode_Descriprion_Mapping.put(204,"No Content");
        statusCode_Descriprion_Mapping.put(301,"Moved Permanently");
        statusCode_Descriprion_Mapping.put(302,"Moved Temporarily");
        statusCode_Descriprion_Mapping.put(304,"Not Modified");
        statusCode_Descriprion_Mapping.put(400,"Bad Request");
        statusCode_Descriprion_Mapping.put(401,"Unauthorized");
        statusCode_Descriprion_Mapping.put(403,"Forbidden");
        statusCode_Descriprion_Mapping.put(404,"Not Found");
        statusCode_Descriprion_Mapping.put(500,"Internal Server Error");
        statusCode_Descriprion_Mapping.put(501,"Not Implemented");
        statusCode_Descriprion_Mapping.put(502,"Bad Gateway");
        statusCode_Descriprion_Mapping.put(503,"Service Unavailable");
    }

    /**
     * 初始化介质类型
     */
    private static void initMimeMapping(){
        /**
         * 修改原有图片格式
         * 丰富图片格式
         */
/*        mime_Mapping.put("html","text/html");
        mime_Mapping.put("png","image/png");
        mime_Mapping.put("gif","image/gif");
        mime_Mapping.put("jpg","image/jpeg");
        mime_Mapping.put("js","application/javascript");
        mime_Mapping.put("css","text/css");*/

        /**
         * 解析conf/web.xml文件，将根标签中所有
         * 名为<mime-mapping>的子标签获取到，并
         * 将该标签中的子标签<extension>中间的文本
         * 作为key，子标签<mime-type>中间的文本作为
         * value保存到mime_mapping这个Map中完成
         * 初始化工作
         */
        try{
            /**
             * 导入dom4j教程
             * 1、在WebServer中新建一个dom4j文件夹
             * 2、将下载好的dom4j-2.13.jar包复制在dom4j文件夹下
             * 3、右键点击dom4j文件夹选择Add as a library
             * 4、出现弹框选择OK即可
             */
            //创建SAXReader对象
            SAXReader reader = new SAXReader();
            //使用SAXReader读取xml文档并生成Document对象
            Document doc = reader.read(
                    new File("conf/web.xml"));
            Element root = doc.getRootElement();
            List<Element> mimeList
                    = root.elements("mime-mapping");
            for(Element mime : mimeList) {
                String ext = mime.elementText("extension");
                String type = mime.elementText("mime-type");
                mime_Mapping.put(ext, type);
            }
            System.out.println(mime_Mapping.size());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 根据状态代码获取对应的状态描述
     * @param code
     * @return
     */
    public static String getStatusDescription(int code){
        return statusCode_Descriprion_Mapping.get(code);
    }
    /**
     * 根据资源后缀获取对应的介质类型
     * @param ext
     * @return
     */
    public static String getMimeTyep(String ext){
        return mime_Mapping.get(ext);
    }
    //测试
    public static void main(String[]args){
        String da=HttpContext.getStatusDescription(500);
        System.out.println(da);
    }
}
