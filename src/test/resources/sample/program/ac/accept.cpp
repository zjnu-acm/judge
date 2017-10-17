#include <stdio.h>

int main() {
    for (int a, b; 2 == scanf("%d%d", &a, &b);) {
        printf("%d\n", a + b);
    }
}
