package com.fish.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @descript:
 * @author: fjjDragon
 * @create: 2021-07-05 15:07
 **/
public class RSAUtil {

    //公钥:
    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCksxYa6LhgpibOA5yIlAUGQ3PgpWW/Vte6+nogud7WivoWLAVXx9ekGuK7c66HX4q5amHWN1NKr4jsK4BqTStntxQiIv0T/C/fHapd0fzTbIuP7ia5k3NVaOU1XFkrK9BmNinb3RFTbbwzZIwvP3+k8ChJHisyXcoVHSU8ZGgn/QIDAQAB";
    //    public static String publicKey;
    //私钥:
    public static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKSzFhrouGCmJs4DnIiUBQZDc+ClZb9W17r6eiC53taK+hYsBVfH16Qa4rtzrodfirlqYdY3U0qviOwrgGpNK2e3FCIi/RP8L98dql3R/NNsi4/uJrmTc1Vo5TVcWSsr0GY2KdvdEVNtvDNkjC8/f6TwKEkeKzJdyhUdJTxkaCf9AgMBAAECgYADbtApI2ykZxIr/RtIJX2tUb6akhf60v6QISRmhjlE04S+7Y/28m1mOEE9kv2ySKWFk9gSNPkBd4MtGQMGNDGRYTJjWB6XccrqGimgm3dNF7d5Y4hGZsI3e+rKar7yCl+SLxkyuuAQ+admnZ6KDaaAuFzQNyrUe02XPa67wI8vaQJBAO3qAiQiW9WFD8A/EV7GXF9NTflp+eMzoQ3D1NXeAvpitY1XpjRyqS/+kJyw94r9RU75NamnSZB1OnoYylQzNocCQQCxOERB9Wm+BqjuhiQjxMpx9nXd77x/CC+lSutLYSWycDWYEfXtD+NECswxtowA3iJfTbPhZB2gVwBBOeeDeYpbAkEAgVHMfiWq80HTkPT3FSQPO8JCWwsuyD/dECVdQC7gQYd8FesJrcKRkkP2CSXsOEF49fdiIuiKhRojghCNkf0QawJBAJ7/Kk47DinM89c8RkRJFJB2XH1Hz0fxYj3cH4o7mISlI5BR3OBZdr7fKSAY9Tb6FMCIiDBZkDMAm1vpxYoxiy0CQDKSNGgif20+CoOmx/09CD/AeTTPV4XgqRh5ISa2nqAEyXAdtiLsg+VJPTY1kPzq/KuE/cwf1GmTljDvDesmF3c=";
//    public static String privateKey;

    /**
     * 生成公钥和私钥
     */
    public static void generateKey() {
        // 1.初始化秘钥
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom sr = new SecureRandom(); // 随机数生成器
            keyPairGenerator.initialize(1024, sr); // 设置4096位长的秘钥
            KeyPair keyPair = keyPairGenerator.generateKeyPair(); // 开始创建
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 进行转码
//            publicKey = Base64.encodeBase64String(rsaPublicKey.getEncoded());
            publicKey = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());

            // 进行转码
//            privateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
            privateKey = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String str) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return encrypt(str, publicKey);
    }

    public static String encrypt(String str, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //base64编码的公钥
        byte[] decoded = Base64.getDecoder().decode(publicKey);

        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes()));

        return outStr;
    }

    public static String decrypt(String str) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return decrypt(str, privateKey);
    }

    public static String decrypt(String str, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str.getBytes());
        //base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static void main(String[] args) throws Exception {
//        // 1. 生成(公钥和私钥)密钥对
        RSAUtil.generateKey();
        System.out.println("公钥:" + RSAUtil.publicKey);
        System.out.println("私钥:" + RSAUtil.privateKey);

//===================================================================================

//        System.out.println("----------公钥加密私钥解密(推荐)，非对称加密，公钥保存在客户端，私钥保存在服务端-------------");
        String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLgJDYvPYWrUfSDeTas8fKb8dBmKt6VNPBMz0KXVTlEMX+3XTv6lHarqspBp/WzX0gfDveKxJ3WnGuJt8jO0ivfOCodSxXc9tvZSC6fhAneAbUkFWiBOa1bMuit6zS7WiQs5draAW6vIRYWBCSZm7jV+HW9BpPYxiI1lpCD8Qd+wIDAQAB";
        // 使用 公钥加密,私钥解密
//        String textsr = "{\n" +
//                "    \"base\":{\n" +
//                "        \"uid\":12,\n" +
//                "        \"name\":\"李四2\"\n" +
//                "    },\n" +
//                "    \"sex\":\"男\"\n" +
//                "}";
        String textsr = "{\n" +
                "    \"openid\":\"test1\",\n" +
                "    \"pwd\":\"\",\n" +
                "    \"nick\":\"\",\n" +
                "    \"avatar\":\"\"\n" +
                "}";
//        String simi = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIuAkNi89hatR9IN5Nqzx8pvx0GY\r\n" +
//                "q3pU08EzPQpdVOUQxf7ddO/qUdquqykGn9bNfSB8O94rEndaca4m3yM7SK984Kh1LFdz229lILp+\r\n" +
//                "ECd4BtSQVaIE5rVsy6K3rNLtaJCzl2toBbq8hFhYEJJmbuNX4db0Gk9jGIjWWkIPxB37AgMBAAEC\r\n" +
//                "gYA7QYtFZU6q50QAp8I0W/VbugtLg8wjFHE2OcuN4YM0IeHYlr0bQ88tMtClhpjSp0qlR+AuSOF5\r\n" +
//                "LUPok75our/umKUsanT46Sepa/Wgh7vhh3oV5fqYA0J9lfdfqInJMCggavntcmHAEDjXbMJ8oMJS\r\n" +
//                "O2v9Tsc4nliLXaW0c/ulkQJBANuEQdzEFAYQPaxri1KZhDICWdLCqnPdda7roK7cYKO1H5akaVLn\r\n" +
//                "aS/DGMq00zpQiV8d9jWXqiYC2DKsUzYEuCUCQQCir/JJdJw+SBcAUSnS0/dZjbDB8mdkW2YafSwP\r\n" +
//                "rftrjHtnDEZoCZrccIv+V48aLXE4xIiHStS2cZRn278zDxOfAkBcryf75ZIqyTqxDStjMcxeLR3t\r\n" +
//                "3iGIEE57D6PlkMkA9h2jRkHuLiT8dJyIIfc5nP2TepwVHrdJm1PbeWUc3rs9AkBoAkrJUYcOfnJO\r\n" +
//                "HpW5lxB9LYwDAuG9FH37k2kXASlLQf4nDH76xqE+K6okyn/PwZuwKl4K+PlzRrwVhjUABLSLAkEA\r\n" +
//                "i87GLH9FV4Af4TMVt7UykSpmtU4BweqY0bQsRZdWnpdZtbmmMmUTB0+Eb1q5okVSgTktCtXECLda\r\n" +
//                "uy2IlrDGuw==";
//        String encryptByPublic = RSAUtil.encryptByPublicKey(textsr, RSAUtil.publicKey, Cipher.ENCRYPT_MODE);
//        System.out.println("公钥加密:" + encryptByPublic);
//        String jiemi = RSAUtil.encryptByprivateKey(encryptByPublic, RSAUtil.privateKey, Cipher.DECRYPT_MODE);
//        System.out.println("私钥解密:" + jiemi);
        System.out.println();
        System.out.println();


        String encrypt = RSAUtil.encrypt(textsr, RSAUtil.publicKey);
        System.out.println(encrypt);
        String decrypt = RSAUtil.decrypt(encrypt, RSAUtil.privateKey);
        System.out.println(decrypt);

    }


}