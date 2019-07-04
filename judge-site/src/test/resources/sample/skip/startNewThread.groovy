new Thread(new Runnable(){
        public void run(){
            for(cin = new Scanner(System.in);cin.hasNextInt();)println cin.nextInt() + cin.nextInt()
        }
    }).start()
