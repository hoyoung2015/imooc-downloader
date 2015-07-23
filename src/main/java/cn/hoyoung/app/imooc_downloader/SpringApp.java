package cn.hoyoung.app.imooc_downloader;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.hoyoung.app.imooc_downloader.entity.User;

public class SpringApp {
	private static String[] config = { "src/main/resources/spring.xml" };
	private static ApplicationContext context;
	static {
		context = new FileSystemXmlApplicationContext(config);
	}

	public static void main(String[] args) {
		SessionFactory sessionFactory = (SessionFactory) context
				.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		User user = new User();
		user.setName("hoyoung");
		session.save(user);
		session.getTransaction().commit();
		HibernateUtil.getSessionFactory().close();
	}

}
