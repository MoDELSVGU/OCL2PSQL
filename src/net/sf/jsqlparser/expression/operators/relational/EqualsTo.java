/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2013 JSQLParser
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 2.1 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package net.sf.jsqlparser.expression.operators.relational;

import java.util.ArrayList;
import java.util.List;

import org.vgu.sqlsi.main.InjectorExpression;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class EqualsTo extends ComparisonOperator {

    public EqualsTo() {
        super("=");
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }
    
    @Override
    public void accept(InjectorExpression injectorExpression) {
    	List<Expression> list = new ArrayList<Expression>();
    	
    	InjectorExpression leftInjectorExpression = new InjectorExpression();
		leftInjectorExpression.setContext(injectorExpression.getContext());
		leftInjectorExpression.setAction(injectorExpression.getAction());
		leftInjectorExpression.setParameters(injectorExpression.getParameters());
		leftInjectorExpression.setTables(injectorExpression.getTables());
		
    	this.getLeftExpression().accept(leftInjectorExpression);
    	list.addAll(leftInjectorExpression.getResult());
    	
    	InjectorExpression rightInjectorExpression = new InjectorExpression();
		rightInjectorExpression.setContext(injectorExpression.getContext());
		rightInjectorExpression.setAction(injectorExpression.getAction());
		rightInjectorExpression.setParameters(injectorExpression.getParameters());
		rightInjectorExpression.setTables(injectorExpression.getTables());
		
    	this.getRightExpression().accept(rightInjectorExpression);
    	list.addAll(rightInjectorExpression.getResult());
    	
    	injectorExpression.setResult(list);
    }
}
