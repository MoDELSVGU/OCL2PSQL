/**************************************************************************
Copyright 2020 Vietnamese-German-University

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

package org.vgu.ttc2020.model;

import org.vgu.se.sql.EStatement;

public class TTCReturnModel {
    private long ocl2sqlNanoTime;
    private Object sql;

    public long getOcl2sqlNanoTime() {
        return ocl2sqlNanoTime;
    }

    public void setOcl2sqlNanoTime(long ocl2sqlNanoTime) {
        this.ocl2sqlNanoTime = ocl2sqlNanoTime;
    }

    public EStatement getEStatement() {
        return (EStatement) sql;
    }

    public void setEStatement(EStatement sql) {
        this.sql = sql;
    }

    public String getStatement() {
        return (String) sql;
    }

    public void setStatement(String sql) {
        this.sql = sql;
    }

}
