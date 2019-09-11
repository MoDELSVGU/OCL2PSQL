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

import java.util.List;

import org.vgu.sqlsi.main.InjectorExpression;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * CASE/WHEN expression.
 *
 * Syntax:  <code><pre>
 * CASE
 * WHEN condition THEN expression
 * [WHEN condition THEN expression]...
 * [ELSE expression]
 * END
 * </pre></code>
 *
 * <br/>
 * or <br/>
 * <br/>
 *
 * <code><pre>
 * CASE expression
 * WHEN condition THEN expression
 * [WHEN condition THEN expression]...
 * [ELSE expression]
 * END
 * </pre></code>
 *
 * See also: https://aurora.vcu.edu/db2help/db2s0/frame3.htm#casexp
 * http://sybooks.sybase.com/onlinebooks/group-as/asg1251e /commands/
 *
 * @ebt-link;pt=5954?target=%25N%15_52628_START_RESTART_N%25
 *
 *
 * @author Havard Rast Blok
 */
public class CaseExpression extends ASTNodeAccessImpl implements Expression {

    private Expression switchExpression;
    private List<WhenClause> whenClauses;
    private Expression elseExpression;

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    /**
     * @return Returns the switchExpression.
     */
    public Expression getSwitchExpression() {
        return switchExpression;
    }

    /**
     * @param switchExpression The switchExpression to set.
     */
    public void setSwitchExpression(Expression switchExpression) {
        this.switchExpression = switchExpression;
    }

    /**
     * @return Returns the elseExpression.
     */
    public Expression getElseExpression() {
        return elseExpression;
    }

    /**
     * @param elseExpression The elseExpression to set.
     */
    public void setElseExpression(Expression elseExpression) {
        this.elseExpression = elseExpression;
    }

    /**
     * @return Returns the whenClauses.
     */
    public List<WhenClause> getWhenClauses() {
        return whenClauses;
    }

    /**
     * @param whenClauses The whenClauses to set.
     */
    public void setWhenClauses(List<WhenClause> whenClauses) {
        this.whenClauses = whenClauses;
    }

    @Override
    public String toString() {
        return "CASE " + ((switchExpression != null) ? switchExpression + " " : "")
                + PlainSelect.getStringList(whenClauses, false, false) + " "
                + ((elseExpression != null) ? "ELSE " + elseExpression + " " : "") + "END";
    }

	@Override
	public void accept(InjectorExpression injectorExpression) {
		//System.out.println("CASE " + this);
		injectorExpression.setResult(this);
		List<WhenClause> whenClauses = this.getWhenClauses();
		for(WhenClause whenClause : whenClauses) {
			InjectorExpression recInjectorExpression = new InjectorExpression();
			recInjectorExpression.setContext(injectorExpression.getContext());
			recInjectorExpression.setAction(injectorExpression.getAction());
			recInjectorExpression.setTables(injectorExpression.getTables());
			whenClause.getWhenExpression().accept(recInjectorExpression);
			whenClause.setWhenExpression(recInjectorExpression.getResult());
			
			InjectorExpression recThenInjectorExpression = new InjectorExpression();
			recThenInjectorExpression.setContext(injectorExpression.getContext());
			recThenInjectorExpression.setAction(injectorExpression.getAction());
			recThenInjectorExpression.setTables(injectorExpression.getTables());
			whenClause.getThenExpression().accept(recThenInjectorExpression);
			whenClause.setThenExpression(recThenInjectorExpression.getResult());
			
		}
		InjectorExpression recElseInjectorExpression = new InjectorExpression();
		recElseInjectorExpression.setContext(injectorExpression.getContext());
		recElseInjectorExpression.setAction(injectorExpression.getAction());
		recElseInjectorExpression.setTables(injectorExpression.getTables());
		this.getElseExpression().accept(recElseInjectorExpression);
		this.setElseExpression(recElseInjectorExpression.getResult());
		
		//System.out.print(this.getSwitchExpression());
		injectorExpression.setResult(this);
		
	}
}
