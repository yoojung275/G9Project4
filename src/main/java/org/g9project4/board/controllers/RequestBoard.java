package org.g9project4.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.g9project4.file.entities.FileInfo;

import java.util.List;
import java.util.UUID;

@Data
public class RequestBoard {
    private Long seq; // 글 번호 - 글 수정시 필요
    private String mode = "write"; // write : 글 작성, update - 글 수정

    @NotBlank
    private String bid; // 게시판 ID

    private String gid = UUID.randomUUID().toString();

    private boolean notice; // 공지글 여부

    private String category;

    @NotBlank
    private String poster; // 작성자

    private boolean guest; // 비회원
    private String guestPw; // 비회원 비밀번호(수정, 삭제)

    @NotBlank
    private String subject;

    @NotBlank
    private String content;

    private Long num1;
    private Long num2;
    private Long num3;

    private String text1;
    private String text2;
    private String text3;

    private String longText1;
    private String longText2;

    private List<FileInfo> editorImages;
    private List<FileInfo> attachFiles;
}