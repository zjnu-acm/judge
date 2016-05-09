def res = []
def cin = new java.util.Scanner(System.in)
while(cin.hasNextInt()) res.add cin.nextInt() + cin.nextInt()
if(res.size())res.remove(res.size()-1)
for(item in res) println item
