package com.flong.springboot.core.util;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 自定义Redis序列化
 */
public class EntityRedisSerializer implements RedisSerializer<Object> {
    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return SerializeUtil.serialize(t);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return SerializeUtil.unserialize(bytes);
    }
}