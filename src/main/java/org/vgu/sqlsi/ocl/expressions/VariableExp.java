/*
// * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.sqlsi.ocl.expressions;

import org.vgu.sqlsi.ocl.context.OclContext;

import net.sf.jsqlparser.statement.Statement;

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
