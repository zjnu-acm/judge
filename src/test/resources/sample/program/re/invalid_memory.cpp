#include<stdio.h>
#include<stdint.h>

int main() {
    typedef void *pointer;
    int i = 1;
    pointer p = (pointer) (uintptr_t) i;
    puts((char*) p);
    return 0;
}
