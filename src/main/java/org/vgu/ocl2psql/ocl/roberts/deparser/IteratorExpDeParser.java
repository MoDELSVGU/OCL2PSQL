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

import org.vgu.ocl2psql.ocl.roberts.expressions.IteratorExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IteratorKind;

public class IteratorExpDeParser extends OclExpressionDeParser {

    private String deParsedStr;

    public IteratorExpDeParser(String deParsedStr) {
        this.deParsedStr = deParsedStr;
    }

    public IteratorExpDeParser() {
        this("");
    }

    public String getDeParsedStr() {
        return deParsedStr;
    }
    
    @Override
    public void visit(IteratorExp iteratorExp) {
        IteratorKind iteratorKind = iteratorExp.kind;
        OclExpressionDeParser deParser = new OclExpressionDeParser( deParsedStr );
        String finalParsedStr = "";
        
        switch ( iteratorKind ) {
            case size:
            case asSet:
            case notEmpty:
            case isEmpty:
            case isUnique:
            case flatten:
                iteratorExp.getSource().accept( deParser );
                finalParsedStr = deParser.getDeParsedStr() + 
                    "->" + iteratorKind + "()"; 
                break;
            case collect:
            case select:
            case reject:
            case forAll:
            case exists:
                iteratorExp.getSource().accept( deParser );
                finalParsedStr = deParser.getDeParsedStr() + "->" + 
                    iteratorKind.toString() + "(" +
                    iteratorExp.getIterator().getName() + "|";

                deParser.setDeParsedStr( finalParsedStr );

                iteratorExp.getBody().accept( deParser );
                finalParsedStr = deParser.getDeParsedStr() + ")";
                break;
            default:
                break;
        }

        this.deParsedStr = this.deParsedStr.concat( finalParsedStr );
    }

    
}
