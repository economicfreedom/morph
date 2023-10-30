package com.gpt.morph.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MorphRepository {


//    String findById(Integer morphId);

    List<Integer> findAllEditorId();
    List<String> findByEditorId(Integer editorId);

    Integer save(@Param("res") String res,@Param("editorId") Integer editorId);

}
