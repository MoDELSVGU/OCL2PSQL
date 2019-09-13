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
