#!/bin/sh
export ProjectPath=$(cd "../$(dirname "$1")"; pwd)
export TargetClassName="ndkdemo.binea.com.ndkdemo.MainActivity"

export SourceFile="${ProjectPath}/app/src/main/java"
export TargetPath="${ProjectPath}/hellojni/src/main/jni"

cd "${SourceFile}"
javah -d ${TargetPath} -classpath "${SourceFile}" "${TargetClassName}"
echo -d ${TargetPath} -classpath "${SourceFile}" "${TargetClassName}"
