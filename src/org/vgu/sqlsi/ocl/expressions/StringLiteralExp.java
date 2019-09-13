/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.sqlsi.ocl.expressions;

import org.vgu.sqlsi.ocl.context.OclContext;
import org.vgu.sqlsi.ocl.exception.OclEvaluationException;
import org.vgu.sqlsi.sql.statement.select.MyPlainSelect;
import org.vgu.sqlsi.sql.statement.select.ResSelectExpression;

import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/**
 * Class StringLiteralExp
 */
public class StringLiteralExp extends PrimitiveLiteralExp {
    /**
     * Attribute stringSymbol
     */
    private final String stringSymbol;

    public StringLiteralExp(String newstringSymbol) {
	this.stringSymbol = newstringSymbol;
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	return this.stringSymbol;
    }

	@Override
	public Statement map(StmVisitor visitor) {
	    MyPlainSelect finalPlainSelect = new MyPlainSelect();
        ResSelectExpression resExpression = new ResSelectExpression(new StringValue(this.stringSymbol));
        finalPlainSelect.setRes(resExpression);
        
        Select finalSelect = new Select();
        finalSelect.setSelectBody(finalPlainSelect);
        return finalSelect;
	}

    public String getStringSymbol() {
        return stringSymbol;
    }

	
}
