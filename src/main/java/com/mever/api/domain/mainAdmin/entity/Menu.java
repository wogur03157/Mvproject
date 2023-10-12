package com.mever.api.domain.mainAdmin.entity;

import com.mever.api.domain.member.dto.MemberRes;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    int seq;
    @Column(name = "menu_index",nullable = true)
    String menuIndex;
    @Column(name = "url",nullable = true)
    String url;
    @Column(name = "menu_name",nullable = true)
    String menuName;
    @Column(name = "use_yn",nullable = true)
    String useYn;
    @Column(name = "music",nullable = true)
    String music;
    @Column(name = "matterLink",nullable = true)
    String matterLink;
    @Column(name = "order_num",nullable = true)
    String orderNum;
    @Column(name = "insert_date",nullable = true)
    String insertDate;
    @Column(name = "map",nullable = true)
    String map;
    @Column(name = "site_category",nullable = true)
    String siteCategory;
    @Column(name = "category",nullable = true)
    String category;
    @Column(name = "video_url",nullable = true)
    String videoUrl;
    @Column(name = "thumb_img",nullable = true)
    String thumbImg;
    @Column(name = "web_img",nullable = true)
    String webImg;
    @Column(name = "og_title",nullable = true)
    String ogTitle;
    @Column(name = "og_description",nullable = true)
    String ogDescription;
    public void setValuesFromRequestData(Map<String, String> requestData) {
        String url = requestData.get("url");
        this.setUrl(url != null ? url : this.getUrl());

        String menuName = requestData.get("menuName");
        this.setMenuName(menuName != null ? menuName : this.getMenuName());

        String videoUrl = requestData.get("videoUrl");
        this.setVideoUrl(videoUrl != null ? videoUrl : this.getVideoUrl());

        String thumbImg = requestData.get("thumbImg");
        this.setThumbImg(thumbImg != null ? thumbImg : this.getThumbImg());

        String useYn = requestData.get("useYn");
        this.setUseYn(useYn != null ? useYn : this.getUseYn());

        String music = requestData.get("music");
        this.setMusic(music != null ? music : this.getMusic());

        String webImg = requestData.get("webImg");
        this.setWebImg(webImg != null ? webImg : this.getWebImg());

        String matterLink = requestData.get("matterLink");
        this.setMatterLink(matterLink != null ? matterLink : this.getMatterLink());

        String map = requestData.get("map");
        this.setMap(map != null ? map : this.getMap());

        String orderNum = requestData.get("orderNum");
        this.setOrderNum(orderNum != null ? orderNum : this.getOrderNum());

        String ogTitle = requestData.get("ogTitle");
        this.setOgTitle(ogTitle != null ? ogTitle : this.getOgTitle());

        String ogDescription = requestData.get("ogDescription");
        this.setOgDescription(ogDescription != null ? ogDescription : this.getOgDescription());
    }
}

