package org.vgu.sqlsi.main;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.vgu.sqlsi.ocl.expressions.IteratorSource;
import org.vgu.sqlsi.ocl.expressions.OclExpression;
import org.vgu.sqlsi.ocl.expressions.StmVisitor;
//import org.vgu.sqlsi.uml.UMLContextBis;

import net.sf.jsqlparser.statement.Statement;

public class Ocl2Sql implements StmVisitor {
	private int aliasId = 0; 
	private List<IteratorSource> context = new ArrayList<IteratorSource>();
	private JSONArray plainUMLContext;
	

	@Override
	public int getAlias() {
		return aliasId;
	}
	@Override
	public void setAlias(int id) {
		aliasId = id;
		
	}
	@Override
	public void genAlias() {
		aliasId = aliasId + 1;
		
	}
	
	@Override
	public List<IteratorSource> getVisitorContext() {
		return this.context;
	}
	@Override
	public void setVisitorContext(List<IteratorSource> context) {
		this.context = context;
		
	}
	@Override
	public Statement visit(OclExpression source) {
		return  source.accept(this);
	}
	public JSONArray getPlainUMLContext() {
		return plainUMLContext;
	}
	public void setPlainUMLContext(JSONArray plainUMLContext) {
		this.plainUMLContext = plainUMLContext;
	}

	
	
}
