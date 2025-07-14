package com.qin.switch705.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    public void hmSet(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ============================== 通用操作 ==============================

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)，time <= 0 表示永久有效
     * @return 操作是否成功
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取键的过期时间
     * @param key 键（非空）
     * @return 过期时间(秒)，0 表示永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断键是否存在
     * @param key 键
     * @return true 存在，false 不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存键
     * @param key 键（可传多个）
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    // ============================== String 类型 ==============================

    /**
     * 获取字符串值
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置字符串值
     * @param key 键
     * @param value 值
     * @return 操作是否成功
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置字符串值并指定过期时间
     * @param key 键
     * @param value 值
     * @param time 过期时间(秒)，time <= 0 表示永久
     * @return 操作是否成功
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 数值递增
     * @param key 键
     * @param delta 递增步长（必须 > 0）
     * @return 递增后的值
     */
    public long incr(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递增步长必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 数值递减
     * @param key 键
     * @param delta 递减步长（必须 > 0）
     * @return 递减后的值
     */
    public long decr(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递减步长必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ============================== Hash 类型 ==============================

    /**
     * 获取Hash表中的某个字段值
     * @param key 键
     * @param item 字段
     * @return 字段值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取Hash表中所有字段和值
     * @param key 键
     * @return 所有字段和值的Map
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 向Hash表中存入多个字段和值
     * @param key 键
     * @param map 字段-值映射
     * @return 操作是否成功
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向Hash表中存入多个字段和值并设置过期时间
     * @param key 键
     * @param map 字段-值映射
     * @param time 过期时间(秒)
     * @return 操作是否成功
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向Hash表中存入单个字段和值
     * @param key 键
     * @param item 字段
     * @param value 值
     * @return 操作是否成功
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向Hash表中存入单个字段和值并设置过期时间
     * @param key 键
     * @param item 字段
     * @param value 值
     * @param time 过期时间(秒)
     * @return 操作是否成功
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除Hash表中的指定字段
     * @param key 键
     * @param items 字段（可多个）
     */
    public void hdel(String key, Object... items) {
        redisTemplate.opsForHash().delete(key, items);
    }

    /**
     * 判断Hash表中是否存在指定字段
     * @param key 键
     * @param item 字段
     * @return true 存在，false 不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * Hash表字段值递增
     * @param key 键
     * @param item 字段
     * @param by 递增步长（> 0）
     * @return 递增后的值
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * Hash表字段值递减
     * @param key 键
     * @param item 字段
     * @param by 递减步长（> 0）
     * @return 递减后的值
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================== Set 类型 ==============================

    /**
     * 获取Set集合中的所有元素
     * @param key 键
     * @return 元素集合
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断Set集合中是否包含指定元素
     * @param key 键
     * @param value 元素
     * @return true 包含，false 不包含
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向Set集合中添加元素
     * @param key 键
     * @param values 元素（可多个）
     * @return 成功添加的元素个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 向Set集合中添加元素并设置过期时间
     * @param key 键
     * @param time 过期时间(秒)
     * @param values 元素（可多个）
     * @return 成功添加的元素个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取Set集合的元素个数
     * @param key 键
     * @return 元素个数
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 从Set集合中移除指定元素
     * @param key 键
     * @param values 元素（可多个）
     * @return 成功移除的元素个数
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================== List 类型 ==============================

    /**
     * 获取List集合的指定范围元素
     * @param key 键
     * @param start 起始索引（0开始）
     * @param end 结束索引（-1表示最后一个元素）
     * @return 元素列表
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取List集合的长度
     * @param key 键
     * @return 集合长度
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取List集合中指定索引的元素
     * @param key 键
     * @param index 索引（正数从头部开始，负数从尾部开始：-1为最后一个）
     * @return 元素值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向List集合尾部添加元素（右推）
     * @param key 键
     * @param value 元素
     * @return 操作是否成功
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向List集合尾部添加元素并设置过期时间（右推）
     * @param key 键
     * @param value 元素
     * @param time 过期时间(秒)
     * @return 操作是否成功
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向List集合尾部批量添加元素（右推）
     * @param key 键
     * @param value 元素列表
     * @return 操作是否成功
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向List集合头部添加元素（左推）
     * @param key 键
     * @param value 元素
     * @return 操作是否成功
     */
    public boolean lLeftPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向List集合头部批量添加元素（左推）
     * @param key 键
     * @param values 元素数组
     * @return 操作是否成功
     */
    public boolean lLeftPushAll(String key, Object... values) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新List集合中指定索引的元素
     * @param key 键
     * @param index 索引
     * @param value 新值
     * @return 操作是否成功
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从List集合中移除指定元素
     * @param key 键
     * @param count 移除数量（正数从头部开始，负数从尾部开始）
     * @param value 要移除的元素
     * @return 实际移除的数量
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================== Sorted Set 类型 ==============================

    /**
     * 向有序集合中添加元素
     * @param key 键
     * @param value 元素
     * @param score 排序权重
     * @return 操作是否成功
     */
    public boolean zAdd(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取有序集合的元素个数
     * @param key 键
     * @return 元素个数
     */
    public Long zCard(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取有序集合中指定范围的元素（按权重升序）
     * @param key 键
     * @param start 起始位置
     * @param end 结束位置
     * @return 元素集合
     */
    public Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}