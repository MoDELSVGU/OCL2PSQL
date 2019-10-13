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

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.sql.statement.select.Select;

public class O2PParser extends O2P {

    public O2PParser() {
    }

    public O2PParser(O2PApi api) {
        super(api);
        api.setDescriptionMode(false);
    }

    @Override
    public String mapToString(String oclExp) {
        return api.mapToString(oclExp);
    }

    @Override
    public Select mapToSQL(String oclExp) {
        return api.mapToSQL(oclExp);
    }

    @Override
    public void setPlainUMLContextFromFile(String filePath)
            throws IOException, ParseException {
        setContext((JSONArray) new JSONParser()
                .parse(new FileReader(filePath)));
    }

    @Override
    public void setPlainUMLContext(String umlCtx)
            throws ParseException {
        setContext((JSONArray) new JSONParser().parse(umlCtx));
    }

    @Override
    public void setContext(JSONArray ctx) {
        api.setContext(ctx);
    }

    @Override
    public void setContextualType(String typeName) {
        api.setContextualType(typeName);
    }

    @Override
    public boolean getDescriptionMode() {
        return api.getDescriptionMode();
    }

    @Override
    public void setDescriptionMode(Boolean descriptionMode) {
        api.setDescriptionMode(descriptionMode);
    }

}
