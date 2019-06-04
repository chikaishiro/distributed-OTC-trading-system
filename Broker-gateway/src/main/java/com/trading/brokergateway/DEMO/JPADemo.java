package com.trading.brokergateway.DEMO;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.ServerException;

class JPADemo {
    public static void main(String[] args) throws Exception{
        sendMessage();
    }
    public static void sendMessage() throws Exception {
        System.out.println("调用servlet开始=================");
        StringBuffer sendStr = new StringBuffer();
        sendStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sendStr.append("<report_data>");
        sendStr.append("<request_req>953943547334</request_req>");
        sendStr.append("<request_time>2012040211394324</request_time>");
        sendStr.append("<request_param>");
        sendStr.append("<query_month>201203</query_month>");
        sendStr.append("</request_param>");
        sendStr.append("</report_data>");

        BufferedReader reader = null;

        try {
            String strMessage = "";
            StringBuffer buffer = new StringBuffer();

            // 接报文的地址
            URL uploadServlet = new URL(
                    "http://localhost:8080/Order");

            HttpURLConnection servletConnection = (HttpURLConnection) uploadServlet
                    .openConnection();
            // 设置连接参数
            servletConnection.setRequestMethod("POST");
            servletConnection.setDoOutput(true);
            servletConnection.setDoInput(true);
            servletConnection.setAllowUserInteraction(true);

            // 开启流，写入XML数据
            OutputStream output = servletConnection.getOutputStream();
            System.out.println("发送的报文：");
            System.out.println(sendStr.toString());

            output.write(sendStr.toString().getBytes());
            output.flush();
            output.close();

            // 获取返回的数据
            InputStream inputStream = servletConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((strMessage = reader.readLine()) != null) {
                buffer.append(strMessage);
            }

            System.out.println("接收返回值:" + buffer);

        } catch (java.net.ConnectException e) {
            throw new Exception();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public class TestTransfers extends HttpServlet {

        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException, IOException {
        }

        protected void doPost(HttpServletRequest request,
                              HttpServletResponse response) throws ServletException, IOException {

            //判断请求报文是否来自代维系统的ip地址
            String ip = request.getRemoteHost();

// 获取收到的报文
            BufferedReader reader = request.getReader();
            String line = "";
            StringBuffer inputString = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }

            //如有必要，可以在报文中增加其他验证和加密的参数
            //解析获取到的报文，根据ip地址、其他验证、加密等等来判断请求报文的服务器是否有权限

            //如果请求验证合格，则根据请求的参数装配返回的报文

            // 要返回的报文
            StringBuffer resultBuffer = new StringBuffer();
            resultBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            resultBuffer.append("<report_data>");
            resultBuffer.append("<respon_req>953947334</respon_req>");
            resultBuffer.append("<respon_time>20120402113943</respon_time>");
            resultBuffer.append("<result>");
            resultBuffer.append("<id>0000</id>");
            resultBuffer.append("<comment>成功</comment>");
            resultBuffer.append("</result>");
            resultBuffer.append("<items>");
            resultBuffer.append("<item>");
            resultBuffer.append("<county>长治县</county>");
            resultBuffer.append("<company>铁通</company>");
            resultBuffer.append("<speciality>线路</speciality>");
            resultBuffer.append("<personnel>王加和</personnel>");
            resultBuffer.append("<begin_time>20120301000000</begin_time>");
            resultBuffer.append("<end_time>20120331235959</end_time>");
            resultBuffer.append("<plan_quantity>50</plan_quantity>");
            resultBuffer.append("<checkout_quantity>40</checkout_quantity>");
            resultBuffer.append("<patrol_rate>0.80</patrol_rate>");
            resultBuffer.append("</item>");
            //......
            //......
            //......
            //循环组装响应的报文

            resultBuffer.append("</items>");
            resultBuffer.append("</report_data>");

            // 设置发送报文的格式
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.println(resultBuffer.toString());
            out.flush();
            out.close();
        }
    }
}