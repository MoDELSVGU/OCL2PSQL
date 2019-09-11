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
package net.sf.jsqlparser.statement.update;

import java.util.ArrayList;
import java.util.List;

import org.vgu.sqlsi.main.InjectorStatement;

import net.sf.jsqlparser.expression.BinaryExpression;
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
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;

/**
 * The update statement.
 */
public class Update implements Statement {

    private List<Table> tables;
    private Expression where;
    private List<Column> columns;
    private List<Expression> expressions;
    private FromItem fromItem;
    private List<Join> joins;
    private Select select;
    private boolean useColumnsBrackets = true;
    private boolean useSelect = false;
    private List<OrderByElement> orderByElements;
    private Limit limit;
    private boolean returningAllColumns = false;
    private List<SelectExpressionItem> returningExpressionList = null;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public List<Table> getTables() {
        return tables;
    }

    public Expression getWhere() {
        return where;
    }

    public void setTables(List<Table> list) {
        tables = list;
    }

    public void setWhere(Expression expression) {
        where = expression;
    }

    /**
     * The {@link net.sf.jsqlparser.schema.Column}s in this update (as col1 and col2 in UPDATE
     * col1='a', col2='b')
     *
     * @return a list of {@link net.sf.jsqlparser.schema.Column}s
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * The {@link Expression}s in this update (as 'a' and 'b' in UPDATE col1='a', col2='b')
     *
     * @return a list of {@link Expression}s
     */
    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setColumns(List<Column> list) {
        columns = list;
    }

    public void setExpressions(List<Expression> list) {
        expressions = list;
    }

    public FromItem getFromItem() {
        return fromItem;
    }

    public void setFromItem(FromItem fromItem) {
        this.fromItem = fromItem;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public boolean isUseColumnsBrackets() {
        return useColumnsBrackets;
    }

    public void setUseColumnsBrackets(boolean useColumnsBrackets) {
        this.useColumnsBrackets = useColumnsBrackets;
    }

    public boolean isUseSelect() {
        return useSelect;
    }

    public void setUseSelect(boolean useSelect) {
        this.useSelect = useSelect;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public List<OrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public Limit getLimit() {
        return limit;
    }

    public boolean isReturningAllColumns() {
        return returningAllColumns;
    }

    public void setReturningAllColumns(boolean returningAllColumns) {
        this.returningAllColumns = returningAllColumns;
    }

    public List<SelectExpressionItem> getReturningExpressionList() {
        return returningExpressionList;
    }

    public void setReturningExpressionList(List<SelectExpressionItem> returningExpressionList) {
        this.returningExpressionList = returningExpressionList;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("UPDATE ");
        b.append(PlainSelect.getStringList(getTables(), true, false)).append(" SET ");

        if (!useSelect) {
            for (int i = 0; i < getColumns().size(); i++) {
                if (i != 0) {
                    b.append(", ");
                }
                b.append(columns.get(i)).append(" = ");
                b.append(expressions.get(i));
            }
        } else {
            if (useColumnsBrackets) {
                b.append("(");
            }
            for (int i = 0; i < getColumns().size(); i++) {
                if (i != 0) {
                    b.append(", ");
                }
                b.append(columns.get(i));
            }
            if (useColumnsBrackets) {
                b.append(")");
            }
            b.append(" = ");
            b.append("(").append(select).append(")");
        }

        if (fromItem != null) {
            b.append(" FROM ").append(fromItem);
            if (joins != null) {
                for (Join join : joins) {
                    if (join.isSimple()) {
                        b.append(", ").append(join);
                    } else {
                        b.append(" ").append(join);
                    }
                }
            }
        }

        if (where != null) {
            b.append(" WHERE ");
            b.append(where);
        }
        if (orderByElements != null) {
            b.append(PlainSelect.orderByToString(orderByElements));
        }
        if (limit != null) {
            b.append(limit);
        }

        if (isReturningAllColumns()) {
            b.append(" RETURNING *");
        } else if (getReturningExpressionList() != null) {
            b.append(" RETURNING ").append(PlainSelect.
                    getStringList(getReturningExpressionList(), true, false));
        }

        return b.toString();
    }

	@Override
	public void accept(InjectorStatement injectorStatement) {
		Update update = new Update();
		if (update.getFromItem() != null) {
			System.out.println("Invalid input: currently, update statements cannot contain a fromItem");
		}
		List<Table> tables = this.getTables();
		if (tables.size() > 1) {
			System.out.println("Invalid input: currently, update statements can only refer to one table");
		}
		List<Expression> expressions = this.getExpressions();
		List<Column> columns = this.getColumns();
		Expression origWhere = this.getWhere();
		
		
    	// build the new-update expression
    	
    	update.setTables(tables);
    	update.setColumns(columns);
    	update.setExpressions(expressions);
    
		// adding auth check
    	List<Expression> authChecks = new ArrayList<Expression>();
    	int countColumn = 0;
    	for(Column column : columns) {
    		ExpressionList list = new ExpressionList();
        	List<Expression> exps = new ArrayList<Expression>();
    		Function function = new Function();
        	function.setName("auth_update_" + tables.get(0).getName() + "_" + column.getColumnName());
        	exps.add(new Column(tables.get(0).getName() + "_id"));
        	exps.add(new Column("kcaller"));
        	exps.add(new Column("krole"));	
        	// value for the update
        	exps.add(expressions.get(countColumn));	
        	countColumn = countColumn + 1;
        	//
        	list.setExpressions(exps);
        	function.setParameters(list);
        	BinaryExpression authCheck = new EqualsTo();
        	authCheck.setLeftExpression(function);
        	authCheck.setRightExpression(new LongValue(1));
        	authChecks.add(authCheck);
    	}
    	
    	//
    	Function checkAuthFun = new Function();
		checkAuthFun.setName("checkAuth");
		ExpressionList pars = new ExpressionList();
		List<Expression> parsList = new ArrayList<Expression>();

    	if (this.getWhere() != null) {
			parsList.add(this.getWhere());
		} else {
			parsList.add(new LongValue(1));
		};
		if (authChecks.size() > 0) {
			MultiAndExpression where = new MultiAndExpression(authChecks);
			parsList.add(where);
		} else {
			parsList.add(new LongValue(1));
		}
		pars.setExpressions(parsList);	
		checkAuthFun.setParameters(pars);
		update.setWhere(checkAuthFun);
	
  		injectorStatement.setResult(update);
	
	}
}
