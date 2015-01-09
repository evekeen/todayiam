#!/bin/sh
DIR=$( cd "$( dirname "$0" )" && pwd)
cd "$DIR"
java -cp ../lib/*:. todayiam.watchdog.Main