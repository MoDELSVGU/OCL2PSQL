/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.sqlsi.ocl.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

import org.vgu.sqlsi.ocl.context.OclContext;
import org.vgu.sqlsi.ocl.exception.OclEvaluationException;
import org.vgu.sqlsi.ocl.exception.SetOfSetException;
import org.vgu.sqlsi.ocl.impl.OclCollectionSupport;
import org.vgu.sqlsi.ocl.impl.OclIteratorSupport;
import org.vgu.sqlsi.ocl.visitor.OCL2SQLParser;
import org.vgu.sqlsi.sql.statement.select.ResSelectExpression;
import org.vgu.sqlsi.sql.statement.select.ValSelectExpression;
import org.vgu.sqlsi.sql.statement.select.VarSelectExpression;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.MyPlainSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * Class IteratorExp
 */
public final class IteratorExp extends LoopExp {

    public final IteratorKind kind;

    public IteratorExp(OclExpression source, IteratorKind kind,
	    Variable iterator, OclExpression body) {
	super(source, iterator, body);
	this.kind = kind;
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	Object source = this.source.eval(context);
	Collection<?> collection;
	if (source == null) {
	    collection = Collections.emptyList();
	} else if (source.getClass().isArray()) {
	    collection = Arrays.asList((Object[])source);
	} else if (!(source instanceof Collection<?>)) {
	    collection = Collections.singletonList(source);
	} else {
	    collection = (Collection<?>) source;
	}
	switch (kind) {
	case isEmpty:
	    return OclCollectionSupport.isEmpty(collection);
	case notEmpty:
	    return OclCollectionSupport.notEmpty(collection);
	case size:
	    return OclCollectionSupport.size(collection);
	case sum:
	    return OclCollectionSupport.sum(collection);
	case asBag:
	    return OclCollectionSupport.asBag(collection);
	case asSet:
	    return OclCollectionSupport.asSet(collection);
	case asOrderedSet:
	    return OclCollectionSupport.asOrderedSet(collection);
	case asSequence:
	    return OclCollectionSupport.asSequence(collection);
	case at:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		if (!(body instanceof Number))
		    throw new OclEvaluationException(
			    "argument for at must be an instance of Integer or Real!");
		if (collection instanceof List<?>) {
		    return OclCollectionSupport.at((List<?>) collection,
			    (Number) body);
		} else {
		    throw new OclEvaluationException("at cannot be applied to "
			    + collection.getClass().getName());
		}
	    }
	case indexOf:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		if (collection instanceof List<?>) {
		    return OclCollectionSupport.indexOf((List<?>) collection,
			    body);
		} else {
		    throw new OclEvaluationException(
			    "indexOf cannot be applied to "
				    + collection.getClass().getName());
		}
	    }
	case count:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		return OclCollectionSupport.count((Collection<?>) collection,
			body);
	    }
	case first:
	    if (collection instanceof List<?>) {
		return OclCollectionSupport.first((List<?>) collection);
	    } else if (collection instanceof SortedSet<?>) {
		return OclCollectionSupport.first((SortedSet<?>) collection);
	    } else {
		throw new OclEvaluationException("first cannot be applied to "
			+ collection.getClass().getName());
	    }
	case last:
	    if (collection instanceof List<?>) {
		return OclCollectionSupport.last((List<?>) collection);
	    } else if (collection instanceof SortedSet<?>) {
		return OclCollectionSupport.last((SortedSet<?>) collection);
	    } else {
		throw new OclEvaluationException("last cannot be applied to "
			+ collection.getClass().getName());
	    }
	case including:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		return OclCollectionSupport.including(collection, body);
	    }
	case excluding:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		return OclCollectionSupport.excluding(collection, body);
	    }
	case includes:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		return OclCollectionSupport.includes(collection, body);
	    }
	case excludes:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		return OclCollectionSupport.excludes(collection, body);
	    }
	case union:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		Collection<?> bodyCollection;
		if (body == null) {
		    bodyCollection = Collections.emptyList();
		} else if (!(body instanceof Collection<?>)) {
		    bodyCollection = Collections.singletonList(body);
		} else {
		    bodyCollection = (Collection<?>) body;
		}
		return OclCollectionSupport.union(collection, bodyCollection);
	    }
	case includesAll:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		Collection<?> bodyCollection;
		if (body == null) {
		    bodyCollection = Collections.emptyList();
		} else if (!(body instanceof Collection<?>)) {
		    bodyCollection = Collections.singletonList(body);
		} else {
		    bodyCollection = (Collection<?>) body;
		}
		return OclCollectionSupport.includesAll(collection,
			bodyCollection);
	    }
	case excludesAll:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		Collection<?> bodyCollection;
		if (body == null) {
		    bodyCollection = Collections.emptyList();
		} else if (!(body instanceof Collection<?>)) {
		    bodyCollection = Collections.singletonList(body);
		} else {
		    bodyCollection = (Collection<?>) body;
		}
		return OclCollectionSupport.excludesAll(collection,
			bodyCollection);
	    }
	case exists:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.exists(collection, context,
			iteratorName, this.getBody());
	    }
	case collect:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.collect(collection, context,
			iteratorName, this.getBody());
	    }
	case select:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.select(collection, context,
			iteratorName, this.getBody());
	    }
	case reject:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.reject(collection, context,
			iteratorName, this.getBody());
	    }
	case any:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.any(collection, context,
			iteratorName, this.getBody());
	    }
	case one:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.one(collection, context,
			iteratorName, this.getBody());
	    }
	case isUnique:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.isUnique(collection, context,
			iteratorName, this.getBody());
	    }
	case forAll:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.forAll(collection, context,
			iteratorName, this.getBody());
	    }
	case sortedBy:
	    if (this.getBody() != null && this.getIterator() != null) {
		String iteratorName = this.getIterator().getName();
		return OclIteratorSupport.sortedBy(collection, context,
			iteratorName, this.getBody());
	    }
	}
	throw new OclEvaluationException("cannot evaluate: '->" + kind.name()
		+ "(" + getIterator().getName() + "|" + getBody() + ")");
    }

	@Override
	public Statement accept(StmVisitor visitor) {
		switch (this.kind) {
		case isEmpty:
			return emptyAccept(visitor);
		case notEmpty:
		    return notEmptyAccept(visitor);	
		case select:
		    return selectAccept(visitor);	
		case forAll:
		    return forAllAccept(visitor);
		case exists:
		    return existsAccept(visitor);   
		case collect:
		    return collectAccept(visitor);  
		case size:
		    return sizeAccept(visitor);
		case includes:
			return includesAccept(visitor);
		default:
			return null;
			
	}
		
}
	
	private Statement notEmptyAccept(StmVisitor visitor) {	
		// 1. save the current iterator source list (outer-iterators), and alias
		// in a temporal variable
		List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
		cur_iters.addAll(visitor.getVisitorContext());
		//
		//
		IteratorSource iter = new IteratorSource();		
		iter.setIterator(null);  
		iter.setSource((Select) visitor.visit(this.getSource()));
		visitor.setVisitorContext(cur_iters);

		SubSelect subselect_Iter = new SubSelect();
		subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
		Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
		subselect_Iter.setAlias(alias_Iter);

		// Build the expression
		PlainSelect pselect = new PlainSelect();

		// 4. create selected item 
		Function count = new Function();
		ArrayList<Expression> parms = new ArrayList<Expression>();

		Column col = new Column();
		col.setColumnName(alias_Iter.getName().concat(".").concat("item"));
		parms.add(col);

		count.setName("COUNT");

		BinaryExpression bexp = new GreaterThan();
		ExpressionList expList = new ExpressionList();
		expList.setExpressions(parms);
		count.setParameters(expList);
		bexp.setLeftExpression(count);
		bexp.setRightExpression(new LongValue(0));

		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(bexp);
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);

		// FROM ITEM 
		pselect.setFromItem(subselect_Iter);

		// rippling
		Utilities.ripplingUpVariables(pselect, null, subselect_Iter, null);

		// grouping
		Utilities.groupingVariables(pselect, null, subselect_Iter, null);

		Select select = new Select();
		select.setSelectBody(pselect);

		return select;	

	}
	
	private Statement emptyAccept(StmVisitor visitor) {
		// 1. save the current iterator source list (outer-iterators), and alias
		// in a temporal variable
		List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
		cur_iters.addAll(visitor.getVisitorContext());
		//
		//
		IteratorSource iter = new IteratorSource();		
		iter.setIterator(null);  
		iter.setSource((Select) visitor.visit(this.getSource()));
		visitor.setVisitorContext(cur_iters);
		
		SubSelect subselect_Iter = new SubSelect();
		subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
		Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
		subselect_Iter.setAlias(alias_Iter);
	
		// Build the expression
		PlainSelect pselect = new PlainSelect();
		
		// 4. create selected item 
		Function count = new Function();
		ArrayList<Expression> parms = new ArrayList<Expression>();

		Column col = new Column();
		col.setColumnName(alias_Iter.getName().concat(".").concat("item"));
		parms.add(col);

		count.setName("COUNT");
		
		BinaryExpression bexp = new EqualsTo();
		ExpressionList expList = new ExpressionList();
		expList.setExpressions(parms);
		count.setParameters(expList);
		bexp.setLeftExpression(count);
		bexp.setRightExpression(new LongValue(0));
		
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(bexp);
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);
		
		// FROM ITEM 
		pselect.setFromItem(subselect_Iter);

		// rippling
		Utilities.ripplingUpVariables(pselect, null, subselect_Iter, null);

		// grouping
		Utilities.groupingVariables(pselect, null, subselect_Iter, null);
		
		Select select = new Select();
		select.setSelectBody(pselect);

		return select;	

	}
	
	private Statement sizeAccept(StmVisitor visitor) {
		// 1. save the current iterator source list (outer-iterators), and alias
		// in a temporal variable
		List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
		cur_iters.addAll(visitor.getVisitorContext());
		//
		IteratorSource iter = new IteratorSource();		
		iter.setIterator(null);  
		iter.setSource((Select) visitor.visit(this.getSource()));
		visitor.setVisitorContext(cur_iters);
	
		// Build the expression
		PlainSelect pselect = new PlainSelect();
		
		SubSelect subselect_Iter = new SubSelect();
		subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
		Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
		subselect_Iter.setAlias(alias_Iter);

		
		Function count = new Function();
		//ArrayList<Expression> parms = new ArrayList<Expression>();

		count.setAllColumns(true);
		//col.setColumnName(alias_Iter.getName().concat(".").concat("item"));
		//parms.add(col);

		count.setName("COUNT");
		//ExpressionList expList = new ExpressionList();
		//expList.setExpressions(parms);
		//count.setParameters(expList);
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(count);
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);
		
		// FROM ITEM 
		pselect.setFromItem(subselect_Iter);
		
		// rippling
		Utilities.ripplingUpVariables(pselect, null, subselect_Iter, null);

		// grouping
		Utilities.groupingVariables(pselect, null, subselect_Iter, null);
		
		Select select = new Select();
		select.setSelectBody(pselect);

		return select;	
	}
	
	private Statement selectAccept(StmVisitor visitor) {
		// 1. save the current iterator source list (outer-iterators), and alias
		// in a temporal variable
		List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
		cur_iters.addAll(visitor.getVisitorContext());
		//
		// 2. create an iterator source for this collect-iterator
		IteratorSource iter = new IteratorSource();		
		iter.setIterator(this.getIterator());  
		iter.setSource((Select) visitor.visit(this.getSource()));
		visitor.setVisitorContext(cur_iters);
		
		// 4. continue visiting the body of the iterator, with the original iterator source list
		// plus the iterator source corresponding to this iterator		
		visitor.getVisitorContext().add(iter);
		
		SubSelect subselect_Body = new SubSelect();
		subselect_Body.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
		Alias alias_Body = new Alias(Utilities.genAliasName(visitor));
		subselect_Body.setAlias(alias_Body);
		

		SubSelect subselect_Iter = new SubSelect();
		subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
		Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
		subselect_Iter.setAlias(alias_Iter);
		
		/* build the expression */
		PlainSelect pselect = new PlainSelect();
		
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(new Column(alias_Iter.getName().concat(".").concat("item")));
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);

		// set select-body-related FROM-ITEM
		pselect.setFromItem(subselect_Body);

		// create & set select-source-related JOIN
		Join join = new Join();
		join.setSimple(true);
		join.setRightItem(subselect_Iter);
		List<Join> joins = new ArrayList<Join>();
		joins.add(join);
		pselect.setJoins(joins);
		
		
		// create select-related WHERE
		BinaryExpression sexp = new EqualsTo();
		sexp.setLeftExpression(new Column(alias_Body.getName().concat(".").concat("item")));
		sexp.setRightExpression(new LongValue(1));
		
		List<SelectItem> vars_Body = Utilities.getVariableAliases(subselect_Body);
		boolean found = false;
		for(SelectItem var_body : vars_Body) {
			String var_body_name = (((SelectExpressionItem) var_body).getAlias().getName()).split("_")[0];
			if (var_body_name.equals(this.getIterator().getName())){
				found = true;
				break;
			};

		}
		
		if (found) {
			// create iter-variable-related WHERE
			BinaryExpression bexp = new EqualsTo();
			bexp.setLeftExpression(new Column(alias_Body.getName().concat(".").concat(this.getIterator().getName()).concat("_var")));
			bexp.setRightExpression(new Column(alias_Iter.getName().concat(".").concat("item")));						
			pselect.setWhere(new AndExpression(bexp, sexp));
		} else {
			pselect.setWhere(sexp);
		}
		// rippling-up nested-iter-variables
		Utilities.ripplingUpVariables(pselect, this.getIterator().getName().concat("_var"), subselect_Iter, subselect_Body);
		
		
		// create result
		Select select = new Select();
		select.setSelectBody(pselect);
		
		
		return select;	

	}
	
	private Statement forAllAccept(StmVisitor visitor) {
		// 1. save the current iterator source list (outer-iterators), and alias
		// in a temporal variable
		List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
		cur_iters.addAll(visitor.getVisitorContext());

		// 2. create an iterator source for this collect-iterator
		IteratorSource iter = new IteratorSource();		
		iter.setIterator(this.getIterator());  
		// 3. visit the source of the iterator and save the result in the source of the iterator source
		iter.setSource((Select) visitor.visit(this.getSource()));
		visitor.setVisitorContext(cur_iters);
		
		// 4. continue visiting the body of the iterator, with the original iterator source list
		// plus the iterator source corresponding to this iterator		
		visitor.getVisitorContext().add(iter);
		
		SubSelect subselect_Body = new SubSelect();
		subselect_Body.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
		Alias alias_Body = new Alias(Utilities.genAliasName(visitor));
		subselect_Body.setAlias(alias_Body);
	
		// create exists-related selected-item
		PlainSelect pselect = new PlainSelect();

		Function count = new Function();
		ArrayList<Expression> parms = new ArrayList<Expression>();
		Column col = new Column();
		col.setColumnName(alias_Body.getName().concat(".").concat("item"));
		parms.add(col);
		count.setName("COUNT");
		
		BinaryExpression cexp = new EqualsTo();
		ExpressionList expList = new ExpressionList();
		expList.setExpressions(parms);
		count.setParameters(expList);
		cexp.setLeftExpression(count);
		cexp.setRightExpression(new LongValue(0));
		
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(cexp);
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);
		
		// set select-body-related FROM-ITEM
		pselect.setFromItem(subselect_Body);

		// create & set select-source-related JOIN

		SubSelect subselect_Iter = new SubSelect();
		subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
		Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
		subselect_Iter.setAlias(alias_Iter);
		
		Join join = new Join();
		join.setSimple(true);
		join.setRightItem(subselect_Iter);
		List<Join> joins = new ArrayList<Join>();
		joins.add(join);
		pselect.setJoins(joins);

		
		// create forall-related WHERE
		
		BinaryExpression eexp = new EqualsTo();
		eexp.setLeftExpression(new LongValue(0));
		eexp.setRightExpression(new Column(alias_Body.getName().concat(".").concat("item")));

		List<SelectItem> vars_Body = Utilities.getVariableAliases(subselect_Body);
		boolean found = false;
		for(SelectItem var_body : vars_Body) {
			String var_body_name = (((SelectExpressionItem) var_body).getAlias().getName()).split("_")[0];
			if (var_body_name.equals(this.getIterator().getName())){
				found = true;
				break;
			};

		}
		if (found) {
			// create iter-variable-related WHERE
			BinaryExpression bexp = new EqualsTo();
			bexp.setLeftExpression(new Column(alias_Body.getName().concat(".").concat(this.getIterator().getName()).concat("_var")));
			bexp.setRightExpression(new Column(alias_Iter.getName().concat(".").concat("item")));						
			pselect.setWhere(new AndExpression(bexp, eexp));
		} else {
			pselect.setWhere(eexp);
		}
		
		
		
		
		
		// rippling-up nested-iter-variables
		Utilities.ripplingUpVariables(pselect, this.getIterator().getName().concat("_var"), subselect_Iter, subselect_Body);
		
		// grouping
		Utilities.groupingVariables(pselect, this.getIterator().getName().concat("_var"), subselect_Iter, subselect_Body);
			
		// create result
		Select select = new Select();
		select.setSelectBody(pselect);
		
		return select;	
		
	}
	
	private Statement existsAccept(StmVisitor visitor) {
		// 1. save the current iterator source list (outer-iterators), and alias
		// in a temporal variable
		List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
		cur_iters.addAll(visitor.getVisitorContext());

		// 2. create an iterator source for this collect-iterator
		IteratorSource iter = new IteratorSource();		
		iter.setIterator(this.getIterator());  
		// 3. visit the source of the iterator and save the result in the source of the iterator source
		iter.setSource((Select) visitor.visit(this.getSource()));
		visitor.setVisitorContext(cur_iters);
		
		// 4. continue visiting the body of the iterator, with the original iterator source list
		// plus the iterator source corresponding to this iterator		
		visitor.getVisitorContext().add(iter);
		
		SubSelect subselect_Body = new SubSelect();
		subselect_Body.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
		Alias alias_Body = new Alias(Utilities.genAliasName(visitor));
		subselect_Body.setAlias(alias_Body);
	
		// create exists-related selected-item
		PlainSelect pselect = new PlainSelect();

		Function count = new Function();
		ArrayList<Expression> parms = new ArrayList<Expression>();
		Column col = new Column();
		col.setColumnName(alias_Body.getName().concat(".").concat("item"));
		parms.add(col);
		count.setName("COUNT");
		
		BinaryExpression cexp = new GreaterThan();
		ExpressionList expList = new ExpressionList();
		expList.setExpressions(parms);
		count.setParameters(expList);
		cexp.setLeftExpression(count);
		cexp.setRightExpression(new LongValue(0));
		
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(cexp);
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);
		
		// set select-body-related FROM-ITEM
		pselect.setFromItem(subselect_Body);

		// create & set select-source-related JOIN

		SubSelect subselect_Iter = new SubSelect();
		subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
		Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
		subselect_Iter.setAlias(alias_Iter);
		
		Join join = new Join();
		join.setSimple(true);
		join.setRightItem(subselect_Iter);
		List<Join> joins = new ArrayList<Join>();
		joins.add(join);
		pselect.setJoins(joins);

		
		// create exists-related WHERE
		BinaryExpression eexp = new EqualsTo();
		eexp.setLeftExpression(new LongValue(1));
		eexp.setRightExpression(new Column(alias_Body.getName().concat(".").concat("item")));

		List<SelectItem> vars_Body = Utilities.getVariableAliases(subselect_Body);
		boolean found = false;
		for(SelectItem var_body : vars_Body) {
			String var_body_name = (((SelectExpressionItem) var_body).getAlias().getName()).split("_")[0];
			if (var_body_name.equals(this.getIterator().getName())){
				found = true;
				break;
			};

		}
		if (found) {
			// create iter-variable-related WHERE
			BinaryExpression bexp = new EqualsTo();
			bexp.setLeftExpression(new Column(alias_Body.getName().concat(".").concat(this.getIterator().getName()).concat("_var")));
			bexp.setRightExpression(new Column(alias_Iter.getName().concat(".").concat("item")));						
			pselect.setWhere(new AndExpression(bexp, eexp));
		} else {
			pselect.setWhere(eexp);
		}
		
		
		
		// rippling-up nested-iter-variables
		Utilities.ripplingUpVariables(pselect, this.getIterator().getName().concat("_var"), subselect_Iter, subselect_Body);
		
		// grouping
		Utilities.groupingVariables(pselect, this.getIterator().getName().concat("_var"), subselect_Iter, subselect_Body);
				

		// create result
		Select select = new Select();
		select.setSelectBody(pselect);
		
		
		return select;	

	}

	private Statement collectAccept(StmVisitor visitor) {

		// Save the current iterator-source list (outer-iterators)
		// The iterator-source list contains the outer-iterator-variables along
		// with their source-sql expressions
		List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
		cur_iters.addAll(visitor.getVisitorContext());
		//
		// Create an iterator source for this collect-iterator
		// Visit the source of the iterator and save the result 
		// in the source of the iterator source
		IteratorSource iter = new IteratorSource();		
		iter.setIterator(this.getIterator());  
		iter.setSource((Select) visitor.visit(this.getSource()));
		visitor.setVisitorContext(cur_iters);
		
		// Build the sql-select corresponding to this collect-expression
		PlainSelect pselect = new PlainSelect();

		// FROM ITEM is generated from the collect-body
		// Visit the body of the iterator, 
		// with the original iterator source list
		// plus the iterator source corresponding to this iterator

		SubSelect subselect_Body = new SubSelect();
		visitor.getVisitorContext().add(iter);
		subselect_Body.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
		Alias alias_Body = new Alias(Utilities.genAliasName(visitor));
		subselect_Body.setAlias(alias_Body);
		pselect.setFromItem(subselect_Body);
		
		// SELECT ITEMS: main-item
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(new Column(alias_Body.getName().concat(".").concat("item")));
		item.setAlias(new Alias("item"));
		pselect.addSelectItems(item);
		
		// JOIN+WHERE with the collect-source
		// variable *does not* appear in the FROM ITEM subselect.
		// The source-related subselect is created anywhere, becau
		// be used for rippling-out variables 
		SubSelect subselect_Iter = new SubSelect();
		subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
		Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
		subselect_Iter.setAlias(alias_Iter);
		
		Join join = new Join();
		join.setSimple(true);
		join.setRightItem(subselect_Iter);
		List<Join> joins = new ArrayList<Join>();
		joins.add(join);
		pselect.setJoins(joins);
		
		// create iter-variable-related WHERE
		List<SelectItem> vars_Body = Utilities.getVariableAliases(subselect_Body);
		boolean found = false;
		for(SelectItem var_body : vars_Body) {
			String var_body_name = (((SelectExpressionItem) var_body).getAlias().getName()).split("_")[0];
			if (var_body_name.equals(this.getIterator().getName())){
				found = true;
				break;
			};

		}
		if (found) {
			BinaryExpression bexp = new EqualsTo();
			bexp.setLeftExpression(new Column(alias_Body.getName().concat(".").concat(this.getIterator().getName()).concat("_var")));
			bexp.setRightExpression(new Column(alias_Iter.getName().concat(".").concat("item")));						
			pselect.setWhere(bexp);
		}		
		// SELECT ITEMS: add items corresponding to rippling-up outer-iter-variables,
		// appearing either in the body and/or in the source, 
		// but not the collect-iter variable
		// Note that if the iter-variable appears in the source, it is certainly
		// not within the scope of the collect-iterator and should not be consider
		// a duplication
		Utilities.ripplingUpVariables(
				pselect, 
				this.getIterator().getName().concat("_var"), 
				subselect_Iter, 
				subselect_Body);
		
		// create result
		Select select = new Select();
		select.setSelectBody(pselect);
		
		return select;	
	}
	
	private Statement includesAccept(StmVisitor visitor) {
	// Save the current iterator-source list (outer-iterators)
	// The iterator-source list contains the outer-iterator-variables along
	// with their source-sql expressions
	List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
	cur_iters.addAll(visitor.getVisitorContext());
	
	// Create an iterator source for this collect-iterator
	// Visit the source of the iterator and save the result 
	// in the source of the iterator source
	IteratorSource iter = new IteratorSource();		
	iter.setIterator(null);  
	iter.setSource((Select) visitor.visit(this.getSource()));
	visitor.setVisitorContext(cur_iters);
	
	// Build the sql-select corresponding to this expression
	PlainSelect pselect = new PlainSelect();

	// Visit the body of the iterator, 
	// with the original iterator source list
	// Since there is not iterator-variable in an includes
	// expression, there is not need to carry out the 
	// iterator-variable's source
	
	// FROM ITEM is generated from the body
	SubSelect subselect_Body = new SubSelect();
	subselect_Body.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
	Alias alias_Body = new Alias(Utilities.genAliasName(visitor));
	subselect_Body.setAlias(alias_Body);
	pselect.setFromItem(subselect_Body);

	// SELECT ITEMS: main-item
	Function count = new Function();
	ArrayList<Expression> parms = new ArrayList<Expression>();

	Column col = new Column();
	col.setColumnName(alias_Body.getName().concat(".").concat("item"));
	parms.add(col);

	count.setName("COUNT");

	BinaryExpression cexp = new GreaterThan();
	ExpressionList expList = new ExpressionList();
	expList.setExpressions(parms);
	count.setParameters(expList);
	cexp.setLeftExpression(count);
	cexp.setRightExpression(new LongValue(0));

	SelectExpressionItem item = new SelectExpressionItem();
	item.setExpression(cexp);
	item.setAlias(new Alias("item"));
	pselect.addSelectItems(item);
	
	// Visit the source
	SubSelect subselect_Iter = new SubSelect();
	subselect_Iter.setSelectBody(iter.getSource().getSelectBody());
	Alias alias_Iter = new Alias(Utilities.genAliasName(visitor));
	subselect_Iter.setAlias(alias_Iter);

	// Add JOIN for the source
	Join join = new Join();
	join.setSimple(true);
	join.setRightItem(subselect_Iter);
	List<Join> joins = new ArrayList<Join>();
	joins.add(join);
	pselect.setJoins(joins);

	//create includes-related WHERE
	BinaryExpression bexp = new EqualsTo();
	bexp.setLeftExpression(new Column(alias_Body.getName().concat(".").concat("item")));
	bexp.setRightExpression(new Column(alias_Iter.getName().concat(".").concat("item")));						
	pselect.setWhere(bexp);

	// SELECT ITEMS: add items corresponding to rippling-up outer-iter-variables,
	// appearing either in the body or in the source
	Utilities.ripplingUpVariables(
			pselect, 
			null, 
			subselect_Iter, 
			subselect_Body);
	
	// grouping
	Utilities.groupingVariables(pselect, null, subselect_Iter, subselect_Body);

	// create result
	Select select = new Select();
	select.setSelectBody(pselect);

	return select;	
	}

    @Override
    public Statement map(StmVisitor visitor) {
        switch (this.kind) {
        case isEmpty:
            throw new NullPointerException("Unsupported isEmpty operation");
//            return emptyMap(visitor);
        case notEmpty:
            throw new NullPointerException("Unsupported notEmpty operation");
//            return notEmptyMap(visitor);
        case select:
            return selectMap(visitor); 
        case reject:
            throw new NullPointerException("Unsupported reject operation");
//            return rejectMap(visitor);  
        case forAll:
            return forAllMap(visitor);
        case exists:
            return existsMap(visitor);  
        case collect:
            return collectMap(visitor);  
        case size:
            return sizeMap(visitor);
        case asSet:
            throw new NullPointerException("Unsupported asSet operation");
//            return asSetMap(visitor);
        case isUnique:
            throw new NullPointerException("Unsupported isUnique operation");
//            return isUniqueMap(visitor);
        case flatten:
            return flattenMap(visitor);
        case sum:
            throw new NullPointerException("Unsupported sum operation");
        case asBag:
            throw new NullPointerException("Unsupported asBag operation");
        case asOrderedSet:
            throw new NullPointerException("Unsupported asOrderedSet operation");
        case asSequence:
            throw new NullPointerException("Unsupported asSequence operation");
        case at:
            throw new NullPointerException("Unsupported at operation");
        case indexOf:
            throw new NullPointerException("Unsupported indexOf operation");
        case count:
            throw new NullPointerException("Unsupported count operation");
        case first:
            throw new NullPointerException("Unsupported first operation");
        case last:
            throw new NullPointerException("Unsupported last operation");
        case including:
            throw new NullPointerException("Unsupported including operation");
        case excluding:
            throw new NullPointerException("Unsupported excluding operation");
        case includes:
            throw new NullPointerException("Unsupported includes operation");
        case excludes:
            throw new NullPointerException("Unsupported excludes operation");
        case union:
            throw new NullPointerException("Unsupported union operation");
        case includesAll:
            throw new NullPointerException("Unsupported includesAll operation");
        case excludesAll:
            throw new NullPointerException("Unsupported excludesAll operation");
        case any:
            throw new NullPointerException("Unsupported any operation");
        case one:
            throw new NullPointerException("Unsupported one operation");
        case sortedBy:
            throw new NullPointerException("Unsupported sortedBy operation");
        default:
            return null;
        }
    }

    /**
     * 
     * @param visitor
     * @return
     * 
     * s->collect(v|b)->flatten()
     */
    private Statement flattenMap_1(StmVisitor visitor) {

        if(this.getSource() instanceof IteratorExp) {
            Select source = (Select) visitor.visit(this.getSource());
            
            
//            if(VariableUtils.isSetOfSetAfterCollect(this, visitor)) {
//                throw new SetOfSetException("Invalid set of sets operation");
//            }
            
            MyPlainSelect finalPlainSelect = new MyPlainSelect();
            Select finalSelect = new Select(finalPlainSelect);
            
            SubSelect tempFlattenSource = new SubSelect();
            tempFlattenSource.setSelectBody( source.getSelectBody() );
            
            List<String> sVarSource = VariableUtils.FVars((MyPlainSelect) tempFlattenSource.getSelectBody());

            if(sVarSource.isEmpty()) {

                Alias aliasTempFlattenSource = new Alias("TEMP_src");
                tempFlattenSource.setAlias(aliasTempFlattenSource);
                
                finalPlainSelect.setFromItem(tempFlattenSource);
                finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempFlattenSource.getName().concat(".res"))));
                finalPlainSelect.setValAsTrue();
                
                BinaryExpression valTrue = new EqualsTo();
                valTrue.setLeftExpression(new Column(aliasTempFlattenSource.getName().concat(".val")));
                valTrue.setRightExpression(new LongValue(1L));
                finalPlainSelect.setWhere(valTrue);
                
                return finalSelect;
            }
            else {
                Alias aliasTempFlattenSource = new Alias("TEMP");
                tempFlattenSource.setAlias(aliasTempFlattenSource);
                
                MyPlainSelect sCollectvB = new MyPlainSelect();
                sCollectvB.setAllColumn();
                sCollectvB.setFromItem(tempFlattenSource);

                BinaryExpression valIsOne = new EqualsTo();
                valIsOne.setLeftExpression(new Column(aliasTempFlattenSource.getName().concat(".val")));
                valIsOne.setRightExpression(new LongValue(1L));
                sCollectvB.setWhere(valIsOne);

                SubSelect tempFlat = new SubSelect( sCollectvB, "TEMP_flat" );

//                SubSelect tempFlat = new SubSelect();
//                tempFlat.setSelectBody(sCollectvB);
                Alias aliasTempFlat = tempFlat.getAlias();
//                tempFlat.setAlias(aliasTempFlat);
                
                Join join = new Join();
                join.setLeft(true);
                join.setRightItem(tempFlat);

                finalPlainSelect.setJoins(Arrays.asList(join));
                
                MyPlainSelect s = (MyPlainSelect) tempFlattenSource.getSelectBody();
                
                SubSelect tempCollectSource = new SubSelect( s, "TEMP_src" );
                
//                SubSelect tempCollectSource = new SubSelect();
//                tempCollectSource.setSelectBody(s);
//                Alias aliasTempCollectSource = new Alias("TEMP_src");
//                tempCollectSource.setAlias(aliasTempCollectSource);

                finalPlainSelect.setFromItem(tempCollectSource);
                
                String flattenVar = ((IteratorExp) this.getSource()).getIterator().getName();
                
                IsNullExpression isOuterRefNull = new IsNullExpression();
                isOuterRefNull.setLeftExpression(new Column(aliasTempFlat.getName().concat(".val")));

                CaseExpression caseResVExpression = new CaseExpression();
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(isOuterRefNull);
                whenResClause.setThenExpression(new NullValue());
                caseResVExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResVExpression.setElseExpression(new Column(aliasTempFlat.getName().concat(".res")));

                CaseExpression caseValVExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(isOuterRefNull);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValVExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValVExpression.setElseExpression(new Column(aliasTempFlat.getName().concat(".val")));

                finalPlainSelect.setRes(new ResSelectExpression(caseResVExpression));
                finalPlainSelect.setVal(new ValSelectExpression(caseValVExpression));
                
                List<String> sVarsSource = VariableUtils.SVars(
                    (MyPlainSelect) tempFlattenSource.getSelectBody(), 
                    visitor.getVisitorContext());
                BinaryExpression onCondition = null;

                for(String v : sVarsSource) {
//                    if(v.equals(flattenVar)) continue;
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempFlat.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);

                    BinaryExpression holderExp = new EqualsTo();
                    holderExp.setLeftExpression(new Column(tempCollectSource.getAlias().getName().concat(".ref_").concat(v)));
                    holderExp.setRightExpression(new Column(aliasTempFlat.getName().concat(".ref_").concat(v)));

                    if(Objects.isNull(onCondition)) {
                        onCondition = holderExp;
                    } else {
                        onCondition = new AndExpression(onCondition, holderExp);
                    }
                }
                join.setOnExpression(onCondition);
                
                
                ((OCL2SQLParser) visitor).decreaseLevelOfSet();
                        
                return finalSelect;
            }
        }
        throw new SetOfSetException("The source is not set of set to be flattened");
    }

    private Statement flattenMap(StmVisitor visitor) {
        if(this.getSource() instanceof IteratorExp) {
            Select source = (Select) visitor.visit(this.getSource());
            
            SubSelect tempFlattenSource = new SubSelect();
            tempFlattenSource.setSelectBody(source.getSelectBody());
            
//            if(VariableUtils.isSetOfSetAfterCollect(this, visitor)) {
//                throw new SetOfSetException("Invalid set of sets operation");
//            }
            
            MyPlainSelect finalPlainSelect = new MyPlainSelect();
            Select finalSelect = new Select(finalPlainSelect);
            
            List<String> sVarSource = VariableUtils.FVars((MyPlainSelect) tempFlattenSource.getSelectBody());
            if(sVarSource.isEmpty()) {
                Alias aliasTempFlattenSource = new Alias("TEMP_src");
                tempFlattenSource.setAlias(aliasTempFlattenSource);
                
                finalPlainSelect.setFromItem(tempFlattenSource);
                finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempFlattenSource.getName().concat(".res"))));
                finalPlainSelect.setValAsTrue();
                
                BinaryExpression valTrue = new EqualsTo();
                valTrue.setLeftExpression(new Column(aliasTempFlattenSource.getName().concat(".val")));
                valTrue.setRightExpression(new LongValue(1L));
                finalPlainSelect.setWhere(valTrue);
                
                return finalSelect;
            }
            else {
                // s->collect(v|b)->flatten()
                Alias aliasTempFlattenSource = new Alias("TEMP");
                tempFlattenSource.setAlias(aliasTempFlattenSource);
                
                MyPlainSelect s = (MyPlainSelect) tempFlattenSource.getSelectBody();
                SubSelect tempCollectSource = new SubSelect();
                tempCollectSource.setSelectBody(s);
                Alias aliasTempCollectSource = new Alias("TEMP_src");
                tempCollectSource.setAlias(aliasTempCollectSource);
                finalPlainSelect.setFromItem(tempCollectSource);
                
                MyPlainSelect sCollectvB = new MyPlainSelect();
                sCollectvB.getSelectItems().clear();
                sCollectvB.addSelectItem(new AllColumns());
                sCollectvB.setFromItem(tempFlattenSource);
                BinaryExpression valisOne = new EqualsTo();
                valisOne.setLeftExpression(new Column(aliasTempFlattenSource.getName().concat(".val")));
                valisOne.setRightExpression(new LongValue(1L));
                sCollectvB.setWhere(valisOne);
                
                SubSelect tempFlat = new SubSelect();
                tempFlat.setSelectBody(sCollectvB);
                Alias aliasTempFlat = new Alias("TEMP_flat");
                tempFlat.setAlias(aliasTempFlat);
                
                Join join = new Join();
                join.setLeft(true);
                join.setRightItem(tempFlat);
                finalPlainSelect.setJoins(Arrays.asList(join));
                
                String flattenVar = ((IteratorExp) this.getSource()).getIterator().getName();
                
                CaseExpression caseResVExpression = new CaseExpression();
                IsNullExpression isOuterRefNull = new IsNullExpression();
                isOuterRefNull.setLeftExpression(new Column(aliasTempFlat.getName().concat(".val")));
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(isOuterRefNull);
                whenResClause.setThenExpression(new NullValue());
                caseResVExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResVExpression.setElseExpression(new Column(aliasTempFlat.getName().concat(".res")));

                CaseExpression caseValVExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(isOuterRefNull);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValVExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValVExpression.setElseExpression(new Column(aliasTempFlat.getName().concat(".val")));

                finalPlainSelect.setRes(new ResSelectExpression(caseResVExpression));
                finalPlainSelect.setVal(new ValSelectExpression(caseValVExpression));
                
                List<String> sVarsSource = VariableUtils.SVars((MyPlainSelect) tempFlattenSource.getSelectBody(), visitor.getVisitorContext());
                BinaryExpression onCondition = null;
                for(String v : sVarsSource) {
//                    if(v.equals(flattenVar)) continue;
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempFlat.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                    BinaryExpression holderExp = new EqualsTo();
                    holderExp.setLeftExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
                    holderExp.setRightExpression(new Column(aliasTempFlat.getName().concat(".ref_").concat(v)));
                    if(Objects.isNull(onCondition)) {
                        onCondition = holderExp;
                    } else {
                        onCondition = new AndExpression(onCondition, holderExp);
                    }
                }
                join.setOnExpression(onCondition);
                
                
                ((OCL2SQLParser) visitor).decreaseLevelOfSet();
                        
                return finalSelect;
            }
        }
        throw new SetOfSetException("The source is not set of set to be flattened");
    }

    private Statement isUniqueMap(StmVisitor visitor) {
        Select sizeSourceSelect = (Select) visitor.visit(this.getSource());
        MyPlainSelect sizeSourcePlainSelect = (MyPlainSelect) sizeSourceSelect.getSelectBody();
        
        SubSelect finalSubSelect = new SubSelect();
        finalSubSelect.setSelectBody(sizeSourcePlainSelect);
        Alias aliasFinalSubSelect = new Alias("TEMP_unique_source");
        finalSubSelect.setAlias(aliasFinalSubSelect);
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();
        Select finalSelect = new Select(finalPlainSelect);
        
        finalPlainSelect.setFromItem(finalSubSelect);
        ResSelectExpression uniqueRes = new ResSelectExpression();
        Function countAll = new Function();
        countAll.setName("COUNT");
        countAll.setParameters(new ExpressionList(Arrays.asList(new Column(aliasFinalSubSelect.getName().concat(".res")))));
        Function countAllDistinct = new Function();
        countAllDistinct.setName("COUNT");
        countAllDistinct.setDistinct(true);
        countAllDistinct.setParameters(new ExpressionList(Arrays.asList(new Column(aliasFinalSubSelect.getName().concat(".res")))));
        BinaryExpression eqEx = new EqualsTo();
        eqEx.setLeftExpression(countAll);
        eqEx.setRightExpression(countAllDistinct);
        uniqueRes.setExpression(eqEx);
        finalPlainSelect.setRes(uniqueRes);
        return finalSelect;
    }

    private Statement rejectMap(StmVisitor visitor) {
        List<IteratorSource> iteratorList = new LinkedList<IteratorSource>();
        iteratorList.addAll(visitor.getVisitorContext());
        visitor.setVisitorContext(iteratorList);

        MyIteratorSource iter = new MyIteratorSource();     
        iter.setIterator(this.getIterator());  
        iter.setSource((Select) visitor.visit(this.getSource()));
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();

        SubSelect tempRejectVar = new SubSelect();
        tempRejectVar.setSelectBody(iter.getSource().getSelectBody());
        Alias aliasTempRejectVar = new Alias("TEMP_reject_var_"+iter.getIterator().getName());
        tempRejectVar.setAlias(aliasTempRejectVar);
        
        visitor.getVisitorContext().add(iter);
        SubSelect tempRejectBody = new SubSelect();
        tempRejectBody.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
        Alias aliasTempRejectBody = new Alias("TEMP_reject_body");
        tempRejectBody.setAlias(aliasTempRejectBody);
        
        ResSelectExpression resExpression = new ResSelectExpression();
        finalPlainSelect.addSelectItems(resExpression);
        
        finalPlainSelect.setFromItem(tempRejectBody);
        
        resExpression.setExpression(new Column(aliasTempRejectBody.getName().concat(".").concat("ref_").concat(iter.getIterator().getName())));
        
        BinaryExpression onCondition = VariableUtils.onMappingCondition(tempRejectBody, tempRejectVar);
        
        if(Objects.isNull(onCondition)) {
            Join join = new Join();
            join.setRightItem(tempRejectBody);
            
            resExpression.setExpression(new Column(aliasTempRejectVar.getName().concat(".").concat("ref_").concat(iter.getIterator().getName())));

            finalPlainSelect.setFromItem(tempRejectVar);
            finalPlainSelect.setJoins(Arrays.asList(join));
            
            VariableUtils.reserveVarsForCollect(finalPlainSelect, tempRejectVar, this.getIterator());
        }
        
        BinaryExpression whereExp = new EqualsTo();
        whereExp.setLeftExpression(new Column(aliasTempRejectBody.getName().concat(".").concat("res")));
        whereExp.setRightExpression(new LongValue(0L));
        finalPlainSelect.setWhere(whereExp);
        
        VariableUtils.reserveVarsForCollect(finalPlainSelect, tempRejectBody, this.getIterator());

        // create result
        Select finalSelect = new Select();
        finalSelect.setSelectBody(finalPlainSelect);
        
        return finalSelect;  
    }

    private Statement asSetMap(StmVisitor visitor) {
        List<IteratorSource> iteratorList = new LinkedList<IteratorSource>();
        iteratorList.addAll(visitor.getVisitorContext());
        visitor.setVisitorContext(iteratorList);
        
        Select finalSelect = (Select) visitor.visit(this.getSource());
        PlainSelect finalPlainSelect = (PlainSelect) finalSelect.getSelectBody();
        
        finalPlainSelect.setDistinct(new Distinct());
       
       return finalSelect; 
    }

    private Statement existsMap(StmVisitor visitor) {
        MyIteratorSource currentIterator = new MyIteratorSource();     
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempExistsSource = new SubSelect();
        tempExistsSource.setSelectBody(source.getSelectBody());
        Alias aliasTempExistsSource = new Alias("TEMP_src");
        tempExistsSource.setAlias(aliasTempExistsSource);
        
//        if(VariableUtils.isSetOfSetAfterCollect(this, visitor)) {
//            throw new SetOfSetException("Invalid set of sets operation");
//        }
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();

        if(visitor.getVisitorContext().stream()
                .map(IteratorSource::getIterator)
                .map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            visitor.getVisitorContext().add(currentIterator);
        }
        SubSelect tempExistsBody = new SubSelect();
        tempExistsBody.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
        Alias aliasTempExistsBody = new Alias("TEMP_body");
        tempExistsBody.setAlias(aliasTempExistsBody);
        
        String currentIter = this.getIterator().getName();
        List<String> fVarsSource = VariableUtils.FVars((MyPlainSelect) tempExistsSource.getSelectBody());
        List<String> fVarsBody = VariableUtils.FVars((MyPlainSelect) tempExistsBody.getSelectBody());
        
        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if(fVarsSource.isEmpty() && fVarsBody.size() == 1) {
                finalPlainSelect.setValAsTrue();
                
                Function count = new Function();
                count.setName("COUNT");
                count.setAllColumns(true);
                BinaryExpression greaterThanZero = new GreaterThan();
                greaterThanZero.setLeftExpression(count);
                greaterThanZero.setRightExpression(new LongValue(0L));
                
                finalPlainSelect.setRes(new ResSelectExpression(greaterThanZero));
                
                finalPlainSelect.setFromItem(tempExistsBody);
                
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempExistsBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                finalPlainSelect.setWhere(bodyWhereExp);
                
            } else {
                finalPlainSelect.setValAsTrue();
                
                String outerVar = VariableUtils.getOuterVariable(this);
                MyIteratorSource outerIter = visitor.getVisitorContext().stream()
                        .filter(v -> v.getIterator().getName().equals(outerVar))
                        .findFirst()
                        .map(MyIteratorSource.class::cast)
                        .get();
                
                SubSelect tempVar = new SubSelect();
                tempVar.setSelectBody(outerIter.getSource().getSelectBody());
                Alias aliasTempVar = new Alias("TEMP_src");
                tempVar.setAlias(aliasTempVar);
                
                MyPlainSelect gBody = new MyPlainSelect();
                gBody.setFromItem(tempExistsBody);
                gBody.getSelectItems().clear();
                
                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);
                
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempExistsBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                gBody.setWhere(bodyWhereExp);
                
                Function count = new Function();
                count.setName("COUNT");
                count.setAllColumns(true);
                BinaryExpression greaterThanZero = new GreaterThan();
                greaterThanZero.setLeftExpression(count);
                greaterThanZero.setRightExpression(new LongValue(0L));
                gBody.setRes(new ResSelectExpression(greaterThanZero));
                
                List<String> SVarsSource = VariableUtils.SVars((MyPlainSelect) tempExistsSource.getSelectBody(), visitor.getVisitorContext());
                List<Expression> groupByExpressions = new ArrayList<Expression>();
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
                    gBody.addVar(newVar);
                    groupByExpressions.add(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
                }
                gBody.setGroupByColumnReferences(groupByExpressions);
                
                List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempExistsBody.getSelectBody(), visitor.getVisitorContext());
//                for(String v : SVarsBody) {
//                    if(v.equals(currentIter)) {
//                        groupByExpressions.add(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
//                        continue;
//                    }
//                    if(SVarsSource.contains(v)) {
//                        continue;
//                    }
//                    VarSelectExpression newVar = new VarSelectExpression(v);
//                    newVar.setRefExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
//                    finalPlainSelect.addVar(newVar);
//                }
                
                finalPlainSelect.setFromItem(tempVar);
                Join join = new Join();
                join.setRightItem(tempGBody);
                
                List<String> sVarsIntercept = new ArrayList<String>();
                for(String sr : SVarsBody) {
                    if(SVarsSource.contains(sr)) sVarsIntercept.add(sr);
                }
                if(!sVarsIntercept.isEmpty()) {
                    join.setLeft(true);
                    BinaryExpression onExp = null;
                    for(String v : sVarsIntercept) {
                        BinaryExpression holderExp = new EqualsTo();
                        holderExp.setLeftExpression(new Column(aliasTempVar.getName().concat(".ref_").concat(v)));
                        holderExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                        VarSelectExpression newVar = new VarSelectExpression(v);
                        newVar.setRefExpression(new Column(aliasTempVar.getName().concat(".ref_").concat(v)));
                        finalPlainSelect.addVar(newVar);
                        if(Objects.isNull(onExp)) {
                            onExp = holderExp;
                        }
                        else {
                            onExp = new AndExpression(onExp, holderExp);
                        }
                    }
                    join.setOnExpression(onExp);
                }
                
                finalPlainSelect.setJoins(Arrays.asList(join));
                
                CaseExpression caseResExpression = new CaseExpression();
                IsNullExpression isOuterRefNull = new IsNullExpression();
                isOuterRefNull.setLeftExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(outerVar)));
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(isOuterRefNull);
                whenResClause.setThenExpression(new LongValue(0L));
                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResExpression.setElseExpression(new Column(aliasTempGBody.getName().concat(".res")));
                
                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
                
            }
            Select finalSelect = new Select(finalPlainSelect);
            return finalSelect;
        }
        else {
            finalPlainSelect.setValAsTrue();
            
            List<String> SVarsSource = VariableUtils.SVars((MyPlainSelect) tempExistsSource.getSelectBody(), visitor.getVisitorContext());
//            List<Expression> groupByExpressions = new ArrayList<Expression>();
            for(String v : SVarsSource) {
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempExistsSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
//                groupByExpressions.add(new Column(aliasTempExistsSource.getName().concat(".ref_").concat(v)));
            }
            List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempExistsBody.getSelectBody(), visitor.getVisitorContext());
//            for(String v : SVarsBody) {
//                if(v.equals(currentIter) || SVarsSource.contains(v)) continue;
//                VarSelectExpression newVar = new VarSelectExpression(v);
//                newVar.setRefExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
//                finalPlainSelect.addVar(newVar);
////                groupByExpressions.add(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
//            }
            BinaryExpression bodyWhereExp = new EqualsTo();
            bodyWhereExp.setLeftExpression(new Column(aliasTempExistsBody.getName().concat(".res")));
            bodyWhereExp.setRightExpression(new LongValue(1L));
            finalPlainSelect.setWhere(bodyWhereExp);
            
            finalPlainSelect.setFromItem(tempExistsSource);
            Join join = new Join();
            join.setRightItem(tempExistsBody);
            
//            BinaryExpression onExp = new EqualsTo();
            BinaryExpression onExp = null;
//            onExp.setLeftExpression(new Column(aliasTempExistsSource.getName().concat(".res")));
//            onExp.setRightExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(currentIter)));
            
            List<String> sVarsIntercept = new ArrayList<String>();
            for(String sr : SVarsBody) {
                if(SVarsSource.contains(sr)) sVarsIntercept.add(sr);
            }
            if(!sVarsIntercept.isEmpty()) {
                join.setLeft(true);
                for(String v : sVarsIntercept) {
                    BinaryExpression holderExp = new EqualsTo();
                    holderExp.setLeftExpression(new Column(aliasTempExistsSource.getName().concat(".ref_").concat(v)));
                    holderExp.setRightExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
                    if(Objects.isNull(onExp)) {
                        onExp = holderExp;
                    } else {
                        onExp = new AndExpression(onExp, holderExp);
                    }
                }
            }
//            join.setOnExpression(onExp);
            
            finalPlainSelect.setJoins(Arrays.asList(join));
            
            CaseExpression caseResExpression = new CaseExpression();
            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempExistsSource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));
            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(0L));
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            BinaryExpression greaterThanZero = new GreaterThan();
            greaterThanZero.setLeftExpression(count);
            greaterThanZero.setRightExpression(new LongValue(0L));
            caseResExpression.setElseExpression(greaterThanZero);
            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            
//            finalPlainSelect.setGroupByColumnReferences(groupByExpressions);
            
            Select finalSelect = new Select(finalPlainSelect);
            return finalSelect;
        }
    }

    private Statement forAllMap(StmVisitor visitor) {
        MyIteratorSource currentIterator = new MyIteratorSource();     
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempForAllSource = new SubSelect();
        tempForAllSource.setSelectBody(source.getSelectBody());
        Alias aliasTempForAllSource = new Alias("TEMP_src");
        tempForAllSource.setAlias(aliasTempForAllSource);
        
//        if(VariableUtils.isSetOfSetAfterCollect(this, visitor)) {
//            throw new SetOfSetException("Invalid set of sets operation");
//        }
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();

        if(visitor.getVisitorContext().stream()
                .map(IteratorSource::getIterator)
                .map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            visitor.getVisitorContext().add(currentIterator);
        }
        SubSelect tempForAllBody = new SubSelect();
        tempForAllBody.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
        Alias aliasTempForAllBody = new Alias("TEMP_body");
        tempForAllBody.setAlias(aliasTempForAllBody);
        
        String currentIter = this.getIterator().getName();
        List<String> fVarsSource = VariableUtils.FVars((MyPlainSelect) tempForAllSource.getSelectBody());
        List<String> fVarsBody = VariableUtils.FVars((MyPlainSelect) tempForAllBody.getSelectBody());
        
        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if(fVarsSource.isEmpty() && fVarsBody.size() == 1) {
                finalPlainSelect.setValAsTrue();
                
                Function count = new Function();
                count.setName("COUNT");
                count.setAllColumns(true);
                BinaryExpression eqZero = new EqualsTo();
                eqZero.setLeftExpression(count);
                eqZero.setRightExpression(new LongValue(0L));
                finalPlainSelect.setRes(new ResSelectExpression(eqZero));
                
                finalPlainSelect.setFromItem(tempForAllBody);
                
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempForAllBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(0L));
                finalPlainSelect.setWhere(bodyWhereExp);
            }
            else {
                finalPlainSelect.setValAsTrue();
                
                String outerVar = VariableUtils.getOuterVariable(this);
                MyIteratorSource outerIter = visitor.getVisitorContext().stream()
                        .filter(v -> v.getIterator().getName().equals(outerVar))
                        .findFirst()
                        .map(MyIteratorSource.class::cast)
                        .get();
                
                SubSelect tempVar = new SubSelect();
                tempVar.setSelectBody(outerIter.getSource().getSelectBody());
                Alias aliasTempVar = new Alias("TEMP_src");
                tempVar.setAlias(aliasTempVar);
                
                MyPlainSelect gBody = new MyPlainSelect();
                gBody.setFromItem(tempForAllBody);
                
                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);
                
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempForAllBody.getName().concat(".val")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                Function ifNull = new Function();
                ifNull.setName("IFNULL");
                ifNull.setParameters(new ExpressionList(new Column(aliasTempForAllBody.getName().concat(".res")), new LongValue(0L)));
                BinaryExpression isNullOrZero = new EqualsTo();
                isNullOrZero.setLeftExpression(ifNull);
                isNullOrZero.setRightExpression(new LongValue(0L));
                gBody.setWhere(new AndExpression(bodyWhereExp, isNullOrZero));
                
                Function count = new Function();
                count.setName("COUNT");
                count.setAllColumns(true);
                BinaryExpression eqZero = new EqualsTo();
                eqZero.setLeftExpression(count);
                eqZero.setRightExpression(new LongValue(0L));
                gBody.setRes(new ResSelectExpression(eqZero));
                
                List<String> SVarsSource = VariableUtils.SVars((MyPlainSelect) tempForAllSource.getSelectBody(), visitor.getVisitorContext());
                List<Expression> groupByExpressions = new ArrayList<Expression>();
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
                    gBody.addVar(newVar);
                    groupByExpressions.add(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
                }
                gBody.setGroupByColumnReferences(groupByExpressions);
                
                List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempForAllBody.getSelectBody(), visitor.getVisitorContext());
//                for(String v : SVarsBody) {
//                    if(v.equals(currentIter)) {
//                        groupByExpressions.add(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
//                        continue;
//                    }
//                    if(SVarsSource.contains(v)) {
//                        continue;
//                    }
//                    VarSelectExpression newVar = new VarSelectExpression(v);
//                    newVar.setRefExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
//                    finalPlainSelect.addVar(newVar);
//                }
                
                finalPlainSelect.setFromItem(tempVar);
                Join join = new Join();
                join.setRightItem(tempGBody);
                
                List<String> sVarsIntercept = new ArrayList<String>();
                for(String sr : SVarsBody) {
                    if(SVarsSource.contains(sr)) sVarsIntercept.add(sr);
                }
                if(!sVarsIntercept.isEmpty()) {
                    join.setLeft(true);
                    BinaryExpression onExp = null;
                    for(String v : sVarsIntercept) {
                        BinaryExpression holderExp = new EqualsTo();
                        holderExp.setLeftExpression(new Column(aliasTempVar.getName().concat(".ref_").concat(v)));
                        holderExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                        VarSelectExpression newVar = new VarSelectExpression(v);
                        newVar.setRefExpression(new Column(aliasTempVar.getName().concat(".ref_").concat(v)));
                        finalPlainSelect.addVar(newVar);
                        if(Objects.isNull(onExp)) {
                            onExp = holderExp;
                        }
                        else {
                            onExp = new AndExpression(onExp, holderExp);
                        }
                    }
                    join.setOnExpression(onExp);
                }
                
                finalPlainSelect.setJoins(Arrays.asList(join));
                
                CaseExpression caseResExpression = new CaseExpression();
                IsNullExpression isOuterRefNull = new IsNullExpression();
                isOuterRefNull.setLeftExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(outerVar)));
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(isOuterRefNull);
                whenResClause.setThenExpression(new LongValue(1L));
                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResExpression.setElseExpression(new Column(aliasTempGBody.getName().concat(".res")));
                
                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
                
            }
            Select finalSelect = new Select(finalPlainSelect);
            return finalSelect;
        }
        else {
            finalPlainSelect.setValAsTrue();
            
            List<String> SVarsSource = VariableUtils.SVars((MyPlainSelect) tempForAllSource.getSelectBody(), visitor.getVisitorContext());
//            List<Expression> groupByExpressions = new ArrayList<Expression>();
            for(String v : SVarsSource) {
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempForAllSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
//                groupByExpressions.add(new Column(aliasTempExistsSource.getName().concat(".ref_").concat(v)));
            }
            List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempForAllBody.getSelectBody(), visitor.getVisitorContext());
//            for(String v : SVarsBody) {
//                if(v.equals(currentIter) || SVarsSource.contains(v)) continue;
//                VarSelectExpression newVar = new VarSelectExpression(v);
//                newVar.setRefExpression(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
//                finalPlainSelect.addVar(newVar);
//                groupByExpressions.add(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
//            }
            BinaryExpression bodyWhereExp = new EqualsTo();
            bodyWhereExp.setLeftExpression(new Column(aliasTempForAllBody.getName().concat(".res")));
            bodyWhereExp.setRightExpression(new LongValue(0L));
            finalPlainSelect.setWhere(bodyWhereExp);
            
            finalPlainSelect.setFromItem(tempForAllSource);
            Join join = new Join();
            join.setRightItem(tempForAllBody);
            
//            BinaryExpression onExp = new EqualsTo();
            BinaryExpression onExp = null;
//            onExp.setLeftExpression(new Column(aliasTempExistsSource.getName().concat(".res")));
//            onExp.setRightExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(currentIter)));
            
            List<String> sVarsIntercept = new ArrayList<String>();
            for(String sr : SVarsBody) {
                if(SVarsSource.contains(sr)) sVarsIntercept.add(sr);
            }
            if(!sVarsIntercept.isEmpty()) {
                join.setLeft(true);
                for(String v : sVarsIntercept) {
                    BinaryExpression holderExp = new EqualsTo();
                    holderExp.setLeftExpression(new Column(aliasTempForAllSource.getName().concat(".ref_").concat(v)));
                    holderExp.setRightExpression(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
                    if(Objects.isNull(onExp)) {
                        onExp = holderExp;
                    } else {
                        onExp = new AndExpression(onExp, holderExp);
                    }
                }
            }
//            join.setOnExpression(onExp);
            
            finalPlainSelect.setJoins(Arrays.asList(join));
            
            CaseExpression caseResExpression = new CaseExpression();
            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempForAllSource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));
            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(1L));
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            BinaryExpression eqZero = new EqualsTo();
            eqZero.setLeftExpression(count);
            eqZero.setRightExpression(new LongValue(0L));
            caseResExpression.setElseExpression(eqZero);
            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            
//            finalPlainSelect.setGroupByColumnReferences(groupByExpressions);
            
            Select finalSelect = new Select(finalPlainSelect);
            return finalSelect;
        }
    }

    private Statement selectMap(StmVisitor visitor) {
        MyIteratorSource currentIterator = new MyIteratorSource();     
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempSelectSource = new SubSelect();
        tempSelectSource.setSelectBody(source.getSelectBody());
        Alias aliasTempSelectSource = new Alias("TEMP_src");
        tempSelectSource.setAlias(aliasTempSelectSource);
        
//        if(VariableUtils.isSetOfSetAfterCollect(this, visitor)) {
//            throw new SetOfSetException("Invalid set of sets operation");
//        }
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();

        if(visitor.getVisitorContext().stream()
                .map(IteratorSource::getIterator)
                .map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            visitor.getVisitorContext().add(currentIterator);
        }
        SubSelect tempSelectBody = new SubSelect();
        tempSelectBody.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
        Alias aliasTempSelectBody = new Alias("TEMP_body");
        tempSelectBody.setAlias(aliasTempSelectBody);
        
        String currentIter = this.getIterator().getName();
        List<String> fVarsSource = VariableUtils.FVars((MyPlainSelect) tempSelectSource.getSelectBody());
        List<String> fVarsBody = VariableUtils.FVars((MyPlainSelect) tempSelectBody.getSelectBody());
        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if(fVarsSource.isEmpty()) {
                MyPlainSelect gBody = new MyPlainSelect();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItem(new AllColumns());
                
                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);
                
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempSelectBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                gBody.setWhere(bodyWhereExp);
                
                finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter))));
                finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempGBody.getName().concat(".val"))));

                List<String> SVarsSource = VariableUtils.SVars((MyPlainSelect) tempSelectSource.getSelectBody(), visitor.getVisitorContext());
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }
                
                finalPlainSelect.setFromItem(tempGBody);
            }
            else {
                MyPlainSelect gBody = new MyPlainSelect();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItem(new AllColumns());
                
                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);
                
                CaseExpression caseResExpression = new CaseExpression();
                BinaryExpression isValValid = new EqualsTo();
                isValValid.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".val")));
                isValValid.setRightExpression(new LongValue(0L));
                IsNullExpression isBodyRefNull = new IsNullExpression();
                isBodyRefNull.setLeftExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter)));
                BinaryExpression caseBinExp = new OrExpression(isValValid, isBodyRefNull);
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(caseBinExp);
                whenResClause.setThenExpression(new NullValue());
                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResExpression.setElseExpression(new Column(aliasTempSelectSource.getName().concat(".res")));
                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
                
                CaseExpression caseValExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(caseBinExp);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValExpression.setElseExpression(new LongValue(1L));
                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));
                
                BinaryExpression onCondition = new EqualsTo();
                onCondition.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".res")));
                onCondition.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter)));
                
                List<String> SVarsSource = VariableUtils.SVars((MyPlainSelect) tempSelectSource.getSelectBody(), visitor.getVisitorContext());
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                    BinaryExpression binExp = new EqualsTo();
                    binExp.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    binExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                    onCondition = new AndExpression(onCondition, binExp);
                }
                List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempSelectBody.getSelectBody(), visitor.getVisitorContext());
                for(String v : SVarsBody) {
                    if(v.equals(currentIter) || SVarsSource.contains(v)) continue;
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempSelectBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                gBody.setWhere(bodyWhereExp);
                
                finalPlainSelect.setFromItem(tempSelectSource);
                Join join = new Join();
//                join.setLeft(true);
                join.setRightItem(tempGBody);
                finalPlainSelect.setJoins(Arrays.asList(join));
                
                join.setOnExpression(onCondition);
            }
            
        }
//        else {
//            CaseExpression caseResExpression = new CaseExpression();
//            BinaryExpression isValValid = new EqualsTo();
//            isValValid.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".val")));
//            isValValid.setRightExpression(new LongValue(0L));
//            WhenClause whenResClause = new WhenClause();
//            whenResClause.setWhenExpression(isValValid);
//            whenResClause.setThenExpression(new NullValue());
//            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
//            caseResExpression.setElseExpression(new Column(aliasTempSelectBody.getName().concat(".res")));
//            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
//            
//            CaseExpression caseValExpression = new CaseExpression();
//            WhenClause whenValClause = new WhenClause();
//            whenValClause.setWhenExpression(isValValid);
//            whenValClause.setThenExpression(new LongValue(0L));
//            caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
//            caseValExpression.setElseExpression(new Column(aliasTempSelectBody.getName().concat(".val")));
//            finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));
//            
//            finalPlainSelect.setFromItem(tempSelectSource);
//            Join join = new Join();
//            join.setRightItem(tempSelectBody);
//            finalPlainSelect.setJoins(Arrays.asList(join));
//            BinaryExpression additionalMappingConditions = VariableUtils.onMappingCondition(tempSelectSource, tempSelectBody);
//            join.setOnExpression(additionalMappingConditions);
//            
//            List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempSelectSource.getSelectBody(), visitor.getVisitorContext());
//            for(String v : SVarsBody) {
//                if(v.equals(currentIter)) continue;
//                VarSelectExpression newVar = new VarSelectExpression(v);
//                newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
//                finalPlainSelect.addVar(newVar);
//            }
//        }
        // create result
        Select finalSelect = new Select(finalPlainSelect);

        return finalSelect;  
    }

    private Statement notEmptyMap(StmVisitor visitor) {
        Select sizeSourceSelect = (Select) visitor.visit(this.getSource());
        MyPlainSelect sizeSourcePlainSelect = (MyPlainSelect) sizeSourceSelect.getSelectBody();
        
        SubSelect finalSubSelect = new SubSelect();
        finalSubSelect.setSelectBody(sizeSourcePlainSelect);
        Alias aliasFinalSubSelect = new Alias("TEMP_size_source");
        finalSubSelect.setAlias(aliasFinalSubSelect);
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();
        Select finalSelect = new Select(finalPlainSelect);
        
        finalPlainSelect.setFromItem(finalSubSelect);
        ResSelectExpression countRes = new ResSelectExpression();
        Function count = new Function();
        count.setName("COUNT");
        count.setAllColumns(true);
//        count.setParameters(new ExpressionList(Arrays.asList(new Column(aliasFinalSubSelect.getName().concat(".res")))));
        BinaryExpression gtEx = new GreaterThan();
        gtEx.setLeftExpression(count);
        gtEx.setRightExpression(new LongValue(0));
        countRes.setExpression(gtEx);
        finalPlainSelect.setRes(countRes);
        
        if(this.getSource() instanceof PropertyCallExp && 
                ((PropertyCallExp) this.getSource()).getSource() instanceof VariableExp) {
            List<Expression> groupByExps = new ArrayList<Expression>();
            Variable currentVar = ((VariableExp)((PropertyCallExp) this.getSource()).getSource()).getReferredVariable();
            for(IteratorSource iter : visitor.getVisitorContext()) {
                if(!groupByExps.isEmpty()) {
                    groupByExps.add(new Column("ref_".concat(iter.getIterator().getName())));
                }
                else if(iter.getIterator().getName().equals(currentVar.getName())) {
                    groupByExps.add(new Column("ref_".concat(iter.getIterator().getName())));
                }
            }
            finalPlainSelect.setGroupByColumnReferences(groupByExps);
            VariableUtils.reserveVars(finalPlainSelect, finalSubSelect);
        }
        
        return finalSelect;
    }

    private Statement emptyMap(StmVisitor visitor) {
        Select sizeSourceSelect = (Select) visitor.visit(this.getSource());
        MyPlainSelect sizeSourcePlainSelect = (MyPlainSelect) sizeSourceSelect.getSelectBody();
        
        SubSelect finalSubSelect = new SubSelect();
        finalSubSelect.setSelectBody(sizeSourcePlainSelect);
        Alias aliasFinalSubSelect = new Alias("TEMP_size_source");
        finalSubSelect.setAlias(aliasFinalSubSelect);
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();
        Select finalSelect = new Select(finalPlainSelect);
        
        finalPlainSelect.setFromItem(finalSubSelect);
        ResSelectExpression countRes = new ResSelectExpression();
        Function count = new Function();
        count.setName("COUNT");
        count.setAllColumns(true);
//        count.setParameters(new ExpressionList(Arrays.asList(new Column(aliasFinalSubSelect.getName().concat(".res")))));
        BinaryExpression eqEx = new EqualsTo();
        eqEx.setLeftExpression(count);
        eqEx.setRightExpression(new LongValue(0));
        countRes.setExpression(eqEx);
        finalPlainSelect.setRes(countRes);
        
        if(this.getSource() instanceof PropertyCallExp && 
                ((PropertyCallExp) this.getSource()).getSource() instanceof VariableExp) {
            List<Expression> groupByExps = new ArrayList<Expression>();
            Variable currentVar = ((VariableExp)((PropertyCallExp) this.getSource()).getSource()).getReferredVariable();
            for(IteratorSource iter : visitor.getVisitorContext()) {
                if(!groupByExps.isEmpty()) {
                    groupByExps.add(new Column("ref_".concat(iter.getIterator().getName())));
                }
                else if(iter.getIterator().getName().equals(currentVar.getName())) {
                    groupByExps.add(new Column("ref_".concat(iter.getIterator().getName())));
                }
            }
            finalPlainSelect.setGroupByColumnReferences(groupByExps);
            VariableUtils.reserveVars(finalPlainSelect, finalSubSelect);
        }
        
        return finalSelect;
    }

    private Statement collectMap(StmVisitor visitor) {
        MyIteratorSource currentIterator = new MyIteratorSource();     
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempCollectSource = new SubSelect();
        tempCollectSource.setSelectBody(source.getSelectBody());
        Alias aliasTempCollectSource = new Alias("TEMP_src");
        tempCollectSource.setAlias(aliasTempCollectSource);
        
//        if(VariableUtils.isSetOfSetAfterCollect(this, visitor)) {
//            throw new SetOfSetException("Invalid set of sets operation");
//        }
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();

        if(visitor.getVisitorContext().stream()
                .map(IteratorSource::getIterator)
                .map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            visitor.getVisitorContext().add(currentIterator);
        }
        SubSelect tempCollectBody = new SubSelect();
        tempCollectBody.setSelectBody(((Select) visitor.visit(this.getBody())).getSelectBody());
        Alias aliasTempCollectBody = new Alias("TEMP_body");
        tempCollectBody.setAlias(aliasTempCollectBody);
        
        String currentIter = this.getIterator().getName();
//        List<String> fVarsExpression = VariableUtils.FVars(this, new ArrayList<String>());
//        List<String> fVarsBody = VariableUtils.FVars(this.getBody(), new ArrayList<String>());
        List<String> fVarsSource = VariableUtils.FVars((MyPlainSelect) tempCollectSource.getSelectBody());
        List<String> fVarsBody = VariableUtils.FVars((MyPlainSelect) tempCollectBody.getSelectBody());
        
        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempCollectBody.getName().concat(".").concat("res"))));
            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempCollectBody.getName().concat(".val"))));
            finalPlainSelect.setFromItem(tempCollectBody);
            
            List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempCollectBody.getSelectBody(), visitor.getVisitorContext());
            for(String v : SVarsBody) {
                if(v.equals(currentIter)) continue;
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempCollectBody.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }
        }
        else {
            finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempCollectBody.getName().concat(".res"))));
            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempCollectBody.getName().concat(".val"))));
            finalPlainSelect.setFromItem(tempCollectSource);
            Join join = new Join();
            join.setRightItem(tempCollectBody);
            finalPlainSelect.setJoins(Arrays.asList(join));
            
            List<String> SVarsSource = VariableUtils.SVars((MyPlainSelect) tempCollectSource.getSelectBody(), visitor.getVisitorContext());
            List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempCollectBody.getSelectBody(), visitor.getVisitorContext());

            List<String> sVarsIntercept = new ArrayList<String>();
            for(String sr : SVarsBody) {
                if(SVarsSource.contains(sr)) sVarsIntercept.add(sr);
            }
            if(!sVarsIntercept.isEmpty()) {
                join.setLeft(true);
                BinaryExpression onExp = null;
                for(String v : sVarsIntercept) {
                    BinaryExpression holderExp = new EqualsTo();
                    holderExp.setLeftExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
                    holderExp.setRightExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                    if(Objects.isNull(onExp)) {
                        onExp = holderExp;
                    }
                    else {
                        onExp = new AndExpression(onExp, holderExp);
                    }
                }
                join.setOnExpression(onExp);
            }
        }
//        }
//        else {
//            if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
//                CaseExpression caseResExpression = new CaseExpression();
//                BinaryExpression isValValid = new EqualsTo();
//                isValValid.setLeftExpression(new Column(aliasTempCollectSource.getName().concat(".val")));
//                isValValid.setRightExpression(new LongValue(0L));
//                WhenClause whenResClause = new WhenClause();
//                whenResClause.setWhenExpression(isValValid);
//                whenResClause.setThenExpression(new NullValue());
//                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
//                caseResExpression.setElseExpression(new Column(aliasTempCollectBody.getName().concat(".res")));
//                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
//                
//                CaseExpression caseValExpression = new CaseExpression();
//                WhenClause whenValClause = new WhenClause();
//                whenValClause.setWhenExpression(isValValid);
//                whenValClause.setThenExpression(new LongValue(0L));
//                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
//                caseValExpression.setElseExpression(new Column(aliasTempCollectBody.getName().concat(".val")));
//                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));
//                
//                finalPlainSelect.setFromItem(tempCollectSource);
//                Join join = new Join();
//                join.setLeft(true);
//                join.setRightItem(tempCollectBody);
//                finalPlainSelect.setJoins(Arrays.asList(join));
//                BinaryExpression onCondition = new EqualsTo();
//                onCondition.setLeftExpression(new Column(aliasTempCollectSource.getName().concat(".res")));
//                onCondition.setRightExpression(new Column(aliasTempCollectBody.getName().concat(".ref_").concat(currentIter)));
//                BinaryExpression additionalMappingConditions = VariableUtils.onMappingCondition(tempCollectSource, tempCollectBody);
//                join.setOnExpression(new AndExpression(onCondition, additionalMappingConditions));
//                
//                List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempCollectSource.getSelectBody(), visitor.getVisitorContext());
//                for(String v : SVarsBody) {
//                    if(v.equals(currentIter)) continue;
//                    VarSelectExpression newVar = new VarSelectExpression(v);
//                    newVar.setRefExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
//                    finalPlainSelect.addVar(newVar);
//                }
//            }
//            else {
//                CaseExpression caseResExpression = new CaseExpression();
//                BinaryExpression isValValid = new EqualsTo();
//                isValValid.setLeftExpression(new Column(aliasTempCollectSource.getName().concat(".val")));
//                isValValid.setRightExpression(new LongValue(0L));
//                WhenClause whenResClause = new WhenClause();
//                whenResClause.setWhenExpression(isValValid);
//                whenResClause.setThenExpression(new NullValue());
//                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
//                caseResExpression.setElseExpression(new Column(aliasTempCollectBody.getName().concat(".res")));
//                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
//                
//                CaseExpression caseValExpression = new CaseExpression();
//                WhenClause whenValClause = new WhenClause();
//                whenValClause.setWhenExpression(isValValid);
//                whenValClause.setThenExpression(new LongValue(0L));
//                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
//                caseValExpression.setElseExpression(new Column(aliasTempCollectBody.getName().concat(".val")));
//                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));
//                
//                finalPlainSelect.setFromItem(tempCollectSource);
//                Join join = new Join();
//                join.setRightItem(tempCollectBody);
//                finalPlainSelect.setJoins(Arrays.asList(join));
//                BinaryExpression additionalMappingConditions = VariableUtils.onMappingCondition(tempCollectSource, tempCollectBody);
//                join.setOnExpression(additionalMappingConditions);
//                
//                List<String> SVarsBody = VariableUtils.SVars((MyPlainSelect) tempCollectSource.getSelectBody(), visitor.getVisitorContext());
//                for(String v : SVarsBody) {
//                    if(v.equals(currentIter)) continue;
//                    VarSelectExpression newVar = new VarSelectExpression(v);
//                    newVar.setRefExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
//                    finalPlainSelect.addVar(newVar);
//                }
//            }
//        }
        // create result
        Select finalSelect = new Select(finalPlainSelect);

        return finalSelect;  
    }

    private Statement sizeMap(StmVisitor visitor) {
        Select source = (Select) visitor.visit(this.getSource());
        
        SubSelect tempSizeSource = new SubSelect(source.getSelectBody(), "TEMP_src");
        
//        SubSelect tempSizeSource = new SubSelect();
//        tempSizeSource.setSelectBody(source.getSelectBody());
//        Alias aliasTempSizeSource = new Alias("TEMP_src");
//        tempSizeSource.setAlias(aliasTempSizeSource);
        
//        if(VariableUtils.isSetOfSetAfterCollect(this, visitor)) {
//            throw new SetOfSetException("Invalid set of sets operation");
//        }
        
        MyPlainSelect finalPlainSelect = new MyPlainSelect();
        Select finalSelect = new Select(finalPlainSelect);
        
        if(VariableUtils.FVars((MyPlainSelect) tempSizeSource.getSelectBody()).isEmpty()) {
            finalPlainSelect.setFromItem(tempSizeSource);
            ResSelectExpression countRes = new ResSelectExpression();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            countRes.setExpression(count);

            finalPlainSelect.setRes(countRes);
            finalPlainSelect.setValAsTrue();
        }
        else {
            finalPlainSelect.setFromItem(tempSizeSource);
            Alias aliasTempSizeSource = tempSizeSource.getAlias();
            
            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);

            CaseExpression caseResExpression = new CaseExpression();

            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempSizeSource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));

            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(0L));

            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            caseResExpression.setElseExpression(count);

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            finalPlainSelect.setValAsTrue();
            
            List<String> SVarsSource = VariableUtils.SVars((MyPlainSelect) tempSizeSource.getSelectBody(), visitor.getVisitorContext());
            
            List<Expression> groupByExpressions = new ArrayList<Expression>();

            for(String v : SVarsSource) {
                groupByExpressions.add(new Column(aliasTempSizeSource.getName().concat(".ref_").concat(v)));
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempSizeSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }

            groupByExpressions.add(new Column(aliasTempSizeSource.getName().concat(".val")));
            finalPlainSelect.setGroupByColumnReferences(groupByExpressions);
            
        }
        
        ((OCL2SQLParser) visitor).decreaseLevelOfSet();
        
        return finalSelect;
    }
}
