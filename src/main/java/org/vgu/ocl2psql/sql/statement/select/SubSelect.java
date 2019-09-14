package org.vgu.ocl2psql.sql.statement.select;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.SelectBody;

public class SubSelect extends net.sf.jsqlparser.statement.select.SubSelect {

    public SubSelect() {
        super();
    }
    
    public SubSelect(SelectBody selectBody, String alias ) {
        super();
        super.setSelectBody( selectBody );
        super.setAlias( new Alias( alias ) );
    }
}
