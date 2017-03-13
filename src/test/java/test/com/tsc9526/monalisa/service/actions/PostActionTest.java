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
import org.testng.Assert;
import org.testng.annotations.Test;
 

import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class PostActionTest extends AbstractActionTest {
	protected String getRequestMethod(){
		return "POST";
	}
	
	public void testPostDbTableByPk()throws Exception{
		Assert.assertNotEquals(selectByPrimaryKey(1).get("name"),"xxyyzz001");
		
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/1");
		req.addParameter("name", "xxyyzz001");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200,resp.getMessage());
		 
		Assert.assertEquals(selectByPrimaryKey(1).get("name"),"xxyyzz001");
 	}
	 	
	public void testPostDbTableByMultiKeys()throws Exception{
		Assert.assertNotEquals(selectByPrimaryKey(2).get("name"),"xxyyzz002");
		
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/record_id=2");
		
		req.addParameter("name", "xxyyzz002");
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200,resp.getMessage()); 
		Assert.assertEquals(selectByPrimaryKey(2).get("name"),"xxyyzz002");
 	}

	public void testPostDbTableNone()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/record_id=2");
		 
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),400,resp.getMessage()); 
 	}
	
	public void testPostDbTableRow1()throws Exception{
		Record base=TestRecordV2().defaults();
		base.save();
		
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("name", "new_002");
		 
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200,resp.getMessage());
		
		DataMap data=resp.getData();
		Assert.assertEquals(data.getInt("rows",0),1);
		
		DataMap entity=(DataMap)data.get("entity");
		Assert.assertEquals(entity.getInt("record_id",0),base.getInteger("record_id")+1);
 	}
	
	public void testPostDbTableRow2()throws Exception{
		Record base=TestRecordV2().defaults();
		base.save();
		
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("title", "title_03x");
		req.addParameter("name", "new_030");
		req.addParameter("name", "new_031");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200,resp.getMessage());
		
		DataTable<DataMap> table=resp.getData();
		Assert.assertEquals(table.size(),2);
		
		int rid=base.getInteger("record_id")+1;
		for(DataMap data:table){
			Assert.assertEquals(data.getInt("rows",0),1);
			DataMap entity=(DataMap)data.get("entity");
			Assert.assertEquals(entity.getInt("record_id",0),rid++);
		}
		
		Record c1=selectByPrimaryKey(base.getInteger("record_id")+1);
		Record c2=selectByPrimaryKey(base.getInteger("record_id")+2);
		Assert.assertEquals(c1.get("name"),"new_030");
		Assert.assertEquals(c2.get("name"),"new_031");
		Assert.assertEquals(c1.get("title"),"title_03x");
		Assert.assertNull(c2.get("title"));
 	}
	
	public void testPostMultiTables()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record,test_record_v2");
		req.addParameter("test_record_v2.title", "title_03x");
		req.addParameter("test_record_v2.name", "new2_030");
		req.addParameter("test_record_v2.name", "new2_031");
		
		req.addParameter("test_record.title", "title_03x");
		req.addParameter("test_record.name", "new_030");
		req.addParameter("test_record.name", "new_031");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200,resp.getMessage());
		
		DataTable<DataMap> table=resp.getData();
		Assert.assertEquals(table.size(),4);
		
		Assert.assertEquals(table.get(0).getString("table"),"test_record");
		Assert.assertEquals(table.get(1).getString("table"),"test_record");
		Assert.assertEquals(table.get(2).getString("table"),"test_record_v2");
		Assert.assertEquals(table.get(3).getString("table"),"test_record_v2");
	}
 
}
