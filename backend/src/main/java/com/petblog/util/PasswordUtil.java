package com.petblog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码工具类
 * 使用 MD5 对密码进行加密处理
 * 注意：生产环境建议使用 BCrypt 等更安全的加密方式
 */
public class PasswordUtil {

    /**
     * 对密码进行 MD5 加密
     * @param password 原始密码
     * @return 加密后的密码字符串
     */
    public static String encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * 验证密码是否正确
     * @param rawPassword 原始密码
     * @param encryptedPassword 加密后的密码
     * @return true-密码正确，false-密码错误
     */
    public static boolean matches(String rawPassword, String encryptedPassword) {
        return encrypt(rawPassword).equals(encryptedPassword);
    }
}
