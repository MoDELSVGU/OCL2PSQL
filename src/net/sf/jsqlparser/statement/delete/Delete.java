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
package net.sf.jsqlparser.statement.delete;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;

import java.util.ArrayList;
import java.util.List;

import org.vgu.sqlsi.main.Injector;
import org.vgu.sqlsi.main.InjectorExpression;
import org.vgu.sqlsi.main.InjectorFromItem;
import org.vgu.sqlsi.main.InjectorStatement;

public class Delete implements Statement {

    private Table table;
    private List<Table> tables;
    private List<Join> joins;
    private Expression where;
    private Limit limit;
    private List<OrderByElement> orderByElements;

    public List<OrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getTable() {
        return table;
    }

    public Expression getWhere() {
        return where;
    }

    public void setTable(Table name) {
        table = name;
    }

    public void setWhere(Expression expression) {
        where = expression;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("DELETE");

        if (tables != null && tables.size() > 0) {
            b.append(" ");
            for (Table t : tables) {
                b.append(t.toString());
            }
        }

        b.append(" FROM ");
        b.append(table);

        if (joins != null) {
            for (Join join : joins) {
                if (join.isSimple()) {
                    b.append(", ").append(join);
                } else {
                    b.append(" ").append(join);
                }
            }
        }

        if (where != null) {
            b.append(" WHERE ").append(where);
        }

        if (orderByElements != null) {
            b.append(PlainSelect.orderByToString(orderByElements));
        }

        if (limit != null) {
            b.append(limit);
        }
        return b.toString();
    }

    @Override
    public void accept(InjectorStatement injectorStatement) {
    	Delete delete = new Delete();
    	Table table = this.getTable();
    	Expression origWhere = this.getWhere();
    	// build the new-delete expression
    	delete.setTable(table);
    
		// adding auth check
    	Function function = new Function();
    	function.setName("auth_delete_" + table.getName());
    	ExpressionList list = new ExpressionList();
    	List<Expression> exps = new ArrayList<Expression>();
    	exps.add(new Column(table.getName() + "_id"));
    	exps.add(new Column("kcaller"));
    	exps.add(new Column("krole"));
    	list.setExpressions(exps);
    	function.setParameters(list);
		
    	BinaryExpression authCheck = new EqualsTo();
    	authCheck.setLeftExpression(function);
    	authCheck.setRightExpression(new LongValue(1));
    	
    	Expression andAuthChecks = new LongValue(1);
    	if (origWhere != null) {
    		andAuthChecks = origWhere;
    	} 
    	
    	//
		Function checkAuthFun = new Function();
		checkAuthFun.setName("checkAuth");
		ExpressionList pars = new ExpressionList();
		List<Expression> parsList = new ArrayList<Expression>();

		checkAuthFun.setParameters(pars);
		if (this.getWhere() != null) {
			parsList.add(this.getWhere());
		} else {
			parsList.add(new LongValue(1));
		}
		
		parsList.add(function);
		pars.setExpressions(parsList);
		checkAuthFun.setParameters(pars);
		
    	
    	delete.setWhere(checkAuthFun);
		
  		injectorStatement.setResult(delete);
		
	
	}
}
