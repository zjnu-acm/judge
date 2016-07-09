INSERT IGNORE INTO language (id, name, source_extension, compile_command, execute_command, executable_extension, time_factor, ext_memory, description) VALUES
	(0, 'GNU C++', 'cc', '"D:\\MinGW\\bin\\g++.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -static -o Main.exe Main.cc', NULL, 'exe', 1, 2400, ''),
	(1, 'GNU C', 'c', '"D:\\MinGW\\bin\\gcc.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -static -o Main.exe Main.c', NULL, 'exe', 1, 2400, ''),
	(2, 'Pascal', 'pas', '"C:\\JudgeOnline\\bin\\fpc\\fpc.exe" -Sg -dONLINE_JUDGE Main.pas', NULL, 'exe', 1, 2400, ''),
	(3, 'Java', 'java', '"D:\\Program Files\\Java\\jdk1.7.0_79\\bin\\javac.exe" Main.java', '"D:\\Program Files\\Java\\jdk1.7.0_79\\bin\\java.exe" -DONLINE_JUDGE -Djava.security.manager -Djava.security.policy=file:/C:/JudgeOnline/bin/judge.policy -cp . Main', 'class', 2, 17000, ''),
	(4, 'VC++', 'cpp', '"C:\\JudgeOnline\\bin\\vc6CompilerAdapter.bat" "D:\\Program Files\\Microsoft Visual Studio" CL.EXE /nologo /ML /W3 /GX /O2 -DONLINE_JUDGE -o Main Main.cpp', NULL, 'exe', 1, 1900, ''),
	(5, 'GNU C++11', 'cc', '"D:\\MinGW\\bin\\g++.exe" -std=c++0x -fno-asm -s -w -DONLINE_JUDGE -o Main.exe Main.cc', NULL, 'exe', 1, 2400, '');
