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

package com.vgu.se.jocl.utils;

import java.util.ArrayList;
import java.util.List;

import com.vgu.se.jocl.expressions.AssociationClassCallExp;
import com.vgu.se.jocl.expressions.IteratorExp;
import com.vgu.se.jocl.expressions.OperationCallExp;
import com.vgu.se.jocl.expressions.PropertyCallExp;
import com.vgu.se.jocl.expressions.Variable;
import com.vgu.se.jocl.expressions.VariableExp;
import com.vgu.se.jocl.expressions.sql.functions.SqlFnTimestampdiff;

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
    public static List<Variable> FVars(com.vgu.se.jocl.expressions.Expression src) {
        ArrayList<Variable> fVars = new ArrayList<Variable>();

        return FVarsAux(src, fVars);
    }

    private static List<Variable> FVarsAux(com.vgu.se.jocl.expressions.Expression src,
            List<Variable> fVars) {
        
        if (src instanceof SqlFnTimestampdiff) {
            SqlFnTimestampdiff sqlFn = (SqlFnTimestampdiff) src;
            FVarsAux(sqlFn.getParams().get(0), fVars);
            return FVarsAux(sqlFn.getParams().get(1), fVars);
        }

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
            case "size":
            case "isEmpty":
            case "notEmpty":
            case "flatten":
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
            return FVarsAux(((AssociationClassCallExp) src)
                    .getNavigationSource(), fVars);
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
    public static List<Variable> SVars(com.vgu.se.jocl.expressions.Expression ePrime) {
        List<Variable> FVars = FVars(ePrime);

        if (FVars.isEmpty()) {
            return new ArrayList<Variable>();
        }

        List<Variable> SVars = new ArrayList<Variable>();

        for (Variable var : FVars) {
            com.vgu.se.jocl.expressions.Expression srcVarExpression = var.getSource();

            if (SVars.contains(var)) {
                continue;
            }

            SVars.add(var);

            if (srcVarExpression == null) {
                continue;
            }

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
        List<Variable> res = new ArrayList<Variable>(B);
        res.removeAll(A);

        return res;
    }
}
