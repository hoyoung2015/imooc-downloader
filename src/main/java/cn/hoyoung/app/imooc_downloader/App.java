package cn.hoyoung.app.imooc_downloader;

import org.hibernate.Session;

import cn.hoyoung.app.imooc_downloader.entity.User;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        User user = new User();
        user.setName("hoyoung");
        session.save(user);
        session.getTransaction().commit();
        HibernateUtil.getSessionFactory().close();
    }
}
