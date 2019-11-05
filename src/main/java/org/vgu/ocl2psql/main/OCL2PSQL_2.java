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

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.ocl.parser.Ocl2PsqlSvc;
import org.vgu.ocl2psql.ocl.parser.simple.SimpleO2PApi;
import org.vgu.ocl2psql.ocl.roberts.exception.OclParseException;
import org.vgu.ocl2psql.ocl.roberts.parser.LegacyO2PApi;
import org.vgu.ocl2psql.ocl.roberts.parser.RobertOCLParser;
import org.vgu.ocl2psql.sql.statement.select.Select;

public class OCL2PSQL_2 {

    private Ocl2PsqlSvc ocl2PsqlSvc;

    private RobertOCLParser ocl2sqlParser;

    public RobertOCLParser getOcl2sqlParser() {
        return ocl2sqlParser;
    }

    public OCL2PSQL_2() {
        this(null);
    }
    
    public OCL2PSQL_2(String name) {

        if (name.equalsIgnoreCase("simple")) {
            ocl2PsqlSvc = new SimpleO2PApi();
        } else {
            ocl2PsqlSvc = new LegacyO2PApi();
        }
        
        ocl2PsqlSvc.setDescriptionMode(false);
    }

    public void setPlainUMLContextFromFile(String filePath)
            throws IOException, ParseException {
        ocl2PsqlSvc.setPlainUMLContextFromFile(filePath);
    }

    public void setPlainUMLContext(String UMLContext)
            throws ParseException, IOException {
        ocl2PsqlSvc.setPlainUMLContextFromString(UMLContext);
    }

    public String mapToString(String oclExpression)
            throws OclParseException, ParseException, IOException {
        return ocl2PsqlSvc.mapToString(oclExpression);
    }

    public Select mapToSQL(String oclExpression)
            throws OclParseException, ParseException, IOException {

        return ocl2PsqlSvc.mapToSQL(oclExpression);
    }

    public void setContext(JSONArray context) {
        ocl2PsqlSvc.setContext(context);
    }

    public Boolean getDescriptionMode() {
        return ocl2PsqlSvc.getDescriptionMode();
    }

    public void setDescriptionMode(Boolean descriptionMode) {
        ocl2PsqlSvc.setDescriptionMode(descriptionMode);
    }

    public void setContextualType(String typeName) {
        ocl2PsqlSvc.setContextualType(typeName);
    }

    public void setContextualType(String varName, String varType) {
        ocl2PsqlSvc.setContextualType(varName, varType);
    }
}
