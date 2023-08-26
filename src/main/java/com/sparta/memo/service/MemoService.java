package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository)
    {
        this.memoRepository = memoRepository;
    }

    // Manual Injection not using auto wired
//    public MemoService(ApplicationContext context) {
//        // Using name from bean
//        // MemoRepository memoRepository = (MemoRepository) context.getBean("");
//
//        // Using bean in class format
//        // MemoRepository memoRepository = context.getBean(MemoRepository.class);
//
//        this.memoRepository = memoRepository;
//    }


    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);
        // DB 저장
        Memo savedMemo = this.memoRepository.save(memo);
        // Entity -> ResponseDto
        return new MemoResponseDto(memo);
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAll().stream().map(MemoResponseDto::new).toList();
    }

    @Transactional
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        memo.update(requestDto);
        return id;
    }

    public Long deleteMemo(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        this.memoRepository.delete(memo);
        return id;

    }

    private Memo findMemo(Long id) {
        return this.memoRepository.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("Not Available")
                );
    }

}
