package org.vgu.sqlsi.sql.statement.select;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class RefSelectExpression extends SelectExpressionItem{
    public RefSelectExpression(String variableName) {
        super();
        super.setAlias(new Alias("ref_".concat(variableName)));
    }
}
