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

import java.io.IOException;
import java.util.Scanner;

import org.vgu.ocl2psql.main.OCL2PSQL_2;
import org.vgu.se.sql.EStatement;
import org.vgu.se.sql.parser.SQLParser;

public class OCL2PSQLXMI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String dataModel = sc.nextLine();
        String input = sc.nextLine();
        OCL2PSQL_2 ocl2psql_2 = new OCL2PSQL_2();
        try {
            EStatement sql = ocl2psql_2.mapOCLXMIToSQLXMI("CarPerson", dataModel, input);
            System.out.println(SQLParser.outputEStatementAsXMI(sql));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
