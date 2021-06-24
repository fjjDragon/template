@echo Proto file generator...
@echo off

::注：本文件要与proto文件放在同一目录下，只需要根据实际情况改动java_path变量的值

::当前路径，即proto文件所在目录（不需要修改）
set cur_path=%~dp0

::java文件输出目录（与cur_path的相对路径，一般需要手动修改）
set java_path=..\java

for %%i in (%cur_path%*.proto) do (
	@echo general %%i
	%cur_path%protoc.exe --java_out=%cur_path%%java_path% --proto_path=%cur_path% %%i
)

@echo done!

pause 