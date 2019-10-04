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

package org.vgu.ocl2psql.ocl.expressions;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.vgu.ocl2psql.sql.statement.select.SubSelect;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Utilities {
    
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
    
    public static String getAssociationOppositeClassName(JSONArray context, String assocName, String className) {
          for(Object object : context) {
              if (((JSONObject) object).containsKey("association") 
                      && ((JSONObject) object).get("association").equals(assocName)) {
                 JSONArray classes = (JSONArray) ((JSONObject) object).get("classes");
                 if(classes.get(0).equals(className))
                     return (String) classes.get(1);
                 else
                     return (String) classes.get(0);
              }
          }
          return null;
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
	
	static public List<SelectItem> getVariableAliases(SubSelect selectBody){
		List<SelectItem> items = new ArrayList<SelectItem>();
		if (selectBody != null) {
			for(SelectItem item : (((PlainSelect) selectBody.getSelectBody()).getSelectItems())) {
				String alias = ((SelectExpressionItem) item).getAlias().getName();
				if (alias.endsWith("_var")){
					items.add(item);

				}

			}
		}
		return items;
	}
	
	static public List<SelectItem> removeVariableAliase(List<SelectItem> items, String alias){
		for(SelectItem var_item : items) {
			
			if ((((SelectExpressionItem) var_item).getAlias().getName()).equals(alias)){
					items.remove(var_item);
					break;
			} 
		}
		return items;
	}
	
	public static void groupingVariables (PlainSelect pselect, String var_iter, SubSelect subselect_Iter, SubSelect subselect_Body) {
		List<SelectItem> vars_Iter = Utilities.getVariableAliases(subselect_Iter);
		List<SelectItem> vars_Body = Utilities.getVariableAliases(subselect_Body);
		if (var_iter != null) {
			Utilities.removeVariableAliase(vars_Iter, var_iter);
			Utilities.removeVariableAliase(vars_Body, var_iter);
		}
		
		List<Expression> gexps = new ArrayList<Expression>();
		
		// 1. add all the variable-item on the Iter
		for(SelectItem item_var : vars_Iter) {
			String var_name = ((SelectExpressionItem) item_var).getAlias().getName().split("_")[0];
			gexps.add(new Column(var_name.concat("_var")));

		}
		// 2. add all the variable-item on the body, which do not appear on the left
		for(SelectItem item_var_body : vars_Body) {
			String var_name_body = ((SelectExpressionItem) item_var_body).getAlias().getName().split("_")[0];
			boolean duplicated = false;
			for(SelectItem item_var_iter : vars_Iter) {
				String var_name_iter = ((SelectExpressionItem) item_var_iter).getAlias().getName().split("_")[0];
				if (var_name_body.equals(var_name_iter)) {
					duplicated = true;
					break;
				}
			}
			if (!duplicated) {
				String var_name = ((SelectExpressionItem) item_var_body).getAlias().getName().split("_")[0];
				gexps.add(new Column(var_name.concat("_var")));
			}
		}
		GroupByElement groupByElemt = new GroupByElement();
		groupByElemt.setGroupByExpressions( gexps );
		pselect.setGroupByElement(groupByElemt);
		
	}
	
	// ripplingUpVariables
	public static void ripplingUpVariablesSimple(PlainSelect pselect, SubSelect selectBody) {
		List<SelectItem> vars_Iter = Utilities.getVariableAliases(selectBody);	
		// 1. add all the variable-item on the Iter
		for(SelectItem item_var : vars_Iter) {
			SelectExpressionItem new_item_var = new SelectExpressionItem();
			String var_name = ((SelectExpressionItem) item_var).getAlias().getName().split("_")[0];
			new_item_var.setExpression(new Column(selectBody.getAlias().getName().concat(".").concat(var_name).concat("_var")));
			new_item_var.setAlias(new Alias(var_name.concat("_var")));
			pselect.addSelectItems(new_item_var);
		}
	}
	
	// ripplingUpVariables is a key function
	// 1. it add selectitems corresponding to outer-nested-variables
	// appearing either in the source or in the body to the pselect
	// 2. it add where-conditions of the form body.x_var = source.x_var,
	// when the same outer-nested-variable x_var
	// appears both in the body and in the source
	// 3. for (1) and (2), the current iterator variables, var_iter, it is not taken into account
	
	public static void ripplingUpVariables(PlainSelect pselect, String var_iter, SubSelect selectBody, SubSelect selectBody2) {
		List<SelectItem> vars_Iter = Utilities.getVariableAliases(selectBody);
		List<SelectItem> vars_Body = Utilities.getVariableAliases(selectBody2);
		if (var_iter != null) {
			Utilities.removeVariableAliase(vars_Iter, var_iter);
			Utilities.removeVariableAliase(vars_Body, var_iter);
		}
		List<Expression> wexps = new ArrayList<Expression>();
		
		// 1. add all the variable-item on the Iter
		for(SelectItem item_var : vars_Iter) {
			SelectExpressionItem new_item_var = new SelectExpressionItem();
			String var_name = ((SelectExpressionItem) item_var).getAlias().getName().split("_")[0];
			new_item_var.setExpression(new Column(selectBody.getAlias().getName().concat(".").concat(var_name).concat("_var")));
			new_item_var.setAlias(new Alias(var_name.concat("_var")));
			pselect.addSelectItems(new_item_var);
		
		}
		// 2. add all the variable-item on the body, which do not appear on the left
		for(SelectItem item_var_body : vars_Body) {
			String var_name_body = ((SelectExpressionItem) item_var_body).getAlias().getName().split("_")[0];
			boolean duplicated = false;
			for(SelectItem item_var_iter : vars_Iter) {
				String var_name_iter = ((SelectExpressionItem) item_var_iter).getAlias().getName().split("_")[0];
				if (var_name_body.equals(var_name_iter)) {
					duplicated = true;
					break;
				}
			}
				// if duplicated, all WHERE!
				if (duplicated) {
					//System.out.println(var_name_body);
					BinaryExpression wexp = new EqualsTo();
					wexp.setLeftExpression(new Column(selectBody.getAlias().getName().concat(".").concat(var_name_body).concat("_var")));
					wexp.setRightExpression(new Column(selectBody2.getAlias().getName().concat(".").concat(var_name_body).concat("_var")));			
					wexps.add(wexp);
				} else {
					SelectExpressionItem new_item_var = new SelectExpressionItem();
					new_item_var.setExpression(new Column(selectBody2.getAlias().getName().concat(".").concat(var_name_body).concat("_var")));
					new_item_var.setAlias(new Alias(var_name_body.concat("_var")));
					pselect.addSelectItems(new_item_var);
				}
			}
		
		Expression where = null;
		int counter = 0;
		for(Expression exp : wexps) {
			if (counter == 0) {
				where = exp;
				} else {
					where = new AndExpression(where, exp);
				};
				counter = counter + 1;
		}
		// create & set AND-WHERE
		if (where != null) {
			if (pselect.getWhere() == null) {
				pselect.setWhere(where);
			} else {
				pselect.setWhere(new AndExpression(pselect.getWhere(), where));
			}
		}


	}

    public static String getAttributeType(JSONArray plainUMLContext, String propertyClass, String propertyName) {
        for(Object entity : plainUMLContext) {
            if (((JSONObject) entity).containsKey("class")) {
                if (((JSONObject) entity).get("class").equals(propertyClass)) {
                    if (((JSONObject) entity).containsKey("attributes")) {
                        for(Object association : (JSONArray) ((JSONObject) entity).get("attributes")) {
                            if (((JSONObject) association).get("name").equals(propertyName)) {
                                return (String) ((JSONObject) association).get("type");
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
	
}
