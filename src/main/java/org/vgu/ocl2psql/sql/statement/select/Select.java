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

import java.util.Iterator;

import net.sf.jsqlparser.statement.select.WithItem;

public class Select extends net.sf.jsqlparser.statement.select.Select{

    public String toStringWithDescription() {
        StringBuilder retval = new StringBuilder();
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
        return retval.toString();
    }

}
