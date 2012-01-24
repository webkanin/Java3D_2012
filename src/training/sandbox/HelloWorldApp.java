package training.sandbox;
/**
 * The HelloWorldApp class implements an application that
 * simply prints "Hello World!" to standard output.
 */
class HelloWorldApp {
    public static void main(String[] args) {
               what();
    }

   static void what(){
       try {
           System.out.println("Hello World!"); // Display the string.
           System.out.print("What");
           System.out.print("Need");
           System.out.print("Why... hello from MSI webkanin");
       } catch (Exception e) {
           e.getCause()     ;
       }
   }
}
