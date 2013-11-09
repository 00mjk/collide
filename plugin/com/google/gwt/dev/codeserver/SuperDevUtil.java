package com.google.gwt.dev.codeserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import xapi.log.X_Log;

import com.google.collide.dto.GwtCompile;
import com.google.gwt.core.ext.TreeLogger;

public class SuperDevUtil {

  private static final ConcurrentHashMap<String, RecompileController> compilers
    = new ConcurrentHashMap<String, RecompileController>();

  public static RecompileController getOrMakeController(TreeLogger logger, GwtCompile request, int port) {
    String module = request.getModule();
    RecompileController ret = compilers.get(module);
    if (ret != null)  {
      ret.cleanup();
      return ret;
    }
    AppSpace app;
    try {
      
      boolean local = !Boolean.getBoolean("xapi.local");
      File tmp = local ? File.createTempFile("recompile", "log")
          : new File("/home/ajaxian/tmp"); // <- My vps has limited /tmp, so I have to move it out
      tmp.deleteOnExit();
      app = AppSpace.create(local ? tmp.getParentFile() : tmp);
    } catch (IOException e1) {
      throw new Error("Unable to initialize gwt recompiler ",e1);
    }
    List<File> sourcePath = new ArrayList<File>();
    for (String src : request.getDeps().asIterable()){
      //TODO: sanitize this somehow?
      if (".".equals(src))src = new File("").getAbsolutePath();
      if (src.startsWith("file:"))src = src.substring(5);
      File dir = new File(src);
      if (!dir.exists()){
        X_Log.error("Gwt source directory "+dir+" does not exist");
      }else
        X_Log.trace("Adding to source: "+dir);
      sourcePath.add(dir);
    }
      Recompiler compiler = new Recompiler(app, module.split("/")[0], sourcePath ,
        "127.0.0.1:"+port,logger);
      try{
        RecompileController recompiler = new RecompileController(compiler);
        compilers.put(module, recompiler);
        return recompiler;
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
  }



}