package com.example.destroyer.morsecodeflashlighttranslator;

public class Translator {

    String primaryText;
    String morseCode;

    public Translator(String primaryText){
        this.primaryText = primaryText;
        morseCode = "";
    }

    public void translate(){
        String text = primaryText.toLowerCase();
        char[] array = text.toCharArray();
        String morse = "";
        String current = "";
        for(int i = 0; i < array.length; i++){
            current = "";
            if(array[i] == 'a'){
                current = ".-";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'b'){
                current = "-...";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'c'){
                current = "-.-.";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'd'){
                current = "-..";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'e'){
                current = ".";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'f'){
                current = "..-.";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'g'){
                current = "--.";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'h'){
                current = "....";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'i'){
                current = "..";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'j'){
                current = ".---";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'k'){
                current = "-.-";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'l'){
                current = ".-..";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'm'){
                current = "--";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'n'){
                current = "-.";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'o'){
                current = "---";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'p'){
                current = ".--.";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'q'){
                current = "--.-";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'r'){
                current = ".-.";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 's'){
                current = "...";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 't'){
                current = "-";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'u'){
                current = "..-";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'v'){
                current = "...-";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'w'){
                current = ".--";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'x'){
                current = "-..-";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'y'){
                current = "-.--";
                morse += current;
                morse += " ";
            }
            else if(array[i] == 'z'){
                current = "--..";
                morse += current;
                morse += " ";
            }
            else if(array[i] == ' '){
                current = "/";
                morse += current;
            }
            else
                continue;
        }
        morseCode = morse;
    }

    public String getMorse(){
        return morseCode;
    }
}
