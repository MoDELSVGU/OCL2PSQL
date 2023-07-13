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

package org.vgu.ocl2psql;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.sql.statement.select.Select;

import com.vgu.se.jocl.exception.OclParserException;
import com.vgu.se.jocl.expressions.Expression;

import modeling.data.entities.DataModel;
import net.sf.jsqlparser.statement.Statement;

public class OCL2PSQL {

    private OCL2PSQLService service;

    public OCL2PSQL() {
        service = new SimpleService();
    }
    
    public String mapToString(String oclExp) {
        return service.mapToString(oclExp);
    }
    
    public String mapToString(Expression oclExp) {
        return service.mapToString(oclExp);
    }

    public Select mapToSQL(String oclExp) {
        return service.mapToSQL(oclExp);
    }
    
    public Select mapToSQL(Expression oclExp) {
        return service.mapToSQL(oclExp);
    }
    
    public boolean getDescriptionMode() {
        return service.getDescriptionMode();
    }

    public void setDescriptionMode(boolean descriptionMode) {
        service.setDescriptionMode(descriptionMode);
    }

    public void setContextualType(String varName, String varType) {
        service.setContextualType(varName, varType);
    }

    public void setDataModelFromFilePath(String filePath)
        throws FileNotFoundException, IOException, ParseException, Exception {
        service.setDataModelFromFilePath(filePath);
    }

    public void setDataModelFromFile(JSONArray filePath)
        throws FileNotFoundException, IOException, ParseException, Exception {
        service.setDataModelFromFile(filePath);
    }

    public void setDataModel(DataModel dataModel) {
        service.setDataModel(dataModel);
    }

    public Statement mapOCLStringToSQLModel(String oclExpression)
        throws OclParserException, ParseException, IOException {
        return service.mapToSQL(oclExpression);
    }

}
