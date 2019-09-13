package org.vgu.sqlsi.ocl.expressions;

import net.sf.jsqlparser.statement.select.Select;

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
