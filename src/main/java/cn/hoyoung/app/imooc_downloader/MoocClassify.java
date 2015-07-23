package cn.hoyoung.app.imooc_downloader;

import java.io.File;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.hoyoung.app.imooc_downloader.entity.VideoItem;

public class MoocClassify {
	private static String[] config = { "src/main/resources/spring.xml" };
	private static ApplicationContext context;
	private static SessionFactory sessionFactory;
	private static Session session;
	static {
		context = new FileSystemXmlApplicationContext(config);
		sessionFactory = (SessionFactory) context.getBean("sessionFactory");
		session = sessionFactory.openSession();
	}
	public static void main(String[] args) {
		String targetPath = "D:/mooc_video/";
		File dir = new File("D:/mooc/");
		File[] files = dir.listFiles();
		for (File file : files) {
			String code = file.getName().substring(0,file.getName().indexOf("."));
			System.out.println("code="+code);
			//查询
			List<VideoItem> list = session.createCriteria(VideoItem.class).add(Restrictions.eq("code", code)).list();
			if(list == null || list.isEmpty()){
				System.out.println("不存在");
				continue;
			}else{
				VideoItem item = list.get(0);
				//判断文件夹是否已存在
//				System.out.println(item.getVideoInfo().getName());
				//windows下把冒号替换掉
				File videoDir = new File(targetPath+item.getVideoInfo().getName().replace(" ", "").replace(":", "'"));
				if(!videoDir.exists()){
					videoDir.mkdir();
					videoDir.setWritable(true);
					videoDir.setReadable(true);
					System.out.println("---"+videoDir.canWrite());
				}
				String ext = file.getName().substring(file.getName().lastIndexOf(".")+1);
				System.out.println(ext);
//				System.out.println(file.toString());
				File newFile = new File(videoDir,item.getName().replace(" ", "").replace(":", "'")+"."+ext);
				System.out.println("newFile:"+newFile.toString());
				boolean rs = file.renameTo(newFile);System.out.println(rs);
				
			}
		}
		System.out.println("结束！");
		/*
		List<VideoItem> list = session.createCriteria(VideoItem.class)
				.add(Restrictions.eq("code", "679"))
				.list();
		for (VideoItem videoItem : list) {
			System.out.println(videoItem.getName());
			System.out.println(videoItem.getVideoInfo().getName());
		}*/
	}

}
