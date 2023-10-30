package com.gpt.morph.controller;

import com.gpt.morph.model.MorphDTO;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class MorphController {


    @GetMapping("/morph")
    public ResponseEntity<?> test(@RequestParam(name = "request") String request) {
        System.out.println("요청 들어옴");
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        KomoranResult analyzeResultList = komoran.analyze(request.replaceAll("[^a-zA-Z가-힣\\s]", ""));
        List<Token> tokenList = analyzeResultList.getTokenList();
        List<MorphDTO> morphDTOS = new ArrayList<>();
        MorphDTO morphDTO;
        for (Token token : tokenList) {
            morphDTO = MorphDTO.builder()
                    .morph(token.getMorph())
                    .end(token.getEndIndex())
                    .start(token.getBeginIndex())
                    .pos(token.getPos())
                    .build();

            morphDTOS.add(morphDTO);

        }

        return ResponseEntity.ok().body(morphDTOS);
    }

}
