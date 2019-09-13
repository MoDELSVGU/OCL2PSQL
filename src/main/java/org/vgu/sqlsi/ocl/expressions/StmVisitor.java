package org.vgu.sqlsi.ocl.expressions;

import java.util.List;

import org.json.simple.JSONArray;

import net.sf.jsqlparser.statement.Statement;


public interface StmVisitor {
	public int getAlias();
	public void setAlias(int id);
	public void genAlias();
	public JSONArray getPlainUMLContext();
	public List<IteratorSource> getVisitorContext();
	public void setVisitorContext(List<IteratorSource> context);
	
	public Statement visit(OclExpression source);
}
	
	