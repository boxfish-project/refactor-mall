# Log
    日志匹配正则表达式:
    ^([\d-]+ [\d:.]+)\s+(\w+)[^\]]+]\s*([\w.]+)\s*:\s*\[(\w+)]\s\[(\w+)]\s([^ ]+)\s*(EXCEPTION=\[([^\]]+)])?\s*(ID=\[([^\]]+)])?\s*(DATA=\[(.+)])?

    2016-04-13 19:59:51.724  INFO 20417 --- [pool-1-thread-1] c.b.b.sample.app.schedule.ScheduleTask3  : [BoxFishComponent] [refresh] 数据更新异常. EXCEPTION=[{}]
    2016-04-13 19:59:51.724  INFO 20417 --- [pool-1-thread-1] c.b.b.sample.app.schedule.ScheduleTask3  : [BoxFishComponent] [refresh] 数据更新异常. EXCEPTION=[{}] ID=[{}]
    2016-04-13 19:59:51.724  INFO 20417 --- [pool-1-thread-1] c.b.b.sample.app.schedule.ScheduleTask3  : [BoxFishComponent] [refresh] 数据更新异常. EXCEPTION=[{}] ID=[{}] DATA=[{}]
    2016-04-13 19:59:51.724  INFO 20417 --- [pool-1-thread-1] c.b.b.sample.app.schedule.ScheduleTask3  :  [BoxFishComponent] [refresh] 数据更新异常. ID=[{}] DATA=[{}] 
    
    2016-04-13 19:59:51.724  INFO 20417 --- [pool-1-thread-1] c.b.b.sample.app.schedule.ScheduleTask3  : [BoxFishComponent] [refresh] 数据更新异常. DATA=[{}] 
    2016-04-13 19:59:51.724  INFO 20417 --- [pool-1-thread-1] c.b.b.sample.app.schedule.ScheduleTask3  : [BoxFishComponent] [refresh] 数据更新异常.




