/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package test.com.tsc9526.monalisa.service.actions;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
 

import test.com.tsc9526.monalisa.service.MysqlDB;

import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.service.DBS;
import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.service.servlet.DbQueryHttpServlet;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class AbstractActionTest {
	@BeforeClass
	public void setup(){
		//MysqlDB.DB.getCfg().setProperty(DbProp.PROP_DB_SQL_DEBUG.getFullKey(), "true");
		System.out.println("Reinit table: test_record_v2");
		DBS.remove("db1");
		DBS.add("db1",MysqlDB.DB);
		
		//clear data
		TestRecordV2().DELETE().truncate();  
		
		for(int i=1;i<=10;i++){
			TestRecordV2().parse("{recordId: "+i+", name: 'ns0"+i+"', title: 'title"+i+"'}").save();
		}
	}
	
	protected Record TestRecordV2(){
		return MysqlDB.DB.createRecord("test_record_v2");
	}
	
	protected Record selectByPrimaryKey(int id){
		return TestRecordV2().WHERE().field("record_id").eq(id).SELECT().selectOne();
	}
	
	protected abstract String getRequestMethod();
	
	protected MockHttpServletRequest createRequest(String requestURI){
		return createRequest(getRequestMethod(),requestURI);
	} 
	
	protected MockHttpServletRequest createRequest(String method,String requestURI){
		MockHttpServletRequest       req=new MockHttpServletRequest(method,requestURI);
			
		return req;
	}
	
	protected Response getRespone(MockHttpServletRequest req)throws Exception{
		MockHttpServletResponse resp=new MockHttpServletResponse(); 
		req.addHeader("DEV_TEST","true");
		
		DbQueryHttpServlet ms=new DbQueryHttpServlet();
		ms.service(req, resp);
		 
		String type=resp.getContentType();
		Assert.assertTrue(type.indexOf("json")>0 && type.indexOf("utf-8")>0);
		
		String body=resp.getContentAsString();
		 
		Response r= Response.fromJson(body); 
		
		if(req.getParameter("page")!=null && r.getStatus()==200){
			int total=Integer.parseInt(resp.getHeader("X-Total-Page"));
			Assert.assertTrue(total>=0);
		}
		
		
		return r;
	}
}
