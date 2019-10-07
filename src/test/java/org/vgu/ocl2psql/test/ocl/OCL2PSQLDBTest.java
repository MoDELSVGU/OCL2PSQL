/**************************************************************************
Copyright 2019 Vietnamese-German-University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
***************************************************************************/
package org.vgu.ocl2psql.test.ocl;

import static org.junit.Assert.assertArrayEquals;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.vgu.ocl2psql.ocl.context.DefaultOclContext;
import org.vgu.ocl2psql.ocl.exception.OclParseException;
import org.vgu.ocl2psql.ocl.expressions.IteratorSource;
import org.vgu.ocl2psql.ocl.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.visitor.OCL2SQLParser;

public class OCL2PSQLDBTest {

	private static Connection getConnection(String db)  {
		Connection con = null;
		try {
			con = DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/" + db +"?useSSL=false","root","admin");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;


	} 
	
	
	private void execute(String db, String[] expected, String exp) {
		JSONArray context;
		try {
			context = (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/programDB_context.json"));
			OCL2SQLParser ocl2sql = new OCL2SQLParser();
			ocl2sql.setPlainUMLContext(context);
			ocl2sql.setVisitorContext(new ArrayList<IteratorSource>());
			
			String sql = ocl2sql.visit(OclExpression.parse(exp, new DefaultOclContext())).toString();
			Connection con = getConnection(db);
			
			PreparedStatement st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<String> list = new ArrayList<String>();
			while (rs.next()) {
				String value = rs.getString(1);
				if (rs.wasNull()) {
					list.add("NULL");
			      } else {
			    	  list.add(value);
				}
			};
			String[] result = new String[list.size()];
			
			System.out.println("==============");
			System.out.println(exp);
			System.out.println(sql);
			System.out.println("---");
			System.out.println("Expected:" + " " + Arrays.toString(expected));
			System.out.println("Result:" + " " + Arrays.toString(list.toArray(result)));
			System.out.println("==============");
			assertArrayEquals("failure", expected, list.toArray(result));
			con.close();

		
		} catch (IOException | ParseException | OclParseException | SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	
	
	@Test
	public void test1() {
		execute("programDB", new String[] {"Manuel"}, "'Manuel'");
	}
	
	@Test
	public void test2() {
		execute("programDB",  
				new String[] {"Manuel", "Manuel", "Manuel", "Manuel", "Manuel"}, 
				"Reg_User::allInstances()->collect(x:Reg_User|'Manuel')");
	}
	@Test
	public void test3() {
		execute("programDB",  
				new String[] {"0"}, 
				"false");
	}
	@Test
	public void test4() {
		execute("programDB",  
				new String[] {"0", "0", "0", "0", "0"}, 
				"Reg_User::allInstances()->collect(x:Reg_User|false)");
	}
	
	@Test
	public void test5() {
		execute("programDB",  
				new String[] {"5"}, 
				"5");
	}
	
	@Test
	public void test6() {
		execute("programDB",  
				new String[] {"5", "5", "5", "5", "5"}, 
				"Reg_User::allInstances()->collect(x:Reg_User|5)");
	}
	
	@Test
	public void test7() {
		execute("programDB",  
				new String[] {"0"}, 
				"4 = 3");
	}
	
	@Test
	public void test8() {
		execute("programDB",  
				new String[] {"0", "0", "0", "0", "0"}, 
				"Reg_User::allInstances()->collect(x:Reg_User|3=4)");
	}
	
	@Test
	public void test9() {
		execute("programDB",  
				new String[] {"1", "2"}, 
				"Lecturer::allInstances()");
	}
	
	@Test
	public void test10() {
		execute("programDB",  
				new String[] {"4", "4", "3", "3", "5", "5", "1", "1", "2", "2"}, 
				"Lecturer::allInstances()->collect(x:Lecturer|Reg_User::allInstances())");
	}
	
	
	@Test
	public void test11() {
		execute("programDB",  
				new String[] {"4", "3", "5", "1", "2"}, 
				"Reg_User::allInstances()->collect(u:Reg_User|u)");
	}
	
	@Test
	public void test12() {
		execute("programDB",  
				new String[] {"1", "1", "1", "1", "1"}, 
				"Reg_User::allInstances()->collect(u:Reg_User|u = u)");
	}
	
	@Test
	public void test13() {
		execute("programDB",  
				new String[] {
						"1", "0", "0", "0", "0", 
						"0", "1", "0", "0", "0", 
						"0", "0", "1", "0", "0", 
						"0", "0", "0", "1", "0" , 
						"0", "0", "0", "0", "1"}, 
				"Reg_User::allInstances()->collect(u:Reg_User|"
						+ "Reg_User::allInstances()->collect(w:Reg_User|"
						+ "u = w))");
	}
	
	@Test
	public void test14() {
		execute("programDB",  
				new String[] {"5"}, 
				"Reg_User::allInstances()->size()");
	}
	
	@Test
	public void test15() {
		execute("programDB",  
				new String[] {"1"}, 
				"Lecturer::allInstances()->notEmpty()");
	}
	
	@Test
	public void test16() {
		execute("programDB",  
				new String[] {"1", "1"}, 
				"Lecturer::allInstances()->collect(x:Lecturer|Lecturer::allInstances()"
				+ "->notEmpty())");
	}
	
	@Test
	public void test17() {
		execute("programDB",  
				new String[] {"Trang", "Manuel", "Jaime", "Aldiyar", "Trung"}, 
				"Reg_User::allInstances()->collect(u:Reg_User|u.Reg_User:given_name)");
	}
	
	@Test
	public void test18() {
		execute("programDB",  
				new String[] {"1", "2", "NULL"}, 
				"Lecturer::allInstances()->collect(u:Lecturer|u.Lecturer:courses)");
	}
	
	@Test
	public void test19() {
		execute("programDB",  
				new String[] {"1", "0"}, 
				"Lecturer::allInstances()->collect(l:lecturer|l.Lecturer:courses->notEmpty())");
	}
	
	@Test
	public void test20() {
		execute("programDB",  
				new String[] {"0"}, 
				"Lecturer::allInstances()->collect(l:lecturer|l.Lecturer:courses)->isEmpty()");
	}
	
	@Test
	public void test21() {
		execute("programDB",  
				new String[] {"2", "0"}, 
				"Lecturer::allInstances()->collect(l:Lecturer|l.Lecturer:courses->size())");
	}
	
	@Test
	public void test22() {
		execute("programDB",  
				new String[] {"2"}, 
				"Lecturer::allInstances()->collect(l:Lecturer|l.Lecturer:courses)->size()");
	}
	
	@Test
	public void test23() {
		execute("programDB",  
				new String[] {"4", "3", "5", "1", "2"}, 
				"Reg_User::allInstances()->select(u:Reg_User|true)");
	}
	
	@Test
	public void test24() {
		execute("programDB",  
				new String[] {"3"}, 
				"Reg_User::allInstances()->select(u:Reg_User|u.Reg_User:given_name = 'Manuel')");
	}
	
	@Test
	public void test25() {
		execute("programDB",  
				new String[] {"Manuel"}, 
				"Reg_User::allInstances()"
				+ "->collect(u:Reg_User|u.Reg_User:given_name)->select(u:String|u = 'Manuel')");
	}
	
	@Test
	public void test26() {
		execute("programDB",  
				new String[] {"1"}, 
				"Lecturer::allInstances()->collect(l:Lecturer|l.Lecturer:courses)"
				+ "->select(c:Course|c.Course:students->notEmpty())");
	}
	
	@Test
	public void test27() {
		execute("programDB",  
				new String[] {"2"}, 
				"Lecturer::allInstances()->collect(l:Lecturer|l.Lecturer:courses"
				+ "->select(c:Course|c.Course:name= 'Distributed Systems'))");
	}
	
	@Test
	public void test28() {
		execute("programDB",  
				new String[] {"1"}, 
				"Reg_User::allInstances()->forAll(u:Reg_User|1)");
	}
	
	@Test
	public void test29() {
		execute("programDB",  
				new String[] {"0"}, 
				"Reg_User::allInstances()->forAll(u:Reg_User|u.Reg_User:given_name = 'Manuel')");
	}
	

	
//	@Test
//	public void testing() throws FileNotFoundException, IOException, ParseException, OclParseException {
//		JSONArray context = (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/programDB_context.json"));
//		Ocl2Sql ocl2sql = new Ocl2Sql();
//		ocl2sql.setAlias(0);
//		ocl2sql.setPlainUMLContext(context);
//		ocl2sql.setVisitorContext(new ArrayList<IteratorSource>());
		
//		String exp = "Reg_User::allInstances()->collect(u:Reg_User|u.oclAsType(Lecturer))->size()";
//		String sql = ocl2sql.visit(OclExpression.parse(exp, new DefaultOclContext())).toString();
//		System.out.print(sql);
//		}
}	
	

