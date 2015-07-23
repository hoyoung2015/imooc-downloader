package cn.hoyoung.app.imooc_downloader.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name="video_info")
public class VideoInfo {	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String code;
	@ManyToOne
	@JoinColumn(name="category_id", referencedColumnName="id")
	private Category category;
	@OneToMany(mappedBy = "videoInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<VideoItem> videoItems;
	public List<VideoItem> getVideoItems() {
		return videoItems;
	}
	public void setVideoItems(List<VideoItem> videoItems) {
		this.videoItems = videoItems;
	}
	public int getId() {
		return id;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
