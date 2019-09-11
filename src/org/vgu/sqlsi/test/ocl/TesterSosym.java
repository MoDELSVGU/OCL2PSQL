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

public class TesterSosym  {
	
	public static void main(String[] args) throws OclParseException, JSQLParserException, FileNotFoundException, IOException, ParseException {
		
		/* Note: the file programDB_context.json is needed for the examples below
		 * it can be found in Documentation
		 */
		JSONArray context =  (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/sosymDB_context.json"));
			
		Ocl2Sql ocl2psql = new Ocl2Sql();
		ocl2psql.setPlainUMLContext(context);
		
		OclExpression exp;
		/* Test */
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		exp = OclExpression.parse("Car::allInstances()->size()", new DefaultOclContext());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test */
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		exp = OclExpression.parse("Car::allInstances()->collect(c|c.Car:model)->size()", new DefaultOclContext());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test */
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		exp = OclExpression.parse("Car::allInstances()->collect(c|c.Car:owners)->size()", new DefaultOclContext());
		System.out.println(ocl2psql.visit(exp));
		
		/* Test */
		ocl2psql.setAlias(0);
		ocl2psql.setVisitorContext(new ArrayList<IteratorSource>());
		exp = OclExpression.parse("Employee::allInstances()->collect(e|e.oclAsType(Person))", new DefaultOclContext());
		System.out.println(ocl2psql.visit(exp));
		
	}	
}