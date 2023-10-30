package com.gpt.morph.service;

import com.gpt.morph.repository.MorphRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class MorphServiceImpl implements MorphService {
    private final MorphRepository morphRepository;
    @Override
    public String getAnalyzingReview(Integer id) {
//        return morphRepository.findById(id);
        return null;
    }

    @Override
    public Integer saveAnalyzingReview() {
        return null;
    }

    @Override
    public List<Integer> getEditorIds() {
        return morphRepository.findAllEditorId();
    }

    @Override
    public List<String> getReviews(Integer editorId) {
        return morphRepository.findByEditorId(editorId);
    }

    @Override
    public Integer save(String res, Integer editorId) {
        return morphRepository.save(res,editorId);
    }
}
