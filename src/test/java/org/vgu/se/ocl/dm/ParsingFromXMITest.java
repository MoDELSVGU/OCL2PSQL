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


package org.vgu.se.ocl.dm;

import java.util.Arrays;
import java.util.List;

import org.vgu.dm2schema.dm.DataModel;
import org.vgu.ocl2psql.ocl.parser.Ocl2PsqlSvc;
import org.vgu.ocl2psql.ocl.parser.simple.SimpleO2PApi;
import org.vgu.se.ocl.parser.OCLParser;

import com.vgu.se.jocl.expressions.OclExp;

public abstract class ParsingFromXMITest {

    public static void main(String[] args) {
        List<String> filePaths = Arrays.asList(
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase0Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase0Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase0Challenge3.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase1Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase1Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase1Challenge3.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase2Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase3Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase3Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase4Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase4Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase4Challenge3.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase5Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase5Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase6Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase6Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase7Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase7Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase7Challenge3.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase7Challenge4.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase8Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase8Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase8Challenge3.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase8Challenge4.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase9Challenge1.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase9Challenge2.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase9Challenge3.xmi",
            "C:\\Users\\ngpbh\\eclipse-workspace\\org.vgu.se.ocl\\model\\Phase9Challenge4.xmi");
        for (String filePath : filePaths) {
            DataModel dm = OCLParser.extractDataModel(filePath);
            OclExp ocl = (OclExp) OCLParser.convertToExp(filePath);
            Ocl2PsqlSvc simpleO2P = new SimpleO2PApi();
            simpleO2P.setDataModel(dm);
            simpleO2P.setDescriptionMode(true);
            String finalStatementWithDescription = simpleO2P.mapToString(ocl);
            System.out.println(finalStatementWithDescription);
        }
    }

}
