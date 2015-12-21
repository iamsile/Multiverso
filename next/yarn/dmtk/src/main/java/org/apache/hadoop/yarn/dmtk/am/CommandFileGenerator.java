package org.apache.hadoop.yarn.dmtk.am;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.hadoop.yarn.dmtk.DSConstants;

public class CommandFileGenerator {
  CommandFileGenerator(boolean isVerboseOn, int workerServerPort,
      int workerNum, int serverNum, String workerArgs, String serverArgs,
      String appId) {
    isVerboseOn_ = isVerboseOn;
    workerServerPort_ = workerServerPort;
    workerNum_ = workerNum;
    serverNum_ = serverNum;
    workerArgs_ = workerArgs;
    serverArgs_ = serverArgs;
    appId_ = appId;
  }

  public void GenerateCommandFile(String type, int id, String fileName, String ip) throws Exception {
    if (type == DSConstants.WORKER)
      GenerateWorkerCommandFile(id, fileName);
    else
      GenerateServerCommandFile(id, ip, fileName);
  }

  /**
  * start.bat endpointlist machinelist workerId workerNum serverNum workerServerPort workerArgs appId
  */
  private void GenerateWorkerCommandFile(int workerId, String fileName) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(
        fileName));
    out.write("cd " + DSConstants.WORKERDIR + " \n");
    if (DSConstants.isWindow) {
	    if (isVerboseOn_) {
	      out.write("echo 'files:'\ndir /s \n");
	      out.write("echo 'user:'\nwhoami\n");
	      out.write("echo 'content of cmd:'\ntype " + DSConstants.STARTFILE + " " + "\n");
	      out.write("echo 'content of endpointlist:'\ntype " + DSConstants.ENDPOINTLIST + "\n");
	      out.write("echo 'content of machinelist:'\ntype " + DSConstants.MACHINELISTFILE + "\n");
	    }
	
	    out.write("call " + DSConstants.STARTFILE + " "
	        + DSConstants.ENDPOINTLIST + " " 
	    	+ DSConstants.MACHINELISTFILE + " " + workerId + " "
	    	+ workerNum_ + " " + serverNum_ + " "
	        + workerServerPort_ + " " + workerArgs_ + " " + appId_ + " 2>&1 \n");
	    out.write("echo worker " + workerId + " exit with code %errorlevel%\n");
	    out.write("exit /b %errorlevel%\n");
	    out.close();
    } else {
	    if (isVerboseOn_) {
		      out.write("echo 'files:'\nls \n");
		      out.write("echo 'user:'\nwhoami\n");
		      out.write("echo 'content of cmd:'\ncat " + DSConstants.STARTFILE + " " + "\n");
		      out.write("echo 'content of endpointlist:'\ncat " + DSConstants.ENDPOINTLIST + "\n");
		      out.write("echo 'content of machinelist:'\ncat " + DSConstants.MACHINELISTFILE + "\n");
		    }
		
		    out.write("./" + DSConstants.STARTFILE + " "
		        + DSConstants.ENDPOINTLIST + " "
                + DSConstants.MACHINELISTFILE + " " + workerId + " " 
                + workerNum_ + " " + serverNum_ + " "
		        + workerServerPort_ + " " + workerArgs_ + " " + appId_ + " 2>&1 \n");
		    out.close();
    }
  }

  /**
  * start.bat serverId workerNum serverNum ip workerServerPort serverArgs
  */
  private void GenerateServerCommandFile(int serverId, String ip, String fileName) throws IOException {
    BufferedWriter out = new BufferedWriter(
        new FileWriter(fileName));
    out.write("cd " + DSConstants.SERVERDIR + " \n");
    if (DSConstants.isWindow) {
	    if (isVerboseOn_) {
	      out.write("echo 'fileslist:'\ndir /s \n");
	      out.write("echo 'user:'\nwhoami\n");
	      out.write("echo 'content of cmd:'\ntype " + " " + DSConstants.STARTFILE + " \n");
	    }
	
	    out.write("call " + DSConstants.STARTFILE + " "
	        + serverId + " " + workerNum_ + " " + serverNum_ + " " + ip + " "
	        + workerServerPort_ + " " + serverArgs_ + " " + appId_ + " 2>&1 \n");
	    out.write("echo server " + serverId +" exit with code %errorlevel%\n");
	    out.write("exit /b %errorlevel%\n");
	    out.close();
    } else {
	    if (isVerboseOn_) {
		      out.write("echo 'fileslist:'\nls \n");
		      out.write("echo 'user:'\nwhoami\n");
		      out.write("echo 'content of cmd:'\ncat " + " " + DSConstants.STARTFILE + " \n");
		    }
		
		    out.write("./" + DSConstants.STARTFILE + " "
		        + serverId + " " + workerNum_ + " " + serverNum_ + " " + ip + " "
		        + workerServerPort_ + " " + serverArgs_ + " " + appId_ + " 2>&1 \n");
		    out.close();
    }
  }

  private boolean isVerboseOn_;
  private int workerServerPort_;
  private int workerNum_, serverNum_;
  private String workerArgs_;
  private String serverArgs_;
  private String appId_;
}
