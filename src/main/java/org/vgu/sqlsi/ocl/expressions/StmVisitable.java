package org.vgu.sqlsi.ocl.expressions;

import net.sf.jsqlparser.statement.Statement;

public interface StmVisitable {
	//This is for the latest mapping version OCL-to-SQL
	public Statement map(StmVisitor visitor);  
}

