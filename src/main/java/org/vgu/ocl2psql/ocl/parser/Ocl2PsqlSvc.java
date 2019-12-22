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

package org.vgu.ocl2psql.ocl.parser;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.sql.statement.select.Select;

public abstract class Ocl2PsqlSvc {

    protected Boolean descriptionMode = false;

    public abstract void setDataModelFromFile(String filePath)
        throws FileNotFoundException, IOException, ParseException, Exception;

    public boolean getDescriptionMode() {
        return this.getDescriptionMode();
    }

    public void setDescriptionMode(Boolean descriptionMode) {
        this.descriptionMode = descriptionMode;
    }

    public abstract String mapToString(String oclExp);

    public abstract Select mapToSQL(String oclExp);

    public abstract void setDataModel(Object dm);

    public void setContextualType(String varName) {
    };

    public void setContextualType(String varName, String varType) {
    };

}
