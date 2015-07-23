package cn.hoyoung.app.imooc_downloader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import cn.hoyoung.app.imooc_downloader.entity.VideoInfo;
import cn.hoyoung.app.imooc_downloader.entity.VideoItem;
/**
 * 爬取所有条目
 * @author hoyoung
 */
public class MoocVideoItemPageProcessor  implements PageProcessor{
	private static String[] config = { "src/main/resources/spring.xml" };
	private static ApplicationContext context;
	private static SessionFactory sessionFactory;
	private static Session session;
	static {
		context = new FileSystemXmlApplicationContext(config);
		sessionFactory = (SessionFactory) context.getBean("sessionFactory");
		session = sessionFactory.openSession();
	}
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	public void process(Page page) {
		String url = page.getUrl().toString();
		url = url.substring(url.lastIndexOf("/")+1);
		System.out.println("key="+url);
		Selectable selectable = page.getHtml().xpath("//a[@class='studyvideo']");
		List<Selectable> anodes = selectable.nodes();
		for (Selectable a : anodes) {
			String code = a.links().nodes().get(0).toString();
			code = code.substring(code.lastIndexOf("/")+1);
			System.out.println("code="+code);
			String name = a.xpath("//*/text()").toString();
			System.out.println("name="+name);
			VideoItem videoItem = new VideoItem();
			videoItem.setCode(code);
			videoItem.setName(name);
			videoItem.setVideoInfo(videoMaps.get(url));
			session.save(videoItem);
			System.out.println("********************************************************");
		}
	}
	public Site getSite() {
		return site;
	}
	private static Map<String,VideoInfo> videoMaps;
	public static void main(String[] args) {
		//查询视频
		String baseUrl = "http://www.imooc.com/learn/";
		Spider spider = Spider.create(new MoocVideoItemPageProcessor());
		List<VideoInfo> videoInfos = session.createCriteria(VideoInfo.class).list();
		videoMaps = new HashMap<String, VideoInfo>();
		for (VideoInfo videoInfo : videoInfos) {
			videoMaps.put(videoInfo.getCode(), videoInfo);
			spider.addUrl(baseUrl+videoInfo.getCode());
//			break;
		}
		session.getTransaction().begin();
		spider.thread(1).run();
		if(session != null){
			session.getTransaction().commit();
			session.close();
		}
		if(sessionFactory != null){
			sessionFactory.close();
		}
		System.out.println("完成!");
	}
}
