package com.example.samplelogin.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

@Controller
public class LoginController {
    @GetMapping("/")
    public String home() {
        // 루트 페이지에 접속했을 때 성공 페이지로 리다이렉트
        return "redirect:/success";
    }

    /**
     * 1차 로그인 페이지 이동
     */
    @GetMapping("/login")
    public String login() {
        // 로그인 페이지로 이동
        return "login";
    }

    /**
     * 2차 로그인 페이지 이동
     */
    @GetMapping("/login2")
    public String login2() {
        // 로그인 페이지로 이동
        return "login2";
    }

    /**
     * 로그인 성공시 이동
     */
    @GetMapping("/success")
    public String success() {
        // 성공 페이지로 이동
        return "success";
    }

    /**
     * 구글 Authenticator 생성 컨트롤러
     * @return QR 이미지 인코딩
     */
    @GetMapping("/google-authenticator")
    @ResponseBody
    public String getGoogleAuthenticator() throws WriterException {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

        String data = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("test",
                "12345",
                googleAuthenticatorKey);

        int width = 150;
        int height = 150;

        // QR Code - BitMatrix: qr code 정보 생성
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, 0); /* default = 4 */

        BitMatrix encode = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(encode, "PNG", out);

            // 생성된 이미지 -> Base64 인코딩
            return Base64.getEncoder().encodeToString(out.toByteArray());

        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
