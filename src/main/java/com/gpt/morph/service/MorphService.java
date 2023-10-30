package com.gpt.morph.service;

import java.util.List;

public interface MorphService {

    String getAnalyzingReview(Integer id);
    Integer saveAnalyzingReview();

    List<Integer> getEditorIds();

    List<String> getReviews(Integer editorId);

    Integer save(String res,Integer editorId);

}
