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

@author: ngpbh
***************************************************************************/
package modeling.ocl.sql.statement.select;

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
