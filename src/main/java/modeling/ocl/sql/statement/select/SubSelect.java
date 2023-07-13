package modeling.ocl.sql.statement.select;

import java.util.Iterator;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.WithItem;

public class SubSelect extends net.sf.jsqlparser.statement.select.SubSelect {

    public SubSelect() {
        super();
    }
    
    public SubSelect(SelectBody selectBody, String alias ) {
        super();
        super.setSelectBody( selectBody );
        super.setAlias( new Alias( alias ) );
    }

    public String toStringWithDescription() {
        StringBuilder retval = new StringBuilder();
        if (super.isUseBrackets()) {
            retval.append("(");
        }
        if (super.getWithItemsList() != null && !super.getWithItemsList().isEmpty()) {
            retval.append("WITH ");
            for (Iterator<WithItem> iter = super.getWithItemsList().iterator(); iter.hasNext();) {
                WithItem withItem = iter.next();
                retval.append(withItem);
                if (iter.hasNext()) {
                    retval.append(",");
                }
                retval.append(" ");
            }
        }
        if(super.getSelectBody() instanceof PlainSelect) {
            retval.append(((PlainSelect)super.getSelectBody()).toStringWithDescription());
        }
        else {
            retval.append(super.getSelectBody());
        }
        if (super.isUseBrackets()) {
            retval.append(")");
        }

        if (super.getAlias() != null) {
            retval.append(super.getAlias().toString());
        }
        if (super.getPivot() != null) {
            retval.append(" ").append(super.getPivot());
        }

        return retval.toString();
    }
}
