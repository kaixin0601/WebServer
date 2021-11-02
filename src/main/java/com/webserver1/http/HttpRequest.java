package com.webserver1.http;

import com.webserver1.core.EmptyRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
/**
 * 请求对象
 * 每个实例表示客户端发送过来的一个具体请求
 * @author ta
 *
 */
public class HttpRequest {
    //客户端连接相关信息
    private Socket socket;
    private InputStream in;

    /**
     * 请求行相关信息定义
     */
    //请求方式
    private String method;
    //资源路径
    private String url;
    //协议版本
    private String protocol;

    //url中的请求部分
    private String requestURI;
    //url中的参数部分
    private String queryString;
    //每个参数
    private Map<String,String> parameters = new HashMap<String,String>();

    /**
     * 消息头相关信息定义
     */
    private Map<String,String> headers=new HashMap<String,String>();

    /**
     * 消息正文相关信息定义
     */

    /**
     * 初始化请求
     * @throws EmptyRequestException
     */
    public HttpRequest(Socket socket)throws EmptyRequestException{
        try {
            this.socket=socket;
            this.in=socket.getInputStream();
            /**
             * 解析请求
             * 1:解析请求行
             * 2:解析消息头
             * 3:解析消息正文
             */
            //消息请求行
            parseRequestLine();
            //消息头
            parseHeader();
            //消息正文
            parseContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析请求行
     * @throws EmptyRequestException
     */
    public void parseRequestLine() throws EmptyRequestException{
        try {
            String line=readLine();
            System.out.println("line:"+line);
            /**
             * 将请求行进行拆分，将每部分内容
             * 对应的设置到属性上。
             * 正则表达式：
             *
             * 字符	描述
             * 1、非打印字符
             * 非打印字符也可以是正则表达式的组成部分。下表列出了表示非打印字符的转义序列：
             *\cx 匹配由x指明的控制字符。例如， \cM 匹配一个 Control-M 或回车符。x 的值必须为 A-Z 或 a-z 之一。否则，将 c 视为一个原义的 'c' 字符。
             *\f	 匹配一个换页符。等价于 \x0c 和 \cL。
             *\n	 匹配一个换行符。等价于 \x0a 和 \cJ。
             *\r	 匹配一个回车符。等价于 \x0d 和 \cM。
             *\s	 匹配任何空白字符，包括空格、制表符、换页符等等。等价于 [ \f\n\r\t\v]。注意 Unicode 正则表达式会匹配全角空格符。
             *\S	 匹配任何非空白字符。等价于 [^ \f\n\r\t\v]。
             *\t	 匹配一个制表符。等价于 \x09 和 \cI。
             *\v	 匹配一个垂直制表符。等价于 \x0b 和 \cK。
             *2、特殊字符
             * 所谓特殊字符，就是一些有特殊含义的字符，
             * 如上面说的 runoo*b 中的 *，简单的说就是表示任何字符串的意思。
             * 如果要查找字符串中的 * 符号，则需要对 * 进行转义，即在其前加一个 \，runo\*ob 匹配字符串 runo*ob。
             * 许多元字符要求在试图匹配它们时特别对待。
             * 若要匹配这些特殊字符，必须首先使字符"转义"，即，将反斜杠字符\ 放在它们前面。下表列出了正则表达式中的特殊字符：
             * 特别字符	描述
             * $	匹配输入字符串的结尾位置。如果设置了 RegExp 对象的 Multiline 属性，则 $ 也匹配 '\n' 或 '\r'。要匹配 $ 字符本身，请使用 \$。
             * ( )	标记一个子表达式的开始和结束位置。子表达式可以获取供以后使用。要匹配这些字符，请使用 \( 和 \)。
             * *	匹配前面的子表达式零次或多次。要匹配 * 字符，请使用 \*。
             * +	匹配前面的子表达式一次或多次。要匹配 + 字符，请使用 \+。
             * .	匹配除换行符 \n 之外的任何单字符。要匹配 . ，请使用 \. 。
             * [	标记一个中括号表达式的开始。要匹配 [，请使用 \[。
             * ?	匹配前面的子表达式零次或一次，或指明一个非贪婪限定符。要匹配 ? 字符，请使用 \?。
             * \	将下一个字符标记为或特殊字符、或原义字符、或向后引用、或八进制转义符。例如， 'n' 匹配字符 'n'。'\n' 匹配换行符。序列 '\\' 匹配 "\"，而 '\(' 则匹配 "("。
             * ^	匹配输入字符串的开始位置，除非在方括号表达式中使用，当该符号在方括号表达式中使用时，表示不接受该方括号表达式中的字符集合。要匹配 ^ 字符本身，请使用 \^。
             * {	标记限定符表达式的开始。要匹配 {，请使用 \{。
             * |	指明两项之间的一个选择。要匹配 |，请使用 \|。
             * 3、限定符
             * 限定符用来指定正则表达式的一个给定组件必须要出现多少次才能满足匹配。有 * 或 + 或 ? 或 {n} 或 {n,} 或 {n,m} 共6种。
             * 正则表达式的限定符有：
             * *	匹配前面的子表达式零次或多次。例如，zo* 能匹配 "z" 以及 "zoo"。* 等价于{0,}。
             * +	匹配前面的子表达式一次或多次。例如，'zo+' 能匹配 "zo" 以及 "zoo"，但不能匹配 "z"。+ 等价于 {1,}。
             * ?	匹配前面的子表达式零次或一次。例如，"do(es)?" 可以匹配 "do" 、 "does" 中的 "does" 、 "doxy" 中的 "do" 。? 等价于 {0,1}。
             * {n}	n 是一个非负整数。匹配确定的 n 次。例如，'o{2}' 不能匹配 "Bob" 中的 'o'，但是能匹配 "food" 中的两个 o。
             * {n,}	n 是一个非负整数。至少匹配n 次。例如，'o{2,}' 不能匹配 "Bob" 中的 'o'，但能匹配 "foooood" 中的所有 o。'o{1,}' 等价于 'o+'。'o{0,}' 则等价于 'o*'。
             * {n,m}	m 和 n 均为非负整数，其中n <= m。最少匹配 n 次且最多匹配 m 次。例如，"o{1,3}" 将匹配 "fooooood" 中的前三个 o。'o{0,1}' 等价于 'o?'。请注意在逗号和两个数之间不能有空格。
             */
            String []data=line.split("\\s");
            if(data.length!=3){
                //空请求
                throw new EmptyRequestException();
            }
            method=data[0];
            url=data[1];
            //进一步解析URL
            parseURL();
            protocol=data[2];
            System.out.println("method:"+method);
            System.out.println("url:"+url);
            System.out.println("protocol:"+protocol);
        } catch (IOException | EmptyRequestException e) {
            e.printStackTrace();
        }
    }
    /**
     * 进一步解析URL
     * url有可能会有两种格式:带参数和不带参数
     * 1,不带参数如:
     * /myweb/index.html
     *
     * 2,带参数如:
     * /myweb/reg?username=zhangsan&password=123456&nickname=asan&age=22
     */
    private void parseURL() {
        /**
         * 首先判断当前url是否含有参数,判断的
         * 依据是看url是否含有"?",含有则认为
         * 这个url是包含参数的，否则直接将url
         * 赋值给requestURI即可。
         *
         *
         * 若有参数:
         * 1:将url按照"?"拆分为两部分，第一部分
         *   为请求部分，赋值给requestURI
         *   第二部分为参数部分，赋值给queryString
         *
         * 2:再对queryString进一步拆分，先按照"&"
         *   拆分出每个参数，再将每个参数按照"="
         *   拆分为参数名与参数值，并存入parameters
         *   这个Map中。
         *
         * 解析过程中要注意url的几个特别情况:
         * 1:url可能含有"?"但是没有参数部分
         * 如:
         * /myweb/reg?
         *
         * 2:参数部分有可能只有参数名没有参数值
         * 如:
         * /myweb/reg?username=&password=123&age=16...
         */
        if(url.indexOf("?")!=-1) {
            //按照"?"拆分
            String[] data = url.split("\\?");
            requestURI = data[0];
            //判断?后面是否有参数
            if(data.length>1) {
                queryString = data[1];
                //进一步解析参数部分
                //按照&拆分出每一个参数
                String[] paraArr = queryString.split("&");
                //遍历每个参数进行拆分
                for(String para : paraArr) {
                    //再按照"="拆分每个参数
                    String[] paras = para.split("=");
                    if(paras.length>1) {
                        //该参数有值
                        parameters.put(paras[0], paras[1]);
                    }else {
                        //没有值
                        parameters.put(paras[0], null);
                    }
                }
            }
        }else {
            //不含有?
            requestURI = url;
        }
        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);
    }
    /**
     * 解析消息头
     */
    public void parseHeader() {
        try{
            /**
             * 解析消息头的流程:
             * 循环调用readLine方法，读取每一个消息头
             * 当readLine方法返回值为空字符串时停止
             * 循环(因为返回空字符串说明单独读取了CRLF
             * 而这是作为消息头结束的标志)
             * 在读取到每个消息头后，根据": "(冒号空格)
             * 进行拆分，并将消息头的名字做为key，消息
             * 头对应的值作为value保存到属性headers这个
             * Map中完成解析工作
             */
            while(true){
                String line=readLine();
                String[]data=line.split("\\:");
                if("".equals(line)){
                    break;
                }
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:"+headers);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 解析消息正文
     */
    public void parseContent() {

    }
    /**
     * 读取一行字符串，当连续读取CR,LF时停止
     * 并将之前的内容以一行字符串形式返回。
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException {
        StringBuilder builder=new StringBuilder();
        //本次读取的字节
        int d=-1;
        //c1表示上次读取的字符，c2表示本次读取的字符
        char c1='a';
        char c2='a';
        while((d=in.read())!=-1){
            c2=(char)d;
            /**
             * 在HttpContext中有定义public static final int CR = 13;
             * public static final int LF = 10;
             */
            if(c1==HttpContext.CR&&c2==HttpContext.LF){
                break;
            }
            builder.append(c2);
            c1=c2;
        }
        return builder.toString().trim();
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    /**
     * 根据给定的消息头的名字获取对应消息头的
     * 值
     * @param name
     * @return
     */
    public String getHeaders(String name){
        return headers.get(name);
    }
    public String getRequestURI() {
        return requestURI;
    }
    public String getQueryString() {
        return queryString;
    }
    public String getParameter(String name){
        return parameters.get(name);
    }

}
