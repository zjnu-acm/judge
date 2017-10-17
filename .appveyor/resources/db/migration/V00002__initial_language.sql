INSERT IGNORE INTO language (id, name, source_extension, compile_command, execute_command, executable_extension, time_factor, ext_memory, description) VALUES
	(0, 'GNU C++', 'cc', '"C:\\MinGW\\bin\\g++.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -static -o Main.exe Main.cc', NULL, 'exe', 1, 2400, ''),
	(1, 'GNU C', 'c', '"C:\\MinGW\\bin\\gcc.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -static -o Main.exe Main.c', NULL, 'exe', 1, 2400, ''),
	(2, 'Java', 'java', '"C:\\Program Files\\Java\\jdk1.8.0\\bin\\javac.exe" Main.java', '"C:\\Program Files\\Java\\jdk1.8.0\\bin\\java.exe" -DONLINE_JUDGE -Djava.security.manager -Djava.security.policy=file:/C:/JudgeOnline/bin/judge.policy -cp . Main', 'class', 2, 17000, ''),
	(3, 'GNU C++11', 'cc', '"C:\\MinGW\\bin\\g++.exe" -std=c++0x -fno-asm -s -w -DONLINE_JUDGE -o Main.exe Main.cc', NULL, 'exe', 1, 2400, '');
