#include<stdio.h>
#include<stdint.h>

int main(int argc, char **args) {
    while (argc--) {
        printf("%d\n", (uintptr_t) (*args++) / argc);
    }
    return 0;
}
