package cn.com.shiro.bean;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 对密码使用Hash运算保存
 * 配置realm.credentialsMatcher
 */
public class MyEncrpRealm extends AuthorizingRealm {

    /**
     *模拟数据库数据
     */
    Map<String,String> userMap = new HashMap<String,String>(16);
    public static String salt = "admin";

    //对象的初始化块
    {
        String password = "123456";
        String enpassword = multiAndSalt(password,salt);
        userMap.put("lisi",enpassword);
        super.setName(getName());  // 设置自定义Realm的名称，取什么无所谓
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        String userName = (String) principalCollection.getPrimaryPrincipal();
        //从数据库获取角色和权限数据
        Set<String> roles = getRolesByUserName(userName);
        Set<String> permissions = getPermissionsByUserName(userName);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从主体传过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();

        //通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(userName);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,password, ByteSource.Util.bytes(salt),getName());

        return authenticationInfo;
    }

    private String multiAndSalt(String password,String salt){
        //String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 100;   //加密次数
        String alogrithmName  = "md5";  //加密算法

        String encodePassword = new SimpleHash(alogrithmName,password,salt,times).toString();
        return encodePassword;
    }

    /**
     * 模拟从数据库中获取权限数据
     */
    private Set<String> getPermissionsByUserName(String userName){
        Set<String> permissions = new HashSet<String>();
        permissions.add("user:delete");
        permissions.add("user:add");
        return permissions;
    }


    /**
     * 模拟从数据库中获取角色数据
     */
    private Set<String> getRolesByUserName(String userName){
        Set<String> roles = new HashSet<String>();
        roles.add("admin");
        roles.add("user");
        return roles;
    }

    /**
     * 模拟从数据库中取凭证
     */
    private String getPasswordByUserName(String userName){
        return userMap.get(userName);
    }
}
