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


package org.vgu.ocl2psql.ocl.deparser;

import org.vgu.ocl2psql.ocl.expressions.IteratorExp;
import org.vgu.ocl2psql.ocl.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.expressions.OperationCallExp;
import org.vgu.ocl2psql.ocl.expressions.PropertyCallExp;
import org.vgu.ocl2psql.ocl.expressions.StringLiteralExp;
import org.vgu.ocl2psql.ocl.expressions.TypeExp;
import org.vgu.ocl2psql.ocl.expressions.VariableExp;

public class OclExpressionDeParser implements DeparserVisitor {
    protected String deParsedStr;

    public OclExpressionDeParser() {
        this( "" );
    }

    public OclExpressionDeParser(String parsedStr) {
        this.deParsedStr = parsedStr;
    }

    public String getDeParsedStr() {
        return deParsedStr;
    }
    
    public void setDeParsedStr(String deParsedStr) {
        this.deParsedStr = deParsedStr;
    }

    public void visit(OclExpression oclExp) {
        
        if ( oclExp instanceof TypeExp ) {
            visit( (TypeExp) oclExp );
        } 
        else if ( oclExp instanceof OperationCallExp ) {
            visit( (OperationCallExp) oclExp );
        }
        else if ( oclExp instanceof IteratorExp ) {
            visit( (IteratorExp) oclExp );
        }
        else if ( oclExp instanceof PropertyCallExp ) {
            visit( (PropertyCallExp) oclExp );
        }
        else if ( oclExp instanceof VariableExp ) {
            visit( (VariableExp) oclExp );
        }
        else if ( oclExp instanceof StringLiteralExp ) {
            visit( (StringLiteralExp) oclExp );
        }

    }
    
    public void visit(TypeExp typeExp) {
        TypeExpDeParser deParser = new TypeExpDeParser();
        typeExp.accept( deParser );
        this.deParsedStr = this.deParsedStr.concat( deParser.getDeParsedString() );
    }
    
    public void visit(OperationCallExp opCallExp) {
        OperationCallExpDeParser deParser = new OperationCallExpDeParser();
        opCallExp.accept( deParser );
        this.deParsedStr = this.deParsedStr.concat( deParser.getDeParsedStr() );
    }
    
    public void visit(IteratorExp iteratorExp) {
        IteratorExpDeParser deParser = new IteratorExpDeParser();
        iteratorExp.accept( deParser );
        this.deParsedStr =  this.deParsedStr.concat( deParser.getDeParsedStr() );
    }
    
    public void visit(PropertyCallExp propCallExp) {
        PropertyCallExpDeParser deParser = new PropertyCallExpDeParser();
        propCallExp.accept( deParser );
        this.deParsedStr = this.deParsedStr.concat( deParser.getDeParsedStr() );
    }
    
    public void visit(VariableExp varExp) {
        VariableExpDeParser deParser = new VariableExpDeParser();
        varExp.accept( deParser );
        this.deParsedStr = this.deParsedStr.concat( deParser.getDeParsedStr() );
    }
    
    public void visit(StringLiteralExp strLiteralExp) {
        StringLiteralExpDeParser deParser = new StringLiteralExpDeParser();
        strLiteralExp.accept( deParser );
        this.deParsedStr = this.deParsedStr.concat( deParser.getDeParsedStr() );
    }
    
}
