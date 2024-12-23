@echo off

set infile=%7
set infile=%infile:/=\%
echo %infile%

copy %infile% %infile%.asc

