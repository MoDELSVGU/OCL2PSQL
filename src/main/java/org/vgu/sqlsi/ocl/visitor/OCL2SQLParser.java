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
***************************************************************************/

package org.vgu.sqlsi.ocl.visitor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.sqlsi.ocl.expressions.IteratorSource;
import org.vgu.sqlsi.ocl.expressions.OclExpression;
import org.vgu.sqlsi.ocl.expressions.StmVisitor;

import net.sf.jsqlparser.statement.Statement;

public class OCL2SQLParser implements StmVisitor{
    private List<IteratorSource> context = new LinkedList<IteratorSource>();
    private JSONArray plainUMLContext;
    private int levelOfSets = 0;
    
    @Override
    public List<IteratorSource> getVisitorContext() {
        return this.context;
    }
    @Override
    public void setVisitorContext(List<IteratorSource> context) {
        this.context = context;
        
    }
    @Override
    public Statement visit(OclExpression source) {
        return source.map(this);
    }
    public JSONArray getPlainUMLContext() {
        return plainUMLContext;
    }
    @Override
    public int getAlias() {
        return 0;
    }
    @Override
    public void setAlias(int id) {
        
    }
    @Override
    public void genAlias() {
    }
    
    public int getLevelOfSets() {
        return levelOfSets;
    }

    public void increaseLevelOfSet() {
        levelOfSets++;
    }
    
    public void decreaseLevelOfSet() {
        levelOfSets--;
    }
    
    public void resetLevelOfSet() {
        levelOfSets = 0;
    }
    
    public void setPlainUMLContextFromFile(String filePath) throws FileNotFoundException, IOException, ParseException {
        this.plainUMLContext = (JSONArray) new JSONParser().parse(new FileReader(filePath));
    }
    
    public void setPlainUMLContextFromString(String umlContext) throws ParseException {
        this.plainUMLContext = (JSONArray) new JSONParser().parse(umlContext);
    }
    
    public void setPlainUMLContext(JSONArray plainUMLContext) {
        this.plainUMLContext = plainUMLContext;
    }
}
