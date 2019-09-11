package org.vgu.sqlsi.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.sqlsi.ocl.exception.OclParseException;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

public class SqlSI {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, OclParseException, JSQLParserException {
		//JSONArray policy =  (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/programDB_policy.json"));
		//JSONArray context =  (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/programDB_context.json"));
		//JSONArray queries = (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/programDB_test.json"));

		//JSONArray policy =  (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/Projects/sacmat19/vgu_policy.json"));
		//JSONArray context =  (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/Projects/sacmat19/vgu_context.json"));
		//JSONArray queries = (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/Projects/sacmat19/vgu_test.json"));

		JSONArray policy =  (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/Projects/sacmat19/alice_policy.json"));
		JSONArray context =  (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/Projects/sacmat19/alice_context.json"));
		JSONArray queries = (JSONArray) new JSONParser().parse(new FileReader("/Users/clavel/Projects/sacmat19/alice_test.json"));

		// SqlSIGenAuthFun generates the definition of all the check auth functions
		// for all the resources in the given context, according to the given policy
		// CRITICALLY, the 3-argument boolean value determines whether the definition
		// implements a TRUMAN-model = false or a NON-TRUMAN model = true.
		// NOTICE: currently, only NON-TRUMAN model is implemented
		SqlSIGenAuthFun(context, policy, false);
		
		//SqlSIGen transforms the given PROCEDURES into SECURE PROCEDURES 
		//by adding WHERE clauses
		SqlSIGen(context, policy, queries);
	}


	private static void SqlSIGenAuthFun(JSONArray context, JSONArray policy, boolean exit) throws OclParseException, IOException  { 
		//File file = new File("/Users/clavel/programDB_fun.sql");
		//File file = new File("/Users/clavel/Projects/sacmat19/vgu_fun.sql");
		File file = new File("/Users/clavel/Projects/sacmat19/alice_fun.sql");
		FileWriter fileWriter = new FileWriter(file);
		
		// printCheckAuthFun simply generates the definition of the 
		// outermost checkAuth function, i.e., the one 
		// that guaranteed that all auth functions have been
		// checked before ``returning'' the value of the WHERE.
		// As expected, the definition of this function is ``constant'': i.e., 
		// it does not depend neither on the context nor on the policy.
		// CRITICALLY: however, it depends on the exit-value (true = NON-TRUMAN, false = TRUMAN)
		String sqlCheckAuthFun = Utilities.printCheckAuthFun(exit);
		fileWriter.write(sqlCheckAuthFun);
		fileWriter.write("\n");
		
		// getAuthFunctions generates the list of the TEMPLATES of
		// ALL the auth functions for all the resources in the given context,
		// according to the given policy
		JSONArray authFunctions = Utilities.getAuthFunctions(context, policy);
		//
		for(Object authFun : authFunctions) {
			// printAuthRoleFun generates the DEFINITIONS of all the
			// auth functions for all the resources in the given context,
			// according to the given policy
			// CRITICALLY, it uses the library Ocl2Sql to map OCL authorization constraints
			// into SQL statements
			String sqlAuthRoleFun = Utilities.printAuthRoleFun(authFun, context, policy);
			
			// printAuthRole generates the DEFINITIONS of the OUTER (independent of the ROLES)
			// auth functions for all the resources in the given context,
			// according to the given policy (roles)
			// IT DOES NOT uses Ocl2SQL. ONLY looks at the roles & default
			String sqlAuthFun = Utilities.printAuthFun(authFun, exit);
			
			try {
				
				fileWriter.write(sqlAuthRoleFun);
				fileWriter.write("\n");
				fileWriter.write(sqlAuthFun);
				fileWriter.write("\n");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fileWriter.flush();
		fileWriter.close();

	}

	private static void SqlSIGen(JSONArray context, JSONArray policy, JSONArray queries) throws OclParseException, JSQLParserException, IOException {
		//File file = new File("/Users/clavel/programDB_proc.sql");
		//File file = new File("/Users/clavel/Projects/sacmat19/vgu_proc.sql");
		File file = new File("/Users/clavel/Projects/sacmat19/alice_proc.sql");
		
		FileWriter fileWriter = new FileWriter(file);
		for(Object query : (JSONArray) queries) {
			String name = (String) ((JSONObject) query).get("name");
			// pars are the arguments of the stored procedure
			JSONArray pars = new JSONArray();
			 if (((JSONObject) query).containsKey("pars")) {
				pars = (JSONArray) ((JSONObject) query).get("pars");
			 }; 
			// vars are the local variables that may be used in the body of the stored procedure
			JSONArray vars = (JSONArray) ((JSONObject) query).get("vars");
			// body is the definition of the stored procedure
			JSONArray body = (JSONArray) ((JSONObject) query).get("body");
			
			List<Statement> statements = new ArrayList<Statement>();
			
			// Here we transform the given BODY-statements into SECURE BODY-statements 
			// by adding WHERE clauses
			// CRITICALLY, we use the visitor InjectorStatement (with the given context
			// and the given policy) 
			
			for(Object statement : body) { 
				Statement statementSql = CCJSqlParserUtil.parse(statement.toString());
				InjectorStatement injectorStatement = new InjectorStatement();
				injectorStatement.setContext(context);
				injectorStatement.setPolicy(policy);
				injectorStatement.setParameters(pars);
				statementSql.accept(injectorStatement);
				statements.add(injectorStatement.getResult());
			}
			
			
			// printProc simply "build" the procedure definion
			// MOSTLY relevant for statements DIFFERENT FROM SELECT (read)
			String sqlProc = Utilities.printProc(name, pars, vars, context, statements);
			try {
				fileWriter.write(sqlProc);
				fileWriter.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fileWriter.flush();
		fileWriter.close();
		//
		System.out.println("Done");
	}
}

