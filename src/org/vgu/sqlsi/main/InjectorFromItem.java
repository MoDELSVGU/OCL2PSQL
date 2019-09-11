package org.vgu.sqlsi.main;

import org.json.simple.JSONArray;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.ParenthesisFromItem;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;

public class InjectorFromItem implements FromItemVisitor{

	private JSONArray context;
	private JSONArray parameters;
	private String action;
	private FromItem result;
	
	public JSONArray getContext() {
		return context;
	}

	public void setContext(JSONArray context) {
		this.context = context;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public FromItem getResult() {
		return result;
	}

	public void setResult(FromItem result) {
		this.result = result;
	}

	public JSONArray getParameters() {
		return parameters;
	}

	public void setParameters(JSONArray parameters) {
		this.parameters = parameters;
	}

	@Override
	public void visit(Table tableName) {
		tableName.accept(this);
		
	}

	@Override
	public void visit(SubSelect subSelect) {
		subSelect.accept(this);
		
	}

	@Override
	public void visit(SubJoin subjoin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LateralSubSelect lateralSubSelect) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ValuesList valuesList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TableFunction tableFunction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ParenthesisFromItem aThis) {
		// TODO Auto-generated method stub
		
	}

}
