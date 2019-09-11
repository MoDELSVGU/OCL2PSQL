package org.vgu.sqlsi.test.ocl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.sqlsi.ocl.context.DefaultOclContext;
import org.vgu.sqlsi.ocl.exception.OclParseException;
import org.vgu.sqlsi.ocl.exception.SetOfSetException;
import org.vgu.sqlsi.ocl.expressions.IteratorSource;
import org.vgu.sqlsi.ocl.expressions.OclExpression;
import org.vgu.sqlsi.ocl.visitor.OCL2SQLParser;
import org.vgu.sqlsi.sql.statement.select.ResSelectExpression;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Runner {
    public String run(String ocl) throws OclParseException {
        String sql = null;
        JSONArray context = null;
        try {
            context = (JSONArray) new JSONParser().parse("[\r\n" + 
                    "{\"class\" : \"Car\",\r\n" + 
                    " \"attributes\" : [\r\n" + 
                    "    {\"name\" : \"Car_ref\", \"type\" : \"String\"},\r\n" + 
                    "    {\"name\" : \"model\", \"type\" : \"String\"},\r\n" + 
                    "    {\"name\" : \"color\", \"type\" : \"String\"},\r\n" + 
                    "    {\"name\" : \"age\", \"type\" : \"Integer\"}]\r\n" + 
                    "},\r\n" + 
                    "{\"class\" : \"Person\",\r\n" + 
                    " \"attributes\" : [\r\n" + 
                    "    {\"name\" : \"Person_ref\", \"type\" : \"String\"},\r\n" + 
                    "    {\"name\" : \"name\", \"type\" : \"String\"},\r\n" + 
                    "    {\"name\" : \"surname\", \"type\" : \"String\"},\r\n" + 
                    "    {\"name\" : \"age\", \"type\" : \"Integer\"}]\r\n" + 
                    " },\r\n" + 
                    "{\"association\" : \"Ownership\",\r\n" + 
                    " \"ends\" : [\"owners\", \"ownedCars\"],\r\n" + 
                    " \"classes\" : [\"Car\", \"Person\"]\r\n" + 
                    "}\r\n" + 
                    "]");
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        OCL2SQLParser ocl2psqlParser = new OCL2SQLParser();
        ocl2psqlParser.setPlainUMLContext(context);
        ocl2psqlParser.resetLevelOfSet();
        OclExpression exp = null;
        ocl2psqlParser.setAlias(0);
        ocl2psqlParser.setVisitorContext(new ArrayList<IteratorSource>());
        exp = OclExpression.parse(ocl, new DefaultOclContext());
        try {
            Select finalStatement = (Select) ocl2psqlParser.visit(exp);
            if(ocl2psqlParser.getLevelOfSets() > 1) {
                throw new SetOfSetException("");
            }
            cookFinalStatement(finalStatement);
            return finalStatement.toString();
        } catch (SetOfSetException e) {
            throw new SetOfSetException("");
        }
    }

    private void cookFinalStatement(Select finalStatement) {
        PlainSelect finalPlainSelect = (PlainSelect) finalStatement.getSelectBody();
        List<SelectItem> newSelectItems = new ArrayList<SelectItem>();
        for(SelectItem item : finalPlainSelect.getSelectItems()) {
            if(item instanceof ResSelectExpression) {
                newSelectItems.add(item);
            } else {
//                RefSelectExpression refExpression = (RefSelectExpression) item;
//                if(!refExpression.getAlias().getName().contains("closed")) {
//                    newSelectItems.add(item);
//                }
            }
        }
        finalPlainSelect.getSelectItems().clear();
        finalPlainSelect.getSelectItems().addAll(newSelectItems);
    }
}
