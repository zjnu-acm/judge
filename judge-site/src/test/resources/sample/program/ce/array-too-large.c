#include <stdio.h>
#include <stdint.h>
#define M 1000000001
int64_t aa[M];

int main() {
    int64_t i, sn;
    int c;
    aa[0] = 0;
    aa[1] = 2;
    aa[2] = 3;
    for (i = 3; i <= M; i++)
        aa[i] = 3 * aa[i - 1] - 2 * aa[i - 2];
    scanf("%d", &c);
    while (c--) {
        sn = 0;
        int64_t n;
        scanf("%" PRId64, &n);
        for (i = 1; i <= n; i++)
            sn += aa[i];
        printf("%" PRId64 "\n", sn % 9973);
    }
    return 0;
}
