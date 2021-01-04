package com.qiandai.fourfactors.common.utils;

import java.io.*;
import java.net.Socket;
import java.net.URL;

/**
 * Created by yuwenzhong on 2016-08-23.
 */
public class SocketUtils {
    /**
     * @Description:使用socket发送post请求
     * @author:liuyc
     * @time:2016年5月18日 上午9:26:22
     */
    public static String sendSocketPost(String urlParam, String data, String charset) throws IOException {
        // 构建请求参数
        Socket socket = null;
        OutputStreamWriter osw = null;
        InputStream is = null;
        try {
            URL url = new URL(urlParam);
            String host = url.getHost();
            int port = url.getPort();
            if (-1 == port) {
                port = 80;
            }
            String path = url.getPath();
            socket = new Socket(host, port);
            StringBuffer sb = new StringBuffer();
            sb.append("POST " + path + " HTTP/1.1\r\n");
            sb.append("Host: " + host + "\r\n");
            sb.append("Connection: Keep-Alive\r\n");
            sb.append("Content-Type: application/x-www-form-urlencoded; charset=utf-8 \r\n");
            sb.append("Content-Length: ").append(sb.toString().getBytes().length).append("\r\n");
            // 这里一个回车换行，表示消息头写完，不然服务器会继续等待
            sb.append("\r\n");
            if (StringUtils.isNotEmpty(data)) {
                sb.append(data);
            }
            osw = new OutputStreamWriter(socket.getOutputStream());
            osw.write(sb.toString());
            osw.flush();

            is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
            String line = null;
            StringBuffer strBuf = new StringBuffer();
            while ((line = br.readLine()) != null) {
                if (line.equals("\r\n")) {
                    break;
                }
                // 读取http响应头部信息
                if (line.startsWith("Content-Length")) {
                    // 拿到响应体内容
                    strBuf.append(line.split(":")[1].trim());
                }
            }
            String respContent = strBuf.toString();
            return respContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (osw != null) {
                osw.close();
                if (socket != null) {
                    socket.close();
                }
            }
            if (is != null) {
                is.close();
                if (socket != null) {
                    socket.close();
                }
            }
        }
    }
}
