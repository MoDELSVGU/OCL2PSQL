/**************************************************************************
Copyright 2019 Vietnamese-German-University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
***************************************************************************/

package org.vgu.sqlsi.ocl.expressions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.vgu.sqlsi.ocl.visitor.OCL2SQLParser;
import org.vgu.sqlsi.sql.statement.select.MyPlainSelect;
import org.vgu.sqlsi.sql.statement.select.RefSelectExpression;
import org.vgu.sqlsi.sql.statement.select.VarSelectExpression;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

public class VariableUtils {
    public static BinaryExpression onMappingCondition(SubSelect mainSubSelect, SubSelect joinSubSelect) {
        MyPlainSelect selectBodyLeft = (MyPlainSelect) mainSubSelect.getSelectBody();
        MyPlainSelect selectBodyRight = (MyPlainSelect) joinSubSelect.getSelectBody();
        BinaryExpression onCondition = null;
        
        for(VarSelectExpression selectItemLeft : selectBodyLeft.getVars()) {
                for(VarSelectExpression selectItemRight : selectBodyRight.getVars()) {
                    if(selectItemLeft.equals(selectItemRight)) {
                        BinaryExpression bexp = new EqualsTo();
                        bexp.setLeftExpression(new Column(mainSubSelect.getAlias().getName().concat(".").concat(selectItemLeft.getRef().getAlias().getName())));
                        bexp.setRightExpression(new Column(joinSubSelect.getAlias().getName().concat(".").concat(selectItemRight.getRef().getAlias().getName())));
                        if(Objects.isNull(onCondition)) {
                            onCondition = bexp;
                        } else {
                            onCondition = new AndExpression(onCondition, bexp);
                        }
                    }
                }
        }
        return onCondition;
    }
    
    public static void reserveVars(MyPlainSelect target, SubSelect source){
        MyPlainSelect selectBody = (MyPlainSelect) source.getSelectBody();
        List<VarSelectExpression> targetRefList = target.getVars();
        for(VarSelectExpression var : selectBody.getVars()) {
                if(!targetRefList.contains(var)) {
                    VarSelectExpression newVar = new VarSelectExpression(var.getVar());
                    newVar.setRefExpression(new Column(source.getAlias().getName().concat(".").concat(var.getRef().getAlias().getName())));
                    target.addVar(newVar);
                }
        }
    }

    public static SelectBody findMappingVars(StmVisitor visitor, String variable) {
        for(IteratorSource iter : visitor.getVisitorContext()) {
            MyIteratorSource myIter = (MyIteratorSource) iter;
            if(myIter.getIterator().getName().equals(variable)) {
                return myIter.getSource().getSelectBody();
            }
        }
        return null;
    }

    public static void reserveVarsForCollect(MyPlainSelect target, SubSelect source, Variable iterator) {
        MyPlainSelect selectBody = (MyPlainSelect) source.getSelectBody();
        LinkedList<VarSelectExpression> targetRefList = target.getVars();
        for(VarSelectExpression var : selectBody.getVars()) {
            if(!targetRefList.contains(var)) {
                VarSelectExpression newVar = new VarSelectExpression(var.getVar());
                newVar.setRefExpression(new Column(source.getAlias().getName().concat(".").concat(var.getRef().getAlias().getName())));
                target.addVar(newVar);
                if(iterator.getName().equals(var.getVar())) {
                }
            }
        }
    }
    
    public static void reserveVarsExcludeOne(MyPlainSelect target, SubSelect source, Variable iterator) {
        MyPlainSelect selectBody = (MyPlainSelect) source.getSelectBody();
        LinkedList<VarSelectExpression> targetRefList = target.getVars();
        for(VarSelectExpression var : selectBody.getVars()) {
            if(!targetRefList.contains(var)) {
                if(iterator.getName().equals(var.getVar())) {
                    continue;
                }
                VarSelectExpression newVar = new VarSelectExpression(var.getVar());
                newVar.setRefExpression(new Column(source.getAlias().getName().concat(".").concat(var.getRef().getAlias().getName())));
                target.addVar(newVar);
            }
        }
    }
    
    public static Variable getCurrentVariable(OclExpression currentExpression) {
        if(currentExpression instanceof PropertyCallExp) {
            PropertyCallExp propertyCallExp = (PropertyCallExp) currentExpression;
            return Optional.ofNullable(propertyCallExp)
                    .map(PropertyCallExp::getSource)
                    .filter(oclExpression -> oclExpression instanceof VariableExp)
                    .map(VariableExp.class::cast)
                    .map(VariableExp::getReferredVariable)
                    .orElse(null);
        }
        else {
            return null;
        }
    }
    
    public static boolean containsNoVariable(Select select) {
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        for(SelectItem item : plainSelect.getSelectItems()) {
            if(item instanceof RefSelectExpression) {
                RefSelectExpression refExpression = (RefSelectExpression) item;
                if(!refExpression.getAlias().getName().contains("closed"))
                    return false;
            }
        }
        return true;
    }

    public static String getOuterVariable(IteratorExp iteratorExp) {
        PropertyCallExp propertyCallExp = (PropertyCallExp) iteratorExp.getSource();
        VariableExp variableExp = (VariableExp) propertyCallExp.getSource();
        return variableExp.getReferredVariable().getName();
    }
    
    public static List<Expression> getGroupingVariablesExcludeOne(MyPlainSelect bodyBooleanExp, Variable excludeVariable) {
        List<Expression> groupByExps = new ArrayList<Expression>();
        
        for(VarSelectExpression var : bodyBooleanExp.getVars()) {
            if(!var.getVar().equals(excludeVariable.getName())) {
                groupByExps.add(var.getRef().getExpression());
            }
        }
        return groupByExps;
    }
    
    public static List<Expression> getGroupingVariables(PlainSelect bodyBooleanExp) {
        List<Expression> groupByExps = new ArrayList<Expression>();
        
        for(SelectItem item : bodyBooleanExp.getSelectItems()) {
            if(item instanceof RefSelectExpression) {
                groupByExps.add(new Column(((RefSelectExpression) item).getAlias().getName()));
            }
        }
        return groupByExps;
    }

    public static boolean isSourceAClassAllInstances(SelectBody selectBody, String className) {
        MyPlainSelect plainSelectVar = (MyPlainSelect) selectBody;
        return (plainSelectVar.getFromItem() instanceof Table) && ((Table) plainSelectVar.getFromItem()).getName().equals(className);
    }

    public static IteratorSource getSourceLevelVar(StmVisitor visitor) {
        int currentLevel = ((OCL2SQLParser) visitor).getLevelOfSets();
        return visitor.getVisitorContext().get(currentLevel-2);
    }

    public static boolean isSetOfSetAfterCollect(IteratorExp iteratorExp, StmVisitor visitor) {
        if(iteratorExp.getSource() instanceof IteratorExp) {
            IteratorExp insideIter = (IteratorExp) iteratorExp.getSource();
            if(insideIter.kind == IteratorKind.collect) {
                if(((OCL2SQLParser) visitor).getLevelOfSets() > 1) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static List<String> FVars(MyPlainSelect selectBody) {
        return selectBody.getVars().stream().map(var -> var.getVar()).collect(Collectors.toList());
    }
    
    public static boolean isVariableOf (List<String> vars, String var) {
        return vars.stream().anyMatch(s -> var.equals(s));
    }

    public static List<String> SVars(MyPlainSelect selectBody, List<IteratorSource> varList) {
        List<String> FVars = FVars(selectBody);
        if(FVars.isEmpty()) return new ArrayList<String>();
        List<String> SVars = new ArrayList<String>();
        for(String var : FVars) {
            if(!SVars.contains(var))
                SVars.add(var);
            MyPlainSelect sourceVar = (MyPlainSelect) varList.stream().filter(v -> v.getIterator().getName().equals(var))
                    .findFirst().map(MyIteratorSource.class::cast).map(myIter -> myIter.getSourceWithoutIter().getSelectBody()).get();
            SVars.addAll(SVars(sourceVar, varList));
        }
        return SVars;
    }

//    public static List<String> FVars(OclExpression ocl, List<String> boundVars) {
//        if(ocl instanceof IteratorExp) {
//            IteratorExp iteratorExp = (IteratorExp) ocl;
//            boundVars.add(iteratorExp.getIterator().getName());
//            List<>
//            return FVars(iteratorExp.getSource(), boundVars).
//        }
//        return null;
//    }
}
