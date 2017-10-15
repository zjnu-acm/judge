for (def cin = new Scanner(System.in); cin.hasNextInt();) {
    def a = cin.nextInt(), b = cin.nextInt();
    if (a == 0 && b == 0) break;
    println a + b;
}
