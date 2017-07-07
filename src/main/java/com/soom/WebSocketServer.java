package com.soom;

import com.soom.utils.StringUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * summary:
 * <p> description:
 * <p><b>History:</b>
 * - �ۼ���, 2017-07-05 ���� �ۼ�<br/>
 *
 * @author Kevin
 * @see
 */
@ServerEndpoint(value = "/ws")
public class WebSocketServer {
    static Map<String, Session> sessionUsers = Collections.synchronizedMap(new HashMap<String, Session>());
    static Map<String, List<String>> discussions = Collections.synchronizedMap(new HashMap<String, List<String>>());
    JSONParser parser;

    public WebSocketServer(JSONParser parser) {
        this.parser = parser;
    }


    @OnOpen
    public void handleOpen(Session userSession) throws IOException {
        String userSessionId = userSession.getId();
        System.out.println("## WebRTC: Client Session is Open. ID is " + userSessionId);

        sessionUsers.put(userSessionId, userSession);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userSessionId);
        userSession.getBasicRemote().sendText(jsonObject.toJSONString());
    }

    @OnMessage
    public void handleMessage(String message) throws IOException, ParseException {
        System.out.println("## WebSocket: Send message to all client.");
        System.out.println("## message:");

        JSONObject signal = (JSONObject) parser.parse(message);

        if(!signal.isEmpty() && signal != null){
            String id = (String) signal.get("id");
            String type = (String) signal.get("type");
            String token = (String) signal.get("token");

            /**
             * caller�� �켱 token�� �����ؼ� �����ϰ� �ִٰ� �����Ѵ�. TODO �����ʿ��� �濡 ���� ��ū�� �����ϴ°��� ���� ����.
             * caller�� ���, discussions ���� ���� ���̹Ƿ� discussion�� �����ϰ� �ش� sessionID�� discussion�� �����Ѵ�.
             */
            if(type.equals("join") && (!StringUtil.nullToWhiteSpace(token).isEmpty())){
                if(discussions.get(token) == null){

                    List<String> userSessionIds = new ArrayList<>();
                    userSessionIds.add(id);
                    discussions.put(token, userSessionIds);
                }
            }else if(!StringUtil.nullToWhiteSpace(token).isEmpty()){
                List<String> userSessionIds = discussions.get(token);

                // callee�� �������� ��� callee�� id�� �����Ѵ�.
                if(type.equals("callee_arrived")){
                    userSessionIds.add(id);
                }

                // discussion�� ����� user�� �߿��� �ڱ� �ڽ��� �ƴ� user���Ը� �ñ׳� �޽����� �����Ѵ�.
                for(String userSessionId : userSessionIds){
                    if(userSessionId != id){
                        sessionUsers.get(userSessionId).getBasicRemote().sendText(message);
                    }
                }

            }else{
                throw new RuntimeException("invalid signal: " + message);
            }
        }
    }

    @OnClose
    public void handleClose(Session userSession) throws IOException {
        System.out.println("## WebSocket: Session remove complete. ID is " + userSession.getId());
        sessionUsers.remove(userSession);

//        for(Session session : sessionUsers){
//            if(session.getId() != userSession.getId()){
//                session.getBasicRemote().sendText("<i>" + session.getId() + " left the chat room.</i>");
//            }
//        }
    }

    public String JSONConverter(String message, String command, String type) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("command", command);
        jsonObject.put("message", message);
        return jsonObject.toString();
    }
}
