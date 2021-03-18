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


package org.vgu.ocl2psql.roberts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.vgu.ocl2psql.main.OCL2PSQL_2;
import org.vgu.se.sql.parser.SQLParser;

import sql.Statement;

public class OCL2PSQLXMI {
    public static void main(String[] args) {
    	List<String> tasks = Arrays.asList(
    			 "Stage0Challenge0"
    			,"Stage0Challenge1"
    			,"Stage0Challenge2"
    			,"Stage1Challenge0"
    			,"Stage1Challenge1"
    			,"Stage1Challenge2"
    			,"Stage2Challenge0"
    			,"Stage3Challenge0"
    			,"Stage3Challenge1"
    			,"Stage4Challenge0"
    			,"Stage4Challenge1"
    			,"Stage4Challenge2"
    			,"Stage5Challenge0"
    			,"Stage5Challenge1"
    			,"Stage6Challenge0"
    			,"Stage6Challenge1"
    			,"Stage7Challenge0"
    			,"Stage7Challenge1"
    			,"Stage7Challenge2"
    			,"Stage7Challenge3"
    			,"Stage8Challenge0"
    			,"Stage8Challenge1"
    			,"Stage8Challenge2"
    			,"Stage8Challenge3"
    			,"Stage9Challenge0"
    			,"Stage9Challenge1"
    			,"Stage9Challenge2"
    			,"Stage9Challenge3"
    			);
    	for(String task : tasks) {
    		String oclExpXMIFullPath = "C:\\Users\\ngpbh\\eclipse-modelling-workspace\\OCLmt\\model\\%s.xmi";
            String sqlExpXMIFullPath = "C:\\Users\\ngpbh\\eclipse-modelling-workspace\\SQLmt\\model\\%s.xmi";
            OCL2PSQL_2 ocl2psql_2 = new OCL2PSQL_2();
            try {
            	String oclExpPath = String.format(oclExpXMIFullPath, task);
            	File oclExpFile = new File(oclExpPath);
                Statement sql = ocl2psql_2.fromOCLXMIFileToSQLXMIStatement(oclExpFile).getEStatement();
                String sqlXMI = SQLParser.outputEStatementAsXMI(sql);
    			BufferedWriter output = null;
    			File dataModelFile = null;
    			try {
    				String sqlExpPath = String.format(sqlExpXMIFullPath, task);
    				dataModelFile = new File(sqlExpPath);
    				output = new BufferedWriter(new FileWriter(dataModelFile));
    				output.write(sqlXMI);
    			} catch (IOException e) {
    				e.printStackTrace();
    			} finally {
    				if (output != null) {
    					output.close();
    				}
    			}
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    	}
    }
}
