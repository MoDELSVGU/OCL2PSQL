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

package org.vgu.ocl2psql.ocl.exception;


public class OclParseException extends OclException {

  private static final long serialVersionUID = 1L;

  public OclParseException() {
    super();
  }

  public OclParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public OclParseException(String message) {
    super(message);
  }

  public OclParseException(Throwable cause) {
    super(cause);
  }

}
