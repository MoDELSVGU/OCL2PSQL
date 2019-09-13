package org.vgu.sqlsi.sql.statement.select;

import java.util.LinkedList;
import java.util.Objects;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class MyPlainSelect extends PlainSelect {
    private LinkedList<VarSelectExpression> vars = new LinkedList<VarSelectExpression>();
    
    public MyPlainSelect() {
        super();
        this.setValAsTrue();
    }
    
    public void setAllColumn() {
        this.getSelectItems().clear();
        this.addSelectItems(new AllColumns());
    }
    
    public void setValAsTrue() {
        ValSelectExpression val = new ValSelectExpression(new LongValue(1L));
        this.setVal(val);
    }

    public ResSelectExpression getRes() {
        if(Objects.isNull(super.getSelectItems())) {
            return null;
        } else {
            return super.getSelectItems().stream()
            .filter(item -> item instanceof ResSelectExpression)
            .map(ResSelectExpression.class::cast)
            .findFirst()
            .orElse(null);
        }
    }
    
    public ValSelectExpression getVal() {
        if(Objects.isNull(super.getSelectItems())) {
            return null;
        } else {
            return super.getSelectItems().stream()
            .filter(item -> item instanceof ValSelectExpression)
            .map(ValSelectExpression.class::cast)
            .findFirst()
            .orElse(null);
        }
    }
    
    public void setRes(ResSelectExpression res) {
        ResSelectExpression curRes = this.getRes();
        if(Objects.isNull(curRes)) {
            super.addSelectItems(res);
        } else {
            curRes = res;
        }
    }
    
    public void setVal(ValSelectExpression val) {
        ValSelectExpression curVal = this.getVal();
        if(Objects.isNull(curVal)) {
            super.addSelectItems(val);
        } else {
            curVal.setExpression(val.getExpression());
        }
    }

    public void addVar(VarSelectExpression varSelectExpression) {
        this.vars.add(varSelectExpression);
        if(Objects.isNull(this.getSelectItems())) {
            this.setSelectItems(new LinkedList<>());
        }
        this.getSelectItems().add(varSelectExpression.getRef());
    }
    
    public LinkedList<VarSelectExpression> getVars() {
        return this.vars;
    }
    
}
