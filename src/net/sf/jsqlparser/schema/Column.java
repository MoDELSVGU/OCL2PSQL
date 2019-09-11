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
package net.sf.jsqlparser.schema;

import java.util.ArrayList;
import java.util.List;

import org.vgu.sqlsi.main.InjectorExpression;
import org.vgu.sqlsi.main.Utilities;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

/**
 * A column. It can have the table name it belongs to.
 */
public final class Column extends ASTNodeAccessImpl implements Expression, MultiPartName {

    private Table table;
    private String columnName;

    public Column() {
    }

    public Column(Table table, String columnName) {
        setTable(table);
        setColumnName(columnName);
    }

    public Column(String columnName) {
        this(null, columnName);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String string) {
        columnName = string;
    }

    @Override
    public String getFullyQualifiedName() {
        return getName(false);
    }

    /**
     * Get name with out without using aliases.
     *
     * @param aliases
     * @return
     */
    public String getName(boolean aliases) {
        StringBuilder fqn = new StringBuilder();

        if (table != null) {
            if (table.getAlias() != null && aliases) {
                fqn.append(table.getAlias().getName());
            } else {
                fqn.append(table.getFullyQualifiedName());
            }
        }
        if (fqn.length() > 0) {
            fqn.append('.');
        }
        if (columnName != null) {
            fqn.append(columnName);
        }
        return fqn.toString();
    }

    @Override
    public void accept(InjectorExpression injectorExpression) {
        //expressionVisitor.visit(this);
    	
    	
		String columnName = this.getColumnName();
		if (Utilities.isPrimaryKey(injectorExpression.getContext(), columnName)) {
			// do nothing, because primary key is HIDDEN
			// from the eyes of the security modeler
			injectorExpression.setResult(new ArrayList<Expression>());
			
		} else if (Utilities.isSuperKey(injectorExpression.getContext(), columnName)){
			// do nothing, because primary key is HIDDEN
			// from the eyes of the security modeler
			injectorExpression.setResult(new ArrayList<Expression>());
			
		} else if (Utilities.isParameter(injectorExpression.getParameters(), columnName)) {
			// do nothing, because primary key is HIDDEN
			// from the eyes of the security modeler
			injectorExpression.setResult(new ArrayList<Expression>());
		}
		
		else {
			// it is either and attribute or an association-end
			// of one of the tables in table
		//SelectExpressionItem siItem = new SelectExpressionItem();
			String columnTableName = 
					Utilities.findColumnTableName(
							injectorExpression.getContext(), 
							injectorExpression.getTables(), columnName);

		//CaseExpression siCase = new CaseExpression();
		Function siFunction = new Function();
		
		siFunction.setName("auth"+ "_" + injectorExpression.getAction() + "_" 
				+ columnTableName + "_" + columnName);

		// parameters
		ArrayList<Expression> pars = new ArrayList<Expression>(); 
		// a. self
		Column selfPar = new Column();
		if (Utilities.isAttribute(injectorExpression.getContext(), 
				columnTableName, columnName)) {
			selfPar.setColumnName(columnTableName + "_" + "id");
		} else if (Utilities.isAssociationEnd(injectorExpression.getContext(), 
				columnTableName, columnName)) {
			String sourceAssoc = Utilities.getAssociationSource(
					injectorExpression.getContext(), columnTableName, columnName);
			String oppositeAssoc = 
					Utilities.getAssociationOpposite(
							injectorExpression.getContext(), sourceAssoc, columnName);

			selfPar.setColumnName(columnTableName + "." + oppositeAssoc);
			
			
		} else {
			System.out.println("MISSING case column" + ":" + columnTableName + ":" + columnName);
			
		}
		if (selfPar.columnName != null) {
		pars.add(selfPar);
		// b. caller
		Column callerPar = new Column();
		callerPar.setColumnName("kcaller");
		pars.add(callerPar);
		// c. caller
		Column rolePar = new Column();
		rolePar.setColumnName("krole");
		pars.add(rolePar);
		// d. query parameters
		ExpressionList siPars = new ExpressionList();
		siPars.setExpressions(pars);
		siFunction.setParameters(siPars);
		
		//siCase.setSwitchExpression(siFunction);

		// when
		//List<WhenClause> whenList = new ArrayList<WhenClause>();
		//WhenClause when = new WhenClause();
		//when.setWhenExpression(new LongValue(1));
		//when.setThenExpression(new Column(columnName));
		//whenList.add(when);
		//siCase.setWhenClauses(whenList);
		List<Expression> siList = new ArrayList<Expression>();
		siList.add(siFunction);
		injectorExpression.setResult(siList);
		} else {
			injectorExpression.setResult(new ArrayList<Expression>());
		}
		}
		
	
    }

    @Override
    public String toString() {
        return getName(true);
    }

	@Override
	public void accept(ExpressionVisitor expressionVisitor) {
		// TODO Auto-generated method stub
		
	}
}
