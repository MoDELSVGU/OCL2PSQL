package org.vgu.sqlsi.test.ocl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.sqlsi.main.Ocl2Sql;
import org.vgu.sqlsi.ocl.context.DefaultOclContext;
import org.vgu.sqlsi.ocl.exception.OclParseException;
import org.vgu.sqlsi.ocl.expressions.IteratorSource;
import org.vgu.sqlsi.ocl.expressions.OclExpression;

import net.sf.jsqlparser.JSQLParserException;

public class TesterOcl2PSql  {
	
	public static void main(String[] args) throws OclParseException, JSQLParserException, FileNotFoundException, IOException, ParseException {
		
		/* Note: the file programDB_context.json is needed for the examples below
		 * it can be found in Documentation
		 */
		JSONArray context =  (JSONArray) new JSONParser().parse(new FileReader("C:\\Users\\ngpbh\\eclipse-workspace\\sqlsi\\Documentation\\programDB_context.json"));
		//JSONArray context =  (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/programDB_context.json"));
			
		Ocl2Sql ocl2psql = new Ocl2Sql();
		ocl2psql.setPlainUMLContext(context);
		
		OclExpression exp;
		/* Test Literal: String */
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		exp = OclExpression.parse("'Manuel'", new DefaultOclContext());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test Literal: collect-String */
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		exp = OclExpression.parse("Reg_User::allInstances()->collect(x:Reg_User|'Manuel')", new DefaultOclContext());
		System.out.println(ocl2psql.visit(exp));
	 
		/* Test Literal: Boolean */
		exp = OclExpression.parse("false", new DefaultOclContext()); 
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test Literal: collect-Boolean */
		exp = OclExpression.parse("Reg_User::allInstances()->collect(x:Reg_User|false)", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test Literal: Integer */
		exp = OclExpression.parse("5", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test Literal: collect-Integer */
		exp = OclExpression.parse("Reg_User::allInstances()->collect(x:Reg_User|5)", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test EqualExp */
		exp = OclExpression.parse("4 = 3", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test EqualExp */
		exp = OclExpression.parse("Reg_User::allInstances()->collect(x:Reg_User|3=4)", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));		
		
		/* Test allInstancesExp */
		exp = OclExpression.parse("Lecturer::allInstances()", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
			
		/* Test allInstancesExp */
		exp = OclExpression.parse("Lecturer::allInstances()->collect(x:Lecturer|Reg_User::allInstances())", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));		
		
		/* Test collect: variable */
		exp = OclExpression.parse("Reg_User::allInstances()->collect(u:Reg_User|u)", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
	    System.out.println(ocl2psql.visit(exp));
	   
	    /* Test collect: equal variable */
		exp = OclExpression.parse("Reg_User::allInstances()->collect(u:Reg_User|u = u)", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
	    System.out.println(ocl2psql.visit(exp));
	    
	    /* Test collect: equal variable */
	    exp = OclExpression.parse(
	    				"Reg_User::allInstances()->collect(u:Reg_User|"
	    				+ "Reg_User::allInstances()->collect(w:Reg_User|"
	    				+ "u = w))", new DefaultOclContext());
	    ocl2psql.setAlias(0);
	    ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
	    System.out.println(ocl2psql.visit(exp));
	   
	    /* Test size */	
		exp = OclExpression.parse("Reg_User::allInstances()->size()", new DefaultOclContext());	
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		
		/* Test notEmpty */
		exp = OclExpression.parse("Lecturer::allInstances()->notEmpty()", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp)); 
	
		/* Test collect-notEmpty */
		exp = OclExpression.parse("Lecturer::allInstances()->collect(x:Lecturer|Lecturer::allInstances()->notEmpty())", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
	    System.out.println(ocl2psql.visit(exp));
	    
	    /* Test collect-attribute */
		exp = OclExpression.parse("Reg_User::allInstances()->collect(u:Reg_User|u.Reg_User:given_name)", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test collect-association */
		exp = OclExpression.parse(
							"Lecturer::allInstances()->collect(u:Lecturer|u.Lecturer:lectures)", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp)); 
	
		/* Test collect-association-notEmpty */
		exp = OclExpression.parse(
								"Lecturer::allInstances()->collect(l:lecturer|l.Lecturer:lectures->notEmpty())", new DefaultOclContext());	
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		

		/* Test collect-association-empty */
		exp = OclExpression.parse(
								"Lecturer::allInstances()->collect(l:lecturer|l.Lecturer:lectures)->isEmpty()", new DefaultOclContext());	
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test sizeCol */
		exp = OclExpression.parse(
								"Lecturer::allInstances()->collect(l:Lecturer|l.Lecturer:lectures->size())", new DefaultOclContext());	
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
	
		/* Test sizeCol-post */
		exp = OclExpression.parse(
								"Lecturer::allInstances()->collect(l:Lecturer|l.Lecturer:lectures)->size()", new DefaultOclContext());	
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test select */
		exp = OclExpression.parse(
						"Reg_User::allInstances()->select(u:Reg_User|u.Reg_User:given_name = 'Manuel')", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test select-total */
		exp = OclExpression.parse(
						"Reg_User::allInstances()->collect(u:Reg_User|u.Reg_User:given_name)->select(u:String|u = 'Manuel')", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test select-more */
		exp = OclExpression.parse(
						"Lecturer::allInstances()->collect(l:Lecturer|l.Lecturer:lectures)->select(c:Course|c.Course:students->notEmpty())", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test select-col */
		exp = OclExpression.parse(
					"Lecturer::allInstances()->collect(l:Lecturer|l.Lecturer:lectures->select(c:Course|c.Course:name= 'Distributed Systems'))", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
	
		/* Test forAll */
		exp = OclExpression.parse(
					"Reg_User::allInstances()->forAll(u:Reg_User|u.Reg_User:given_name = 'Manuel')", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test Exists */
		exp = OclExpression.parse(
					"Reg_User::allInstances()->exists(u:Reg_User|u.Reg_User:given_name = 'Manuel')", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test ForAllExists */
		exp  = OclExpression.parse(
					"Reg_User::allInstances()->forAll(u:Reg_User|Reg_User::allInstances()->exists(w|u.Reg_User:given_name = w.Reg_User:given_name))", new DefaultOclContext());
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		System.out.println(ocl2psql.visit(exp));	
	}	
}
