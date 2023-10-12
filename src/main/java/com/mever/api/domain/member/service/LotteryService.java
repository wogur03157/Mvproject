package com.mever.api.domain.member.service;

import com.mever.api.domain.member.dto.LotteryDto;
import com.mever.api.domain.member.repository.MemberMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class LotteryService {
    private final MemberMapper memberMapper;

    @Transactional
    public List<LotteryDto.LotteryResult> drawWinners() {
        List<LotteryDto.LotteryResult> results = new ArrayList<>();
        List<LotteryDto> entries =memberMapper.getLotteryList();
        int totalWeight = entries.stream().mapToInt(LotteryDto::getWeight).sum();

        for (int rank = 1; rank <= 3; rank++) {
            LotteryDto.LotteryResult winner = drawWinner(entries, totalWeight, rank);
            if (winner != null) {
                results.add(winner);
                // 제거된 항목을 다시 뽑히지 않도록 리스트에서 제거
                entries.removeIf(entry -> entry.getEmail().equals(winner.getEmail()));
            }
        }

        return results;
    }

    private LotteryDto.LotteryResult drawWinner(List<LotteryDto> entries, int totalWeight, int rank) {
        int randomWeight = new Random().nextInt(totalWeight) + 1;
        int currentWeight = 0;
        for (LotteryDto entry : entries) {
            currentWeight += entry.getWeight();
            if (randomWeight <= currentWeight) {
                return new LotteryDto.LotteryResult(entry.getEmail(), rank, getPrizeByRank(rank));
            }
        }

        return null;
    }

    private String getPrizeByRank(int rank) {
        switch (rank) {
            case 1:
                return "1등 상품";
            case 2:
                return "2등 상품";
            case 3:
                return "3등 상품";
            default:
                return "참가상";
        }
    }
}
