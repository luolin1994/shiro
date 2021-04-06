import cn.com.shiro.Utils.encrption;
import cn.com.shiro.bean.MyEncrpRealm;
import cn.com.shiro.bean.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * 实际进行权限信息验证的是我们的 Realm，Shiro 框架内部默认提供了两种实现，一种是查询.ini文件的IniRealm，另一种是查询数据库的JdbcRealm
 */
public class DefineRealm {


    /**
     * 查询.ini文件的IniRealm
     */
    @Test
    public void testRealmFromIni(){

        //加载配置文件，并获取工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //获取安全管理实例
        SecurityManager securityManager = factory.getInstance();
        //将安全管理者放入全局对象
        SecurityUtils.setSecurityManager(securityManager);
        //全局对象通过安全管理者生成Subject对象
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaoming", "123456");
        subject.login(token); //登陆
        System.out.println("isAuthenticated:" + subject.isAuthenticated());   //输出true
        System.out.println("subject.hasRole(\"admin\"): "+ subject.hasRole("admin")); //判断登陆的用户是否拥有某种角色
        System.out.println("subject.isPermitted(\"addProduct\"): " + subject.isPermitted("addProduct")); //判断登陆的用户是否拥有某种权限

    }

    /**
     * 自定义realm，查询数据库的JdbcRealm
     */
    @Test
    public void testRealmFromMyRealm(){
        MyRealm myRealm = new MyRealm();

        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(myRealm);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);// 设置SecurityManager环境
        Subject subject = SecurityUtils.getSubject();// 获取当前主体

        UsernamePasswordToken token = new UsernamePasswordToken("lisi","123456");
        subject.login(token);

        System.out.println("isAuthenticated: " + subject.isAuthenticated());
        subject.checkRoles("admin","user");
        subject.checkPermission("user:add");

    }

    /**
     * 测试shiro的加密方法
     */
    @Test
    public void testEncry(){
        System.out.println( encrption.md5("123456"));
        System.out.println(encrption.multiAndSalt("123456","admin",1024));
    }

    /**
     * 对密码使用Hash运算保存
     *  配置realm.credentialsMatcher
     * 自定义realm，查询数据库的JdbcRealm
     */
    @Test
    public void testRealmFromMyEncrpRealm(){

        //配置HashedCredentialsMatcher参数
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); //算法类型
        hashedCredentialsMatcher.setHashIterations(100); //加密次数

        MyEncrpRealm myEncrpRealm = new MyEncrpRealm();
        myEncrpRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(myEncrpRealm);


        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);// 设置SecurityManager环境
        Subject subject = SecurityUtils.getSubject();// 获取当前主体

        UsernamePasswordToken token = new UsernamePasswordToken("lisi","123456");
        subject.login(token);

        System.out.println("isAuthenticated: " + subject.isAuthenticated());

    }

}
