package org.vgu.ocl2psql.ocl.roberts.expressions;

import org.vgu.ocl2psql.sql.statement.select.Select;

public class IteratorSource {
	private Variable iterator;
	private Select source;
	private String alias;
	
	public Variable getIterator() {
		return iterator;
	}
	public void setIterator(Variable iterator) {
		this.iterator = iterator;
	}
	public Select getSource() {
		return source;
	}
	public void setSource(Select statement) {
		this.source = statement;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

}
