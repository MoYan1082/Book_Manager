package com.moyan.service.impl;

import com.moyan.entity.Tag;
import com.moyan.mapper.TagMapper;
import com.moyan.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author moyan
 * @since 2021-05-14
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
