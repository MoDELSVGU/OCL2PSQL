package org.vgu.sqlsi.sql.statement.select;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class ResSelectExpression extends SelectExpressionItem{
    public ResSelectExpression() {
        super();
        super.setAlias(new Alias("res"));
    }
    
    public ResSelectExpression(Expression expression) {
        super(expression);
        super.setAlias(new Alias("res"));
    }
}
