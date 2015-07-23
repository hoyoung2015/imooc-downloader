package cn.hoyoung.app.imooc_downloader;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class GenerateDb {
	public static void main( String[] args )
    {
		Configuration conf = new Configuration().configure();
		SchemaExport schemaExport = new SchemaExport(conf);
		schemaExport.drop(true, true);
		schemaExport.create(true, true);		
    }
}
