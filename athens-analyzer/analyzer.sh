LANG=en_EN.utf-8
this="${BASH_SOURCE-$0}"
while [ -h "$this" ]; do
  ls=`ls -ld "$this"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    this="$link"
  else
    this=`dirname "$this"`/"$link"
  fi
done

if [ -z "$ATHENS_HOME" ]; then
  ATHENS_HOME=`dirname "$this"`
fi

# Add libs to CLASSPATH
for f in $ATHENS_HOME/libs/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done

CLASSPATH=$ATHENS_HOME/conf:${CLASSPATH};
CLASSPATH=$ATHENS_HOME/classes:${CLASSPATH};

nohup java -Xmx1024m -Xms512m -ea -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -classpath "$CLASSPATH" com.eincs.athens.AnalyzerMain > /dev/null &
#java -Xmx1024m -Xms512m -ea -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -classpath "$CLASSPATH" com.eincs.athens.AnalyzerMain