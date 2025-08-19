package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @GetMapping("/api/members")
    public Result<List<MemberDto>> members() {
        List<MemberDto> members = memberService.findMembers()
                .stream()
                .map(m -> new MemberDto(m.getName()))
                .toList();

        return new Result<>(members);
    }


    @PatchMapping("/api/members/{memberId}")
    public UpdateMemberResponse updateMember(@RequestBody @Valid UpdateMemberRequest request,
                                             @PathVariable("memberId") Long memberId) {
        memberService.update(memberId, request.getName());
        Member member = memberService.findOne(memberId);
        return new UpdateMemberResponse(memberId, member.getName());
    }

    @Data
    @AllArgsConstructor
    public static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    public static class MemberDto {
        private String name;
    }

    @Data
    public static class CreateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class CreateMemberResponse {
        private Long id;
    }

    @Data
    public static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
