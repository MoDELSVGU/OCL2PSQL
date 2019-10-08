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
package org.vgu.ocl2psql.ocl.roberts.deparser;

import org.vgu.ocl2psql.ocl.roberts.expressions.OperationCallExp;

public class OperationCallExpDeParser extends OclExpressionDeParser {
    private String deParsedStr;
    
    public OperationCallExpDeParser() {
        this( "" );
    }
    
    public OperationCallExpDeParser(String deParsedStr) {
        this.deParsedStr = deParsedStr;
    }
    
    public String getDeParsedStr() {
        return deParsedStr;
    }
    
    @Override
    public void visit( OperationCallExp opCallExp ) {
        String operationName = opCallExp.getName();
        String finalParsedStr = "";
        OclExpressionDeParser deParser = new OclExpressionDeParser(deParsedStr);

        switch ( operationName ) {
            case "allInstances":
                opCallExp.getSource().accept( deParser );
                
                finalParsedStr = deParser.getDeParsedStr() + 
                    "::" + operationName + "()";
                break;
            case "not":
                opCallExp.getArguments().get( 0 ).accept( deParser );
                finalParsedStr = operationName + " (" + deParser.getDeParsedStr() + ")";
                break;
            case "oclIsUndefined":
                opCallExp.getSource().accept( deParser );
                finalParsedStr = deParser.getDeParsedStr() + 
                    "." + operationName + "()";
                break;
            default:
//          these iclude : "=", "<", ">", "<=", ">=", "<>"
                opCallExp.getSource().accept( deParser );
                finalParsedStr = deParser.getDeParsedStr() 
                    + " " + operationName + " ";

                deParser.setDeParsedStr( finalParsedStr );

                opCallExp.getArguments().get( 0 ).accept( deParser );
                finalParsedStr = deParser.getDeParsedStr();
        }

        this.deParsedStr = this.deParsedStr.concat( finalParsedStr );
    }

}
