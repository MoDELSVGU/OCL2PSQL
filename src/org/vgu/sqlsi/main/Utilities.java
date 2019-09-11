package org.vgu.sqlsi.main;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.vgu.sqlsi.ocl.context.DefaultOclContext;
import org.vgu.sqlsi.ocl.exception.OclParseException;
import org.vgu.sqlsi.ocl.expressions.IteratorSource;
import org.vgu.sqlsi.ocl.expressions.OclExpression;
import org.vgu.sqlsi.uml.Multiplicity;
import org.vgu.sqlsi.uml.UMLAssociationEnd;
import org.vgu.sqlsi.uml.UMLAttribute;
import org.vgu.sqlsi.uml.UMLClass;
import org.vgu.sqlsi.uml.UMLContextBis;
import org.vgu.sqlsi.uml.UMLMultiplicity;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class Utilities {
	
	/* context */
	public static ArrayList<String> getEntities(JSONArray context){
		ArrayList<String> classes = new ArrayList<String>();

		for(Object entity : context) {
			if (((JSONObject) entity).containsKey("class")) {
				classes.add((String) ((JSONObject) entity).get("class"));
			}
		}
		return classes;		
	}

	public static JSONArray getAttributes(JSONArray context, String className){
		JSONArray attributes = new JSONArray();
		for(Object entity : context) {
			if (((JSONObject) entity).containsKey("class")) {
			if (((JSONObject) entity).get("class").equals(className)) {
				if (((JSONObject) entity).containsKey("attributes")) {
					attributes = (JSONArray) ((JSONObject) entity).get("attributes");
				}
				 
			}
		}
		}
		return attributes;		
	}
	

	/* className is the name of the UMLclass (i.e., not an UMLassociation*/
public static JSONArray getAssociations(JSONArray context, String className){
	JSONArray associations = new JSONArray();
		for(Object object : context) {
			if (((JSONObject) object).containsKey("association")) {
				JSONArray classes = (JSONArray) ((JSONObject) object).get("classes");
				JSONArray ends = (JSONArray) ((JSONObject) object).get("ends");
				for (int index_class = 0; index_class < classes.size(); index_class++) {
					if (classes.get(index_class).equals(className)) {
						JSONObject resource = new JSONObject();
						resource.put("name", ends.get(index_class));
						resource.put("association", ((JSONObject) object).get("association"));
						associations.add(resource);
					}
				}
			}
			
		}
		return associations;		
	}
	
	public static UMLContextBis buildUMLContextBis(JSONArray umlContext) {
		UMLContextBis context = new UMLContextBis();
		List<UMLClass> classes = new ArrayList<UMLClass>();
		List<UMLAttribute> attributes = new ArrayList<UMLAttribute>();
		List<UMLAssociationEnd> associationEnds = new ArrayList<UMLAssociationEnd>();
		//List<UMLMultiplicity> multiplicities = new ArrayList<UMLMultiplicity>();
		
		// classes
		for(Object JSONclass : umlContext) {
			if (((JSONObject) JSONclass).containsKey("class")){
			String className =  (String) ((JSONObject) JSONclass).get("class");
			UMLClass umlClass = new UMLClass();
			umlClass.setName(className);
			if (((JSONObject) JSONclass).containsKey("isUserClass")){
				if (((String) ((JSONObject) JSONclass).get("isUserClass")).equals("true")){
					context.setUserClass(umlClass);
				};
			};
			if (((JSONObject) JSONclass).containsKey("isRoleClass")){
				if (((String) ((JSONObject) JSONclass).get("isRoleClass")).equals("true")){
					context.setRoleClass(umlClass);
				};
			};
			classes.add(umlClass); 
		}
		}
		context.setClasses(classes);
		// attributes
		for(Object JSONclass : umlContext) {
			if (((JSONObject) JSONclass).containsKey("class")){
			String sourceName =  (String) ((JSONObject) JSONclass).get("class");
			for(Object JSONAttribute : (JSONArray) ((JSONObject) JSONclass).get("attributes")) {
				UMLAttribute umlAttribute = new UMLAttribute();
				String attributeName =  (String) ((JSONObject) JSONAttribute).get("name");
				String attributeType =  (String) ((JSONObject) JSONAttribute).get("type");
				umlAttribute.setName(attributeName);
				umlAttribute.setType(attributeType);
				umlAttribute.setSource(findUMLClass(classes, sourceName));
				attributes.add(umlAttribute);
			}
		}
		}
		context.setAttributes(attributes);
		// association ends	
		for(Object object : umlContext) {
			if (((JSONObject) object).containsKey("association")){
			UMLAssociationEnd umlAssociationEnd1 = new UMLAssociationEnd();
			umlAssociationEnd1.setName((String) ((JSONArray) ((JSONObject) object).get("ends")).get(0));
			umlAssociationEnd1.setSource(findUMLClass(classes, (String) ((JSONArray) ((JSONObject) object).get("classes")).get(0))); 
			umlAssociationEnd1.setTarget(findUMLClass(classes, (String) ((JSONArray) ((JSONObject) object).get("classes")).get(1))); 
			umlAssociationEnd1.setOpposite((String) ((JSONArray) ((JSONObject) object).get("ends")).get(1));

			UMLMultiplicity umlMultiplicity1 = new UMLMultiplicity();
			umlMultiplicity1.setAssociationend(umlAssociationEnd1);
			String multiplicity = (String) ((JSONArray) ((JSONObject) object).get("multis")).get(0);
			switch (multiplicity) {
			case "*" : 
			{
				umlMultiplicity1.setMultiplicity(Multiplicity.Multi);
				break;
			}
			default : umlMultiplicity1.setMultiplicity(Multiplicity.Multi);
			}
			umlAssociationEnd1.setMultiplicity(umlMultiplicity1);	
			associationEnds.add(umlAssociationEnd1);

			UMLAssociationEnd umlAssociationEnd2 = new UMLAssociationEnd();
			umlAssociationEnd2.setName((String) ((JSONArray) ((JSONObject) object).get("ends")).get(1));
			umlAssociationEnd2.setSource(findUMLClass(classes, (String) ((JSONArray) ((JSONObject) object).get("classes")).get(1))); 
			umlAssociationEnd2.setTarget(findUMLClass(classes, (String) ((JSONArray) ((JSONObject) object).get("classes")).get(0))); 
			umlAssociationEnd2.setOpposite((String) ((JSONArray) ((JSONObject) object).get("ends")).get(0));
			
			UMLMultiplicity umlMultiplicity2 = new UMLMultiplicity();
			umlMultiplicity2.setAssociationend(umlAssociationEnd2);
			multiplicity = (String) ((JSONArray) ((JSONObject) object).get("multis")).get(1);
			switch (multiplicity) {
			case "*" : 
			{
				umlMultiplicity2.setMultiplicity(Multiplicity.Multi);
				break;
			}
			default : umlMultiplicity2.setMultiplicity(Multiplicity.Multi);
			}
			umlAssociationEnd2.setMultiplicity(umlMultiplicity2);	
			associationEnds.add(umlAssociationEnd2);



			}
		}
		context.setAssociationends(associationEnds);

		return context;
	}
		

	public static UMLClass findUMLClass(List<UMLClass> classes, String name) {
		for(UMLClass umlClass : classes) {
			if (umlClass.getName().equals(name)) {
				return umlClass;
			}
		}
		return null;
	}
	
	
	/* policy */
	/*
	public static ArrayList<String> getAuth(String entity, String resource, String role, JSONArray policy) {
		ArrayList<String> auths = new ArrayList<String>();
		
		for(int i=0; i < policy.size(); i++) {
			JSONObject entityPolicy = (JSONObject) policy.get(i);
			
			if(entityPolicy.get("entity").equals(entity)) {
				// entity
				JSONArray permissions = (JSONArray) entityPolicy.get("permissions");
				for(int j = 0; j < permissions.size(); j++) {
					JSONObject permission = (JSONObject) permissions.get(j);
					JSONArray resources = (JSONArray) permission.get("resources");
					// resource
					for(int h = 0; h < resources.size(); h++) {
					if(resources.get(h).equals(resource)) {
						JSONArray roles = (JSONArray) permission.get("roles");
						for(int w = 0; w < roles.size(); w++) {
							if (roles.get(w).equals(role)) {
								auths.add((String) permission.get("auth"));
							}
						}
						
					}
				}
				
			}
			}	
		}
		return auths;
	}
	*/
	public static ArrayList<String> getAuth(String action, String entity, String resource, String role, JSONArray policy) {
		ArrayList<String> auths = new ArrayList<String>();

		for(int i=0; i < policy.size(); i++) {
			JSONObject entityPolicy = (JSONObject) policy.get(i);

			if(entityPolicy.get("entity").equals(entity)) {
				// entity
				JSONArray permissions = (JSONArray) entityPolicy.get("permissions");
				for(int j = 0; j < permissions.size(); j++) {
					JSONObject permission = (JSONObject) permissions.get(j);
					JSONArray actions = (JSONArray) permission.get("actions");
					for(int a = 0; a < actions.size(); a++) {
						if(actions.get(a).equals(action)) {

							JSONArray resources = (JSONArray) permission.get("resources");
							// resource
							for(int h = 0; h < resources.size(); h++) {
								if(resources.get(h).equals(resource)) {
									if (((JSONObject) permission).containsKey("roles")){
										JSONArray roles = (JSONArray) permission.get("roles");
										for(int w = 0; w < roles.size(); w++) {
											if (roles.get(w).equals(role)) {
												auths.add((String) permission.get("auth"));
											}
										}

									}
									else {
										if (((JSONObject) permission).containsKey("default")){	
											auths.add((String) permission.get("default"));
											//auths.add("ALSO BAD");
										}
									}
								}
							}

						}
					}	
				}
			}
		}
		return auths;
	}


	public static ArrayList<String> getAuth(String action, String entity, String role, JSONArray policy) {
		ArrayList<String> auths = new ArrayList<String>();

		for(int i=0; i < policy.size(); i++) {
			JSONObject entityPolicy = (JSONObject) policy.get(i);

			if(entityPolicy.get("entity").equals(entity)) {
				// entity
				JSONArray permissions = (JSONArray) entityPolicy.get("permissions");
				for(int j = 0; j < permissions.size(); j++) {
					JSONObject permission = (JSONObject) permissions.get(j);
					JSONArray actions = (JSONArray) permission.get("actions");
					// resource
					for(int h = 0; h < actions.size(); h++) {
						if(actions.get(h).equals(action)) {
							if (((JSONObject) permission).containsKey("roles")){
								JSONArray roles = (JSONArray) permission.get("roles");
								for(int w = 0; w < roles.size(); w++) {
									if (roles.get(w).equals(role)) {
										auths.add((String) permission.get("auth"));
									}
								}
							} else {
								if (((JSONObject) permission).containsKey("default")){	
									auths.add((String) permission.get("default"));
								}
							}
						}

					}
				}	
			}
		}
		return auths;
	}

	
	
	public static String printAuthFun(Object authFun, boolean exit) {
		
		String table = (String) ((JSONObject) authFun).get("table");
		String action = (String) ((JSONObject) authFun).get("action");
		String name = "";
		JSONArray roles = (JSONArray) ((JSONObject) authFun).get("roles");
		JSONArray parameters = (JSONArray) ((JSONObject) authFun).get("parameters");
			
		switch (action) {
		case "read" : {
			String column = (String) ((JSONObject) authFun).get("column");
			name = "auth" + "_" + "read" +  "_" + table  + "_" + column;
			break;
		}
		case "update" : {
			String column = (String) ((JSONObject) authFun).get("column");
			name = "auth" + "_" + "update" +  "_" + table  + "_" + column;
			break;
		}
		case "delete" : {
			name = "auth" + "_" + "delete" +  "_" + table ;
			break;
		}
		case "create" : {
			name = "auth" + "_" + "create" +  "_" + table ;
			break;
		}
		default : {
			break;
		}
		}
		
		String s = "";
		s += "DROP FUNCTION IF EXISTS" + " " + name + ";\n";
		s += "DELIMITER //\n";
		s += "CREATE FUNCTION " + name ;
		s += "(" + printParameters(parameters, true) + ")" + "\n";
		s += "RETURNS INT DETERMINISTIC" + "\n";
		s += "BEGIN" + "\n";
		s += "DECLARE result INT DEFAULT 0;"  + "\n";
		int count = roles.size();
		if (count == 0) {
			 s +=  //"SIGNAL SQLSTATE '45000'" + "\n"
					//  + "SET MESSAGE_TEXT = 'Security exception';" + "\n" 
					//  + 
					"RETURN (0);" + "\n";
		} else {
		for(Object role : roles) {
			s += 
			"IF (krole =" + " " + "'"+ role +"'" + ")" + "\n" 
			+"THEN" + " " 
			   + "IF" + " " + name + "_" + role+ "(" +
			printParameters(removeParameter(parameters, "krole"), false) +
			")" + "\n"
			     + "THEN" + " " + "\n" 
			           + "RETURN (1);" + "\n" 
			     + "ELSE" + "\n";
			//
			if (exit) {
			    // s += "SIGNAL SQLSTATE '45000'"  + "\n"
			    //    + "SET MESSAGE_TEXT = 'Security exception';" + "\n";
			}; 
			//
			s += "RETURN (0);" + "\n"
					+ "END IF;" + "\n";
			count = count - 1;
			if (count > 0) {
			  s += "ELSE" + "\n";	
			} else {
				if (roles.contains("Default")) {
					s += "ELSE" + "\n";
					s +=  "IF" + " " + name + "_" + "Default" + "(" +
							printParameters(removeParameter(parameters, "krole"), false) +
							")" + "\n"
							+ "THEN" + " " + "\n" 
							+ "RETURN (1);" + "\n" 
							+ "ELSE" + "\n"
							//+ "SIGNAL SQLSTATE '45000'" + "\n"
							//+ "SET MESSAGE_TEXT = 'Security exception';" + "\n"  
							+ "RETURN (0);" + "\n"
							+ "END IF;" + "\n";
				} else {
					 s +=  "ELSE" + "\n" 
							  //+ "SIGNAL SQLSTATE '45000'" + "\n"
							  //+ "SET MESSAGE_TEXT = 'Security exception';" + "\n"  
							  + "RETURN (0);" + "\n";
				}
				for (int i = 0; i < roles.size(); i++)  {
					s += "END IF;" + "\n";
				}
			}
		}
		}
		s += "END";
		s +=  " //" + "\n" + "DELIMITER ;";
		return s;
	}

	
	public static String printAuthRoleFun(Object authFun, JSONArray context, JSONArray policy) throws OclParseException {
		
		String entity = (String) ((JSONObject) authFun).get("entity");
		String table = (String) ((JSONObject) authFun).get("table");
		String action = (String) ((JSONObject) authFun).get("action");
		String name = "";
		JSONArray roles = (JSONArray) ((JSONObject) authFun).get("roles");
		JSONArray parameters = (JSONArray) ((JSONObject) authFun).get("parameters");
		
		switch (action) {
		case "read" : {
			String column = (String) ((JSONObject) authFun).get("column");
			name = "auth" + "_" + "read" +  "_" + table  + "_" + column;
			break;
		}
		case "update" : {
			String column = (String) ((JSONObject) authFun).get("column");
			name = "auth" + "_" + "update" +  "_" + table  + "_" + column;
			break;
		}
		case "delete" : {
			name = "auth" + "_" + "delete" +  "_" + table;
			break;
		}
		case "create" : {
			name = "auth" + "_" + "create" +  "_" + table;
			break;
		}
		case "default" : {
			break;
		}
		}
		
		String s = "";
		for(Object role : roles) {
			s += "DROP FUNCTION IF EXISTS" + " " + name + "_" + role + ";\n";
			s += "DELIMITER //\n";
			s += "CREATE FUNCTION " +name + "_" + role + " ";
			s += "(" + printParameters(removeParameter(parameters, "krole"), true) + ")" + "\n";
			s += "RETURNS INT DETERMINISTIC" + "\n";
			s += "BEGIN" + "\n";
			s += "DECLARE result INT DEFAULT 0;"  + "\n";
			//
			List<String> auths = new ArrayList<String>();
			switch (action) {
			case "read" : {
				String column = (String) ((JSONObject) authFun).get("column");
				auths = getAuth("read", entity, column, (String) role, policy);
				break;
			}
			case "update" : {
				String column = (String) ((JSONObject) authFun).get("column");
				auths = getAuth("update", entity, column, (String) role, policy);
				//System.out.print(column + " " + auths.get(0));
				break;
			}
			case "delete" : {
				auths = getAuth("delete", entity, (String) role, policy);
				break;
			}
			case "create" : {
				auths = getAuth("create", entity, (String) role, policy);
				break;
			}
			case "default" : {
				break;
			}
			}
			
			String disjAuths = "false";
			int count = 0;
			
			for(String auth : auths) {
				if (count > 0) {
				disjAuths =  disjAuths + " " + "or" + " " + "(" + auth +")" ;
				} else {
					disjAuths = "(" + auth + ")";
				}
				count = count + 1;	
			}
			
			Ocl2Sql ocl2sql = new Ocl2Sql();
			//ocl2sql.setUMLContext(Utilities.buildUMLContextBis(context));
			//ocl2sql.setUMLContext(context);
			ocl2sql.setAlias(0);
			ocl2sql.setPlainUMLContext(context);
			ocl2sql.setVisitorContext(new ArrayList<IteratorSource>());

			Select siAuth = (Select) ocl2sql.visit(OclExpression.parse((String) disjAuths, new DefaultOclContext()));
			List<Table> resultTables = new ArrayList<Table>();
			resultTables.add(new Table("result"));
			((PlainSelect) siAuth.getSelectBody()).setIntoTables(resultTables);

			s += siAuth.getSelectBody().toString();
			s += ";\n";
				
			s += "RETURN (result);" + "\n";		
			//
			s += "END";
			s +=  " //" + "\n" + "DELIMITER ;" ;
			s += "\n";
		}
		return s;
	}

	
	public static JSONArray removeParameter(JSONArray pars, String name) {
		JSONArray result = new JSONArray();
		for(Object par : pars) {
			if (!(((JSONObject) par).get("name").equals(name))) {
				result.add(par);
			}
		}
		return result ;
	}
	
	public static String printParameters(JSONArray parameters, boolean wtype) {
		String s = "";
		int countPars = parameters.size();
		for(Object par : parameters) {
			s += ((JSONObject) par).get("name");
				if (wtype) {
					s += " " + ((JSONObject) par).get("type");
				}
				countPars = countPars - 1;
				if (countPars > 0) {
					s += "," + " ";
				}
			}
		return s;
	}

	public static String printProc(String name, JSONArray pars, JSONArray vars, JSONArray context, List<Statement> statements) {
		
		
		String s = "";
		s += "DROP PROCEDURE IF EXISTS" + " " + name + ";\n";
		s += "DELIMITER //\n";
		s += "CREATE PROCEDURE " + name;
		s += "(";
		// parameters
		// new caller parameter
		s += "IN" + " " + "kcaller" + " " + "INT";
		// original parameters
		for(Object par : pars) {
			s += "," + " ";
			String parName = (String) ((JSONObject) par).get("name");
			String parType = "";
			switch ((String) ((JSONObject) par).get("type")) {
			case "Integer" : {
				parType = "INT";
				break;
			}
			case "String" : {
				parType = "VARCHAR(250)";
				break;
			}
			default : {
				break;
			}
			}
			s += "IN" + " " + parName + " " + parType;
		};
		s += ")";
		// body
		s += "\nBEGIN\n";
		for(Object var : vars) {
			String vname = (String) ((JSONObject) var).get("name");
			String vtype = "";
			switch ((String) ((JSONObject) var).get("type")) {
			case "Integer" : {
				vtype = "INT";
				break;
			}
			case "String" : {
				vtype = "VARCHAR(250)";
				break;
			}
			default : {
				break;
			}
			}
			s += "DECLARE" + " "  + vname + " " +  vtype + ";" + "\n";
			
		}
		//
		// declare rollback switch
		s += "DECLARE _rollback int DEFAULT 0;" + "\n";
						
		// declare caller's role variable and defines its value
		s += "DECLARE krole varchar(100) DEFAULT 'Default';" + "\n";
				
		// handler for SQL exception
		
		s += "DECLARE EXIT HANDLER FOR SQLEXCEPTION" + "\n"; 
		s += "BEGIN" + "\n";
		s += "SET _rollback = 1;" + "\n";
		s += "GET STACKED DIAGNOSTICS CONDITION 1 @p1 = RETURNED_SQLSTATE, @p2 = MESSAGE_TEXT;" + "\n";
		s +=  "SELECT @p1, @p2;" + "\n";
		s +=  "ROLLBACK;" + "\n";
		s += "END;" + "\n";
		
		
		s += "IF kcaller > 0 THEN \n";
		Select roleSelect = new Select();
		PlainSelect rolePlainSelect = new PlainSelect();
		rolePlainSelect.setFromItem(new Table(getUserClass(context)));
		SelectExpressionItem item = 
				new SelectExpressionItem
				(new Column(getRoleClass(context) + "." + "name"));
		List<Table> into = new ArrayList<Table>();
		into.add(new Table("krole"));
		rolePlainSelect.setIntoTables(into);
		rolePlainSelect.addSelectItems(item);
		// role join
		List<Join> roleJoins = new ArrayList<Join>();
		Join roleJoin = new Join();
		roleJoin.setRight(true);
		roleJoin.setRightItem(new Table(getRoleClass(context)));
		EqualsTo onJoin = new EqualsTo();
		onJoin.setLeftExpression(new Column(getUserClass(context) + "." + "role"));
		onJoin.setRightExpression(new Column(getRoleClass(context) + "." + "role_id"));
		roleJoin.setOnExpression(onJoin);
		roleJoins.add(roleJoin);
		rolePlainSelect.setJoins(roleJoins);
		// role where
		EqualsTo whereRole = new EqualsTo();
		whereRole.setLeftExpression(new Column("kcaller"));
		whereRole.setRightExpression(new Column(
				getUserClass(context) + "."
						+ getUserClass(context) + "_id"));
		rolePlainSelect.setWhere(whereRole);
		//
		roleSelect.setSelectBody(rolePlainSelect);
		//
		s += roleSelect.toString();
		s += ";";
		s += "\n";	
		s += "END IF;\n";
		s += "START TRANSACTION;\n";
		for(Statement statement : statements) {
			
		// update 
		
		if (statement.getClass().getSimpleName().equals("Update")) {
			s +=  statement.toString();
		}

		// delete
		else if (statement.getClass().getSimpleName().equals("Delete")) {
			s +=  statement.toString();
		}
		// create
		else if (statement.getClass().getSimpleName().equals("Insert")) {
			//s +=  statement.toString();
			
			List<Column> update_cols =  ((Insert) statement).getColumns();
			List<Expression> update_values =  
					((ExpressionList) ((Insert) statement).getItemsList()).getExpressions();

			List<String> checks = new ArrayList<String>();
			for(int column_index = 0; column_index < update_cols.size(); column_index++) {
				checks.add("auth_update_" + ((Insert) statement).getTable().getName()
						+ "_" + update_cols.get(column_index)
						+ "(" + "-1" + "," + "kcaller" + "," +  "krole"
						+ "," + update_values.get(column_index)+ ")");
			}
			
			//
			String checksIf = "checkAuth(1,";
			
			
			
			for(int check_index = 0; check_index < checks.size(); check_index++) {
				checksIf += checks.get(check_index);
				checksIf += " " + "AND" + " ";
			};
			

			s +=  "IF" + " " 
					+ checksIf 
					+ "auth_create_" + ((Insert) statement).getTable().getName()
					+ "(kcaller, krole)"
					+ ")" + "\n"
					+ "THEN" + " " 
					+ statement.toString()  + ";" + "\n"
					+ "END IF";
			
			
		} else {	
			// read
			if (((PlainSelect) 
					((Select) statement).getSelectBody()).getIntoTables() != null) {
				s += statement.toString();	
			} else {
				s += "DROP TEMPORARY TABLE IF EXISTS Result;" + "\n";
				s += "CREATE TEMPORARY TABLE IF NOT EXISTS Result AS  (";
				s += statement.toString();
				s += ");" + "\n";
				s += "IF _rollback = 0" + "\n";
				s += "THEN SELECT * from Result;" + "\n";
				s += "END IF" ;
			}	
		}
		//
		s += ";\n";
		}
		s += "END" + " " + "//" + "\n";
		s +=  "DELIMITER ;";
		return s;
	}

	private static String getUserClass(JSONArray context) {
		String userClass = null;
		for(Object entity : context) {
			if (((JSONObject) entity).containsKey("isUserClass")){
				if (((JSONObject) entity).get("isUserClass").equals("true")) {
					userClass = (String) ((JSONObject) entity).get("class");
					break;
				}	
			}
		}
		return userClass;
	}

	private static String getRoleClass(JSONArray context) {
		String roleClass = null;
		for(Object entity : context) {
			if (((JSONObject) entity).containsKey("isRoleClass")){
				if (((JSONObject) entity).get("isRoleClass").equals("true")) {
					roleClass = (String) ((JSONObject) entity).get("class");
					break;
				}	
			}
		}
		return roleClass;
	}

	public static JSONArray getResourceRoles(JSONArray policy, String entityName, String resourceName) {
		JSONArray roles = new JSONArray();
		for(Object entity : policy) {
			if (((JSONObject) entity).get("entity").equals(entityName)) {
				for (Object permission : (JSONArray) ((JSONObject) entity).get("permissions")) {
					if (((JSONObject) permission).containsKey("resources")) {
					for (Object resource : (JSONArray) ((JSONObject) permission).get("resources")) {
						if (((String) resource).equals(resourceName)) {
							if (((JSONObject) permission).containsKey("roles")){
								for (Object role : (JSONArray) ((JSONObject) permission).get("roles")) {
									if (!roles.contains(role)) {
										roles.add(role);
									}
								}
							} else {
								if (((JSONObject) permission).containsKey("default")){
									roles.add("Default");

								}
							}
						}
					}
				}
			}
		}
		}
		return roles;
	}
	
	public static JSONArray getDeleteRoles(JSONArray policy, String entityName) {
		JSONArray roles = new JSONArray();
		for(Object entity : policy) {
			if (((JSONObject) entity).get("entity").equals(entityName)) {
				for (Object permission : (JSONArray) ((JSONObject) entity).get("permissions")) {
					for (Object action : (JSONArray) ((JSONObject) permission).get("actions")) {
						if (((String) action).equals("delete")) {
							if (((JSONObject) permission).containsKey("roles")){
								for (Object role : (JSONArray) ((JSONObject) permission).get("roles")) {
									if (!roles.contains(role)) {
										roles.add(role);
									}
								}
							} else {
								if (((JSONObject) permission).containsKey("default")){
									roles.add("Default");
								}
							}
						}
					}
				}
			}
		}
		return roles;
	}
	
	public static JSONArray getCreateRoles(JSONArray policy, String entityName) {
		JSONArray roles = new JSONArray();
		for(Object entity : policy) {
			if (((JSONObject) entity).get("entity").equals(entityName)) {
				for (Object permission : (JSONArray) ((JSONObject) entity).get("permissions")) {
					for (Object action : (JSONArray) ((JSONObject) permission).get("actions")) {
						if (((String) action).equals("create")) {
							if (((JSONObject) permission).containsKey("roles")){
								for (Object role : (JSONArray) ((JSONObject) permission).get("roles")) {
									if (!roles.contains(role)) {
										roles.add(role);
									}
								}
							} else {
								if (((JSONObject) permission).containsKey("default")){
									roles.add("Default");
								}
							}
						}
					}
				}
			}
		}
		return roles;
	}

	/*
	  public static String getTableNameResource(JSONArray context, String entityName, String resourceName) {
		 String tableName = null;
		  if (isAttribute(context, entityName, resourceName)) {
					tableName = entityName;
				} else {
					String target = getAssociationTarget(context, entityName, resourceName);
					if (target.compareTo(entityName) > 1) {
						  tableName = entityName + "_" + target;
					  } else {
						  tableName = target + "_" + entityName;
					  }
				}
		  return tableName;
	  }
	  */

	  /*
	  public static String getAssociationTarget(JSONArray context, String entityName, String associationEnd) {
		  String target = null;
		  for(Object entity : context) {
			  if (((JSONObject) entity).get("class").equals(entityName)) {
				  for(Object association : (JSONArray) ((JSONObject) entity).get("associations")) {
					  if (((JSONObject) association).get("name").equals(associationEnd)) {
						  target = (String) ((JSONObject) association).get("target");	  
					  }
				  }
			  }
		  }
		  return target;
	  }
	  */
	  
	public static String getAssociation(JSONArray context, String className, String endName) {
		String association = null;
		  for(Object object : context) {
			  if (((JSONObject) object).containsKey("association")) {
				 JSONArray classes = (JSONArray) ((JSONObject) object).get("classes");
				 JSONArray ends = (JSONArray) ((JSONObject) object).get("ends");
					  for (int index_end = 0; index_end < classes.size(); index_end++) {
						  if (classes.get(index_end).equals(className)
								  && ends.get(index_end).equals(endName)) {
							  association = (String) ((JSONObject) object).get("association");
							  break;
						  };
					  }
				  
			  }
		  }

		  return association;
	  }

	  /* getAssociationSource */
	  /* className is the association */
	  /* endName is the assoc end */
	  /* we assume that association+assoc_end is unique */
	  public static String getAssociationSource(JSONArray context, String className, String endName) {
		  String source = null;
		  for(Object object : context) {
				  if (((JSONObject) object).containsKey("association")) {
					  if (((JSONObject) object).get("association").equals(className)) {
					  JSONArray ends = (JSONArray) ((JSONObject) object).get("ends");
					  JSONArray classes = (JSONArray) ((JSONObject) object).get("classes");
					  for (int index_end = 0; index_end < ends.size(); index_end++) {
						   if (ends.get(index_end).equals(endName)) {
								 source = (String) classes.get(index_end);
								 break;
							  };
						  }
					  }
				  }
		  }
		  return source;
	  }
	  
	  /* getAssociationOpposite */
	  /* className is the source */
	  /* endName is the assoc end */
	  /* we assume that source+assoc_end is unique */
	  public static String getAssociationOpposite(JSONArray context, String className, String endName) {
		  String opposite = null;
		  for(Object object : context) {
			  if (((JSONObject) object).containsKey("association")) {
				 JSONArray classes = (JSONArray) ((JSONObject) object).get("classes");
				 JSONArray ends = (JSONArray) ((JSONObject) object).get("ends");
					  for (int index_end = 0; index_end < classes.size(); index_end++) {
						  if (classes.get(index_end).equals(className)
								  && ends.get(index_end).equals(endName)) {

							 if (index_end == 0) {
								  opposite =  (String) ends.get(1);
							  } else {
								  opposite =  (String) ends.get(0);
							  }
							  break;
						  };
					  }
				  
			  }
		  }

		  return opposite;
	  }


	  public static boolean isAttribute(JSONArray context, String entityName, String attribute) {
		  boolean result = false;
		  for(Object entity : context) {
			  if (((JSONObject) entity).containsKey("class")) {
				  if (((JSONObject) entity).get("class").equals(entityName)) {
					  if (((JSONObject) entity).containsKey("attributes")) {
						  for(Object association : (JSONArray) ((JSONObject) entity).get("attributes")) {
							  if (((JSONObject) association).get("name").equals(attribute)) {
								  result = true;
								  break;
							  }
						  }
					  }
				  }
			  }
		  }
		  return result;
	  }
	  
	  public static boolean isAssociationEnd(JSONArray context, String className,  String endName) {
		  boolean result = false;
		  for(Object object : context) {
			  if (((JSONObject) object).containsKey("association")) {
				  if (((JSONObject) object).get("association").equals(className)) {
					  JSONArray ends = (JSONArray) ((JSONObject) object).get("ends");
					  for (int index_end = 0; index_end < ends.size(); index_end++) {
						  if (ends.get(index_end).equals(endName)) {
								  result = true;
								  break;
							  }
					  }
				  }
			  }
		  }

		  return result;
	  }
	  
	  public static boolean isAssociation(JSONArray context, String className,  String endName) {
          boolean result = false;
          for(Object object : context) {
              if (((JSONObject) object).containsKey("association")) {
                  JSONArray ends = (JSONArray) ((JSONObject) object).get("ends");
                  for (int index_end = 0; index_end < ends.size(); index_end++) {
                      if (ends.get(index_end).equals(endName)) {
                              result = true;
                              break;
                          }
                  }
              }
          }

          return result;
      }
	  
	 // super
	  public static String getSuperClass(JSONArray context, String entityName) {
		  //System.out.println(entityName);
		  String superClass = null;
		  for(Object entity : context) {
			  if (((JSONObject) entity).get("class").equals(entityName)) {
				  if (((JSONObject) entity).containsKey("super")) {
					   superClass = (String) ((JSONObject) entity).get("super");
					   break;
				  }
			  }
		  }
		  return superClass;
	  }

	public static String findColumnTableName(JSONArray context, List<String> tables, String columnName) {
		// columnName should be an attribute or and association-end
		String result = null;
		for(String table : tables) {
			String tableName = table;
			if (Utilities.isAttribute(context, tableName, columnName)) {
				result =  tableName;
			} 
			else if (Utilities.isAssociationEnd(context, tableName, columnName)){
				result = tableName;
			}
		}
		return result;
	}
	
	public static boolean isPrimaryKey(JSONArray context, String columnName) {
		boolean result = false;
		for(Object entity : context) {
			if (((JSONObject) entity).containsKey("class")) {
				String entityName = (String) ((JSONObject) entity).get("class");
				if (entityName.concat("_id").equals(columnName)) {
					result = true;
				}
			}
		}
		return result;
	}
	
	// super
	public static boolean isSuperKey(JSONArray context, String columnName) {
		boolean result = false;
		  for(Object entity : context) {
			  if (((JSONObject) entity).containsKey("class")) {
				  if (((JSONObject) entity).containsKey("super")) {
					  String superClass = (String) ((JSONObject) entity).get("super");
					  if (superClass.equals(columnName)) {
					  result = true;
				  }
			  }
		  }
		  }
		  return result;
	  }
		
	  // super
	public static boolean isSuper(JSONArray context, String entityName, String attributeName) {
		boolean result = false;
		for(Object entity : context) {
			if (((JSONObject) entity).get("class").equals(entityName)) {
				if (((JSONObject) entity).containsKey("super")) {
					String superClass = (String) ((JSONObject) entity).get("super");
					if (superClass.equals(attributeName)) {
						result = true;
					}
				}
			}
		}
		return result;
	}
	 
	 public static JSONArray getAuthFunctions(JSONArray context, JSONArray policy) {
		 JSONArray authFunctions = new JSONArray();
			/* resources */
			List<String> actions = new ArrayList<String>();
			actions.add("read");
			actions.add("delete");
			actions.add("create");
			actions.add("update");
			for(String action : actions) {	
				for(Object entityName : Utilities.getEntities(context)) {
					switch (action) {
						case "read" : {
							JSONArray resources = new JSONArray();
							resources.addAll(Utilities.getAttributes(context, (String) entityName));
							for(Object resource : resources) {
								String resourceName = (String) ((JSONObject) resource).get("name");
								// autFunctions
								JSONObject authFun = new JSONObject();
								// key: action
								authFun.put("action", "read");
								// key: entity
								authFun.put("entity", entityName);
								// key: resource
								authFun.put("column", resourceName);
								// key: table
								authFun.put("table", entityName);
										//Utilities.getTableNameResource(context, (String) entityName, resourceName));
								// key : parameters
								JSONArray authFunPars = new JSONArray();
								JSONObject authFunSelf = new JSONObject();
								authFunSelf.put("name", "kself");
								authFunSelf.put("type", "INT");
								authFunPars.add(authFunSelf);
								JSONObject authFunCaller = new JSONObject();
								authFunCaller.put("name", "kcaller");
								authFunCaller.put("type", "INT");
								authFunPars.add(authFunCaller);
								JSONObject authFunRole = new JSONObject();
								authFunRole.put("name", "krole");
								authFunRole.put("type", "VARCHAR(100)");
								authFunPars.add(authFunRole);	
								//authFunPars.addAll(injector.getParameters());
								authFun.put("parameters", authFunPars);
								// key: roles
								JSONArray authRoles = new JSONArray();
								for(Object role : Utilities.getResourceRoles(policy, (String) entityName, resourceName)) {
									authRoles.add((String) role);
								}
								authFun.put("roles", authRoles); 
								//
								authFunctions.add(authFun);
							}
							// association-ends
							JSONArray assocEnds = new JSONArray();
							assocEnds.addAll(Utilities.getAssociations(context, (String) entityName));
							for(Object resource : assocEnds) {
								String resourceName = (String) ((JSONObject) resource).get("name");
								// autFunctions
								JSONObject authFun = new JSONObject();
								// key: action
								authFun.put("action", "read");
								// key: entity
								authFun.put("entity", entityName);
								// key: resource
								authFun.put("column", resourceName);
								// key: table
								authFun.put("table", (String) ((JSONObject) resource).get("association"));
										//Utilities.getTableNameResource(context, (String) entityName, resourceName));
								// key : parameters
								JSONArray authFunPars = new JSONArray();
								JSONObject authFunSelf = new JSONObject();
								authFunSelf.put("name", "kself");
								authFunSelf.put("type", "INT");
								authFunPars.add(authFunSelf);
								JSONObject authFunCaller = new JSONObject();
								authFunCaller.put("name", "kcaller");
								authFunCaller.put("type", "INT");
								authFunPars.add(authFunCaller);
								JSONObject authFunRole = new JSONObject();
								authFunRole.put("name", "krole");
								authFunRole.put("type", "VARCHAR(100)");
								authFunPars.add(authFunRole);	
								//authFunPars.addAll(injector.getParameters());
								authFun.put("parameters", authFunPars);
								// key: roles
								JSONArray authRoles = new JSONArray();
								for(Object role : Utilities.getResourceRoles(policy, (String) entityName, resourceName)) {
									authRoles.add((String) role);
								}
								authFun.put("roles", authRoles); 
								//
								authFunctions.add(authFun);
							}
							break;
						}
						case "update" : {
							JSONArray attributes = new JSONArray();
							attributes.addAll(Utilities.getAttributes(context, (String) entityName));
							for(Object resource : attributes) {
								String resourceName = (String) ((JSONObject) resource).get("name");
								String resourceType = "";
								switch ((String) ((JSONObject) resource).get("type")) {
								case "Integer" : {
									resourceType = "INT";
									break;
								}
								case "String" : {
									resourceType = "VARCHAR(250)";
									break;
								}
								default : {
									break;
								}
								}
								
								// autFunctions
								JSONObject authFun = new JSONObject();
								// key: action
								authFun.put("action", "update");
								// key: entity
								authFun.put("entity", entityName);
								// key: resource
								authFun.put("column", resourceName);
								// key: table
								authFun.put("table", entityName);
										//Utilities.getTableNameResource(context, (String) entityName, resourceName));
								// key : parameters
								JSONArray authFunPars = new JSONArray();
								JSONObject authFunSelf = new JSONObject();
								authFunSelf.put("name", "kself");
								authFunSelf.put("type", "INT");
								authFunPars.add(authFunSelf);
								JSONObject authFunCaller = new JSONObject();
								authFunCaller.put("name", "kcaller");
								authFunCaller.put("type", "INT");
								authFunPars.add(authFunCaller);
								JSONObject authFunRole = new JSONObject();
								authFunRole.put("name", "krole");
								authFunRole.put("type", "VARCHAR(100)");
								authFunPars.add(authFunRole);
								//
								JSONObject authFunValue = new JSONObject();
								authFunValue.put("name", "p" + resourceName);
								authFunValue.put("type", resourceType);
								authFunPars.add(authFunValue);
								//authFunPars.addAll(injector.getParameters());
								authFun.put("parameters", authFunPars);
								// key: roles
								JSONArray authRoles = new JSONArray();
								for(Object role : Utilities.getResourceRoles(policy, (String) entityName, resourceName)) {
									authRoles.add((String) role);
								}
								authFun.put("roles", authRoles); 
								//
								authFunctions.add(authFun);
							};
							// association-ends
							JSONArray associationEnds = new JSONArray();
							associationEnds.addAll(Utilities.getAssociations(context, (String) entityName));
							for(Object resource : associationEnds) {
								String resourceName = (String) ((JSONObject) resource).get("name");
								String resourceType = "INT";							
								// autFunctions
								JSONObject authFun = new JSONObject();
								// key: action
								authFun.put("action", "update");
								// key: entity
								authFun.put("entity", entityName);
								// key: resource
								authFun.put("column", resourceName);
								// key: table
								authFun.put("table", (String) ((JSONObject) resource).get("association"));
										//Utilities.getTableNameResource(context, (String) entityName, resourceName));
								// key : parameters
								JSONArray authFunPars = new JSONArray();
								JSONObject authFunSelf = new JSONObject();
								authFunSelf.put("name", "kself");
								authFunSelf.put("type", "INT");
								authFunPars.add(authFunSelf);
								JSONObject authFunCaller = new JSONObject();
								authFunCaller.put("name", "kcaller");
								authFunCaller.put("type", "INT");
								authFunPars.add(authFunCaller);
								JSONObject authFunRole = new JSONObject();
								authFunRole.put("name", "krole");
								authFunRole.put("type", "VARCHAR(100)");
								authFunPars.add(authFunRole);
								//
								JSONObject authFunValue = new JSONObject();
								authFunValue.put("name", "p" + resourceName);
								authFunValue.put("type", resourceType);
								authFunPars.add(authFunValue);
								//authFunPars.addAll(injector.getParameters());
								authFun.put("parameters", authFunPars);
								// key: roles
								JSONArray authRoles = new JSONArray();
								for(Object role : Utilities.getResourceRoles(policy, (String) entityName, resourceName)) {
									authRoles.add((String) role);
								}
								authFun.put("roles", authRoles); 
								//
								authFunctions.add(authFun);
								
							}
							break;
						}
						case "delete" : {
								// autFunctions
								JSONObject authFun = new JSONObject();
								// key: action
								authFun.put("action", "delete");
								// key: entity
								authFun.put("entity", entityName);
								// key: table
								authFun.put("table", entityName);
								// key : parameters
								JSONArray authFunPars = new JSONArray();
								JSONObject authFunSelf = new JSONObject();
								authFunSelf.put("name", "kself");
								authFunSelf.put("type", "INT");
								authFunPars.add(authFunSelf);
								JSONObject authFunCaller = new JSONObject();
								authFunCaller.put("name", "kcaller");
								authFunCaller.put("type", "INT");
								authFunPars.add(authFunCaller);
								JSONObject authFunRole = new JSONObject();
								authFunRole.put("name", "krole");
								authFunRole.put("type", "VARCHAR(100)");
								authFunPars.add(authFunRole);	
								//authFunPars.addAll(injector.getParameters());
								authFun.put("parameters", authFunPars);
								// key: roles
								JSONArray authRoles = new JSONArray();
								for(Object role : Utilities.getDeleteRoles(policy, (String) entityName)) {
									authRoles.add((String) role);
								}
								authFun.put("roles", authRoles); 
								//
								authFunctions.add(authFun);
							break;
						}
						case "create" : {
							// autFunctions
							JSONObject authFun = new JSONObject();
							// key: action
							authFun.put("action", "create");
							// key: entity
							authFun.put("entity", entityName);
							// key: table
							authFun.put("table", entityName);
							
							// key : parameters
							JSONArray authFunPars = new JSONArray();
							//JSONObject authFunSelf = new JSONObject();
							//authFunSelf.put("name", "kself");
							//authFunSelf.put("type", "INT");
							//authFunPars.add(authFunSelf);
							JSONObject authFunCaller = new JSONObject();
							authFunCaller.put("name", "kcaller");
							authFunCaller.put("type", "INT");
							authFunPars.add(authFunCaller);
							JSONObject authFunRole = new JSONObject();
							authFunRole.put("name", "krole");
							authFunRole.put("type", "VARCHAR(100)");
							authFunPars.add(authFunRole);	
							//authFunPars.addAll(injector.getParameters());
							authFun.put("parameters", authFunPars);
							// key: roles
							JSONArray authRoles = new JSONArray();
							for(Object role : Utilities.getCreateRoles(policy, (String) entityName)) {
								authRoles.add((String) role);
							}
							authFun.put("roles", authRoles); 
							//
							authFunctions.add(authFun);
							
							
							// association-ends
							JSONArray associationEnds = new JSONArray();
							associationEnds.addAll(Utilities.getAssociations(context, (String) entityName));
							for(Object resource : associationEnds) {
							// autFunctions
							
							authFun = new JSONObject();
							// key: action
							authFun.put("action", "create");
							// key: entity
							authFun.put("entity", entityName);
							// key: table
							authFun.put("table", (String) ((JSONObject) resource).get("association"));
							
							// key : parameters
							authFunPars = new JSONArray();
							//JSONObject authFunSelf = new JSONObject();
							//authFunSelf.put("name", "kself");
							//authFunSelf.put("type", "INT");
							//authFunPars.add(authFunSelf);
							authFunCaller = new JSONObject();
							authFunCaller.put("name", "kcaller");
							authFunCaller.put("type", "INT");
							authFunPars.add(authFunCaller);
							authFunRole = new JSONObject();
							authFunRole.put("name", "krole");
							authFunRole.put("type", "VARCHAR(100)");
							authFunPars.add(authFunRole);	
							//authFunPars.addAll(injector.getParameters());
							authFun.put("parameters", authFunPars);
							// key: roles
							authRoles = new JSONArray();
							for(Object role : Utilities.getCreateRoles(policy, (String) entityName)) {
								authRoles.add((String) role);
							}
							authFun.put("roles", authRoles); 
							//
							authFunctions.add(authFun);
								
							}
				

							
							break;
					}
						default : {
							break;
						}


					}

				}
			}   
			return authFunctions;
	 }

	public static String printCheckAuthFun(boolean exit) {
		String trigger = "";
		if (exit) {
			trigger =  
					"SIGNAL SQLSTATE '45000'" + "\n"
							+ "SET MESSAGE_TEXT = 'Unauthorized access';" + "\n";
		};

		String s = "DROP FUNCTION IF EXISTS checkAuth;" + "\n"
		+ "DELIMITER //" + "\n"
		+ "CREATE FUNCTION checkAuth(origWhere INT, authWhere INT)" + "\n"
		+ "RETURNS INT DETERMINISTIC" + "\n"
		+ "BEGIN" + "\n"
		+ "DECLARE result INT DEFAULT 0;" + "\n"
		+ "IF (origWhere = 1 and authWhere = 1)" + "\n"
		+ "THEN RETURN(1);" + "\n"
		+ "ELSE IF (origWhere = 1 and authWhere = 0)" + "\n"
		         + "THEN" + "\n"
		         // add trigger is exit = true
		         + trigger
		         //
		         + "RETURN (0);" + "\n"
		         + "ELSE RETURN (0);" + "\n"
		      + "END IF;" + "\n"
		+ "END IF;" + "\n"
		+ "END //" + "\n"
		+ "DELIMITER ;" + "\n";
		return s;
	}

	public static boolean isParameter(JSONArray parameters, String columnName) {
		boolean result = false;
		for(Object par : parameters) {
			if (((JSONObject) par).containsKey("name")) {
				if (((JSONObject) par).get("name").equals(columnName)) {
					result = true;
				}
			}
		}
		return result;
	}

    public static boolean isClass(JSONArray context, String entityName) {
          boolean result = false;
          for(Object entity : context) {
              if (((JSONObject) entity).containsKey("class")) {
                  if (((JSONObject) entity).get("class").equals(entityName)) {
                      result = true;
                      break;
                  }
              }
          }
          return result;
      }
}



