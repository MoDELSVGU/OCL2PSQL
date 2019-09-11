package org.vgu.sqlsi.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import org.vgu.sqlsi.uml.UMLContext;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

public class Injector implements SelectVisitor {
	
	private JSONArray context;
	private JSONArray policy;
	private String action;
	private JSONArray parameters;
	private SelectBody result;
	private JSONArray authFunctions = new JSONArray();
	

	public JSONArray getParameters() {
		return parameters;
	}

	public void setParameters(JSONArray parameters) {
		this.parameters = parameters;
	}

	
	public SelectBody getResult() {
		return result;
	}

	public void setResult(SelectBody result) {
		this.result = result;
	}


	public JSONArray getAuthFunctions() {
		return authFunctions;
	}

	public void setAuthFunctions(JSONArray authFunctions) {
		this.authFunctions = authFunctions;
	}

	public JSONArray getContext() {
		return context;
	}

	public void setContext(JSONArray context) {
		this.context = context;
	}

	public JSONArray getPolicy() {
		return policy;
	}

	public void setPolicy(JSONArray policy) {
		this.policy = policy;
	}

	/*
	public JSONArray getAuthRoles(String entity, String resource, String action) {
		JSONArray roles = new JSONArray();
		JSONArray policy = this.getPolicy();
		for(int i=0; i < policy.size(); i++) {
			JSONObject entityPolicy = (JSONObject) policy.get(i);

			if(entityPolicy.get("entity").equals(entity)) {
				// entity
				JSONArray permissions = (JSONArray) entityPolicy.get("permissions");
				for(int j = 0; j < permissions.size(); j++) {
					JSONObject permission = (JSONObject) permissions.get(j);
					for(Object actionPer : (JSONArray) permission.get("actions")) {
						if (((String) actionPer).equals(action)) {
							JSONArray resources = (JSONArray) permission.get("resources");
							// resource
							for(int h = 0; h < resources.size(); h++) {
								if(resources.get(h).equals(resource)) {
									roles.addAll((JSONArray) permission.get("roles"));
								}
							}
							}
					}	
				}
			}	
		}
		return roles;
	}
*/
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	@Override
	public void visit(PlainSelect plainSelect) {
		plainSelect.accept(this);
		
	}

	@Override
	public void visit(SetOperationList setOpList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WithItem withItem) {
		// TODO Auto-generated method stub
		
	}
}
