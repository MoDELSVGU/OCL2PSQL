package org.vgu.sqlsi.sql.statement.select;

import net.sf.jsqlparser.expression.Expression;

public class VarSelectExpression {
    private String var;
    private RefSelectExpression ref;
    
    public VarSelectExpression(String var) {
        this.var = var;
        this.ref = new RefSelectExpression(var);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public RefSelectExpression getRef() {
        return ref;
    }

    public void setRef(RefSelectExpression ref) {
        this.ref = ref;
    }
    
    public void setRefExpression(Expression ex) {
        this.getRef().setExpression(ex);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((var == null) ? 0 : var.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VarSelectExpression other = (VarSelectExpression) obj;
        if (var == null) {
            if (other.var != null)
                return false;
        } else if (!var.equals(other.var))
            return false;
        return true;
    }
    
    
}
