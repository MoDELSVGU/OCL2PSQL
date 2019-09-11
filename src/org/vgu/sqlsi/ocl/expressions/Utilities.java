package org.vgu.sqlsi.ocl.expressions;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

public class Utilities {
	
	static public String genAliasName(StmVisitor visitor) {
		String new_alias = "TEMP_".concat(String.valueOf(visitor.getAlias()));
		visitor.setAlias(visitor.getAlias() + 1);
		return new_alias;
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
		pselect.setGroupByColumnReferences(gexps);
		
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
	
}
