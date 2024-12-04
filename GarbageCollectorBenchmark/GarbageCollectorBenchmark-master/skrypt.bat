::SET HEAP_SIZE=-Xms256m -Xmx256m
SET HEAP_SIZE=-Xms1024m -Xmx1024m
SET LOG_OPTIONS= -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime
java -classpath bin %HEAP_SIZE% %LOG_OPTIONS% -Xloggc:serial.log -XX:+UseSerialGC Main
java -classpath bin %HEAP_SIZE% %LOG_OPTIONS% -Xloggc:pareallel.log -XX:+UseParallelOldGC Main
java -classpath bin %HEAP_SIZE% %LOG_OPTIONS% -Xloggc:marksweep.log -XX:+UseConcMarkSweepGC Main
java -classpath bin %HEAP_SIZE% %LOG_OPTIONS% -Xloggc:gc1.log -XX:+UseG1GC Main


