package com.lyloou.component.scenarioitem.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyloou.component.scenarioitem.repository.entity.ScenarioItemEntity;
import com.lyloou.component.scenarioitem.repository.mapper.ScenarioItemMapper;
import com.lyloou.component.scenarioitem.repository.service.ScenarioItemService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通用场景值配置 服务实现类
 * </p>
 *
 * @author lilou
 * @since 2021-05-25
 */
@Service
public class ScenarioItemServiceImpl extends ServiceImpl<ScenarioItemMapper, ScenarioItemEntity> implements ScenarioItemService {

}
