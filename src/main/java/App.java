import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

		OrderDispatcher orderDispatcher = ctx.getBean(OrderDispatcher.class);
		orderDispatcher.start();
		orderDispatcher.join();

		ctx.close();
	}
}
