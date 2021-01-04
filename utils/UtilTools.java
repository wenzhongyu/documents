package com.qiandai.fourfactors.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilTools {

    final static Logger log = LoggerFactory.getLogger(UtilTools.class);

    /**
     * InputStream转为字符串
     *
     * @param in
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count = -1;
        while ((count = in.read(data, 0, 4096)) != -1)
            outStream.write(data, 0, count);
        return new String(outStream.toByteArray(), "UTF-8");
    }

    /**
     * <ROWS><ROW no="1"><INPUT><gmsfhm>210xxx</gmsfhm><xm>姓名xxx</xm></INPUT><OUTPUT><ITEM><gmsfhm></gmsfhm><result_gmsfhm>一致</result_gmsfhm></ITEM><ITEM><xm></xm><result_xm>一致</result_xm></ITEM><ITEM><zz>辽宁省本xxxx街</zz><result_zz></result_zz></ITEM><ITEM><xp>身份证图片</xp></ITEM></OUTPUT></ROW></ROWS>
     * xml字符串转换为Map
     *
     * @param xml
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    public static Map<String, Object> strXmlTOMap(String xml) throws JDOMException, IOException {
        StringReader reader = new StringReader(xml);
        InputSource source = new InputSource(reader);
        SAXBuilder sax = new SAXBuilder();
        Map<String, Object> resultMap;

        Document doc = sax.build(source);
        Element root = doc.getRootElement();
        List<?> node = root.getChildren();
        resultMap = forXmlToObject(node, root);
        return resultMap;
    }

    /**
     * 无限循环解析xml 为Map
     *
     * @param node
     * @param el
     * @return {zz=辽宁省xxxx石子街, xm=姓名xx, gmsfhm=21050219820xxx, xp=身份证图片, result_gmsfhm=一致, result_xm=一致}
     */
    public static Map<String, Object> forXmlToObject(List<?> node, Element el) {

        Map<String, Object> wsMap = new HashMap<>();

        Element vaEl = null;
        for (int i = 0; i < node.size(); i++) {
            vaEl = (Element) node.get(i);
            List<?> nodes = vaEl.getChildren();
            if (vaEl.getChildren().size() > 0) {
                wsMap.putAll(forXmlToObject(nodes, vaEl));
            } else {
                if (vaEl.getValue() != null && !(vaEl.getValue().equals("")) && !vaEl.getValue().isEmpty()) {
                    wsMap.put(vaEl.getName(), vaEl.getValue());
                }
            }
        }
        return wsMap;
    }

    /**
     * 获取外网接口 为了测试用的
     *
     * @param IName
     * @return
     */
    public static Map<String, Object> getExternalInterface(String IName) {
        Map<String, Object> baseUrlObj = new HashMap<String, Object>();
        baseUrlObj.put("BaseUrl", "");//访问地址
        baseUrlObj.put("SoftVersion", "");//版本信息
        baseUrlObj.put("accountSid", "");//开发者账号ID
        baseUrlObj.put("token", "");//开发者账号TOKEN
        baseUrlObj.put("appId", "");//

        switch (IName) {
            case "ucpaas": {
                baseUrlObj.put("BaseUrl", "https://api.ucpaas.com/");
                baseUrlObj.put("SoftVersion", "2014-06-30");
                baseUrlObj.put("accountSid", "bf81952f7836cea0f09c879c3c15ab91");
                baseUrlObj.put("token", "ea383b9121daf2a8a522757661f29266");
                baseUrlObj.put("appId", "7dee6ec4e6ab46ed96284a18b41e4c63");
                break;
            }
            case "phoneOwnership": {
                baseUrlObj.put("BaseUrl", "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=AAAAAAAAAAA");
            }
            case "phoneOwnership2": {
                baseUrlObj.put("BaseUrl", "http://www.apifree.net/mobile/AAAAAAAAAAA.xml");
            }
        }
        return baseUrlObj;
    }

    /**
     * MD5加密
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String MD5(String str) throws NoSuchAlgorithmException {
        byte[] buf = str.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buf);
        byte[] tmp = md5.digest();
        StringBuilder sb = new StringBuilder();

        for (byte b : tmp) {
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }

    public static String SHA1(String decript) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(decript.getBytes());
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        // 字节数组转换为 十六进制 数
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }

    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    /**
     * 恒信通——使用AES加密
     *
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String HXT_AesEncrypt(String data, String key, String iv) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes("UTF-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return Base64.encodeBase64String(encrypted);

        } catch (Exception e) {
            log.error(""+e);
            throw new Exception(e);
        }
    }

    /**
     * 使用AES解密
     *
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String HXT_DesEncrypt(String data, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = Base64.decodeBase64(data);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString;
        } catch (Exception e) {
            log.error("" + e);
            throw new Exception(e);
        }
    }

    /**
     * AES加密
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String encryptAES(byte[] sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc);
        return Base64.encodeBase64String(encrypted);
    }
}