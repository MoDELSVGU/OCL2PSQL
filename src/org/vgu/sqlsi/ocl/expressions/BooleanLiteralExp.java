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

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
/**
 * Class BooleanLiteralExp
 */
public final class BooleanLiteralExp extends PrimitiveLiteralExp {
    /**
     * Attribute booleanSymbol
     */
    private final boolean booleanSymbol;

    public BooleanLiteralExp(boolean newbooleanSymbol) {
	this.booleanSymbol = newbooleanSymbol;
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	return this.booleanSymbol;
    }

	@Override
	public Statement accept(StmVisitor visitor) {
		PlainSelect pselect = new PlainSelect();
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(new LongValue(((this.booleanSymbol) == true)? 1 : 0));
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);	
		
		Select select = new Select();
		select.setSelectBody(pselect);
		return select;
	}

	@Override
	public Statement map(StmVisitor visitor) {
		MyPlainSelect finalPlainSelect = new MyPlainSelect();
		ResSelectExpression resExpression = new ResSelectExpression(new LongValue(((this.booleanSymbol) == true)? "TRUE" : "FALSE"));
		finalPlainSelect.setRes(resExpression);
		Select finalSelect = new Select();
		finalSelect.setSelectBody(finalPlainSelect);
		return finalSelect;
	}

    public boolean isBooleanSymbol() {
        return booleanSymbol;
    }

}
