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


package org.vgu.sqlsi.main;

public class Pair<L,R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
      this.left = left;
      this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Pair)) return false;
      Pair<?,?> pairo = (Pair<?,?>) o;
      return this.left.equals(pairo.getLeft()) &&
             this.right.equals(pairo.getRight());
    }

  }
