package org.example;
import java.io.*;
import java.util.Base64;
import java.util.stream.Collectors;

public class Main {
    public static String FILE_PATH = "src/main/resources/Encoded.txt";

    public static void main(String[] args) {

        File file = new File(FILE_PATH);
        System.out.println("Введите текст, для завершения ввода введите 'end' + Enter");

        String input;
        try {
            input = readInput();
            saveEncoded(input, file);
            System.out.println("Файл успешно закодирован и записан!");
            System.out.println("Содержимое в закодированном виде:");
            printEncoded(file);
            System.out.println("\nСодержимое в декодированном виде:");
            printDecoded(file);

        }
        catch (IOException e) {
            throw new CustomException("Сбой при операции ввода-вывода!", e);
        }
        //CustomException, которое может выбросить метод printDecoded не требуется обрабатывать, т.к. оно Непроверяемое
    }

    public static String readInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String str;
        while(!("end".trim().equals(str = reader.readLine())))
            sb.append(str).append('\n');
        return sb.toString().trim();
    }

    //тут метод может выбросить исключение, его обработает вызывающий блок, поэтому try не пишем
    public static void saveEncoded(String str, File file) throws IOException {
        Writer writer = new BufferedWriter(new FileWriter(file));
        byte[] encodedBytes = Base64.getEncoder().encode(str.getBytes());
        writer.write(new String(encodedBytes));
        writer.close(); //из за того что у нас нет try-with-resource, вынуждены закрывать вручную
    }

    //тут метод одно исключение обрабатывет, используя try, а второе выбрасывает, его обработает вызывающий блок
    public static void printEncoded(File file) throws IOException{
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            int i;
            while ((i = inputStream.read())!=-1)
                System.out.print((char) i);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            inputStream.close();//т.к. try есть, но не try-with-resource, закрываем здесь
        }

    }

    //тут метод обрабатывет все исключения, используя try-with-resource, поэтому throws не пишем
    public static void printDecoded(File file){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String encodedStr = reader.lines().collect(Collectors.joining());
            byte [] bytesDecoded = Base64.getDecoder().decode(encodedStr.getBytes());
            String decodedStr = new String(bytesDecoded);

            System.out.println(decodedStr);
        }
        catch (FileNotFoundException e) {
            throw new CustomException("Файл не найден!", e);
        }
        catch (IOException e) {
            throw new CustomException("Сбой при выполнении операцмм ввода-вывода!", e);
        }
        //т.к. используем try-with-resource, можем не закрывать поток вручную

    }
}