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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.vgu.dm2schema.dm.DataModel;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.se.ocl.parser.OCLParser;
import org.vgu.se.sql.parser.SQLParser;
import org.vgu.ttc2020.model.TTCReturnModel;

import com.vgu.se.jocl.exception.OclParserException;
import com.vgu.se.jocl.expressions.Expression;
import com.vgu.se.jocl.expressions.OclExp;

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

    public TTCReturnModel mapOCLStringToSQLXMI(String oclExpression) {
        TTCReturnModel returnModel = new TTCReturnModel();
        final long startNanoTime = System.nanoTime();
        Statement statement = service.mapToSQL(oclExpression);
        final long endNanoTime = System.nanoTime();
        returnModel.setOcl2sqlNanoTime(endNanoTime - startNanoTime);
        returnModel.setEStatement(SQLParser.transform(statement));
        return returnModel;
    }

    public TTCReturnModel mapOCLXMIToSQLXMI(String dataModelName,
        String dataModel, String oclXMIExpression) throws IOException {
        final String dirPath = System.getProperty("java.io.tmpdir");
        TTCReturnModel returnModel = new TTCReturnModel();
        BufferedWriter output = null;
        File dataModelFile = null;
        try {
            dataModelFile = new File(
                dirPath.concat("//").concat(dataModelName).concat(".xmi"));
            output = new BufferedWriter(new FileWriter(dataModelFile));
            output.write(dataModel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
        }
        File file = null;
        try {
            file = new File(dirPath.concat("//").concat("input.xmi"));
            output = new BufferedWriter(new FileWriter(file));
            output.write(oclXMIExpression);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
        }
        String filePath = file.getAbsolutePath();
        DataModel dm = OCLParser.extractDataModel(filePath);
        OclExp ocl = (OclExp) OCLParser.convertToExp(filePath);
        service.setDataModel(dm);
        final long startNanoTime = System.nanoTime();
        Statement statement = service.mapToSQL(ocl);
        final long endNanoTime = System.nanoTime();
        returnModel.setOcl2sqlNanoTime(endNanoTime - startNanoTime);
        returnModel.setEStatement(SQLParser.transform(statement));
        return returnModel;
    }

    public TTCReturnModel mapOCLStringToSQLString(String oclExpression)
        throws OclParserException, ParseException, IOException {
        TTCReturnModel returnModel = new TTCReturnModel();
        final long startNanoTime = System.nanoTime();
        Statement statement = service.mapToSQL(oclExpression);
        final long endNanoTime = System.nanoTime();
        returnModel.setOcl2sqlNanoTime(endNanoTime - startNanoTime);
        returnModel
            .setStatement(((SimpleService) service).m2t((Select) statement));
        return returnModel;
    }

    public Statement mapOCLStringToSQLModel(String oclExpression)
        throws OclParserException, ParseException, IOException {
        return service.mapToSQL(oclExpression);
    }

    public TTCReturnModel mapOCLXMIToSQLString(String dataModelName,
        String dataModel, String oclXMIExpression) throws IOException {
        final String dirPath = System.getProperty("java.io.tmpdir");
        TTCReturnModel returnModel = new TTCReturnModel();
        BufferedWriter output = null;
        File dataModelFile = null;
        try {
            dataModelFile = new File(
                dirPath.concat("//").concat(dataModelName).concat(".xmi"));
            output = new BufferedWriter(new FileWriter(dataModelFile));
            output.write(dataModel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
        }
        File file = null;
        try {
            file = new File(dirPath.concat("//").concat("input.xmi"));
            output = new BufferedWriter(new FileWriter(file));
            output.write(oclXMIExpression);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
        }
        String filePath = file.getAbsolutePath();
        DataModel dm = OCLParser.extractDataModel(filePath);
        OclExp ocl = (OclExp) OCLParser.convertToExp(filePath);
        service.setDataModel(dm);
        final long startNanoTime = System.nanoTime();
        Statement statement = service.mapToSQL(ocl);
        final long endNanoTime = System.nanoTime();
        returnModel.setOcl2sqlNanoTime(endNanoTime - startNanoTime);
        returnModel
            .setStatement(((SimpleService) service).m2t((Select) statement));
        return returnModel;
    }

}
