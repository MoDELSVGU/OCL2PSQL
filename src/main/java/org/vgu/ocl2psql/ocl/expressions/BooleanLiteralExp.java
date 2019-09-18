/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.deparser.DeparserVisitor;
import org.vgu.ocl2psql.ocl.deparser.OclExpressionDeParser;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.statement.Statement;
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
    public void accept(DeparserVisitor visitor) {
        visitor.visit( this );
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	return this.booleanSymbol;
    }

	@Override
	public Statement map(StmVisitor visitor) {
		PlainSelect finalPlainSelect = new PlainSelect();
		OclExpressionDeParser oclExpressionDeParser = new OclExpressionDeParser();
		this.accept(oclExpressionDeParser);
		finalPlainSelect.setCorrespondOCLExpression(oclExpressionDeParser.getDeParsedStr());
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
