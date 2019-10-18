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

@author: thian
***************************************************************************/

package org.vgu.ocl2psql.ocl.parser.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.VarSelectExpression;

import com.vgu.se.jocl.expressions.AssociationClassCallExp;
import com.vgu.se.jocl.expressions.IteratorExp;
import com.vgu.se.jocl.expressions.OclExp;
import com.vgu.se.jocl.expressions.OperationCallExp;
import com.vgu.se.jocl.expressions.PropertyCallExp;
import com.vgu.se.jocl.expressions.Variable;
import com.vgu.se.jocl.expressions.VariableExp;

import net.sf.jsqlparser.schema.Column;

public class VariableUtils {

    /**
     * <p>
     * This is an implementation of FVars function in the manuscript
     * <b>OCL2PSQL: An OCL-to-SQL Code-Generator for Model Driven
     * Engineering</b>. Assuming <code>c</code> is the valid OCL
     * expression.
     * </p>
     * 
     * @param ? the valid OCL sub-expression
     * @return the set of variables that occurs free in ?
     * @since 1.0
     */
    public static List<Variable> FVars(OclExp src) {
        ArrayList<Variable> fVars = new ArrayList<Variable>();

        return FVarsAux(src, fVars);
    }

    private static List<Variable> FVarsAux(OclExp src,
            List<Variable> fVars) {

        if (src instanceof IteratorExp) {
            return FVarsAux(((IteratorExp) src).getSource(), fVars);
        }

        if (src instanceof OperationCallExp) {
            OperationCallExp opCallExpSrc = (OperationCallExp) src;

            switch (opCallExpSrc.getReferredOperation().getName()) {
            case "not":
                return FVarsAux(opCallExpSrc.getArguments().get(0),
                        fVars);
            case "oclIsUndefined":
            case "oclIsTypeOf":
            case "oclIsKindOf":
            case "oclAsType":
                return FVarsAux(opCallExpSrc.getSource(), fVars);
            case "=":
            case "<>":
            case "<=":
            case "<":
            case ">=":
            case ">":
            case "and":
            case "or":
                FVarsAux(opCallExpSrc.getArguments().get(0), fVars);
                return FVarsAux(opCallExpSrc.getSource(), fVars);
            case "allInstances":
                return fVars;
            default:
            }

        }

        if (src instanceof PropertyCallExp) {
            return FVarsAux(
                    ((PropertyCallExp) src).getNavigationSource(),
                    fVars);
        }

        if (src instanceof AssociationClassCallExp) {
            return FVarsAux(
                    ((AssociationClassCallExp) src).getNavigationSource(),
                    fVars);
        }

        if (src instanceof VariableExp) {
            VariableExp varExpSrc = (VariableExp) src;
            Variable v = varExpSrc.getVariable();
            if (!fVars.contains(v)) {
                fVars.add(v);
            }
            return fVars;
        }

        // ELSE
        return fVars;
    }

    /**
     * <p>
     * This is an implementation of SVars function in the manuscript
     * <b>OCL2PSQL: An OCL-to-SQL Code-Generator for Model Driven
     * Engineering</b>. Assuming e is the whole OCL expression.
     * </p>
     * 
     * @param ePrime  (e') the sub-expression of e
     * @param visitor the context of OCL expression
     * @return the set of variables which e' depends on
     * @since 1.0
     */
    public static List<Variable> SVars(OclExp ePrime) {
        List<Variable> FVars = FVars(ePrime);

        if (FVars.isEmpty()) {
            return new ArrayList<Variable>();
        }

        List<Variable> SVars = new ArrayList<Variable>();

        for (Variable var : FVars) {
            OclExp srcVarExpression = var.getSource();

            if (SVars.contains(var) || srcVarExpression == null) {
                continue;
            }

            SVars.add(var);

            for (Variable srcVar : SVars(srcVarExpression)) {
                if (SVars.contains(srcVar)) {
                    continue;
                }

                SVars.add(srcVar);
            }
        }

        return SVars;
    }
    
    public static List<Variable> getComplement(List<Variable> A,
            List<Variable> B) {
        List<Variable> res = new ArrayList<Variable>();

        System.arraycopy(B, 0, res, 0, B.size());

        res.removeAll(A);

        return res;
    }
    
    public static void addVar(List<Variable> vList,
            PlainSelect plainSelect, String tableAlias) {
        
        for (Variable v : vList) {
            VarSelectExpression varExp = new VarSelectExpression(
                    v.getName());

            Column refCol = new Column(Arrays.asList(
                    tableAlias,
                    varExp.getRef().getAlias().getName()));

            varExp.setRefExpression(refCol);
            plainSelect.addVar(varExp);
        }

    }
}
