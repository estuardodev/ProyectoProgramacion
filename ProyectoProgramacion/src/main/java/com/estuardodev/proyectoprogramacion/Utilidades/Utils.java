package com.estuardodev.proyectoprogramacion.Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;

public class Utils {

    public static String GenerarCode() {
        String code = "";
        String numbers = "0123456789";
        Random random = new Random();

        while (code.length() < 4) {
            int index = random.nextInt(numbers.length());
            code += numbers.charAt(index);
        }

        return code;
    }

    public static void SendCode(String code, String correo){
        File file = new File("secret-key.txt");
        String apiKey = "";
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            apiKey = br.readLine();

        }catch (Exception e){
            e.printStackTrace();
        }

        Resend resend = new Resend(apiKey);

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("Biblioteca <no-reply@mailer.estuardo.dev>")
                .to(correo)
                .subject("Código de restablecimiento de contraseña")
                .html("<h1>Sistema bibliotecario</h1> <br> <p>Este es tu código de restablecimiento de contraseña: <strong>"+code+"</strong></p>")
                .build();
        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

}
