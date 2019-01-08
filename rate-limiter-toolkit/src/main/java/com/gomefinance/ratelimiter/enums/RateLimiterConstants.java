package com.gomefinance.ratelimiter.enums;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by Administrator on 2018/4/17.
 */
public class RateLimiterConstants {
    public static final String RATE_LIMITER_KEY_PREFIX = "rate_limiter:";


    public static final String RATE_LIMITER_INIT_METHOD = "init";
    public static final String RATE_LIMITER_DELETE_METHOD = "delete";
    public static final String RATE_LIMITER_ACQUIRE_METHOD = "acquire";
    // starter中无法读取资源文件，转而使用text方式
    public static final String LUA_SCRIPT_TEXT = "local function contains(source_str, sub_str)\n" +
                    "-- string.find方法 ，不识别横线 - ,参数中带有- ，则返回nil\n" +
                    "    local start_pos, end_pos = string.find(source_str, sub_str);\n" +
                    "    if start_pos == nil then\n" +
                    "        return false;\n" +
                    "    end\n" +
                    "    local source_str_len = string.len(source_str);\n" +
                    "\n" +
                    "    if source_str_len == end_pos then\n" +
                    "        return true\n" +
                    "    elseif string.sub(source_str, end_pos + 1, end_pos + 1) == \",\" then\n" +
                    "        return true\n" +
                    "    end\n" +
                    "    return false;\n" +
                    "end\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "local function acquire(key, permits, curr_mill_second, context)\n" +
                    "    local rate_limit_info = redis.pcall(\"HMGET\", key, \"last_mill_second\", \"curr_permits\", \"max_permits\", \"rate\", \"apps\")\n" +
                    "    local last_mill_second = rate_limit_info[1]\n" +
                    "    local curr_permits = tonumber(rate_limit_info[2])\n" +
                    "    local max_permits = tonumber(rate_limit_info[3])\n" +
                    "    local rate = rate_limit_info[4]\n" +
                    "    local apps = rate_limit_info[5]\n" +
                    "\n" +
                    "    if type(apps) == 'boolean' or apps == nil or not contains(apps, context) then\n" +
                    "        return 0\n" +
                    "    end\n" +
                    "\n" +
                    "\n" +
                    "    local local_curr_permits = max_permits;\n" +
                    "\n" +
                    "\n" +
                    "    if (type(last_mill_second) ~= 'boolean'  and last_mill_second ~= nil) then\n" +
                    "        local reverse_permits = math.floor(((curr_mill_second - last_mill_second) / 1000) * rate)\n" +
                    "        local expect_curr_permits = reverse_permits + curr_permits;\n" +
                    "        local_curr_permits = math.min(expect_curr_permits, max_permits);\n" +
                    "\n" +
                    "        --- 大于0表示不是第一次获取令牌，也没有向桶里添加令牌\n" +
                    "        if (reverse_permits > 0) then\n" +
                    "            redis.pcall(\"HSET\", key, \"last_mill_second\", curr_mill_second)\n" +
                    "        end\n" +
                    "    else\n" +
                    "        redis.pcall(\"HSET\", key, \"last_mill_second\", curr_mill_second)\n" +
                    "    end\n" +
                    "\n" +
                    "\n" +
                    "    local result = -1\n" +
                    "    if (local_curr_permits - permits >= 0) then\n" +
                    "        result = 1\n" +
                    "        redis.pcall(\"HSET\", key, \"curr_permits\", local_curr_permits - permits)\n" +
                    "    else\n" +
                    "        redis.pcall(\"HSET\", key, \"curr_permits\", local_curr_permits)\n" +
                    "    end\n" +
                    "\n" +
                    "    return result\n" +
                    "end\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "local function init(key, max_permits, rate, apps)\n" +
                    "    local rate_limit_info = redis.pcall(\"HMGET\", key, \"last_mill_second\", \"curr_permits\", \"max_permits\", \"rate\", \"apps\")\n" +
                    "    local org_max_permits = tonumber(rate_limit_info[3])\n" +
                    "    local org_rate = rate_limit_info[4]\n" +
                    "    local org_apps = rate_limit_info[5]\n" +
                    "\n" +
                    "    if (org_max_permits == nil) or (apps ~= org_apps or rate ~= org_rate or max_permits ~= org_max_permits) then\n" +
                    "        redis.pcall(\"HMSET\", key, \"max_permits\", max_permits, \"rate\", rate, \"curr_permits\", max_permits, \"apps\", apps)\n" +
                    "    end\n" +
                    "    return 1;\n" +
                    "end\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "local function delete(key)\n" +
                    "    redis.pcall(\"DEL\", key)\n" +
                    "    return 1;\n" +
                    "end\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "local key = KEYS[1]\n" +
                    "local method = ARGV[1]\n" +
                    "\n" +
                    "if method == 'acquire' then\n" +
                    "    return acquire(key, ARGV[2], ARGV[3], ARGV[4])\n" +
                    "elseif method == 'init' then\n" +
                    "    return init(key, ARGV[2], ARGV[3], ARGV[4])\n" +
                    "elseif method == 'delete' then\n" +
                    "    return delete(key)\n" +
                    "else\n" +
                    "    --ignore\n" +
                    "end\n";
}
