package cn.hoyoung.app.imooc_downloader;

import java.util.List;

import cn.hoyoung.app.imooc_downloader.entity.VideoItem;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class FileUrlProcessor implements PageProcessor {
	private Site site = Site.me();
	public Site getSite() {
		return site;
	}
	public void process(Page page) {
		System.out.println(page.getRawText());
		String mid = new JsonPathSelector("$.data.result.mid").select(page.getRawText());
		List<String> medias = new JsonPathSelector("$.data.result.mpath").selectList(page.getRawText());
		System.err.println(medias.size());
		System.err.println(mid);
		
		VideoItem vi = new VideoItem();
		vi.setCode(mid);
		vi.setFileUrlH(medias.get(0));
		vi.setFileUrlM(medias.get(1));
		vi.setFileUrlL(medias.get(2));
		
		MoocPageProcessor.update(vi);
	}
}
