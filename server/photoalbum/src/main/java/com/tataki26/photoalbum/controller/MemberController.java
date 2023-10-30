package com.tataki26.photoalbum.controller;

import com.tataki26.photoalbum.dto.MemberDto;
import com.tataki26.photoalbum.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/users")
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody final MemberDto memberDto) {
        return new ResponseEntity<>(memberService.addNewMember(memberDto), HttpStatus.CREATED);
    }

    @PostMapping("/users/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("userId") final Long id,
                                               @Valid @RequestBody final MemberDto memberDto) {
        memberService.changePassword(id, memberDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<Void> deactivateMember(@PathVariable("userId") final Long id,
                                                 @Valid @RequestBody final MemberDto memberDto) {
        memberService.changeMemberStatus(id, memberDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<MemberDto> signIn(@Valid @RequestBody final MemberDto memberDto) {
        return new ResponseEntity<>(memberService.authenticateMember(memberDto), HttpStatus.OK);
    }
}
