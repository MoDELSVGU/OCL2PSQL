package org.vgu.sqlsi.ocl.expressions;

import java.util.Objects;

import org.vgu.sqlsi.sql.statement.select.MyPlainSelect;
import org.vgu.sqlsi.sql.statement.select.ResSelectExpression;
import org.vgu.sqlsi.sql.statement.select.ValSelectExpression;
import org.vgu.sqlsi.sql.statement.select.VarSelectExpression;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

public class MyIteratorSource extends IteratorSource{
    
    private Select sourceWithoutIter;
    
    public Select getSourceWithoutIter() {
        return sourceWithoutIter;
    }

    public void setSourceWithoutIter(Select sourceWithoutIter) {
        this.sourceWithoutIter = sourceWithoutIter;
    }

    @Override
    public void setSource(Select statement) {
        if(Objects.nonNull(this.getIterator())) {
            MyPlainSelect selectBody = (MyPlainSelect) statement.getSelectBody();
            if(selectBody.getFromItem() instanceof Table) {
                MyPlainSelect finalPlainSelect = new MyPlainSelect();
                finalPlainSelect.getSelectItems().clear();
                for(SelectItem item : selectBody.getSelectItems()) {
                    finalPlainSelect.addSelectItems(item);
                }
                finalPlainSelect.setFromItem(selectBody.getFromItem());
                finalPlainSelect.setWhere(selectBody.getWhere());
                finalPlainSelect.setJoins(selectBody.getJoins());
                
                finalPlainSelect.setGroupByElement( selectBody.getGroupBy() );
//                finalPlainSelect.setGroupByColumnReferences(selectBody.getGroupByColumnReferences());
                
                VarSelectExpression newVar = new VarSelectExpression(this.getIterator().getName());
                newVar.setRefExpression(selectBody.getRes().getExpression());
                finalPlainSelect.addVar(newVar);
                Select finalSelect = new Select();
                finalSelect.setSelectBody(finalPlainSelect);
                
                this.setSourceWithoutIter(statement);
                super.setSource(finalSelect);
                return;
            }
            else {
                SubSelect dmn = new SubSelect();
                dmn.setSelectBody(selectBody);
                Alias temp_dmn = new Alias("TEMP_dmn");
                dmn.setAlias(temp_dmn);
                MyPlainSelect finalPlainSelect = new MyPlainSelect();
                
                VarSelectExpression varSelectExpression = new VarSelectExpression(this.getIterator().getName());
                varSelectExpression.setRefExpression(new Column(temp_dmn.getName().concat(".res")));
                
                finalPlainSelect.setRes(new ResSelectExpression(new Column(temp_dmn.getName().concat(".res"))));
                finalPlainSelect.setVal(new ValSelectExpression(new Column(temp_dmn.getName().concat(".val"))));
                
                finalPlainSelect.addVar(varSelectExpression);
                finalPlainSelect.setFromItem(dmn);
                
                VariableUtils.reserveVars(finalPlainSelect, dmn);
                Select finalSelect = new Select();
                finalSelect.setSelectBody(finalPlainSelect);
                this.setSourceWithoutIter(statement);
                super.setSource(finalSelect);
                return;
            }
        }
        super.setSource(statement);
        this.setSourceWithoutIter(statement);
    }
}
