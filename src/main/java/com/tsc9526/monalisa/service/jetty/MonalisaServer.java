package com.tsc9526.monalisa.service.jetty;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.service.DBS;
import com.tsc9526.monalisa.service.servlet.DbQueryHttpServlet;

/**
 * 
 * Usage:<br>
 *   &nbsp;&nbsp;&nbsp;&nbsp;	java MonalisaServer &lt;OPTIONS&gt; <br>
   OPTIONS: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;	-port    [option]  9526: listen port <br>		
	&nbsp;&nbsp;&nbsp;&nbsp;	-prefix  [option]  /   : root path <br>		
	&nbsp;&nbsp;&nbsp;&nbsp;	-dbs     [option]  dbs : database service root name <br>
	&nbsp;&nbsp;&nbsp;&nbsp;	-database          database name<br>
	&nbsp;&nbsp;&nbsp;&nbsp;	-url               jdbc:mysql://127.0.0.1:3306/test<br>
	&nbsp;&nbsp;&nbsp;&nbsp;	-username          database username<br>
	&nbsp;&nbsp;&nbsp;&nbsp;	-password          database password<br>
					
 * @author zzg.zhou(11039850@qq.com)
 */
public class MonalisaServer {
	public static void main(String[] args)throws Exception{
		if(args.length<1){
			System.out.println(""+/**~!{*/""
				+ "Usage:"
				+ "\r\n	java MonalisaServer <OPTIONS>"
				+ "\r\nOPTIONS:"
				+ "\r\n	-port    [option]  9526: listen port"
				+ "\r\n	"
				+ "\r\n	-prefix  [option]  /   : root path"
				+ "\r\n	"
				+ "\r\n	-dbs     [option]  dbs : database service root name "
				+ "\r\n	-database          database name"
				+ "\r\n	-url               jdbc:mysql://127.0.0.1:3306/test"
				+ "\r\n	-username          database username"
				+ "\r\n	-password          database password"
			+ "\r\n"/**}*/);
			return;
		}
		
		System.setProperty("file.encoding","UTF-8");
		
		int    port=9526;
		String prefix="/",dbs="dbs",database=null,url=null,username=null, password=null;
		
		for(int i=0;i<args.length;i++){
			if(args[i].equals("-port")){
				port= Integer.parseInt(args[i+1]) ; 
			}else if(args[i].equals("-prefix")){
				prefix=args[i+1];
			}else if(args[i].equals("-dbs")){
				dbs=args[i+1];
			}else if(args[i].equals("-database")){
				database=args[i+1];
			}else if(args[i].equals("-url")){
				url=args[i+1];
			}else if(args[i].equals("-username")){
				username=args[i+1];
			}else if(args[i].equals("-password")){
				password=args[i+1];
			}else{
				System.out.print("Invalid args: "+args[i]);
				return;
			}
			
			i++;
		} 
		
		if(!prefix.startsWith("/")){
			prefix="/"+prefix;
		}
		
		if(prefix.length()>1 && prefix.endsWith("/")){
			prefix=prefix.substring(0,prefix.length()-1);
		}
	 
		System.out.println("Starting monalisa server at port: "+port+", path: "+prefix+", dbs: "+dbs+", database-url: "+url);
		
		
		WebAppContext webapp = new WebAppContext("",prefix);  
		webapp.setResourceBase("monalisa/web");  
		webapp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
	 
		webapp.addServlet(DbQueryHttpServlet.class,"/"+dbs+"/*");
 		 
		DBS.add(database,DBConfig.fromJdbcUrl(url, username, password));
		 
		Server  server = new Server(port);  
	    server.setHandler(webapp);  
	    
	    try{
	    	server.start();  
	    }catch(Exception e){
	    	e.printStackTrace();
	    	server.stop();
	    }
	}
}

