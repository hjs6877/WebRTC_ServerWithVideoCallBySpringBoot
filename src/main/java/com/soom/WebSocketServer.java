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
 * - 작성자, 2017-07-05 최초 작성<br/>
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
             * caller는 우선 token을 생성해서 보유하고 있다고 가정한다. TODO 서버쪽에서 방에 대한 토큰을 생성하는것은 나중 문제.
             * caller일 경우, discussions 방은 없을 것이므로 discussion을 생성하고 해당 sessionID를 discussion에 저장한다.
             */
            if(type.equals("join") && (!StringUtil.nullToWhiteSpace(token).isEmpty())){
                if(discussions.get(token) == null){

                    List<String> userSessionIds = new ArrayList<>();
                    userSessionIds.add(id);
                    discussions.put(token, userSessionIds);
                }
            }else if(!StringUtil.nullToWhiteSpace(token).isEmpty()){
                List<String> userSessionIds = discussions.get(token);

                // callee가 접속했을 경우 callee의 id를 저장한다.
                if(type.equals("callee_arrived")){
                    userSessionIds.add(id);
                }

                // discussion에 저장된 user들 중에서 자기 자신이 아닌 user에게만 시그널 메시지를 전송한다.
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
