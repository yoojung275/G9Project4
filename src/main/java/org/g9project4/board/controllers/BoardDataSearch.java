package org.g9project4.board.controllers;

import lombok.Data;
import org.g9project4.global.CommonSearch;

import java.util.List;

@Data
public class BoardDataSearch extends CommonSearch {

    private int limit;

    private String bid; // 게시판 ID
    private List<String> bids; // 게시한 ID 여러개

    private List<String> category; // 분류 검색

    private Boolean notice; // 공지글

    private String sort; // 정렬 조건

    private Long num1;
}