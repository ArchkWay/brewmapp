
package com.brewmapp.data.entity;

import java.util.HashMap;
import java.util.Map;

public class LocalizedStrings extends HashMap<String, String> {

  public HashMap<String, String> getStrings() {

    return this;
  }

  @Override
  public String toString() {

//    Map.Entry<String, String> entry = this.entrySet().iterator().next();
//    String value = entry.getValue();
    return this.get("1");
  }

  public String getString(String key){
      return this.get(key);
  }

  public String setString(String val){
    return this.put("1", val);
  }
}
