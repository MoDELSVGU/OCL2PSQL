package org.vgu.sqlsi.sql.statement.select;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class ValSelectExpression extends SelectExpressionItem{
    public ValSelectExpression() {
        super();
        super.setAlias(new Alias("val"));
    }
    
    public ValSelectExpression(Expression expression) {
        super(expression);
        super.setAlias(new Alias("val"));
    }
}
