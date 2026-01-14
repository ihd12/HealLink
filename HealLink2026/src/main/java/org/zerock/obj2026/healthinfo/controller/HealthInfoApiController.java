package org.zerock.obj2026.healthinfo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.obj2026.healthinfo.dto.HealthInfoAddRequest;
import org.zerock.obj2026.healthinfo.dto.HealthInfoResponse;
import org.zerock.obj2026.healthinfo.service.HealthInfoService;

import java.util.List;

@RestController
@RequiredArgsConstructor

// Axios
public class HealthInfoApiController
{
    private final HealthInfoService healthInfoService;

    // 목록
    @GetMapping("/api/healthinfo")
    public ResponseEntity<List<HealthInfoResponse>> findAllHealthInfo() {
        List<HealthInfoResponse> healthinfos = healthInfoService.findAll();
        return ResponseEntity.ok().body(healthinfos);
    }

    // 쓰기
    @PostMapping("/api/healthinfo")
    public ResponseEntity<?> create(@Valid @RequestBody HealthInfoAddRequest dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("입력값이 올바르지 않습니다.");
        }
        Long savedId = healthInfoService.createPost(dto);
        return ResponseEntity.ok().body(savedId + "번 등록 성공");
    }

    // 삭제
    @DeleteMapping("/api/healthinfo/{id}")
    public ResponseEntity<Void> deleteHealthInfo(@PathVariable Long id) {
        healthInfoService.delete(id); // 서비스에서 deleteById 실행
        return ResponseEntity.ok().build();
    }
    // 수정
    @PutMapping("/api/healthinfo/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody HealthInfoAddRequest dto) {
        Long updatedId = healthInfoService.update(id, dto);
        return ResponseEntity.ok().body(updatedId);
    }


}
