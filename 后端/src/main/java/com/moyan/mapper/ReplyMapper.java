package com.moyan.mapper;

import com.moyan.entity.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author moyan
 * @since 2021-05-14
 */
@Mapper
@Repository
public interface ReplyMapper extends BaseMapper<Reply> {

}
