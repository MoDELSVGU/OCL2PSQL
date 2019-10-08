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


package org.vgu.ocl2psql.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.ocl.roberts.context.DefaultOclContext;
import org.vgu.ocl2psql.ocl.roberts.exception.OclParseException;
import org.vgu.ocl2psql.ocl.roberts.expressions.IteratorSource;
import org.vgu.ocl2psql.ocl.roberts.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.roberts.parse.SimpleParser;
import org.vgu.ocl2psql.parser.RobertOCLParser;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.utils.SQLAsStringUtils;

import net.sf.jsqlparser.statement.select.SelectItem;

public class OCL2PSQL {
    private RobertOCLParser ocl2sqlParser;
    private Boolean descriptionMode;
    
    public RobertOCLParser getOcl2sqlParser() {
        return ocl2sqlParser;
    }

    public OCL2PSQL() {
        ocl2sqlParser = new RobertOCLParser();
        this.setDescriptionMode(false);
    }
    
    public void setPlainUMLContextFromFile(String filePath) throws FileNotFoundException, IOException, ParseException {
        ocl2sqlParser.setPlainUMLContextFromFile(filePath);
    }
    
    public void setPlainUMLContext(String UMLContext) throws ParseException {
        ocl2sqlParser.setPlainUMLContextFromString(UMLContext);
    }
    
    public String mapToString(String oclExpression) throws OclParseException {
        Select finalStatement = this.mapToSQL(oclExpression);
        if(this.descriptionMode) {
            String finalStatementString = finalStatement.toStringWithDescription();
            return SQLAsStringUtils.applyIndent(finalStatementString);
        } else {
            return finalStatement.toString();
        }
    }
    
    public Select mapToSQL(String oclExpression) throws OclParseException {
        ocl2sqlParser.resetLevelOfSet();
        ocl2sqlParser.setVisitorContext(new ArrayList<IteratorSource>());
        OclExpression exp = SimpleParser.parse(oclExpression, new DefaultOclContext());
        exp.accept(ocl2sqlParser);
        return cookFinalStatement(ocl2sqlParser.getFinalSelect());
    }
    
    private Select cookFinalStatement(Select finalStatement) {
        PlainSelect finalPlainSelect = (PlainSelect) finalStatement.getSelectBody();
        List<SelectItem> newSelectItems = new ArrayList<SelectItem>();
        for(SelectItem item : finalPlainSelect.getSelectItems()) {
            if(item instanceof ResSelectExpression || item instanceof ValSelectExpression) {
                newSelectItems.add(item);
            }
        }
        finalPlainSelect.getSelectItems().clear();
        finalPlainSelect.getSelectItems().addAll(newSelectItems);
        return finalStatement;
    }
    
    public void setContext(JSONArray context) {
        this.ocl2sqlParser.setPlainUMLContext(context);
    }

    public Boolean getDescriptionMode() {
        return descriptionMode;
    }

    public void setDescriptionMode(Boolean descriptionMode) {
        this.descriptionMode = descriptionMode;
    }
}
