package com.electriccouriers.bass.activities;

public class VerifyEmail extends Application {

        private StringBuffer buffer;

        @Override
        public void start(Stage primaryStage) throws Exception{

            String s = "tanpantaan@gmail.com";

            buffer = new StringBuffer(s);

            int length = buffer.length();
            System.out.println(length);

            char a = buffer.charAt(25);
            System.out.println(a);

            Boolean containsApestaartje;
            int c = 0;
            while(true){
                if (buffer.charAt(c) == '@') {
                    containsApestaartje = true;
                    break;
                } else if (c == buffer.length()){
                    containsApestaartje = false;
                    break;
                } else {
                    c++;
                }
            }

            System.out.println(containsApestaartje);

        }
        public static void main(String[] args) {
            launch(args);
        }
    }


}
