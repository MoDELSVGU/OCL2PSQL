package org.vgu.sqlsi.test.ocl;

import java.util.List;

public class Pair {

    private final String ocl;
    private final List<String> vars;

    public Pair(String ocl, List<String> vars) {
      this.ocl = ocl;
      this.vars = vars;
    }

    public String getOcl() { return ocl; }
    public List<String> getVars() { return vars; }

    @Override
    public int hashCode() { return ocl.hashCode() ^ vars.hashCode(); }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Pair)) return false;
      Pair pairo = (Pair) o;
      return this.ocl.equals(pairo.getOcl()) &&
             this.vars.equals(pairo.getVars());
    }

  }

