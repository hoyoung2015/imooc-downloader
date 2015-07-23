package cn.hoyoung.app.imooc_downloader;

import us.codecraft.webmagic.Spider;

public class TestFileUrlProcessor {

	public static void main(String[] args) {
		long start = System.currentTimeMillis()/1000;
		Spider fileUrlSpider = Spider.create(new FileUrlProcessor());
		fileUrlSpider.addUrl("http://www.imooc.com/course/ajaxmediainfo/?mid=3337&mode=flash");
		fileUrlSpider.addUrl("http://www.imooc.com/course/ajaxmediainfo/?mid=3292&mode=flash");
		fileUrlSpider.addUrl("http://www.imooc.com/course/ajaxmediainfo/?mid=4093&mode=flash");
		fileUrlSpider.addUrl("http://www.imooc.com/course/ajaxmediainfo/?mid=4095&mode=flash");
		fileUrlSpider.thread(5).run();
		long end = System.currentTimeMillis()/1000;
		System.out.println("耗时:"+(end - start));
	}

}
