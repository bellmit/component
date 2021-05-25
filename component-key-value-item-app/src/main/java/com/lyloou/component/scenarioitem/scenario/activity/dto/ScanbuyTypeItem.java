package com.lyloou.component.scenarioitem.scenario.activity.dto;


import com.lyloou.component.scenarioitem.dto.ItemType;
import com.lyloou.component.scenarioitem.dto.ScenarioItem;

/**
 * @author lilou
 * @since 2021/4/27
 */
public enum ScanbuyTypeItem implements ScenarioItem {

    /* 基础配置 */
    ifLogin(ActiveItemType.common, Boolean.class, true, "是否要求先登陆"),
    scanbuyBasicQrCodeProductId(ActiveItemType.common, Integer.class, true, "扫码购基础配置 二维码商品id"),
    scanbuyBasicQrCodeContentShow(ActiveItemType.common, Boolean.class, true, "扫码购基础配置 二维码说明方案是否显示"),
    scanbuyBasicQrCodeContent(ActiveItemType.common, String.class, true, "扫码购基础配置 二维码说明方案"),
    scanbuyBasicQrCodeContentTextColor(ActiveItemType.common, String.class, true, "扫码购基础配置 二维码说明方案字体颜色"),
    scanbuyBasicMemo(ActiveItemType.common, String.class, false, "扫码购基础配置 备注"),

    /* 抽奖配置 */
    scanbuyLotteryInitialChance(ActiveItemType.lottery, Integer.class, true, "扫码购抽奖配置 初始机会"),
    scanbuyLotteryAddChancePerBuy(ActiveItemType.lottery, Integer.class, true, "扫码购抽奖配置 每次购买赠送机会"),
    scanbuyLotteryContentPrimaryTextColor(ActiveItemType.lottery, String.class, true, "扫码购抽奖配置 剩余文案颜色 主文案颜色"),
    scanbuyLotteryContentNumberTextColor(ActiveItemType.lottery, String.class, true, "扫码购抽奖配置 剩余文案颜色 次数颜色"),
    scanbuyLotteryTitleTextColor(ActiveItemType.lottery, String.class, true, "扫码购抽奖配置 奖品标题颜色"),

    /* 放映厅配置 */
    scanbuyScreeningRoomMovieListUrl(ActiveItemType.screeningRoom, String.class, false, "扫码购放映厅配置 视频列表URL"),
    scanbuyScreeningRoomMovieListSize(ActiveItemType.screeningRoom, Integer.class, false, "扫码购放映厅配置 视频列表数量"),
    scanbuyScreeningRoomBtn1TaskId(ActiveItemType.screeningRoom, Integer.class, false, "扫码购放映厅配置 按钮1任务ID"),
    scanbuyScreeningRoomBtn2TaskId(ActiveItemType.screeningRoom, Integer.class, false, "扫码购放映厅配置 按钮2任务ID"),
    scanbuyScreeningRoomMovieTitleTextColor(ActiveItemType.screeningRoom, Integer.class, false, "扫码购放映厅配置 视频标题颜色"),

    /* 推荐位配置 */
    scanbuyRecommendationBtn1TaskId(ActiveItemType.recommendation, Integer.class, false, "扫码购推荐位配置 按钮1任务ID"),
    scanbuyRecommendationBtn2TaskId(ActiveItemType.recommendation, Integer.class, false, "扫码购推荐位配置 按钮2任务ID"),
    scanbuyRecommendationBtn3TaskId(ActiveItemType.recommendation, Integer.class, false, "扫码购推荐位配置 按钮3任务ID"),

    ;


    public ItemType itemType;
    public Class<?> itemValueType;
    private final boolean isRequired;
    public String description;

    ScanbuyTypeItem(ItemType itemType, Class<?> itemValueType, boolean isRequired, String description) {
        this.itemType = itemType;
        this.itemValueType = itemValueType;
        this.isRequired = isRequired;
        this.description = description;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println(ScanbuyTypeItem.valueOf("scanbuyBasicQrCodeContent"));
        System.out.println(ScenarioItem.getItemTypeToScenarioItemDocJsonMap(ScanbuyTypeItem.values()));
        final Class<?> aClass = Class.forName(ScanbuyTypeItem.class.getName());
        final Object o = aClass.newInstance();
    }


    @Override
    public ItemType getItemType() {
        return this.itemType;
    }

    @Override
    public String getItemKey() {
        return name();
    }

    @Override
    public Class<?> getItemValueType() {
        return this.itemValueType;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Boolean isRequired() {
        return isRequired;
    }


}
