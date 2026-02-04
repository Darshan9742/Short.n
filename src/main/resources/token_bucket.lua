-- tb:{key}
local key = KEYS[1]

local capacity = tonumber(ARGV[1])        -- max tokens
local refill_rate = tonumber(ARGV[2])     -- tokens per second
local now = tonumber(ARGV[3])             -- seconds
local cost = tonumber(ARGV[4])            -- tokens per request

-- get state
local data = redis.call("HMGET", key, "tokens", "lastRefillTs")

local tokens = data[1]
local lastRefillTs = data[2]

-- init bucket
if tokens == false or tokens == nil then
	tokens = capacity
	lastRefillTs = now
else
	tokens = tonumber(tokens)
	lastRefillTs = tonumber(lastRefillTs)
end

-- refill calculation
local elapsed = now - lastRefillTs
if elapsed < 0 then
	elapsed = 0
end

local refill = elapsed * refill_rate
tokens = tokens + refill

if tokens > capacity then
	tokens = capacity
end

-- consume
if tokens < cost then
	redis.call("HMSET", key, "tokens", tokens, "lastRefillTs", now)
	redis.call("EXPIRE", key, 3600)
	return 0
else
	tokens = tokens - cost
	redis.call("HMSET", key, "tokens", tokens, "lastRefillTs", now)
	redis.call("EXPIRE", key, 3600)
	return 1
end
