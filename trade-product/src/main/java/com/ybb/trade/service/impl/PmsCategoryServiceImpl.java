package com.ybb.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybb.trade.entity.PmsCategory;
import com.ybb.trade.service.PmsCategoryService;
import com.ybb.trade.mapper.PmsCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author zhouf
* @description 针对表【pms_category(商品三级分类)】的数据库操作Service实现
* @createDate 2024-02-06 16:38:04
*/
@Service
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory>
    implements PmsCategoryService{

}




