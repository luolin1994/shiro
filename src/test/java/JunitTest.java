import org.junit.*;

/**
 * @BeforeClass – 在当前类的所有测试方法之前执行。注解在【静态方法】上。
 * @AfterClass – 在当前类中的所有测试方法之后执行。注解在【静态方法】上。
 * @Before – 表示在任意使用@Test注解标注的public void方法执行之前执行
 * @After – 表示在任意使用@Test注解标注的public void方法执行之后执行
 * @Test – 使用该注解标注的public void方法会表示为一个测试方法
 */
public class JunitTest {

    @BeforeClass
    public static void runOnceBeforeClass(){
        System.out.println("@BeforeClass--runOnceBeforeClass");
    }


    @AfterClass
    public static void runAfterBeforeClass(){
        System.out.println("@AfterClass--runOnceAfterClass");
    }


    @Before
    public void runBeforeTestMethod(){
        System.out.println("@Before--runBeforeTestMethod");
    }

    @After
    public void runAfterTestMethod(){
        System.out.println("@After--runAfterTestMethod");
    }

    @Test
    public void test_method_1(){
        System.out.println("@Test--test_method_1");
    }


    @Test
    public void test_method_2(){
        System.out.println("@Test--test_method_2");
    }






}
