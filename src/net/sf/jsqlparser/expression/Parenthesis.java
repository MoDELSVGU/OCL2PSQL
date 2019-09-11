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
package net.sf.jsqlparser.expression;

import org.vgu.sqlsi.main.InjectorExpression;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

/**
 * It represents an expression like "(" expression ")"
 */
public class Parenthesis extends ASTNodeAccessImpl implements Expression {

    private Expression expression;
    
    private boolean not = false;

    public Parenthesis() {
    }

    public Parenthesis(Expression expression) {
        setExpression(expression);
    }

    public Expression getExpression() {
        return expression;
    }

    public final void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public void setNot() {
        not = true;
    }

    public boolean isNot() {
        return not;
    }

    @Override
    public String toString() {
        return (not ? "NOT " : "") + "(" + expression + ")";
    }

	@Override
	public void accept(InjectorExpression injectorExpression) {
		// TODO Auto-generated method stub
		//System.out.println("HERE" + this);
		InjectorExpression recInjectorExpression = new InjectorExpression();
		recInjectorExpression.setContext(injectorExpression.getContext());
		recInjectorExpression.setAction(injectorExpression.getAction());
		recInjectorExpression.setTables(injectorExpression.getTables());
		//this.getExpression().accept(recInjectorExpression);
		//Parenthesis parenthesis = new Parenthesis();
		//parenthesis.setExpression(recInjectorExpression.getResult());
		this.getExpression().accept(recInjectorExpression);
		injectorExpression.setResult(this);
		
		
	}
}
