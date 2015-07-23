package cn.hoyoung.app.imooc_downloader;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     */
    public void testApp() throws IOException
    {
    	System.out.println("dsf");
    	File userRootDir =
    			                        new File(System.getProperty("java.util.prefs.userRoot",
    			                        System.getProperty("user.home")), ".java/.userPrefs");
    	
    	System.out.println(userRootDir);
    	System.out.println(userRootDir.exists());
    	
    	String systemPrefsDirName = (String)System.getProperty("java.util.prefs.systemRoot","/etc/.java");
    	File systemRootDir =
    			                      new File(systemPrefsDirName, ".systemPrefs");
    	System.out.println(systemRootDir);
    	System.out.println(systemRootDir.exists());
    	if(!systemRootDir.exists()){
    		systemRootDir.mkdir();
    	}
        assertTrue( true );
    }
}
