package org.g9project4.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.member.services.MemberSaveService;
import org.g9project4.member.validators.JoinValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@SessionAttributes({"requestLogin", "EmailAuthVerified"})
public class MemberController implements ExceptionProcessor {

    private final JoinValidator joinValidator;
    private final MemberSaveService memberSaveService;
    private final Utils utils;

    @ModelAttribute
    public RequestLogin requestLogin() {
        return new RequestLogin();
    }

    @ModelAttribute("EmailAuthVerified")
    public Boolean emailAuthVerified() {
        return false; // 이메일 인증 여부 초기화
    }

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);
        model.addAttribute("addCss", "join");
        model.addAttribute("EmailAuthVerified", false); // 이메일 인증 여부 초기화
        return "front/member/join";
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model, SessionStatus sessionStatus) {
        model.addAttribute("addCss", "join");
        joinValidator.validate(form, errors);
        errors.getAllErrors().forEach(System.out::println);
        if (errors.hasErrors()) {
            return "front/member/join";
        }
        memberSaveService.save(form);
        sessionStatus.setComplete(); // EmailAuthVerified 세션값 비우기
        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(@Valid @ModelAttribute RequestLogin form, Errors errors, Model model) {
        model.addAttribute("addCss", "join");
        String code = form.getCode();
        if (StringUtils.hasText(code)) {
            errors.reject(code, form.getDefaultMessage());
            //비번이 만료인 경우 비번 재설정 페이지 이동
            if (code.equals("CredentialsExpired.Login")) {
                return "redirect:/member/password/reset ";
            }
        }
        return "front/member/login";
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "join";
        String pageTitle = utils.getMessage("회원가입");

        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        if (mode.equals("login")) { // 로그인
            pageTitle = utils.getMessage("로그인");

        } else if (mode.equals("join")) { // 회원가입
            addCss.add("member/join");
            addScript.add("member/join");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCss", addCss);
        model.addAttribute("addScript", addScript);
    }
}
