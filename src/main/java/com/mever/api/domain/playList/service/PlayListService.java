package com.mever.api.domain.playList.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberRepository;
import com.mever.api.domain.payment.dto.CancelOrderDto;
import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import com.mever.api.domain.payment.entity.Orders;
import com.mever.api.domain.payment.entity.Subscription;
import com.mever.api.domain.payment.repository.CancelOrderRepository;
import com.mever.api.domain.payment.repository.OrderMapper;
import com.mever.api.domain.payment.repository.OrderRepository;
import com.mever.api.domain.payment.repository.SubscriptionRepository;
import com.mever.api.domain.playList.dto.PlayListDto;
import com.mever.api.domain.playList.entity.PlayList;
import com.mever.api.domain.playList.repository.PlayListRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayListService {
    private final PlayListRepository playListRepository;

    @Transactional
    public List<PlayList> playList(String homepage) {
        List<PlayList> playList=playListRepository.findByHomePage(homepage);
        return playList;
    }
    @Transactional
    public ResponseEntity playListAdd(PlayListDto playListDto) {
     playListRepository.save(PlayList.builder()
             .videoId(playListDto.getVideoId())
             .trackTitle(playListDto.getTrackTitle())
             .homePage(playListDto.getHomePage())
             .build());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity playListDel(PlayListDto playListDto) {
        playListRepository.deleteByVideoIdAndHomePage(playListDto.getVideoId(),playListDto.getHomePage());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
