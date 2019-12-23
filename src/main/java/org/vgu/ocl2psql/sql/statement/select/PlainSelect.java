package org.vgu.ocl2psql.sql.statement.select;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Join;

public class PlainSelect
    extends net.sf.jsqlparser.statement.select.PlainSelect {
    private LinkedList<VarSelectExpression> vars = new LinkedList<VarSelectExpression>();
    private String correspondOCLExpression;

    public PlainSelect() {
        super();
    }

    public void setType(TypeSelectExpression type) {
        TypeSelectExpression curType = this.getType();
        if (Objects.isNull(curType)) {
            super.addSelectItems(type);
        } else {
            curType.setExpression(type.getExpression());
        }
    }

    public TypeSelectExpression getType() {
        if (Objects.isNull(super.getSelectItems())) {
            return null;
        } else {
            return super.getSelectItems().stream()
                .filter(item -> item instanceof TypeSelectExpression)
                .map(TypeSelectExpression.class::cast).findFirst().orElse(null);
        }
    }

    public void setAllColumn() {
        this.getSelectItems().clear();
        this.addSelectItems(new AllColumns());
    }

    public void createTrueValColumn() {
        ValSelectExpression val = new ValSelectExpression(new LongValue(1L));
        this.setVal(val);
    }

    public ResSelectExpression getRes() {
        if (Objects.isNull(super.getSelectItems())) {
            return null;
        } else {
            return super.getSelectItems().stream()
                .filter(item -> item instanceof ResSelectExpression)
                .map(ResSelectExpression.class::cast).findFirst().orElse(null);
        }
    }

    public ValSelectExpression getVal() {
        if (Objects.isNull(super.getSelectItems())) {
            return null;
        } else {
            return super.getSelectItems().stream()
                .filter(item -> item instanceof ValSelectExpression)
                .map(ValSelectExpression.class::cast).findFirst().orElse(null);
        }
    }

    public void setRes(ResSelectExpression res) {
        ResSelectExpression curRes = this.getRes();
        if (Objects.isNull(curRes)) {
            super.addSelectItems(res);
        } else {
            curRes = res;
        }
    }

    public void setVal(ValSelectExpression val) {
        ValSelectExpression curVal = this.getVal();
        if (Objects.isNull(curVal)) {
            super.addSelectItems(val);
        } else {
            curVal.setExpression(val.getExpression());
        }
    }

    public void addVar(VarSelectExpression varSelectExpression) {
        this.vars.add(varSelectExpression);
        if (Objects.isNull(this.getSelectItems())) {
            this.setSelectItems(new LinkedList<>());
        }
        this.getSelectItems().add(varSelectExpression.getRef());
    }

    public LinkedList<VarSelectExpression> getVars() {
        return this.vars;
    }

    public String toStringWithDescription() {
        StringBuilder sql = new StringBuilder();
        if (super.isUseBrackets()) {
            sql.append("(");
            if (this.correspondOCLExpression != null
                && !this.correspondOCLExpression.isEmpty())
                sql.append(String.format("\n/*** BEGIN: %s ***/\n",
                    this.correspondOCLExpression));
        } else {
            if (this.correspondOCLExpression != null
                && !this.correspondOCLExpression.isEmpty())
                sql.append(String.format("\n/*** BEGIN: %s ***/\n",
                    this.correspondOCLExpression));
        }
        sql.append("SELECT ");

        if (super.getOracleHint() != null) {
            sql.append(super.getOracleHint()).append(" ");
        }

        if (super.getSkip() != null) {
            sql.append(super.getSkip()).append(" ");
        }

        if (super.getFirst() != null) {
            sql.append(super.getFirst()).append(" ");
        }

        if (super.getDistinct() != null) {
            sql.append(super.getDistinct()).append(" ");
        }
        if (super.getTop() != null) {
            sql.append(super.getTop()).append(" ");
        }
        if (super.getMySqlSqlNoCache()) {
            sql.append("SQL_NO_CACHE").append(" ");
        }
        if (super.getMySqlSqlCalcFoundRows()) {
            sql.append("SQL_CALC_FOUND_ROWS").append(" ");
        }
        sql.append(getStringList(super.getSelectItems()));

        if (super.getIntoTables() != null) {
            sql.append(" INTO ");
            for (Iterator<Table> iter = super.getIntoTables().iterator(); iter
                .hasNext();) {
                sql.append(iter.next().toString());
                if (iter.hasNext()) {
                    sql.append(", ");
                }
            }
        }

        if (super.getFromItem() != null) {
            if (super.getFromItem() instanceof SubSelect) {
                sql.append("\nFROM ").append(((SubSelect) super.getFromItem())
                    .toStringWithDescription());
            } else {
                sql.append("\nFROM ").append(super.getFromItem());
            }
            if (super.getJoins() != null) {
                Iterator<Join> it = super.getJoins().iterator();
                while (it.hasNext()) {
                    Join join = it.next();
                    String joinText;
                    if (join instanceof org.vgu.ocl2psql.sql.statement.select.Join) {
                        joinText = ((org.vgu.ocl2psql.sql.statement.select.Join) join)
                            .toStringWithDescription();
                    } else {
                        joinText = join.toString();
                    }
                    if (join.isSimple()) {
                        sql.append(",\n").append(joinText);
                    } else {
                        sql.append("\n").append(joinText);
                    }
                }
            }

            if (super.getKsqlWindow() != null) {
                sql.append(" WINDOW ").append(super.getKsqlWindow().toString());
            }
            if (super.getWhere() != null) {
                sql.append("\nWHERE ").append(super.getWhere());
            }
            if (super.getOracleHierarchical() != null) {
                sql.append(super.getOracleHierarchical().toString());
            }
            if (super.getGroupBy() != null) {
                sql.append("\n").append(super.getGroupBy().toString());
            }
            if (super.getHaving() != null) {
                sql.append("\nHAVING ").append(super.getHaving());
            }
            sql.append(orderByToString(super.isOracleSiblings(),
                super.getOrderByElements()));
            if (super.getLimit() != null) {
                sql.append(super.getLimit());
            }
            if (super.getOffset() != null) {
                sql.append(super.getOffset());
            }
            if (super.getFetch() != null) {
                sql.append(super.getFetch());
            }
            if (isForUpdate()) {
                sql.append(" FOR UPDATE");

                if (super.getForUpdateTable() != null) {
                    sql.append(" OF ").append(super.getForUpdateTable());
                }

                if (super.getWait() != null) {
                    // Wait's toString will do the formatting for us
                    sql.append(super.getWait());
                }
            }
            if (super.getOptimizeFor() != null) {
                sql.append(super.getOptimizeFor());
            }
        } else {
            // without from
            if (super.getWhere() != null) {
                sql.append(" WHERE ").append(super.getWhere());
            }
        }
        if (super.getForXmlPath() != null) {
            sql.append(" FOR XML PATH(").append(super.getForXmlPath())
                .append(")");
        }
        if (super.isUseBrackets()) {
            if (this.correspondOCLExpression != null
                && !this.correspondOCLExpression.isEmpty())
                sql.append(String.format("\n/*** END: %s ***/\n",
                    this.correspondOCLExpression));
            sql.append(")");
        } else {
            if (this.correspondOCLExpression != null
                && !this.correspondOCLExpression.isEmpty())
                sql.append(String.format("\n/*** END: %s ***/\n",
                    this.correspondOCLExpression));
        }
        return sql.toString();
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        if (super.isUseBrackets()) {
            sql.append("(");
            if (this.correspondOCLExpression != null
                && !this.correspondOCLExpression.isEmpty())
                sql.append(String.format("\n/*** BEGIN: %s ***/\n",
                    this.correspondOCLExpression));
        } else {
            if (this.correspondOCLExpression != null
                && !this.correspondOCLExpression.isEmpty())
                sql.append(String.format("\n/*** BEGIN: %s ***/\n",
                    this.correspondOCLExpression));
        }
        sql.append("SELECT ");

        if (super.getOracleHint() != null) {
            sql.append(super.getOracleHint()).append(" ");
        }

        if (super.getSkip() != null) {
            sql.append(super.getSkip()).append(" ");
        }

        if (super.getFirst() != null) {
            sql.append(super.getFirst()).append(" ");
        }

        if (super.getDistinct() != null) {
            sql.append(super.getDistinct()).append(" ");
        }
        if (super.getTop() != null) {
            sql.append(super.getTop()).append(" ");
        }
        if (super.getMySqlSqlNoCache()) {
            sql.append("SQL_NO_CACHE").append(" ");
        }
        if (super.getMySqlSqlCalcFoundRows()) {
            sql.append("SQL_CALC_FOUND_ROWS").append(" ");
        }
        sql.append(getStringList(super.getSelectItems()));

        if (super.getIntoTables() != null) {
            sql.append(" INTO ");
            for (Iterator<Table> iter = super.getIntoTables().iterator(); iter
                .hasNext();) {
                sql.append(iter.next().toString());
                if (iter.hasNext()) {
                    sql.append(", ");
                }
            }
        }

        if (super.getFromItem() != null) {
            if (super.getFromItem() instanceof SubSelect) {
                sql.append("\nFROM ").append(((SubSelect) super.getFromItem())
                    .toStringWithDescription());
            } else {
                sql.append("\nFROM ").append(super.getFromItem());
            }
            if (super.getJoins() != null) {
                Iterator<Join> it = super.getJoins().iterator();
                while (it.hasNext()) {
                    Join join = it.next();
                    String joinText;
                    if (join instanceof org.vgu.ocl2psql.sql.statement.select.Join) {
                        joinText = ((org.vgu.ocl2psql.sql.statement.select.Join) join)
                            .toStringWithDescription();
                    } else {
                        joinText = join.toString();
                    }
                    if (join.isSimple()) {
                        sql.append(",\n").append(joinText);
                    } else {
                        sql.append("\n").append(joinText);
                    }
                }
            }

            if (super.getKsqlWindow() != null) {
                sql.append(" WINDOW ").append(super.getKsqlWindow().toString());
            }
            if (super.getWhere() != null) {
                sql.append("\nWHERE ").append(super.getWhere());
            }
            if (super.getOracleHierarchical() != null) {
                sql.append(super.getOracleHierarchical().toString());
            }
            if (super.getGroupBy() != null) {
                sql.append("\n").append(super.getGroupBy().toString());
            }
            if (super.getHaving() != null) {
                sql.append("\nHAVING ").append(super.getHaving());
            }
            sql.append(orderByToString(super.isOracleSiblings(),
                super.getOrderByElements()));
            if (super.getLimit() != null) {
                sql.append(super.getLimit());
            }
            if (super.getOffset() != null) {
                sql.append(super.getOffset());
            }
            if (super.getFetch() != null) {
                sql.append(super.getFetch());
            }
            if (isForUpdate()) {
                sql.append(" FOR UPDATE");

                if (super.getForUpdateTable() != null) {
                    sql.append(" OF ").append(super.getForUpdateTable());
                }

                if (super.getWait() != null) {
                    // Wait's toString will do the formatting for us
                    sql.append(super.getWait());
                }
            }
            if (super.getOptimizeFor() != null) {
                sql.append(super.getOptimizeFor());
            }
        } else {
            // without from
            if (super.getWhere() != null) {
                sql.append(" WHERE ").append(super.getWhere());
            }
        }
        if (super.getForXmlPath() != null) {
            sql.append(" FOR XML PATH(").append(super.getForXmlPath())
                .append(")");
        }
        if (super.isUseBrackets()) {
            if (this.correspondOCLExpression != null
                && !this.correspondOCLExpression.isEmpty())
                sql.append(String.format("\n/*** END: %s ***/\n",
                    this.correspondOCLExpression));
            sql.append(")");
        } else {
            if (this.correspondOCLExpression != null
                && !this.correspondOCLExpression.isEmpty())
                sql.append(String.format("\n/*** END: %s ***/\n",
                    this.correspondOCLExpression));
        }
        return sql.toString();
    }

    public String getCorrespondOCLExpression() {
        return correspondOCLExpression;
    }

    public void setCorrespondOCLExpression(String correspondOCLExpression) {
        this.correspondOCLExpression = correspondOCLExpression;
    }

    public static String getStringList(List<?> list) {
        return getStringList(list, true, false);
    }

    public static String getStringList(List<?> list, boolean useComma,
        boolean useBrackets) {
        StringBuilder ans = new StringBuilder();
//        String ans = "";
        String comma = ",";
        if (!useComma) {
            comma = "";
        }
        if (list != null) {
            if (useBrackets) {
                ans.append("(");
//                ans += "(";
            }
            List<?> newList = list.stream()
                .filter(i -> !(i instanceof TypeSelectExpression
                    && !((TypeSelectExpression) i).isTypeMode))
                .collect(Collectors.toList());
            for (int i = 0; i < newList.size(); i++) {
                ans.append(newList.get(i))
                    .append((i < newList.size() - 1) ? comma + " " : "");
//                ans += "" + list.get(i) + ((i < list.size() - 1) ? comma + " " : "");
            }

            if (useBrackets) {
                ans.append(")");
//                ans += ")";
            }
        }

        return ans.toString();
    }

}
