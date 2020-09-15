# JVM 案例分析

## 问题一 Major GC和Minor GC频繁

服务情况：Minor GC每分钟100次 ，Major GC每4分钟一次，单次Minor GC耗时25ms，单次Major GC耗时200ms，接口响应时间50ms。

### 优化前

jvm 内存分配情况：

eden：500m

from: 60m

old: 2.5G, used 300m

优化方案：增大 Eden 区 2 倍，old 区

![img](../typ-pic/1470cba5.png)

### 优化后

FGC: 14次/h --> 6/h

YGC: 80/min --> 30/min

总GC：2000ms/min  --> 90ms/min

## 案例二 请求高峰期发生GC，导致服务可用性下降

GC日志显示，高峰期CMS在重标记（Remark）阶段耗时1.39s。

![img](../typ-pic/ee8d24f9.png)

结果

经过增加CMSScavengeBeforeRemark参数，单次执行时间>200ms的GC停顿消失，从监控上观察，GCtime和业务波动保持一致，不再有明显的毛刺。

## 案例三 发生Stop-The-World的GC







```
-Xms4g 
-Xmx4g 
-Xmn2g 
-Xss1024K 
-XX:PermSize=256m 
-XX:MaxPermSize=512m 
-XX:ParallelGCThreads=20 
-XX:+UseConcMarkSweepGC 
-XX:+UseParNewGC 
-XX:+UseCMSCompactAtFullCollection 
-XX:CMSInitiatingOccupancyFraction=80

```

