# Time-Based One Time Password
Springboot example with TOTP

Service endpoints
/api/generateSecret
Generate 64 characters String used for the authenticacion
Example: "CKEGR2WLS54NNQBIPHC5VJ3BCPIIZA3TVT77KKEVJAXLKC3SNU3BTKZ4DLTCDZIX"

/api/generateQr
Generate a base 64 image of QR used for Google Authenticator, Microsoft Authenticator or any other. Scanning ang you get your generating code configured.

/api/verify/{key}
Varifies key generated with authenticator and return true if the six numbers are correct.  
