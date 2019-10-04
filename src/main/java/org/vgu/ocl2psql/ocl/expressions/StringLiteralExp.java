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
import org.vgu.ocl2psql.ocl.deparser.OclExpressionDeParser;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.TypeSelectExpression;

import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.statement.Statement;

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
	    PlainSelect finalPlainSelect = new PlainSelect();
	    OclExpressionDeParser oclExpressionDeParser = new OclExpressionDeParser();
	    this.accept(oclExpressionDeParser);
	    finalPlainSelect.setCorrespondOCLExpression(oclExpressionDeParser.getDeParsedStr());
        ResSelectExpression resExpression = new ResSelectExpression(new StringValue(this.stringSymbol));
        finalPlainSelect.setRes(resExpression);
        finalPlainSelect.setType(new TypeSelectExpression("String"));
        
        Select finalSelect = new Select();
        finalSelect.setSelectBody(finalPlainSelect);
        return finalSelect;
	}

    public String getStringSymbol() {
        return stringSymbol;
    }

	
}
