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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.ocl.parser.simple.SimpleOclParser;
import org.vgu.ocl2psql.ocl.roberts.context.DefaultOclContext;
import org.vgu.ocl2psql.ocl.roberts.exception.OclParseException;
import org.vgu.ocl2psql.ocl.roberts.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.roberts.parser.RobertOCLParser;
import org.vgu.ocl2psql.ocl.roberts.parser.RobertOcl2PojoParser;
import org.vgu.ocl2psql.ocl.type.SingleType;
import org.vgu.ocl2psql.ocl.type.Type;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.TypeSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.utils.SQLAsStringUtils;

import com.vgu.se.jocl.expressions.Expression;
import com.vgu.se.jocl.parser.interfaces.Parser;
import com.vgu.se.jocl.parser.simple.SimpleParser;

import net.sf.jsqlparser.statement.select.SelectItem;

public class OCL2PSQL {
    private RobertOCLParser ocl2sqlParser;
    private Boolean descriptionMode;

    private SimpleOclParser ocl2PsqlParser = new SimpleOclParser();
    private Parser parser;
    private String filePath;
    
    private boolean isNewParser;
    
    public RobertOCLParser getOcl2sqlParser() {
        return ocl2sqlParser;
    }

    public OCL2PSQL() {
        ocl2sqlParser = new RobertOCLParser();
        this.setDescriptionMode(false);
    }
    
    public void setPlainUMLContextFromFile(String filePath) throws FileNotFoundException, IOException, ParseException {
        ocl2sqlParser.setPlainUMLContextFromFile(filePath);
        this.filePath = filePath;
    }
    
    public void setPlainUMLContext(String UMLContext) throws ParseException {
        ocl2sqlParser.setPlainUMLContextFromString(UMLContext);
    }
    
    public void setNewParser(boolean isNewParser) {
        this.isNewParser = isNewParser;
    }

    public String mapToString(String oclExpression) throws OclParseException, ParseException, IOException {
        Select finalStatement = this.mapToSQL(oclExpression);
        if(this.descriptionMode) {
            String finalStatementString = finalStatement.toStringWithDescription();
            return SQLAsStringUtils.applyIndent(finalStatementString);
        } else {
            return finalStatement.toString();
        }
    }
    
    public Select mapToSQL(String oclExpression) throws OclParseException, ParseException, IOException {
        
        if (this.isNewParser) {
            parser = new SimpleParser();
            JSONArray ctx = null;
            ctx = (JSONArray) new JSONParser()
                    .parse(new FileReader(filePath));

            Expression newExp = parser.parse(oclExpression, ctx);
            newExp.accept(ocl2PsqlParser);

            return cookFinalStatement(ocl2PsqlParser.getSelect());
        }

        ocl2sqlParser.resetLevelOfSet();
        ocl2sqlParser.resetVisitorContext();
        OclExpression exp = RobertOcl2PojoParser.parse(oclExpression, new DefaultOclContext());
        exp.accept(ocl2sqlParser);
        
        return cookFinalStatement(ocl2sqlParser.getFinalSelect());
    }
    
    private Select cookFinalStatement(Select finalStatement) {
        PlainSelect finalPlainSelect = (PlainSelect) finalStatement.getSelectBody();
        List<SelectItem> newSelectItems = new ArrayList<SelectItem>();
        for(SelectItem item : finalPlainSelect.getSelectItems()) {
            if(item instanceof ResSelectExpression 
                    || item instanceof ValSelectExpression
                    || item instanceof TypeSelectExpression) {
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
    
    public void setContextualType (String typeName) {
        Type type = new SingleType(typeName);
        ocl2sqlParser.setContextualType(type);
    }
}
