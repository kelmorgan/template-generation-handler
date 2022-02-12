package com.kelmorgan.templategenerationhandler.service;

import com.kelmorgan.templategenerationhandler.request.RequestXml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Generate implements GenerateDocumentHandler {

    private String wiName;
    private String jtsPort;
    private String jtsIp;
    private String sessionId;
    private String serverIp;
    private String serverPort;
    private String serverName;
    private String cabinetName;
    private String processName;
    private String templateName;
    private String currentWorkStep;
    private int templatePort;

    public Generate(String wiName, String jtsPort, String jtsIp, String sessionId, String serverIp, String serverPort, String serverName, String cabinetName, String processName, String templateName, String currentWorkStep, int templatePort) {
        this.wiName = wiName;
        this.jtsPort = jtsPort;
        this.jtsIp = jtsIp;
        this.sessionId = sessionId;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.serverName = serverName;
        this.cabinetName = cabinetName;
        this.processName = processName;
        this.templateName = templateName;
        this.currentWorkStep = currentWorkStep;
        this.templatePort = templatePort;
    }

    public Generate() {
    }

    @Override
    public String generateDocument() {

        String response;
        try {
            String request = RequestXml.getGenerateTemplateRequest(wiName, jtsIp, jtsPort, sessionId, serverIp, serverPort, serverName, cabinetName, processName, templateName, currentWorkStep);
            try {
                response = callSocketServer(serverIp, templatePort, request);
            } catch (Exception e) {
                response = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<message>\n"
                        + "<ErrorCode>1</ErrorCode>\n"
                        + "<ErrorDesc>Error while generating template.</ErrorDesc>\n"
                        + "</message>";
            }
        } catch (Exception e) {
            response = "Exception occurred in generateDoc Method: " + e.getMessage();
        }
        return response;
    }

    private String callSocketServer(String serverIp, int templatePort, String request) {
        final String SS_EXEC_ERROR_MSG = "Error from Call Client Socket Server while Web-Service execution";
        final String SS_CONN_ERROR_MSG = "Could not connect to Call Client Socket Server";
        String responseXml;
        String sTemp = "";
        try {
            String tempResponseXml;
            Socket client = new Socket(serverIp, templatePort);
            client.setSoTimeout(300000);

            try {
                DataOutputStream outData = new DataOutputStream(client.getOutputStream());
                byte[] dataByteArr = request.getBytes(StandardCharsets.UTF_8);
                outData.writeInt(dataByteArr.length);
                outData.write(dataByteArr);
                DataInputStream in = new DataInputStream(client.getInputStream());
                int dataLength = in.readInt();
                byte[] data = new byte[dataLength];
                in.readFully(data);
                tempResponseXml = new String(data, StandardCharsets.UTF_8);
                in.close();
            } catch (IOException e) {
                tempResponseXml = SS_CONN_ERROR_MSG;
            } catch (Exception e) {
                tempResponseXml = SS_EXEC_ERROR_MSG;
            }

            if (tempResponseXml.length() == 0) {
                responseXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<message>\n" +
                        "<MainCode>-1</MainCode>\n" +
                        "<EDESC>No Response Received from Call Client Socket Server.</EDESC>\n" +
                        "</message>";
            } else {
                if (tempResponseXml.equals(SS_EXEC_ERROR_MSG) || tempResponseXml.equals(SS_CONN_ERROR_MSG)) {
                    responseXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                            + "<message>\n"
                            + "<MainCode>-1</MainCode>\n"
                            + "<EDESC>" + tempResponseXml + "</EDESC>\n"
                            + "</message>";
                } else {
                    responseXml = tempResponseXml;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<message>\n" +
                    "<MainCode>-1</MainCode>\n" +
                    "<EDESC>Not able to Connect with Socket Server.</EDESC>\n" +
                    "</message>";
        }

        return sTemp + responseXml;
    }

    public void setWiName(String wiName) {
        this.wiName = wiName;
    }

    public void setJtsPort(String jtsPort) {
        this.jtsPort = jtsPort;
    }

    public void setJtsIp(String jtsIp) {
        this.jtsIp = jtsIp;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setCurrentWorkStep(String currentWorkStep) {
        this.currentWorkStep = currentWorkStep;
    }

    public void setTemplatePort(int templatePort) {
        this.templatePort = templatePort;
    }
}
