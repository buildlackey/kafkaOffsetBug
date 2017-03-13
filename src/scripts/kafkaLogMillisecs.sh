#!/bin/bash 

for file in $1/*.log ; do 
    ms=`createDateInMillisecs.sh $file ` 
    echo $file $ms
done
