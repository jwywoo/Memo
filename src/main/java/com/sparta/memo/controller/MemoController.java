package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto creteMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDTO -> entity
        Memo memo = new Memo(requestDto);
        // Memo maxId Check
        Long maxId = (memoList.size() > 0) ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);
        // Database Saving
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto
        return new MemoResponseDto(memo);
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        return memoList.values().stream().map(MemoResponseDto::new).toList();
    }

    @PutMapping("/memos/{id}")
    public Long modifyMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        if (memoList.containsKey(id)) {
            Memo memo = memoList.get(id);
            memo.update(requestDto);
            return id;
        } else {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        if (memoList.containsKey(id)) {
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("Invalid ID");
        }
    }
}
