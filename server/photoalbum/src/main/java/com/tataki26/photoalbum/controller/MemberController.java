package com.tataki26.photoalbum.controller;

import com.tataki26.photoalbum.dto.MemberDto;
import com.tataki26.photoalbum.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody final MemberDto memberDto) {
        return new ResponseEntity<>(memberService.addNewMember(memberDto), HttpStatus.CREATED);
    }
}
