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


package org.vgu.ocl2psql.sql.statement.select;

import net.sf.jsqlparser.statement.select.PlainSelect;

public class Join extends net.sf.jsqlparser.statement.select.Join {
    public String toStringWithDescription() {
        String rightItem;
        if(super.getRightItem() instanceof SubSelect) {
            rightItem = ((SubSelect) super.getRightItem()).toStringWithDescription();
        } else {
            rightItem = super.getRightItem().toString();
        }
        if (isSimple() && isOuter()) {
            return "OUTER " + rightItem;
        } else if (isSimple()) {
            return "" + rightItem;
        } else {
            String type = "";

            if (isRight()) {
                type += "RIGHT ";
            } else if (isNatural()) {
                type += "NATURAL ";
            } else if (isFull()) {
                type += "FULL ";
            } else if (isLeft()) {
                type += "LEFT ";
            } else if (isCross()) {
                type += "CROSS ";
            }

            if (isOuter()) {
                type += "OUTER ";
            } else if (isInner()) {
                type += "INNER ";
            } else if (isSemi()) {
                type += "SEMI ";
            }

            return type + "JOIN " + rightItem + ((super.getJoinWindow() != null) ? " WITHIN " + super.getJoinWindow() : "")
                    + ((super.getOnExpression() != null) ? "\nON " + super.getOnExpression() + "" : "")
                    + PlainSelect.getFormatedList(super.getUsingColumns(), "USING", true, true);
        }

    }
}
