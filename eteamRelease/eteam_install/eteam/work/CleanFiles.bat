pushd C:\eteam\work\files
del /q *.*
for /D %%f in ( * ) do rmdir /s /q "%%f"
