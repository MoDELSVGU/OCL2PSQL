package org.vgu.sqlsi.test.ocl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;

public class Tester {
	static final int size1 = 1000000;
	static final int size2 = 100000;
	
	//sosymdb1000
//	static final int size1 = 1000;
//	static final int size2 = 100;
	
	//sosymdb10000
//	static final int size1 = 10000;
//	static final int size2 = 1000;
	
	//sosymdb10000
    //static final int size1 = 100000;
    //static final int size2 = 10000;
    
    //sosymdb107
//    static final int size1 = 10000000;
//    static final int size2 = 1000000;
	
	public static Connection getConnection()
		            throws SQLException, NamingException, ClassNotFoundException {

		    	Class.forName("com.mysql.cj.jdbc.Driver");
		    	Connection con = DriverManager.getConnection(
		    	                  "jdbc:mysql://localhost:3306/thesis_test?useSSL=false", "tanbinhtech", "4tanbinhtech");
		        return con;
		    }

		
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, NamingException, IOException {
		
		
		
		//Get the file reference
//	    Path path = Paths.get("/Users/clavel/sosymdb107_car");
		Path path = Paths.get("./sosymdb107_car");
		 
		try (BufferedWriter writer = Files.newBufferedWriter(path))
		{
			for(int i = 1; i <= size1; i++) {
			    String value = "#".concat("'color_".concat(String.valueOf(i))).concat("\r\n");
//				String value = "#".concat(String.valueOf(i)).concat(",").concat("'color_".concat(String.valueOf(i)));
				writer.write(value);
				writer.newLine();
			}
		}

		//
//		path = Paths.get("/Users/clavel/sosymdb107_person");
		path = Paths.get("./sosymdb107_person");

		try (BufferedWriter writer = Files.newBufferedWriter(path))
		{
			for(int i = 1; i <= size2; i++) {
			    String value = "#".concat("'name_".concat(String.valueOf(i))).concat("\r\n");
//				String value = "#".concat(String.valueOf(i)).concat(",").concat("'name_".concat(String.valueOf(i)));
				writer.write(value);
				writer.newLine();
			}
		}
		//
//		path = Paths.get("/Users/clavel/sosymdb107_employee");
//		path = Paths.get("./sosymdb107_employee");

		try (BufferedWriter writer = Files.newBufferedWriter(path))
		{
			for(int i = 1; i <= size2; i++) {
			    String value = "#".concat(String.valueOf(i)).concat("\r\n");
//				String value = "#".concat(String.valueOf(i)).concat(",").concat(String.valueOf(i));
				writer.write(value);
				writer.newLine();
			}
		}
		//
//		path = Paths.get("/Users/clavel/sosymdb107_car_person");
		path = Paths.get("./sosymdb107_car_person");

		try (BufferedWriter writer = Files.newBufferedWriter(path))
		{
			//int car = 1000127;
			int person = 1;
			int count = 1;
			for(int i = 1; i <= size1; i++) {
			   if (count > 10) {
				   person = person + 1;
				   count = 1;
			   }
				String value = "#".concat(String.valueOf(i)).concat(",")
							.concat(String.valueOf(person)).concat("\r\n");
					writer.write(value);
					writer.newLine();
					count = count + 1;
				
			}
		}

		
		
		//Statement st = con.createStatement();
		//st.execute("source /Users/clavel/test_securesql");
		
		
		 //FileOutputStream fos = new FileOutputStream("/Users/clavel/test_securesql");
	    //DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
	    //outStream.writeChars(value);
	    //
	   //outStream.close();
		//Connection con = Tester.getConnection();
		
		//Statement st = con.createStatement();
		//st.execute("DELETE FROM Reg_User");
		
		//String multiselect = "";
		//for(int i = 1; i <= size1; i++) {
		//	if (i > 1) {
		//		multiselect.concat(",");
		//	}
		//	multiselect.
		//	if (i % 10000 == 0) {
		//		System.out.println(LocalTime.now());
		//	}
		//	}
		//System.out.print(multiselect);
		//st.execute(multiselect);
		//con.commit();
		System.out.println("Done");
		}
	}
