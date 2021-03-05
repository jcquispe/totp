package com.jcquispe.totp.controllers;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

@RestController
@RequestMapping("api")
public class TotpController {
private String userSecret = "CKEGR2WLS54NNQBIPHC5VJ3BCPIIZA3TVT77KKEVJAXLKC3SNU3BTKZ4DLTCDZIX";
	
	@GetMapping("generateSecret")
	public String GenerateSecret() {
		SecretGenerator secretGenerator = new DefaultSecretGenerator(64);
		String secret = secretGenerator.generate();
		
		return secret;
	}
	
	
	@GetMapping("generateQr")
	public String GenerateQr() throws QrGenerationException {
		QrData data = new QrData.Builder()
				   .label("Servicio de autenticación de dos factores")
				   .secret(userSecret)
				   .issuer("JUANKY")
				   .algorithm(HashingAlgorithm.SHA1) // More on this below
				   .digits(6)
				   .period(30)
				   .build();
		
		QrGenerator generator = new ZxingPngQrGenerator();
		byte[] imageData = generator.generate(data);
		
		String mimeType = generator.getImageMimeType();
		
		String dataUri = getDataUriForImage(imageData, mimeType);
		
		return dataUri;
	}
	
	
	@GetMapping("verify/{key}")
	public Boolean VerifyKey(@PathVariable String key) {
		TimeProvider timeProvider = new SystemTimeProvider();
		CodeGenerator codeGenerator = new DefaultCodeGenerator();
		CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

		boolean successful = verifier.isValidCode(userSecret, key);

		return successful;
	}
}
