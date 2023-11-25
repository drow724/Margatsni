package com.instagl.controller;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login/")
@RequiredArgsConstructor
public class LoginController {
    //TODO Service

    @CrossOrigin
    @PostMapping("google")
    public void getLoginInfoWithGoogle(@RequestBody Object info) throws InterruptedException, ParseException {
        //TODO 구글 로그인
    }

}
