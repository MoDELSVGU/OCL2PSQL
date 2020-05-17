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

package org.vgu.ocl2psql;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.sql.statement.select.Select;

import com.vgu.se.jocl.expressions.Expression;

public abstract class OCL2PSQLService {

    protected boolean descriptionMode;

    protected abstract void setDataModelFromFilePath(String filePath)
        throws FileNotFoundException, IOException, ParseException, Exception;

    protected boolean getDescriptionMode() {
        return this.getDescriptionMode();
    }

    protected void setDescriptionMode(Boolean descriptionMode) {
        this.descriptionMode = descriptionMode;
    }

    protected abstract String mapToString(String oclExp);
    
    protected abstract String mapToString(Expression oclExp);

    protected abstract Select mapToSQL(String oclExp);
    
    protected abstract Select mapToSQL(Expression oclExp);

    protected abstract void setDataModel(Object dm);

    protected abstract void setContextualType(String varName, String varType);

    protected abstract void setDataModelFromFile(JSONArray json) throws Exception;

}
