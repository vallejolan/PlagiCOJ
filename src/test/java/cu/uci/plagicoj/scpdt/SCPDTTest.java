package cu.uci.plagicoj.scpdt;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Unit test for simple App.
 */
@Configuration
@ComponentScan(value="cu.uci.plagicoj")
public class SCPDTTest {
	
//	private AnnotationConfigApplicationContext context = null;
//
//	@Bean
//	public MessageService getMessageService() {
//		return new MessageService(){
//
//			public boolean sendMessage(String msg, String rec) {
//				System.out.println("Mock Service");
//				return true;
//			}
//			
//		};
//	}
//
//	@Before
//	public void setUp() throws Exception {
//		context = new AnnotationConfigApplicationContext(SCPDTTest.class);
//	}
//	
//	@After
//	public void tearDown() throws Exception {
//		context.close();
//	}
//
////	@Test
////	public void test() {
////		MyApplication app = context.getBean(MyApplication.class);
////		Assert.assertTrue(app.processMessage("Hi Pankaj", "pankaj@abc.com"));
////	}
}