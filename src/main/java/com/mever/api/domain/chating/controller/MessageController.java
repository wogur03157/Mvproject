package com.mever.api.domain.chating.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@ServerEndpoint("/websocket")
public class MessageController {

    // 현재 채팅 서버에 접속한 클라이언트(WebSocket Session) 목록
    // static 붙여야함!!
    private static List<Session> list = new ArrayList<Session>();

    private void print(String msg) {
        System.out.printf("[%tT] %s\n", Calendar.getInstance(), msg);
    }

    @OnOpen
    public void handleOpen(Session session) {
        print("클라이언트 연결");
        list.add(session); // 접속자 관리(****)
    }

    @OnMessage
    public void handleMessage(String msg, Session session) {
        // 로그인할 때: 1#유저명
        // 대화  할 때: 2유저명#메세지
        int index = msg.indexOf("#", 2);
        String no = msg.substring(0, 1);
        String user = msg.substring(2, index);
        String txt = msg.substring(index + 1);
        if (no.equals("1")) {
            // 누군가 접속 > 1#아무개
            for (Session s : list) {
                if (s != session) { // 현재 접속자가 아닌 나머지 사람들
                    try {
                        s.getBasicRemote().sendText("1#" + user + "#");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (no.equals("2")) {
            // 누군가 메세지를 전송
            for (Session s : list) {

                if (s != session) { // 현재 접속자가 아닌 나머지 사람들
                    try {
                        s.getBasicRemote().sendText("2#" + user + ":" + txt);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (no.equals("3")) {
            // 누군가 접속 > 3#아무개
            for (Session s : list) {

                if (s != session) { // 현재 접속자가 아닌 나머지 사람들
                    try {
                        s.getBasicRemote().sendText("3#" + user + "#");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.remove(session);
        }
    }

    @OnClose
    public void handleClose(Session session) {
        print("클라이언트 연결 종료");
        list.remove(session); // 접속자 관리에서 해당 세션 제거(****)
    }

    @OnError
    public void handleError(Session session, Throwable t) {
        print("에러 발생");
        t.printStackTrace();

        // 에러가 발생한 세션을 접속자 목록에서 제거
        list.remove(session);

        try {
            session.close(); // 에러가 발생한 세션을 닫음
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}