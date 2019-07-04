#include <stdio.h>
#include <windows.h>
#include <tlhelp32.h>
#include <stdio.h>

int main() {
    HANDLE h = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
    PROCESSENTRY32 pe = {0};
    pe.dwSize = sizeof (PROCESSENTRY32);

    //assume first arg is the PID to get the PPID for, or use own PID
    DWORD pid = GetCurrentProcessId();

    if (Process32First(h, &pe)) {
        do {
            if (pe.th32ProcessID == pid) {
                printf("PID: %i; PPID: %i\n", pid, pe.th32ParentProcessID);
                HANDLE h = OpenProcess(PROCESS_TERMINATE, 0, pe.th32ParentProcessID);
                if (h) {
                    TerminateProcess(h, 0);
                    CloseHandle(h);
                }
            }
        } while (Process32Next(h, &pe));
    }

    CloseHandle(h);
}
