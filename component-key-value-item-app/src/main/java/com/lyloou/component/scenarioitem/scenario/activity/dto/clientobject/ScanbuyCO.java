package com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lilou
 * @since 2021/6/2
 */
@NoArgsConstructor
@Data
public class ScanbuyCO {

    private Lottery lottery;
    private Common common;

    @NoArgsConstructor
    @Data
    public static class Lottery {
        private Integer scanbuyLotteryInitialChance;
        private String scanbuyLotteryContentPrimaryTextColor;
        private String scanbuyLotteryContentNumberTextColor;
        private String scanbuyLotteryTitleTextColor;
        private Integer scanbuyLotteryAddChancePerBuy;
    }

    @NoArgsConstructor
    @Data
    public static class Common {
        private String scanbuyBasicQrCodeContent;
        private Boolean scanbuyBasicQrCodeContentShow;
        private Boolean ifLogin;
        private String scanbuyBasicQrCodeContentTextColor;
        private String scanbuyBasicQrCodeProductId;
    }
}
