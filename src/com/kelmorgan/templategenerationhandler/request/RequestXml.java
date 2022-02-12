package com.kelmorgan.templategenerationhandler.request;

public class RequestXml {

    public static String getGenerateTemplateRequest(String wiName, String jtsIp,String jtsPort, String sessionId, String serverIp, String serverPort,String serverName, String cabinetName, String processName,String templateName,String currentWorkStep){
        return "WI_NAME=" + wiName + "~~JTS_IP="+jtsIp+"~~JTS_PORT="+jtsPort+"~~SESSION_ID=" + sessionId + "~~SERVER_IP=" + serverIp + "~~SERVER_PORT=" + serverPort + "~~SERVER_NAME="+serverName+"~~CABINET_NAME=" + cabinetName + "~~PROCESS_NAME=" + processName + "~~TEMPLATE_NAME=" + templateName + "~~ACTIVITY_NAME=" + currentWorkStep;
    }
}
