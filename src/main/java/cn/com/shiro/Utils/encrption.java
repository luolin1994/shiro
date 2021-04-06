package cn.com.shiro.Utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

public class encrption {


    /**
     * md5方法加密
     * @param password
     * @return
     */
    public static String md5(String password){
        return new Md5Hash(password).toString();
    }

    /**
     *加盐 + 多次加密
     * 需要把这个随机数盐也保存进数据库中
     */
    public static String multiAndSalt(String password,String salt,int times){
        //String salt = new SecureRandomNumberGenerator().nextBytes().toString();

        String alogrithmName  = "md5";  //加密算法

        String encodePassword = new SimpleHash(alogrithmName,password,salt,times).toHex();
        return encodePassword;
    }


}
