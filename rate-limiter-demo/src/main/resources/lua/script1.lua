-- @key 记录时间窗信息的key
-- @limit 流量阈值
local function acquire(key,limit,ttlTime)
    -- 返回get到的结果，如果为null，则返回0
    local current = tonumber(redis.call("get", key) or "0")
    -- local initTime=redis.call("ttl",key)
    -- 判定 当前请求是否超出了请求阈值
    if current + 1 > limit then
        redis.call("INCRBY", key,"1")
        -- 返回不通过
        return 0
    else
        redis.call("INCRBY", key,"1")
        -- if initTime<0 then
        if current==0 then
            if tonumber(ttlTime) > -1 then
                redis.call("expire", key,tonumber(ttlTime))
            end
        end
        -- 返回通过
        return 1
    end
end



    -- key的值为 时间戳
local key = KEYS[1]
local method = ARGV[1]
local limit = tonumber(ARGV[2])
local ttlTime=tonumber(ARGV[3])

if method=='acquire' then
    return acquire(key,limit,ttlTime)
else
    return 0
end

