#!/bin/bash
toNanosec=`stat $1 | grep Modify  | sed -e's/.*Modify://'  | sed -e's/ //' | sed -e's/ /T/' | sed -e's/ //g'  ; `

part1=` echo $toNanosec | cut -c1-23 `
part2=` echo $toNanosec | sed -e's/.*-/-/'  `
echo ${part1}${part2}

