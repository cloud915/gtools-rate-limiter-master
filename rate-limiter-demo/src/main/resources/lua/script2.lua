-- 假设一个队列，里面存储的是令牌数量，每次申请多少，就向队列存储多少
-- 队列中的令牌，都是在取令牌的时刻，向里面添加，添加数量就是 申请的数量（而取的值直接count++)

-- 向队列，存储指定个数的令牌
-- @key list的key
-- @argCount 需要的令牌数
-- @argCurrTime 当前时间戳
local function addToQueue(key,argCount,argCurrTime)
    local count=0
    for i=1,argCount,1 do
        redis.call('lpush',key,argCurrTime)
        count = count + 1
    end
    return count
end

-- 核心逻辑tonumber(currTime) - tonumber(timeBase)>tonumber(rateTime)
--  当前时间戳-上次取令牌的时间戳> 阈值时间
-- @key
-- @count 申请发送的数量
-- @rateCount 阀值数量
-- @rateTime 阀值时间（毫秒）
-- @currTime 申请的时间戳
local function acquire(key,count,rateCount,rateTime,currTime)
    local result=0
    -- 从list中，计算 如果取N个令牌，当前的list中存储的令牌是否够；
    -- lindex返回list指定索引的值,如果没有则返回nil/false, rateCount-count<0,说明当前令牌数不够
    -- 如果timeBase有返回值，说明令牌数量满足阈值范围，并且由于队列中存储的都是某一次的时间戳
    -- 重点：（当前时间戳-上次取令牌的时间戳> 阈值时间） 那么 应该向队列加数据了
    local timeBase = redis.call('lindex',key, tonumber(rateCount)-tonumber(count))
    if (timeBase == false) or (tonumber(currTime) - tonumber(timeBase)>tonumber(rateTime)) then
      result = result + addToQueue(tonumber(count),tonumber(currTime))
    end
    if (timeBase ~= false) then
        -- 裁剪list，按阈值数量，保留list的长度
       redis.call('ltrim',key,0,tonumber(rateCount))
    end
    return result
end



-- key的值为 时间戳
local key = KEYS[1]
-- 方法名
local method = ARGV[1]
-- 申请发送的数量1
local count= ARGV[2]
-- 阀值数量2
local rateCount = tonumber(ARGV[3])
-- 阀值时间（毫秒）3
local rateTime=tonumber(ARGV[4])
-- 申请的时间4
local currTime=tonumber(ARGV[5])

if method=='acquire' then
    return acquire(key,count,rateCount,rateTime,currTime)
else
    return 0
end

