@echo off

set count=194

:loop
set /a count=%count% + 101

make clean
make day14 "NASM_FLAGS=-f elf64 -g -DSTEPS=%count%"
day14

echo Seconds: %count%
pause > nul
goto loop