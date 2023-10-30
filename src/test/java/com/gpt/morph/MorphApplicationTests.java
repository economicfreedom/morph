package com.gpt.morph;

import com.gpt.morph.model.ChatGptRequest;
import com.gpt.morph.model.ChatGptResponse;
import com.gpt.morph.service.MorphService;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
class MorphApplicationTests {

    private final MorphService morphService;
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String apiURL;
    private static final String PROMPT_1 = "\n" +
            "나는 지금 형태소 분석된 데이터로 너에게 감정분석을 의뢰하고 싶어 괄호로 감싸진 부분은 형태소 분석된 단어들이야 비속어가 섞여 있을 수도 있어 하지만\n" +
            "넌 나에게 이런 대답을 해줘야해 \n" +
            "\n" +
            "예시로 해당 작가의 리뷰를 분석한 결과 전체적으로 평가가 좋지 않습니다\n" +
            "\n" +
            "[";
    private static final String PROMPT_2 = "]\n" +
            "\n" +
            "이 단어들은 해당 작가의 리뷰에 있는 리뷰들을 형태소 분석하여 일반명사,고유명사,형용사,일반 부사로만 이루어진 단어들 이야 이 단어들을 읽고  감정분석을 해줘\n" +
            "결과 앞에는 $붙여주고 결과에 끝에도 $를 붙여줘";

    private final RestTemplate template;


    @Autowired
    public MorphApplicationTests(MorphService morphService, RestTemplate template) {
        this.morphService = morphService;

        this.template = template;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void morphTest() {
        int a = 0;
        StringBuffer stringBuffer = new StringBuffer();
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        StringBuffer res = new StringBuffer();
        List<Integer> editorIds = morphService.getEditorIds();

        for (Integer editorId : editorIds) {

            List<String> reviews = morphService.getReviews(editorId);

            for (String review : reviews) {
                a++;
                stringBuffer.append(review);
                System.out.println(a);
            }

            KomoranResult analyzeResultList = komoran.analyze(stringBuffer.toString().replaceAll("[^a-zA-Z가-힣\\s]", ""));
            stringBuffer.setLength(0);

            List<Token> tokenList = analyzeResultList.getTokenList();
            for (Token token : tokenList) {
                if (token.getPos().equals("NNG") ||
                        token.getPos().equals("NNG") ||
                        token.getPos().equals("VA") ||
                        token.getPos().equals("VV") ||
                        token.getPos().equals("MAG")
                ) {
                    res.append(token.getMorph()).append(" ");

                }
            }
            String prompt = PROMPT_1 + res.toString() + PROMPT_2;
            System.out.println(res.toString());
            ChatGptRequest request = new ChatGptRequest(model, prompt);

            ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);
            String content = chatGptResponse.getChoices().get(0).getMessage().getContent();
            System.out.println(content);
            Pattern pattern = Pattern.compile("\\$(.*?)\\$");
            Matcher matcher = pattern.matcher(content);
            String findedRes ="";
            while (matcher.find()) {
                findedRes = matcher.group(1);
            }
            morphService.save(findedRes,editorId);




        }


    }

    @Test
    void komoranTest() {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        String strToAnalyze = "대한민국은 민주공화국이다.";

        KomoranResult analyzeResultList = komoran.analyze(strToAnalyze);

        System.out.println(analyzeResultList.getPlainText());

        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token token : tokenList) {
            System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());
        }
    }

}
