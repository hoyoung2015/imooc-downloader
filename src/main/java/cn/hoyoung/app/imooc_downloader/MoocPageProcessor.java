package cn.hoyoung.app.imooc_downloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import cn.hoyoung.app.imooc_downloader.entity.Category;
import cn.hoyoung.app.imooc_downloader.entity.VideoInfo;
import cn.hoyoung.app.imooc_downloader.entity.VideoItem;

/**
 * 爬取所有视频
 * 
 * @author hoyoung
 *
 */
public class MoocPageProcessor implements PageProcessor {
	private static String[] config = { "src/main/resources/spring.xml" };
	private static ApplicationContext context;
	private static SessionFactory sessionFactory;
	public static Session session;
	static {
		context = new FileSystemXmlApplicationContext(config);
		sessionFactory = (SessionFactory) context.getBean("sessionFactory");
		session = sessionFactory.openSession();
	}
	private Site site = Site.me().setRetryTimes(10).setSleepTime(100);

	public void process(Page page) {
		String url = page.getUrl().toString();
		System.out.println("url=" + url);
		url = url.replace("http://www.imooc.com", "");
		if ("/course/list".equals(url)) {// 类目
			this.categoryPageUrlProcessor(page);
		}
		// 视频
		// 第一遍获取页码
		if (url.contains("/course/list?c=")) {
			if (url.contains("&page=")) {
				// 视频条目
				System.out.println("获取视频条目");
				this.videoInfoPageUrlProcessor(page);
			} else {
				// 获取页码
				Selectable selectable = page.getHtml().xpath(
						"//div[@class='page']");
				if (selectable != null && selectable.toString() != null) {
					List<Selectable> linodes = selectable.xpath("//a").nodes();
					String lastUrl = linodes.get(linodes.size() - 1).links()
							.get();
					int totalPage = Integer.parseInt(lastUrl.substring(lastUrl
							.lastIndexOf("page=") + 5));
					System.out.println(totalPage);
					System.err.println("有分页");
					for (int i = 0; i < totalPage; i++) {
						// System.out.println(url+"&page="+(i+1));
						page.addTargetRequest(url + "&page=" + (i + 1));
					}
				} else {// 不要忽略了只有一页的情况
					page.addTargetRequest(url + "&page=1");
				}
			}
		}
		if (url.contains("learn")) {
			this.videoItemPageUrlProcessor(page);
		}
	}

	public Site getSite() {
		return site;
	}

	public static void update(Object obj) {

		// VideoItem videoItem
		if (obj instanceof VideoItem) {
			VideoItem videoItem = (VideoItem) obj;
			synchronized (session) {
				if (session != null) {
					session.createQuery(
							"update VideoItem vi set vi.fileUrlH='"
									+ videoItem.getFileUrlH()
									+ "',vi.fileUrlM='"
									+ videoItem.getFileUrlM()
									+ "',vi.fileUrlL='"
									+ videoItem.getFileUrlL()
									+ "' where vi.code='" + videoItem.getCode()
									+ "'").executeUpdate();
				}
			}
		}

	}

	public static void save(Object obj) {
		if (obj instanceof VideoInfo || obj instanceof VideoItem
				|| obj instanceof Category) {
			synchronized (session) {
				if (session != null) {
					session.save(obj);
				}
			}
		}
	}

	private static List<String> videoItemUrl = new ArrayList<String>();
	private static String baseUrl = "http://www.imooc.com/course/list";

	public static void main(String[] args) {
		long start = System.currentTimeMillis() / 1000;
		// 课程首页，获取大类
		Spider spider = Spider.create(new MoocPageProcessor());
		// ajax获取下载地址
		Spider fileUrlSpider = Spider.create(new FileUrlProcessor());

		spider.addUrl(baseUrl);
		session.getTransaction().begin();
		spider.thread(1).run();

		if (videoItemUrl != null && videoItemUrl.size() > 0) {
			for (String url : videoItemUrl) {
				fileUrlSpider.addUrl(url);
			}
		}
		 //fileUrlSpider.thread(5).run();//启动ajax爬虫
		if (session != null) {
			session.getTransaction().commit();
			session.close();
		}
		if (sessionFactory != null) {
			sessionFactory.close();
		}
		System.out.println("完成:" + (System.currentTimeMillis() / 1000 - start));
	}

	private void categoryPageUrlProcessor(Page page) {
		List<Selectable> linodes = page.getHtml().xpath("//div[@class='bd']")
				.nodes().get(1).xpath("//li/a").nodes();
		int count = 0;
		for (Selectable selectable : linodes) {
			if (count++ == 0) {
				continue;
			}
			System.out.println(selectable.xpath("//*/@href"));
			String url = selectable.xpath("//*/@href").toString();
			System.out.println(selectable.xpath("//*/text()"));
			String name = selectable.xpath("//*/text()").toString();
			String code = url.substring(url.indexOf("?c=") + 3);
			System.out.println(code);

			Category category = new Category();
			category.setUrl(url);
			category.setCode(code);
			category.setName(name);

			MoocPageProcessor.save(category);
			categoryMap.put(category.getCode(), category);
			page.addTargetRequest(url);
		}
	}

	private Map<String, Category> categoryMap = new HashMap<String, Category>();
	private Map<String, VideoInfo> videoInfoMap = new HashMap<String, VideoInfo>();

	private void videoInfoPageUrlProcessor(Page page) {
		Selectable selectable = page.getHtml().xpath(
				"//div[@class='js-course-lists']/ul/li");
		List<Selectable> linodes = selectable.nodes();
		String url = page.getUrl().toString();
		System.out.println(url.substring(url.indexOf("?c=") + 3,
				url.lastIndexOf("&page=")));
		String categoryCode = url.substring(url.indexOf("?c=") + 3,
				url.lastIndexOf("&page="));
		Category category = categoryMap.get(categoryCode);
		for (Selectable li : linodes) {
			Selectable anode = li.xpath("//a");
			// 检查是否更新完毕
			// if("更新完毕".equals(anode.xpath("//div[@class='tips']/span[@class='l'][1]/text()").toString())){
			String code = li.xpath("//a/@href").toString();
			code = code.substring(code.lastIndexOf("/") + 1);
			System.out.println(code);
			String name = anode.xpath("//h5/span/text()").toString();
			System.out.println(name);

			VideoInfo videoInfo = new VideoInfo();
			videoInfo.setName(name);
			videoInfo.setCode(code);
			if (category != null) {
				videoInfo.setCategory(category);
			}
				MoocPageProcessor.save(videoInfo);
			// }
			videoInfoMap.put(code, videoInfo);
			page.addTargetRequest("http://www.imooc.com/learn/" + code);
		}
	}

	private void videoItemPageUrlProcessor(Page page) {
		String url = page.getUrl().toString();
		url = url.substring(url.lastIndexOf("/") + 1);
		System.out.println("key=" + url);
		Selectable selectable = page.getHtml()
				.xpath("//a[@class='studyvideo']");
		List<Selectable> anodes = selectable.nodes();
		for (Selectable a : anodes) {
			String code = a.links().nodes().get(0).toString();
			code = code.substring(code.lastIndexOf("/") + 1);
			System.out.println("code=" + code);
			String name = a.xpath("//*/text()").toString();
			System.out.println("name=" + name);
			VideoItem videoItem = new VideoItem();
			videoItem.setCode(code);
			videoItem.setName(name);
			videoItem.setVideoInfo(videoInfoMap.get(url));
			MoocPageProcessor.save(videoItem);
			videoItemUrl.add("http://www.imooc.com/course/ajaxmediainfo/?mid="
					+ code + "&mode=flash");
		}
	}
}
