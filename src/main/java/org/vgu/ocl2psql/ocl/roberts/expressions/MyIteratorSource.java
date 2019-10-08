package org.vgu.ocl2psql.ocl.roberts.expressions;

import java.util.Objects;

import org.vgu.ocl2psql.ocl.roberts.utils.VariableUtils;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.SubSelect;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.VarSelectExpression;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SelectItem;

public class MyIteratorSource extends IteratorSource{
    
    private Select sourceWithoutIter;
    private OclExpression sourceExpression;
    
    public Select getSourceWithoutIter() {
        return sourceWithoutIter;
    }

    public void setSourceWithoutIter(Select sourceWithoutIter) {
        this.sourceWithoutIter = sourceWithoutIter;
    }

    @Override
    public void setSource(Select statement) {
        if(Objects.nonNull(this.getIterator())) {
            PlainSelect selectBody = (PlainSelect) statement.getSelectBody();
            if(selectBody.getFromItem() instanceof Table) {
                PlainSelect finalPlainSelect = new PlainSelect();
                finalPlainSelect.getSelectItems().clear();
                for(SelectItem item : selectBody.getSelectItems()) {
                    finalPlainSelect.addSelectItems(item);
                }
                finalPlainSelect.setFromItem(selectBody.getFromItem());
                finalPlainSelect.setWhere(selectBody.getWhere());
                finalPlainSelect.setJoins(selectBody.getJoins());
                
                finalPlainSelect.setGroupByElement( selectBody.getGroupBy() );
                
                VarSelectExpression newVar = new VarSelectExpression(this.getIterator().getName());
                newVar.setRefExpression(selectBody.getRes().getExpression());
                finalPlainSelect.addVar(newVar);
                Select finalSelect = new Select();
                finalSelect.setSelectBody(finalPlainSelect);
                
                Select finalSelectWithoutIter = new Select();
                finalSelectWithoutIter.setSelectBody(statement.getSelectBody());
                this.setSourceWithoutIter(finalSelectWithoutIter);
                super.setSource(finalSelect);
                return;
            }
            else {
                SubSelect dmn = new SubSelect();
                dmn.setSelectBody(selectBody);
                Alias temp_dmn = new Alias("TEMP_dmn");
                dmn.setAlias(temp_dmn);
                PlainSelect finalPlainSelect = new PlainSelect();
                
                VarSelectExpression varSelectExpression = new VarSelectExpression(this.getIterator().getName());
                varSelectExpression.setRefExpression(new Column(temp_dmn.getName().concat(".res")));
                
                finalPlainSelect.setRes(new ResSelectExpression(new Column(temp_dmn.getName().concat(".res"))));
                finalPlainSelect.setVal(new ValSelectExpression(new Column(temp_dmn.getName().concat(".val"))));
                
                finalPlainSelect.addVar(varSelectExpression);
                finalPlainSelect.setFromItem(dmn);
                
                VariableUtils.reserveVars(finalPlainSelect, dmn);
                Select finalSelect = new Select();
                finalSelect.setSelectBody(finalPlainSelect);
                
                Select finalSelectWithoutIter = new Select();
                finalSelectWithoutIter.setSelectBody(statement.getSelectBody());
                this.setSourceWithoutIter(finalSelectWithoutIter);
                super.setSource(finalSelect);
                return;
            }
        }
        super.setSource(statement);
        this.setSourceWithoutIter(statement);
    }

    public OclExpression getSourceExpression() {
        return sourceExpression;
    }

    public void setSourceExpression(OclExpression sourceExpression) {
        this.sourceExpression = sourceExpression;
    }
}
