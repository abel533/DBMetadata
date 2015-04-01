package com.github.abel533.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.Security;

public class EncrypDES {

    private static String defaultKey = "abel533l";
    // SecretKey 负责保存对称密钥
    private static SecretKey deskey;

    static {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        // 生成密钥
        deskey = new SecretKeySpec(defaultKey.getBytes(), "DES");
        // 生成Cipher对象,指定其支持的DES算法
    }

    private static Cipher getCipher() {
        try {
            Cipher c = Cipher.getInstance("DES");
            return c;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String Encrytor(String str) throws Exception {
        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        Cipher c = getCipher();
        c.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] src = str.getBytes();
        // 加密，结果保存进cipherByte
        byte[] cipherByte = c.doFinal(src);
        return EncrypDES.byteArr2HexStr(cipherByte);
    }

    /**
     * 解密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String Decryptor(String str) throws Exception {
        byte[] buff = EncrypDES.hexStr2ByteArr(str);
        // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
        Cipher c = getCipher();
        c.init(Cipher.DECRYPT_MODE, deskey);
        byte[] cipherByte = c.doFinal(buff);
        return new String(cipherByte);
    }


    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * MD5加密
     *
     * @param info
     * @return
     * @throws Exception
     */
    public static String Md5(String info) throws Exception {
        // 根据MD5算法生成MessageDigest对象
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] srcBytes = info.getBytes();
        // 使用srcBytes更新摘要
        md5.update(srcBytes);
        // 完成哈希计算，得到result
        byte[] resultBytes = md5.digest();
        return byteArr2HexStr(resultBytes);
    }
}