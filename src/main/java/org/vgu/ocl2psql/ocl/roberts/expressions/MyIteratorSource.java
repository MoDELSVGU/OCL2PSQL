package org.vgu.ocl2psql.ocl.roberts.expressions;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.vgu.ocl2psql.ocl.roberts.utils.VariableUtils;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.RefSelectExpression;
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
    
    private OclExpression sourceExpression;
    
    public Select getSourceWithoutIter() {
        Select newSelect = new Select();
        newSelect.setSelectBody(this.getSource().getSelectBody());
        PlainSelect newPlainSelect = (PlainSelect) newSelect.getSelectBody();
        List<SelectItem> items = newPlainSelect.getSelectItems().stream()
                .filter(i -> !(i instanceof RefSelectExpression 
                        && this.getIterator().getName().equals(((RefSelectExpression) i).getVariableName())))
                .collect(Collectors.toList());
        newPlainSelect.getSelectItems().clear();
        newPlainSelect.getSelectItems().addAll(items);
        return newSelect;
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
                super.setSource(finalSelect);
                return;
            }
        }
        super.setSource(statement);
    }

    public OclExpression getSourceExpression() {
        return sourceExpression;
    }

    public void setSourceExpression(OclExpression sourceExpression) {
        this.sourceExpression = sourceExpression;
    }
}
