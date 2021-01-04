package com.qiandai.fourfactors.common.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class HttpUtil {

    private static SslContextUtils sslContextUtils = new SslContextUtils();

    /**
     * 发送POST请求报文XML
     * @param postUrl
     * @param xmlData
     * @return
     * @throws IOException
     */
    public static String sendXMLDataByPost(String postUrl, String xmlData) throws IOException {
        BufferedReader reader = null;
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(postUrl);
            httpConn = (HttpURLConnection) url.openConnection();

            if (httpConn instanceof HttpsURLConnection) {
                sslContextUtils.initHttpsConnect((HttpsURLConnection) httpConn);
            }

            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-type", "text/xml");
            httpConn.setConnectTimeout(10000);
            httpConn.setReadTimeout(10000);
            // 发送请求
            DataOutputStream out = new DataOutputStream(httpConn.getOutputStream()); // utf-8编码
            out.writeBytes(xmlData);
            out.flush();
            out.close();

            // 获取输入流
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
            String lines;
            StringBuffer strBuf = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                strBuf.append(lines);
            }
            return strBuf.toString();
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }finally {
            if(reader != null){
                reader.close();
            }
            httpConn.disconnect();
        }
    }

    /**
     * post请求方式发送json数据
     *
     * @param postUrl
     * @param jsonData
     * @return
     * @throws Exception
     */
    public static String sendJsonDataByPost(String postUrl, String jsonData) throws Exception {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(postUrl);// 创建连接
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream()); // utf-8编码
            out.writeBytes(jsonData);
            out.flush();
            out.close();

            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String lines;
            StringBuffer strBuf = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                strBuf.append(lines);
            }
            return strBuf.toString();
        } catch (IOException e) {
            throw new Exception(e);
        }finally {
            if(reader != null){
                reader.close();
            }
            // 断开连接
            connection.disconnect();
        }
    }

    /**
     * 这个可能是比较标准的发送请求并获取响应的代码
     *
     * @param urlString 请求地址
     * @param params    参数 数据键值对 格式为id=123&name=jack... 也可以是流的方式发送请求 比如param为xml或json
     * @param charset   应该和响应时的编码响应
     * @return 请求返回的响应
     * @throws IOException
     */
    public static String post(String urlString, String params, String charset)
            throws IOException {
        /**
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using
         * java.net.URL and //java.net.URLConnection
         */
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        /**
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        /**
         * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...
         */
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), charset);
        out.write(params); // post的关键所在！
        // remember to clean up
        out.flush();
        out.close();
        /**
         * 这样就可以发送一个看起来象这样的POST： POST /jobsearch/jobsearch.cgi HTTP 1.0 ACCEPT:
         * text/plain Content-type: application/x-www-form-urlencoded
         * Content-length: 99 username=bob password=someword
         */
        // 一旦发送成功，用以下方法就可以得到服务器的回应：
        String currentLine;
        StringBuffer htmlSource = new StringBuffer();
        InputStream urlStream = connection.getInputStream();
        // 传说中的三层包装阿！
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(urlStream, charset));
            while ((currentLine = reader.readLine()) != null) {
                htmlSource.append(currentLine);
                // htmlSource.append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return htmlSource.toString();
    }

    /**
     * 以get方式提交。注意，数据可能超长，请注意GET方式的允许的数据长度
     *
     * @param urlString url
     * @return 获得的源码
     * @throws IOException IOException
     */
    public static String get(String urlString, String charset)
            throws IOException {
        /**
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using
         * java.net.URL and //java.net.URLConnection
         */
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        /**
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);

        // 一旦发送成功，用以下方法就可以得到服务器的回应：
        String currentLine;
        StringBuffer htmlSource = new StringBuffer();
        InputStream urlStream;
        urlStream = connection.getInputStream();
        // 传说中的三层包装阿！
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(urlStream,
                    charset));
            while ((currentLine = reader.readLine()) != null) {
                htmlSource.append(currentLine);
                htmlSource.append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        urlStream.close();
        return htmlSource.toString();
    }
}
