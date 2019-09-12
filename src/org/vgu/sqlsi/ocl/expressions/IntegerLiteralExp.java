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
 * Class IntegerLiteralExp
 */
public class IntegerLiteralExp extends NumericLiteralExp {
    /**
     * Attribute integerSymbol
     */
    private final int integerSymbol;

    public IntegerLiteralExp(int newintegerSymbol) {
	this.integerSymbol = newintegerSymbol;
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	return this.integerSymbol;
    }

	@Override
	public Statement accept(StmVisitor visitor) {
		PlainSelect pselect = new PlainSelect();
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(new LongValue(this.integerSymbol));
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);	
		
		Select select = new Select();
		select.setSelectBody(pselect);
		return select;
	
	}

	@Override
	public Statement map(StmVisitor visitor) {
	    MyPlainSelect finalPlainSelect = new MyPlainSelect();
        ResSelectExpression resExpression = new ResSelectExpression(new LongValue(this.integerSymbol));
        finalPlainSelect.setRes(resExpression);
        
        Select finalSelect = new Select();
        finalSelect.setSelectBody( finalPlainSelect );
        return finalSelect;
	}

    public int getIntegerSymbol() {
        return integerSymbol;
    }

	
}
