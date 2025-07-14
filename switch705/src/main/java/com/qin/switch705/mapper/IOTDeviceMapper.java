package com.qin.switch705.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qin.switch705.domain.IOTDevice;
import org.apache.ibatis.annotations.Mapper;
//与数据库交互
@Mapper
public interface IOTDeviceMapper extends BaseMapper<IOTDevice> {
IOTDevice selectByDeviceId(String deviceId);
}
