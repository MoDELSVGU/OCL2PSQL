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


package org.vgu.sqlsi.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.vgu.sqlsi.ocl.context.DefaultOclContext;
import org.vgu.sqlsi.ocl.exception.OclParseException;
import org.vgu.sqlsi.ocl.expressions.IteratorSource;
import org.vgu.sqlsi.ocl.expressions.OclExpression;
import org.vgu.sqlsi.ocl.visitor.OCL2SQLParser;
import org.vgu.sqlsi.sql.statement.select.ResSelectExpression;
import org.vgu.sqlsi.sql.statement.select.ValSelectExpression;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class OCL2PSQL {
    private OCL2SQLParser ocl2sqlParser;
    
    public OCL2PSQL() {
        ocl2sqlParser = new OCL2SQLParser();
    }
    
    public void setPlainUMLContextFromFile(String filePath) throws FileNotFoundException, IOException, ParseException {
        ocl2sqlParser.setPlainUMLContextFromFile(filePath);
    }
    
    public void setPlainUMLContext(String UMLContext) throws ParseException {
        ocl2sqlParser.setPlainUMLContextFromString(UMLContext);
    }
    
    public String mapToString(String oclExpression) throws OclParseException {
        Select finalStatement = this.mapToSQL(oclExpression);
        return finalStatement.toString();
    }
    
    public Select mapToSQL(String oclExpression) throws OclParseException {
        ocl2sqlParser.resetLevelOfSet();
        ocl2sqlParser.setVisitorContext(new ArrayList<IteratorSource>());
        OclExpression exp = OclExpression.parse(oclExpression, new DefaultOclContext());
        Select finalStatement = (Select) ocl2sqlParser.visit(exp);
        cookFinalStatement(finalStatement);
        return finalStatement;
    }
    
    private void cookFinalStatement(Select finalStatement) {
        PlainSelect finalPlainSelect = (PlainSelect) finalStatement.getSelectBody();
        List<SelectItem> newSelectItems = new ArrayList<SelectItem>();
        for(SelectItem item : finalPlainSelect.getSelectItems()) {
            if(item instanceof ResSelectExpression || item instanceof ValSelectExpression) {
                newSelectItems.add(item);
            }
        }
        finalPlainSelect.getSelectItems().clear();
        finalPlainSelect.getSelectItems().addAll(newSelectItems);
    }
}
