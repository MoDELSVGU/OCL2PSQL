package net.sf.jsqlparser.expression.operators.conditional;

import org.vgu.sqlsi.main.InjectorExpression;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class XorExpression extends BinaryExpression {
    public XorExpression() {
        super();
    }
    
    public XorExpression(Expression leftExpression, Expression rightExpression) {
        setLeftExpression(leftExpression);
        setRightExpression(rightExpression);
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "XOR";
    }

    @Override
    public void accept(InjectorExpression injectorExpression) {
        // TODO Auto-generated method stub
        
    }
}
