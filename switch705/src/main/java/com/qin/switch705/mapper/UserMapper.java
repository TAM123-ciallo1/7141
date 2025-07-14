package com.qin.switch705.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qin.switch705.domain.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<user> {
    @Select("SEELECT * FROM tb_user WHERE username=#{username}")
user selectByUsername(String username);
}
