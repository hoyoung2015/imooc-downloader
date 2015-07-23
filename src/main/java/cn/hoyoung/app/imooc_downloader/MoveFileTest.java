package cn.hoyoung.app.imooc_downloader;

import java.io.File;

public class MoveFileTest {

	public static void main(String[] args) {
		try {  
            File afile = new File("/home/hoyoung/tmp/1441.mp4");  
            if (afile.renameTo(new File("/home/hoyoung/tmp/video/新文件.mp4"))) {  
                System.out.println("File is moved successful!");  
            } else {  
                System.out.println("File is failed to move!");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  


	}

}
