/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.deparser.DeparserVisitor;
import org.vgu.ocl2psql.ocl.deparser.OclExpressionDeParser;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;
import org.vgu.ocl2psql.ocl.exception.SetOfSetException;
import org.vgu.ocl2psql.ocl.impl.OclCollectionSupport;
import org.vgu.ocl2psql.ocl.impl.OclIteratorSupport;
import org.vgu.ocl2psql.ocl.visitor.OCL2SQLParser;
import org.vgu.ocl2psql.sql.statement.select.Join;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.SubSelect;
import org.vgu.ocl2psql.sql.statement.select.TypeSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.VarSelectExpression;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.GroupByElement;


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
    public void accept( DeparserVisitor visitor ) {
        visitor.visit( this );
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	Object source = this.source.eval(context);
	Collection<Object> collection;
	if (source == null) {
	    collection = Collections.emptyList();
	} else if (source.getClass().isArray()) {
	    collection = Arrays.asList((Object[])source);
	} else if (!(source instanceof Collection)) {
	    collection = Collections.singletonList(source);
	} else {
	    collection = (Collection<Object>) source;
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
		if (collection instanceof List) {
		    return OclCollectionSupport.at((List<Object>) collection,
			    (Number) body);
		} else {
		    throw new OclEvaluationException("at cannot be applied to "
			    + collection.getClass().getName());
		}
	    }
	case indexOf:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		if (collection instanceof List) {
		    return OclCollectionSupport.indexOf((List<Object>) collection,
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
		return OclCollectionSupport.count((Collection<Object>) collection,
			body);
	    }
	case first:
	    if (collection instanceof List) {
		return OclCollectionSupport.first((List<Object>) collection);
	    } else if (collection instanceof SortedSet) {
		return OclCollectionSupport.first((SortedSet<Object>) collection);
	    } else {
		throw new OclEvaluationException("first cannot be applied to "
			+ collection.getClass().getName());
	    }
	case last:
	    if (collection instanceof List) {
		return OclCollectionSupport.last((List<Object>) collection);
	    } else if (collection instanceof SortedSet) {
		return OclCollectionSupport.last((SortedSet<Object>) collection);
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
		Collection<Object> bodyCollection;
		if (body == null) {
		    bodyCollection = Collections.emptyList();
		} else if (!(body instanceof Collection)) {
		    bodyCollection = Collections.singletonList(body);
		} else {
		    bodyCollection = (Collection<Object>) body;
		}
		return OclCollectionSupport.union(collection, bodyCollection);
	    }
	case includesAll:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		Collection<Object> bodyCollection;
		if (body == null) {
		    bodyCollection = Collections.emptyList();
		} else if (!(body instanceof Collection)) {
		    bodyCollection = Collections.singletonList(body);
		} else {
		    bodyCollection = (Collection<Object>) body;
		}
		return OclCollectionSupport.includesAll(collection,
			bodyCollection);
	    }
	case excludesAll:
	    if (this.getBody() != null) {
		Object body = this.getBody().eval(context);
		Collection<Object> bodyCollection;
		if (body == null) {
		    bodyCollection = Collections.emptyList();
		} else if (!(body instanceof Collection<?>)) {
		    bodyCollection = Collections.singletonList(body);
		} else {
		    bodyCollection = (Collection<Object>) body;
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
    default:
        break;
	}
	throw new OclEvaluationException("cannot evaluate: '->" + kind.name()
		+ "(" + getIterator().getName() + "|" + getBody() + ")");
    }

    @Override
    public Statement map(StmVisitor visitor) {
        OclExpressionDeParser oclExpressionDeParser = new OclExpressionDeParser();
        this.accept(oclExpressionDeParser);
        String oclExpression = oclExpressionDeParser.getDeParsedStr();
        switch (this.kind) {
        case isEmpty:
            return emptyMap(visitor,oclExpression);
        case notEmpty:
            return notEmptyMap(visitor,oclExpression);
        case select:
            return selectMap(visitor,oclExpression); 
        case reject:
            return rejectMap(visitor,oclExpression);  
        case forAll:
            return forAllMap(visitor,oclExpression);
        case exists:
            return existsMap(visitor,oclExpression);  
        case collect:
            return collectMap(visitor,oclExpression);  
        case size:
            return sizeMap(visitor,oclExpression);
        case asSet:
            return asSetMap(visitor,oclExpression);
        case isUnique:
            return isUniqueMap(visitor,oclExpression);
        case flatten:
            return flattenMap(visitor,oclExpression);
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
    private Statement flattenMap(StmVisitor visitor, String oclExpression) {
        if(this.getSource() instanceof IteratorExp) {
            Select source = (Select) visitor.visit(this.getSource());
            
            PlainSelect finalPlainSelect = new PlainSelect();
            finalPlainSelect.setCorrespondOCLExpression(oclExpression);
            Select finalSelect = new Select();
            finalSelect.setSelectBody(finalPlainSelect);
            
            SubSelect tempFlattenSource = new SubSelect();
            tempFlattenSource.setSelectBody( source.getSelectBody() );
            
            List<String> sVarSource = VariableUtils.FVars(this.getSource());

            if(sVarSource.isEmpty()) {

                Alias aliasTempFlattenSource = new Alias("TEMP_src");
                tempFlattenSource.setAlias(aliasTempFlattenSource);
                
                finalPlainSelect.setFromItem(tempFlattenSource);
                finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempFlattenSource.getName().concat(".res"))));
                finalPlainSelect.createTrueValColumn();
                finalPlainSelect.setType(new TypeSelectExpression(this.getSource()));
                
                BinaryExpression valTrue = new EqualsTo();
                valTrue.setLeftExpression(new Column(aliasTempFlattenSource.getName().concat(".val")));
                valTrue.setRightExpression(new LongValue(1L));
                finalPlainSelect.setWhere(valTrue);
                
                return finalSelect;
            }
            else {
                Alias aliasTempFlattenSource = new Alias("TEMP");
                tempFlattenSource.setAlias(aliasTempFlattenSource);
                
                PlainSelect sCollectvB = new PlainSelect();
                sCollectvB.setAllColumn();
                sCollectvB.setFromItem(tempFlattenSource);

                BinaryExpression valIsOne = new EqualsTo();
                valIsOne.setLeftExpression(new Column(aliasTempFlattenSource.getName().concat(".val")));
                valIsOne.setRightExpression(new LongValue(1L));
                sCollectvB.setWhere(valIsOne);

                SubSelect tempFlat = new SubSelect( sCollectvB, "TEMP_flat" );

                Alias aliasTempFlat = tempFlat.getAlias();
                
                Join join = new Join();
                join.setLeft(true);
                join.setRightItem(tempFlat);

                finalPlainSelect.setJoins(Arrays.asList(join));
                
                PlainSelect s = (PlainSelect) tempFlattenSource.getSelectBody();
                
                SubSelect tempCollectSource = new SubSelect( s, "TEMP_src" );

                finalPlainSelect.setFromItem(tempCollectSource);
                
//                String flattenVar = ((IteratorExp) this.getSource()).getIterator().getName();
                
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
                
                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(isOuterRefNull);
                whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression.setElseExpression(new StringValue(this.getSource().getType()));

                finalPlainSelect.setRes(new ResSelectExpression(caseResVExpression));
                finalPlainSelect.setVal(new ValSelectExpression(caseValVExpression));
                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));
                
                List<String> sVarsSource = VariableUtils.SVars(this.getSource(), visitor);
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
                
                this.setType(this.getSource().getType());
                ((OCL2SQLParser) visitor).decreaseLevelOfSet();
                        
                return finalSelect;
            }
        }
        throw new SetOfSetException("The source is not set of set to be flattened");
    }

    private Statement isUniqueMap(StmVisitor visitor, String oclExpression) {
        Select sizeSourceSelect = (Select) visitor.visit(this.getSource());
        PlainSelect sizeSourcePlainSelect = (PlainSelect) sizeSourceSelect.getSelectBody();
        
        SubSelect finalSubSelect = new SubSelect();
        finalSubSelect.setSelectBody(sizeSourcePlainSelect);
        Alias aliasFinalSubSelect = new Alias("TEMP_unique_source");
        finalSubSelect.setAlias(aliasFinalSubSelect);
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);
        Select finalSelect = new Select();
        finalSelect.setSelectBody(finalPlainSelect);
        
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
        
        this.setType("Boolean");
        finalPlainSelect.setType(new TypeSelectExpression(this));
        return finalSelect;
    }

    private Statement rejectMap(StmVisitor visitor, String oclExpression) {
        MyIteratorSource currentIterator = new MyIteratorSource();
        currentIterator.setSourceExpression(this.getSource());
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempSelectSource = new SubSelect();
        tempSelectSource.setSelectBody(source.getSelectBody());
        Alias aliasTempSelectSource = new Alias("TEMP_src");
        tempSelectSource.setAlias(aliasTempSelectSource);
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);

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

//        List<String> fVarsSource = VariableUtils.FVars((PlainSelect) tempSelectSource.getSelectBody());
//        List<String> fVarsBody = VariableUtils.FVars((PlainSelect) tempSelectBody.getSelectBody());
        List<String> fVarsSource = VariableUtils.FVars(this.getSource());
        List<String> fVarsBody = VariableUtils.FVars(this.getBody());

        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if(fVarsSource.isEmpty()) {
                PlainSelect gBody = new PlainSelect();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItems(new AllColumns());
                
                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);
                
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempSelectBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(0L));
                
                IsNullExpression isNullExpr = new IsNullExpression();
                isNullExpr.setLeftExpression( new Column(aliasTempSelectBody.getName().concat(".res")) );
                
                OrExpression orExpr = new OrExpression( bodyWhereExp, isNullExpr );

                gBody.setWhere(orExpr);
                
                finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter))));
                finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempGBody.getName().concat(".val"))));
                finalPlainSelect.setType(new TypeSelectExpression(this.getSource()));
                
                List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }
                
                finalPlainSelect.setFromItem(tempGBody);
            }
            else {
                PlainSelect gBody = new PlainSelect();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItems(new AllColumns());
                
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
                
                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(caseBinExp);
                whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression.setElseExpression(new StringValue(this.getSource().getType()));
                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));
                
                BinaryExpression onCondition = new EqualsTo();
                onCondition.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".res")));
                onCondition.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter)));
                
                List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                    BinaryExpression binExp = new EqualsTo();
                    binExp.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    binExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                    onCondition = new AndExpression(onCondition, binExp);
                }
                List<String> SVarsBody = VariableUtils.SVars(this.getBody(), visitor);
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

                join.setRightItem(tempGBody);
                finalPlainSelect.setJoins(Arrays.asList(join));
                
                join.setOnExpression(onCondition);
            }
            
        }

        this.setType(this.getSource().getType());
        // create result
        Select finalSelect = new Select();
        finalSelect.setSelectBody( finalPlainSelect );

        return finalSelect;  
    }

    private Statement asSetMap(StmVisitor visitor, String oclExpression) {
        this.setType(this.getSource().getType());
        Select source = (Select) visitor.visit(this.getSource());
        
        SubSelect tempAsSetSource = new SubSelect(source.getSelectBody(), "TEMP_src");
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);
        Select finalSelect = new Select();
        finalSelect.setSelectBody( finalPlainSelect );
        
        finalPlainSelect.setFromItem(tempAsSetSource);
        
        Distinct distinct = new Distinct();
        finalPlainSelect.setDistinct(distinct);
        
        VariableUtils.reserveVars(finalPlainSelect, tempAsSetSource);
        finalPlainSelect.setRes(new ResSelectExpression(new Column("TEMP_src.res")));
        finalPlainSelect.setVal(new ValSelectExpression(new Column("TEMP_src.val")));
        finalPlainSelect.setType(new TypeSelectExpression(new Column("TEMP_src.type")));
        
        return finalSelect;
    }

    private Statement existsMap(StmVisitor visitor, String oclExpression) {
        this.setType("Boolean");
        MyIteratorSource currentIterator = new MyIteratorSource();
        currentIterator.setSourceExpression(this.getSource());
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempExistsSource = new SubSelect();
        tempExistsSource.setSelectBody(source.getSelectBody());
        Alias aliasTempExistsSource = new Alias("TEMP_src");
        tempExistsSource.setAlias(aliasTempExistsSource);
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);

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
        List<String> fVarsSource = VariableUtils.FVars(this.getSource());
        List<String> fVarsBody = VariableUtils.FVars(this.getBody());
        finalPlainSelect.setType(new TypeSelectExpression(this));
        
        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if(fVarsSource.isEmpty() && fVarsBody.size() == 1) {
                finalPlainSelect.createTrueValColumn();
                
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
                finalPlainSelect.createTrueValColumn();
                
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
                
                PlainSelect gBody = new PlainSelect();
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
                
                List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
                List<Expression> groupByExpressions = new ArrayList<Expression>();
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
                    gBody.addVar(newVar);
                    groupByExpressions.add(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
                }

                GroupByElement groupByElement = new GroupByElement();
                groupByElement.setGroupByExpressions( groupByExpressions );
                gBody.setGroupByElement( groupByElement );
                
                List<String> SVarsBody = VariableUtils.SVars(this.getBody(), visitor);

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
            Select finalSelect = new Select();
            finalSelect.setSelectBody( finalPlainSelect );
            return finalSelect;
        }
        else {
            finalPlainSelect.createTrueValColumn();
            
            List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
            for(String v : SVarsSource) {
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempExistsSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }
            List<String> SVarsBody = VariableUtils.SVars(this.getBody(), visitor);

            BinaryExpression bodyWhereExp = new EqualsTo();
            bodyWhereExp.setLeftExpression(new Column(aliasTempExistsBody.getName().concat(".res")));
            bodyWhereExp.setRightExpression(new LongValue(1L));
            finalPlainSelect.setWhere(bodyWhereExp);
            
            finalPlainSelect.setFromItem(tempExistsSource);
            Join join = new Join();
            join.setRightItem(tempExistsBody);
            
            BinaryExpression onExp = null;
            
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
            
            Select finalSelect = new Select();
            finalSelect.setSelectBody( finalPlainSelect );
            return finalSelect;
        }
    }

    private Statement forAllMap(StmVisitor visitor, String oclExpression) {
        this.setType("Boolean");
        MyIteratorSource currentIterator = new MyIteratorSource();
        currentIterator.setSourceExpression(this.getSource());
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempForAllSource = new SubSelect();
        tempForAllSource.setSelectBody(source.getSelectBody());
        Alias aliasTempForAllSource = new Alias("TEMP_src");
        tempForAllSource.setAlias(aliasTempForAllSource);
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);

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
        List<String> fVarsSource = VariableUtils.FVars(this.getSource());
        List<String> fVarsBody = VariableUtils.FVars( this.getBody() );
        finalPlainSelect.setType(new TypeSelectExpression(this));
        
        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if(fVarsSource.isEmpty() && fVarsBody.size() == 1) {
                finalPlainSelect.createTrueValColumn();
                
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
                finalPlainSelect.createTrueValColumn();
                
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
                
                PlainSelect gBody = new PlainSelect();
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
                
                List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
                List<Expression> groupByExpressions = new ArrayList<Expression>();
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
                    gBody.addVar(newVar);
                    groupByExpressions.add(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
                }
                GroupByElement groupByElement = new GroupByElement();
                groupByElement.setGroupByExpressions( groupByExpressions );
                gBody.setGroupByElement( groupByElement );
                
                
                List<String> SVarsBody = VariableUtils.SVars(this.getBody(), visitor);

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
            Select finalSelect = new Select();
            finalSelect.setSelectBody( finalPlainSelect );
            return finalSelect;
        }
        else {
            finalPlainSelect.createTrueValColumn();
            
            List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
            for(String v : SVarsSource) {
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempForAllSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }
            List<String> SVarsBody = VariableUtils.SVars(this.getBody(), visitor);

            BinaryExpression bodyWhereExp = new EqualsTo();
            bodyWhereExp.setLeftExpression(new Column(aliasTempForAllBody.getName().concat(".res")));
            bodyWhereExp.setRightExpression(new LongValue(0L));
            finalPlainSelect.setWhere(bodyWhereExp);
            
            finalPlainSelect.setFromItem(tempForAllSource);
            Join join = new Join();
            join.setRightItem(tempForAllBody);
            
            BinaryExpression onExp = null;

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
            
            Select finalSelect = new Select();
            finalSelect.setSelectBody( finalPlainSelect );
            return finalSelect;
        }
    }

    private Statement selectMap(StmVisitor visitor, String oclExpression) {
        MyIteratorSource currentIterator = new MyIteratorSource();     
        currentIterator.setSourceExpression(this.getSource());
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempSelectSource = new SubSelect();
        tempSelectSource.setSelectBody(source.getSelectBody());
        Alias aliasTempSelectSource = new Alias("TEMP_src");
        tempSelectSource.setAlias(aliasTempSelectSource);
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);

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
        List<String> fVarsSource = VariableUtils.FVars(this.getSource());
        List<String> fVarsBody = VariableUtils.FVars(this.getBody());
        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if(fVarsSource.isEmpty()) {
                PlainSelect gBody = new PlainSelect();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItems(new AllColumns());
                
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
                finalPlainSelect.setType(new TypeSelectExpression(this.getSource()));
                
                List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }
                
                finalPlainSelect.setFromItem(tempGBody);
            }
            else {
                PlainSelect gBody = new PlainSelect();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItems(new AllColumns());
                
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
                
                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(caseBinExp);
                whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression.setElseExpression(new StringValue(this.getSource().getType()));
                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));
                
                BinaryExpression onCondition = new EqualsTo();
                onCondition.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".res")));
                onCondition.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter)));
                
                List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
                for(String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                    BinaryExpression binExp = new EqualsTo();
                    binExp.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    binExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                    onCondition = new AndExpression(onCondition, binExp);
                }
                List<String> SVarsBody = VariableUtils.SVars(this.getBody(), visitor);
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

                join.setRightItem(tempGBody);
                finalPlainSelect.setJoins(Arrays.asList(join));
                
                join.setOnExpression(onCondition);
            }
            
        }

        // create result
        Select finalSelect = new Select();
        finalSelect.setSelectBody( finalPlainSelect );

        return finalSelect;  
    }

    private Statement notEmptyMap(StmVisitor visitor, String oclExpression) {
        this.setType("Boolean");
        Select source = (Select) visitor.visit(this.getSource());
        
        SubSelect tempNotEmptySource = new SubSelect(source.getSelectBody(), "TEMP_src");
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);
        Select finalSelect = new Select();
        finalSelect.setSelectBody( finalPlainSelect );
        
        if(VariableUtils.FVars(this.getSource()).isEmpty()) {
            finalPlainSelect.setFromItem(tempNotEmptySource);
            ResSelectExpression countRes = new ResSelectExpression();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            
            BinaryExpression isNotEqualsZero = new NotEqualsTo();
            isNotEqualsZero.setLeftExpression(count);
            isNotEqualsZero.setRightExpression(new LongValue(0L));
            countRes.setExpression(isNotEqualsZero);

            finalPlainSelect.setRes(countRes);
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(this));
        }
        else {
            finalPlainSelect.setFromItem(tempNotEmptySource);
            Alias aliasTempNotEmptySource = tempNotEmptySource.getAlias();
            
            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);

            CaseExpression caseResExpression = new CaseExpression();

            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempNotEmptySource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));

            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(0L));
            
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            caseResExpression.setElseExpression(new LongValue(1L));

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(this));
            
            List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
            
            List<Expression> groupByExpressions = new ArrayList<Expression>();

            for(String v : SVarsSource) {
                groupByExpressions.add(new Column(aliasTempNotEmptySource.getName().concat(".ref_").concat(v)));
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempNotEmptySource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }

            groupByExpressions.add(new Column(aliasTempNotEmptySource.getName().concat(".val")));

            GroupByElement groupByElement = new GroupByElement();
            groupByElement.setGroupByExpressions( groupByExpressions );
            finalPlainSelect.setGroupByElement(groupByElement);
            
        }
        
        ((OCL2SQLParser) visitor).decreaseLevelOfSet();
        
        return finalSelect;
    }

    private Statement emptyMap(StmVisitor visitor, String oclExpression) {
        this.setType("Boolean");
        Select source = (Select) visitor.visit(this.getSource());
        
        SubSelect tempEmptySource = new SubSelect(source.getSelectBody(), "TEMP_src");
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);
        Select finalSelect = new Select();
        finalSelect.setSelectBody( finalPlainSelect );
        
        if(VariableUtils.FVars(this.getSource()).isEmpty()) {
            finalPlainSelect.setFromItem(tempEmptySource);
            ResSelectExpression countRes = new ResSelectExpression();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            
            BinaryExpression isEqualsZero = new EqualsTo();
            isEqualsZero.setLeftExpression(count);
            isEqualsZero.setRightExpression(new LongValue(0L));
            countRes.setExpression(isEqualsZero);

            finalPlainSelect.setRes(countRes);
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(this));
        }
        else {
            finalPlainSelect.setFromItem(tempEmptySource);
            Alias aliasTempEmptySource = tempEmptySource.getAlias();
            
            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);

            CaseExpression caseResExpression = new CaseExpression();

            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempEmptySource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));

            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(1L));
            
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            caseResExpression.setElseExpression(new LongValue(0L));

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(this));
            
            List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
            
            List<Expression> groupByExpressions = new ArrayList<Expression>();

            for(String v : SVarsSource) {
                groupByExpressions.add(new Column(aliasTempEmptySource.getName().concat(".ref_").concat(v)));
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempEmptySource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }

            groupByExpressions.add(new Column(aliasTempEmptySource.getName().concat(".val")));

            GroupByElement groupByElement = new GroupByElement();
            groupByElement.setGroupByExpressions( groupByExpressions );
            finalPlainSelect.setGroupByElement(groupByElement);
            
        }
        
        ((OCL2SQLParser) visitor).decreaseLevelOfSet();
        
        return finalSelect;
    }

    private Statement collectMap(StmVisitor visitor, String oclExpression) {
        MyIteratorSource currentIterator = new MyIteratorSource(); 
        currentIterator.setSourceExpression(this.getSource());
        currentIterator.setIterator(this.getIterator());  
        Select source = (Select) visitor.visit(this.getSource());
        currentIterator.setSource(source);
        
        SubSelect tempCollectSource = new SubSelect();
        tempCollectSource.setSelectBody(source.getSelectBody());
        Alias aliasTempCollectSource = new Alias("TEMP_src");
        tempCollectSource.setAlias(aliasTempCollectSource);
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);

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
        
        this.setType(this.getBody().getType());
        
        String currentIter = this.getIterator().getName();
//        List<String> fVarsSource = VariableUtils.FVars(this.getSource());
        List<String> fVarsBody = VariableUtils.FVars(this.getBody());
        
        if(VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempCollectBody.getName().concat(".res"))));
            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempCollectBody.getName().concat(".val"))));
            finalPlainSelect.setType(new TypeSelectExpression(new Column(aliasTempCollectBody.getName().concat(".type"))));
            finalPlainSelect.setFromItem(tempCollectBody);
            
            List<String> SVarsBody = VariableUtils.SVars(this.getBody(), visitor);
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
            finalPlainSelect.setType(new TypeSelectExpression(new Column(aliasTempCollectBody.getName().concat(".type"))));
            finalPlainSelect.setFromItem(tempCollectSource);
            Join join = new Join();
            join.setRightItem(tempCollectBody);
            finalPlainSelect.setJoins(Arrays.asList(join));
            
            List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
            List<String> SVarsBody = VariableUtils.SVars(this.getBody(), visitor);

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
        // create result
        Select finalSelect = new Select();
        finalSelect.setSelectBody( finalPlainSelect );

        return finalSelect;  
    }

    private Statement sizeMap(StmVisitor visitor, String oclExpression) {
        this.setType("Integer");
        Select source = (Select) visitor.visit(this.getSource());
        
        SubSelect tempSizeSource = new SubSelect(source.getSelectBody(), "TEMP_src");
        
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.setCorrespondOCLExpression(oclExpression);
        Select finalSelect = new Select();
        finalSelect.setSelectBody( finalPlainSelect );
        
        if(VariableUtils.FVars(this.getSource()).isEmpty()) {
            finalPlainSelect.setFromItem(tempSizeSource);
            ResSelectExpression countRes = new ResSelectExpression();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            countRes.setExpression(count);

            finalPlainSelect.setRes(countRes);
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(this));
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
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(this));
            
            List<String> SVarsSource = VariableUtils.SVars(this.getSource(), visitor);
            
            List<Expression> groupByExpressions = new ArrayList<Expression>();

            for(String v : SVarsSource) {
                groupByExpressions.add(new Column(aliasTempSizeSource.getName().concat(".ref_").concat(v)));
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempSizeSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }

            groupByExpressions.add(new Column(aliasTempSizeSource.getName().concat(".val")));

            GroupByElement groupByElement = new GroupByElement();
            groupByElement.setGroupByExpressions( groupByExpressions );
            finalPlainSelect.setGroupByElement(groupByElement);
            
        }
        
        ((OCL2SQLParser) visitor).decreaseLevelOfSet();
        
        return finalSelect;
    }
}
