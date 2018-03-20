package test.com.tsc9526.monalisa.service.jetty;

import test.com.tsc9526.monalisa.service.TestConstants;

import com.tsc9526.monalisa.service.jetty.MonalisaServer;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MonalisaServerTest {

	public static void main(String[] args)throws Exception {
		args=new String[]{
			"-database" , "test"
			,"-url"     , TestConstants.url
			,"-username", TestConstants.username
			,"-password", TestConstants.password
		};
		MonalisaServer.main(args);
	}

}
