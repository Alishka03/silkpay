# silkpay
Откройте <em>http://localhost:8080/h2-console</em> для того чтобы контролировать БД.<br>
url:jdbc:h2:mem:testdb<br>
password:sa<br>
<br>
Откройте :<code>http://localhost:8080/auth/sign-up</code> для того чтобы авторизоваться.POST запрос:Body->raw_>JSON:<br>
{
   "username":"username",
   "password":"password"
}
<br>
После этого: <code>http://localhost:8080/auth/sign-in</code> для того чтобы войти в систему.POST : Body->raw_>JSON:<br>
<code>{
   "username":"username",
   "password":"password"
}
</code>
Получите такой ответ:<br>
<code>{
    "status": "SUCCESS",
    "id": 4,
    "username": "username",
    "tokenType": "Bearer",
    "accessToken": "some token"
}
</code><br>Копируйте accessToken для того чтобы аутенифицироваться в дальшейших действиях:Authorization->Bearer <br>
<code>http://localhost:8080/my-accounts</code> - для того чтобы увидеть мои счета (GET)<br>
<code>http://localhost:8080/new-account</code> - для того чтобы открыть новый счет вы должны указать начальный баланс (POST): RequestBody: <br><code>{
    "balance":"4000"
}</code> <br>
<code>http://localhost:8080/transfer?senderAccountId=4&receiverAccountId=3&amount=4000</code> для того чтобы перевести деньги со своего счета (POST)
![image](https://github.com/Alishka03/silkpay/assets/90178590/72c35c9e-4732-420d-8c93-6fec5199d64d)

