package cn.hoyoung.app.imooc_downloader.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="video_item")
public class VideoItem {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String code;
	@Column(name="file_url_h")
	private String fileUrlH;
	@Column(name="file_url_m")
	private String fileUrlM;
	@Column(name="file_url_l")
	private String fileUrlL;
	@ManyToOne
	@JoinColumn(name="video_info_id", referencedColumnName="id")
	private VideoInfo videoInfo;
	public VideoInfo getVideoInfo() {
		return videoInfo;
	}
	
	public String getFileUrlH() {
		return fileUrlH;
	}

	public void setFileUrlH(String fileUrlH) {
		this.fileUrlH = fileUrlH;
	}

	public String getFileUrlM() {
		return fileUrlM;
	}

	public void setFileUrlM(String fileUrlM) {
		this.fileUrlM = fileUrlM;
	}

	public String getFileUrlL() {
		return fileUrlL;
	}

	public void setFileUrlL(String fileUrlL) {
		this.fileUrlL = fileUrlL;
	}

	public void setVideoInfo(VideoInfo videoInfo) {
		this.videoInfo = videoInfo;
	}
	public int getId() {
		return id;
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
