def cin = new java.util.Scanner(System.in)
for(def first = true; cin.hasNextInt(); first =false) {
    if(first) print ' ';
    print cin.nextInt() + cin.nextInt()
}
