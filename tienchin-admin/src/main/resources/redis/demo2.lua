local key = KEYS[1]
local value = ARGV[1]
return redis.call("bf.exists",key,value)