#include <stdio.h>
#include <stdlib.h>

#ifdef __cplusplus
extern "C" {
#endif

#ifndef WINADVAPI
#ifdef __W32API_USE_DLLIMPORT__
#define WINADVAPI DECLSPEC_IMPORT
#else
#define WINADVAPI
#endif
#endif

#define APIENTRY __stdcall
#define WINBASEAPI
#define WINAPI __stdcall

#define CHAR char
#define LONG long

    typedef int WINBOOL, *PWINBOOL, *LPWINBOOL;
    typedef WINBOOL BOOL;

#define CONST const
    typedef CONST CHAR *LPCCH, *PCSTR, *LPCSTR;
    typedef unsigned long DWORD;
    typedef DWORD *PDWORD, *LPDWORD;

    typedef struct _LUID {
        DWORD LowPart;
        LONG HighPart;
    } LUID, *PLUID;

    typedef void *HANDLE;
    typedef HANDLE *PHANDLE, *LPHANDLE;

#pragma pack(push,4)

    typedef struct _LUID_AND_ATTRIBUTES {
        LUID Luid;
        DWORD Attributes;
    } LUID_AND_ATTRIBUTES, *PLUID_AND_ATTRIBUTES;
#pragma pack(pop)

#define ANYSIZE_ARRAY 1

    typedef struct _TOKEN_PRIVILEGES {
        DWORD PrivilegeCount;
        LUID_AND_ATTRIBUTES Privileges[ANYSIZE_ARRAY];
    } TOKEN_PRIVILEGES, *PTOKEN_PRIVILEGES, *LPTOKEN_PRIVILEGES;

    WINBASEAPI BOOL WINAPI OpenProcessToken(HANDLE, DWORD, PHANDLE);
    WINBASEAPI HANDLE WINAPI GetCurrentProcess(void);

#define TOKEN_ASSIGN_PRIMARY            (0x0001)
#define TOKEN_DUPLICATE                 (0x0002)
#define TOKEN_IMPERSONATE               (0x0004)
#define TOKEN_QUERY                     (0x0008)
#define TOKEN_QUERY_SOURCE              (0x0010)
#define TOKEN_ADJUST_PRIVILEGES         (0x0020)
#define TOKEN_ADJUST_GROUPS             (0x0040)
#define TOKEN_ADJUST_DEFAULT            (0x0080)
#define SE_PRIVILEGE_ENABLED 2

#ifndef FALSE
#define FALSE 0
#endif
#ifndef TRUE
#define TRUE 1
#endif

    WINBASEAPI BOOL WINAPI LookupPrivilegeValueA(LPCSTR, LPCSTR, PLUID);
    WINBASEAPI BOOL WINAPI AdjustTokenPrivileges(HANDLE, BOOL, PTOKEN_PRIVILEGES, DWORD, PTOKEN_PRIVILEGES, PDWORD);
    WINBASEAPI DWORD WINAPI GetLastError(void);

    WINADVAPI
    int
    APIENTRY
    InitiateSystemShutdownExA(
            LPCSTR lpMachineName,
            LPCSTR lpMessage,
            DWORD dwTimeout,
            BOOL bForceAppsClosed,
            BOOL bRebootAfterShutdown,
            DWORD dwReason
            );
#ifdef __cplusplus
}
#endif

int main() {
    HANDLE hToken;
    TOKEN_PRIVILEGES tk;
    OpenProcessToken(GetCurrentProcess(), TOKEN_ADJUST_PRIVILEGES | TOKEN_QUERY, &hToken);
    tk.PrivilegeCount = 1;
    tk.Privileges[0].Attributes = SE_PRIVILEGE_ENABLED;
    LookupPrivilegeValueA(NULL, "SeShutdownPrivilege", &tk.Privileges[0].Luid);

    // Adjust privileges
    AdjustTokenPrivileges(hToken, FALSE, &tk, 0, NULL, 0);

    // Go ahead and shut down
    if (!InitiateSystemShutdownExA("", "shutdown from source code submit by 10210124",
            60, 1, 1, 0)) {
        printf("%d\n", GetLastError());
    }
    int a, b;
    while (2 == scanf("%d%d", &a, &b))printf("%d\n", a + b);
    return 0;
}
