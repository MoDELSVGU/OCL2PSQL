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

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * Class VariableExp
 */
public class VariableExp extends OclExpression {
    /**
     * Attribute referredVariable
     */
    private final Variable referredVariable;

    public VariableExp(Variable referredVariable) {
	this.referredVariable = referredVariable;
    }

    @Override
    public Object eval(OclContext context) {
	return context.getVariable(referredVariable.getName());
    }

	public Variable getReferredVariable() {
		return referredVariable;
	}

	@Override
	public Statement accept(StmVisitor visitor) {
		PlainSelect pselect = new PlainSelect();
		String var_name = this.getReferredVariable().getName();
		// if the variable is an iterator-variable, save the
		// corresponding iteratore-source in iter
		IteratorSource iter = null;
		for(IteratorSource iter_item : visitor.getVisitorContext()) {
			if(iter_item.getIterator().getName().equals(var_name)) {
				iter = iter_item;
				break;
			}
		}
		// if is an iterationVariable ...
		// otherwise, ...
		if (iter != null) {
			SubSelect subselect_Iter = new SubSelect();
			subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
			Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
			subselect_Iter.setAlias(alias_Iter);
		
			SelectExpressionItem item = new SelectExpressionItem();
			item.setExpression(new Column(alias_Iter.getName().concat(".").concat("item")));
			item.setAlias(new Alias("item"));
			pselect.addSelectItems(item);
			
			SelectExpressionItem item_var = new SelectExpressionItem();
			item_var.setExpression(new Column(alias_Iter.getName().concat(".").concat("item")));
			item_var.setAlias(new Alias(this.getReferredVariable().getName().concat("_var")));
			pselect.addSelectItems(item_var);
			
			// FROM ITEM 
			pselect.setFromItem(subselect_Iter);
			
			// variables rippling up
			Utilities.ripplingUpVariables(pselect, null, subselect_Iter, null);
			
		} 
		else {
			//System.out.println("HERE!!!");
			SelectExpressionItem item = new SelectExpressionItem();
			item.setExpression(new Column(var_name));
			item.setAlias(new Alias("item"));
			pselect.addSelectItems(item);			
		}	
		///
		
		Select select = new Select();
		select.setSelectBody(pselect);
		
		return select;	
	}

    @Override
    public Statement map(StmVisitor visitor) {
//        PlainSelect pselect = new PlainSelect();
        String var_name = this.getReferredVariable().getName();
        // if the variable is an iterator-variable, save the
        // corresponding iteratore-source in iter
        IteratorSource iter = null;
        for(IteratorSource iter_item : visitor.getVisitorContext()) {
            if(iter_item.getIterator().getName().equals(var_name)) {
                iter = iter_item;
                break;
            }
        }
        // if is an iterationVariable ...
        // otherwise, ...
//        if (iter != null) {
            return iter.getSource();
//        } 
//        else {
//            //System.out.println("HERE!!!");
//            SelectExpressionItem item = new SelectExpressionItem();
//            item.setExpression(new Column(var_name));
//            item.setAlias(new Alias("item"));
//            pselect.addSelectItems(item);           
//        }   
//        ///
//        
//        Select select = new Select();
//        select.setSelectBody(pselect);
//        
//        return select;  
    }
}
