package org.g9project4.member.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.g9project4.file.services.FileUploadDoneService;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.constants.Gender;
import org.g9project4.member.constants.Interest;
import org.g9project4.member.controllers.RequestJoin;
import org.g9project4.member.entities.Authorities;
import org.g9project4.member.entities.Interests;
import org.g9project4.member.entities.Member;
import org.g9project4.member.exceptions.MemberNotFoundException;
import org.g9project4.member.repositories.AuthoritiesRepository;
import org.g9project4.member.repositories.InterestsRepository;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.mypage.controllers.RequestProfile;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberSaveService {
    private final FileUploadDoneService uploadDoneService;
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberUtil memberUtil;
   private final InterestsRepository interestsRepository;

    /**
     * 회원 가입 처리
     *
     * @param form
     */
    public void save(RequestJoin form) {
        Member member = new ModelMapper().map(form, Member.class);
        String hash = passwordEncoder.encode(member.getPassword());
        member.setPassword(hash);
        save(member, List.of(Authority.USER));
    }

    /**
     * 회원정보 수정
     *
     * @param form
     */
    public void save(RequestProfile form) {

        Member member = memberUtil.getMember();
        String email = member.getEmail();
        member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        String password = form.getPassword();
        String mobile = form.getMobile();
        LocalDate birth = form.getBirth();
        Gender gende = form.getGende();
        Boolean isForeigner = form.getIsForeigner();



        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
        }

        member.setUserName(form.getUserName());
        member.setMobile(mobile);

        if (StringUtils.hasText(password)) {
            String hash = passwordEncoder.encode(password);
            member.setPassword(hash);
        }

        member.setBirth(birth);
        member.setGende(gende);
        member.setIsForeigner(isForeigner);


        // 기존 관심사 데이터 삭제
        interestsRepository.deleteByMember(member); // Use repository to delete existing interests

        final Member finalMember = member;

        // RequestProfile form에서 Interest 목록 가져오기
        List<Interests> newInterestsEntities = form.getInterests().stream()
                .map(interest -> new Interests(finalMember, interest.getInterest()))
                .collect(Collectors.toList());

        // Save new interests
        interestsRepository.saveAllAndFlush(newInterestsEntities);

        save(member, null);
    }


    public void save(Member member, List<Authority> authorities) {
        //휴대폰 번호 숫자만 기록
        String mobile = member.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
            member.setMobile(mobile);
        }

        memberRepository.saveAndFlush(member);
        if (authorities != null) {
            List<Authorities> items = authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();

            items = authorities.stream().map(authority -> Authorities.builder()
                    .member(member)
                    .authority(authority)
                    .build()).toList();
            authoritiesRepository.saveAllAndFlush(items);
        }

        //파일 업로드 완료 처리
        System.out.println("여기???");
        uploadDoneService.process(member.getGid());
    }
}
