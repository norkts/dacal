package com.norkts.dacal.domain.params.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.norkts.dacal.domain.GamblingData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftNotice {
    private String roll2Count;
    private String rollSummaryText;
    private String roll2TimeText;
    private String planetSummaryText;
    private String curTimeText;
    private String bigCardSumText;
    private String yuanYangTimeText;
    private String yuanYangPeriodText;
    private String moheSummaryText;


    private List<String> yuanYangPeriodTexts;

    private List<String> rollSummaryTexts;

    private List<String> bigCardSumTexts;

    private List<String> moheSummaryTexts;

    private GamblingData oriGamblingData;
}
